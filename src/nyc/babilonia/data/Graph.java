package nyc.babilonia.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/*
 * Abstract graph class giving subclasses basic structure.
 */

public abstract class Graph
{
	//Set<Point> pointSet = new HashSet<Point>();//used to keep only 1 of each point
	Set<Edge>  edges = new TreeSet<Edge>();//used to keep set of edges and also pre-sort for computations.
	ArrayList<Point> points = new ArrayList<Point> ();//used for easy access to points.
	
	public Graph()
	{
		//creates empty graph;
	}
	/**
	 *  creates a graph from a saved file.
	 * @param graphFile - A graph saved to a file.
	 * @throws IOException
	 */
	public Graph(File graphFile) throws IOException
	{
		openGraphFromFile(graphFile);
	}
	/**
	 * Opens graph from file, Will clear content of the graph before proceeding.
	 * @param graphFile - A graph saved to a file.
	 * @throws IOException
	 */
	public void openGraphFromFile(File graphFile) throws IOException
	{
		try
		{
		clear();
			Scanner scanner = new Scanner(graphFile);
			int numOfPoints = scanner.nextInt();
			int first , second , weight;
			for(int pointNum = 0 ; pointNum < numOfPoints ; pointNum++)
			{
				first = scanner.nextInt();
				second = scanner.nextInt();
				addPoint(new Point(pointNum,first,second));
			}
			while(scanner.hasNextInt())
			{
				first = scanner.nextInt();
				second = scanner.nextInt();
				weight = scanner.nextInt();
				addEdge(new Edge(points.get(first),points.get(second),weight)); 
			}
			scanner.close();
		}
		catch(Exception e)
		{
			throw new IOException("Invalid file format encountered.");
		}
	}
	/**
	 * returns list of points(V) in graph.
	 * @return An ArrayList of points currently in the graph.
	 */
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	/**
	 * returns set of Edges (E) in the graph.
	 * @return Set of edges in the graph.
	 */
	public Set<Edge> getEdges()
	{
		return edges;
	}
	/**
	 * Saves the graph to a file using the following convention
	 * The first line will have a single number n for the number of Vertices in the graph
	 * followed by n lines containing the coordinates of each point
	 * after n lines edges will follow by the format of point1.id point2.id and weight
	 * @param outFile - File to which the graph will be saved.
	 * @throws IOException
	 */
	public void saveGraph(File outFile) throws IOException
	{
		PrintWriter pw = new PrintWriter(new FileWriter(outFile));
		pw.println(points.size());
		for (Point p : points)
			pw.println(p.x  + " " + p.y);
		for (Edge e : edges)
			pw.println(e.point1.id + " " + e.point2.id + " " + e.getWeight());
		pw.flush();
		pw.close();
	}
	/**
	 * Adds point to graph only if the new point will not overlap with another point currently in the Graph.
	 * Run time O(V).
	 * <br>
	 * @param point - Potential point to be added to the graph.
	 * @return Returns true if point was added to the graph, false otherwise.
	 */
	public boolean addPoint(Point point)
	{
		for(Point p :points)
		{
			if(p.doOverLap(point))
				return false;
		}
		points.add(point);
		return true;
	}
	//adds Edge to the graph while keeping internals intact.
	protected abstract boolean _addEdge(Edge edge);
	/**
	 * public method to add an Edge shared by all graphs.
	 * @param edge - Potential Edge to be added to the graph.
	 * @return True if edge was added, false if it was not.
	 */
	public boolean addEdge(Edge edge)
	{
		return _addEdge(edge);
	}
	/**
	 * clears internal data structures of graph
	 */
	public void clear()
	{
		edges.clear();
		points.clear();
	}
	/**
	 * updates the weight of a given edge in the Graph.
	 */
	public void updateEdge(Edge e, int update)
	{
		e.updateWeight(update);
	}
	/**
	 * Runs Dijkstra's single source shortest path algorithm.
	 * The source argument is the id of the source the algorithm will use.
	 * <br>
	 * the history argument will be used to store changes between each 
	 * Iteration of the internal loop.  Passing null pointer for history will result
	 * in no history being preserved.  There is a runtime hit for keeping track of the 
	 * History since entire structures of TreeNodes and DijkstraData objects will be
	 * deep copied during each iteration.
	 * <br>
	 * The method will return a Map of strings keys, and path values containing 
	 * every path found from the source.  
	 * <br>
	 * Dijkstra's can be run on both directed and undirected graphs so implementing it here.
	 * 
	 * @param source - ID of point used as the source for the algorithm.
	 * @param history - ArrayList of TreeNodes for tracking changes during the algorithm.
	 *                  If you do not want to keep track of the history passing null will,
	 *                  suffice.
	 * @return Map of string keys, and Path values containing all paths found by algorithm.
	 */
	public Map<String, Path> Dijkstras(int source, ArrayList<TreeNode> history)
	{
		Map<String,Path> paths = new HashMap<String,Path>(); 
		DijkstraData data = new DijkstraData(points.get(source),points);
		TreeNode head = new TreeNode(data, points.get(source)) , current; //will always need a reference to the head of the tree.
		current = head;
		Point currentPoint;
		if(history != null) //only keep track of history if it is not null.
			history.add(new TreeNode(head));
		do
		{
			if(current.isValid) //only expand if the current treeNode is still valid
			{
				currentPoint = current.point;
				data.open.remove(currentPoint); //remove current point from the openList
				for(Edge edge : current.point.edges)//Iterate through edges for the list.
				{
					Point otherPoint = edge.point2; //point2 is always where the edge is going to;
					int newDistance = current.distanceFromSource + edge.getWeight(); //calculate new distance for child
					boolean isChildValid =(data.distances[otherPoint.id] == -1 
							|| newDistance < data.distances[otherPoint.id]); //check if child is valid.
					TreeNode child = new TreeNode(current , isChildValid , edge); // create new child for treeNode
					current.children.add(child); //add child to children
					//if the child is valid then it has to be added to the candidates of TreeNodes to be expanded.
					//it must also mean that a better distance has been found so the best paths should be updated
					//and the distances array has to be updated.
					if(child.isValid)
					{
						current.candidates.add(child);
						current.best.put(otherPoint.id, child);
						data.distances[child.point.id] = child.distanceFromSource;
					}
					head.update();
					data.closed.add(currentPoint); //add Current point to the closed set
				}
				//If history is being tracked, add the current state to the history ArrayList
				if(history != null)
					history.add(new TreeNode(head));
			}

			current = current.candidates.pollFirst();//Get the next best TreeNode 
		}
		//do above loop until either there are no more points in the open Set 
		//or no valid children are left(cases where no paths exists between points)
		while(data.open.size()>0 && current != null);
		
		//Generate the map to be returned.
		Iterator<Entry<Integer, TreeNode>> it = head.best.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Integer, TreeNode> pairs = (Entry<Integer, TreeNode>) it
					.next();
			Path p = pairs.getValue().getPath(points.get(source));
			System.out.println(p);
			paths.put(p.toString(), p);
		}
		return paths;
	}
	/**
	 * 
	 * @param mstPoints
	 * @param mstEdges
	 * @param eh
	 * @param ph
	 * @return
	 */
	public abstract int prim(Set<Point> mstPoints, Set<Edge> mstEdges,
			ArrayList<Set<Edge>> eh, ArrayList<Set<Point>> ph);
	/**
	 * 
	 * @param pstates
	 * @param estates
	 * @param gobble
	 * @return
	 */
	public abstract int kruskal(ArrayList<ArrayList<Set<Point>>> pstates,
			ArrayList<Set<Edge>> estates, ArrayList<Point> gobble);
	/**
	 * Draws the graph with default colors.
	 * @param g2d - The Graphics2D Object of the application.
	 */
	public abstract void draw(Graphics2D g2d);
	/**
	 * Draws the graph with supplied colors for points, and for edges.
	 * @param g2d - The Graphics2D Object of the application.
	 * @param pointColor - the color to draw all points(Vertices) with.
	 * @param lineColor - the color to draw all lines (Edges) with.
	 */
	public abstract void draw(Graphics2D g2d , Color pointColor , Color lineColor);
}
