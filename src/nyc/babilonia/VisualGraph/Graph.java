package nyc.babilonia.VisualGraph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public abstract class Graph
{
	Set<Point> pointSet = new HashSet<Point>();//used to keep only 1 of each point
	Set<Edge>  edges = new TreeSet<Edge>();//used to keep set of edges and also pre-sort for computations.
	ArrayList<Point> points = new ArrayList<Point> ();//used for easy access to points.
	
	public Graph()
	{
		//creates empty graph;
	}
	//just creates a graph from a pre-saved file.
	public Graph(File graphFile) throws IOException
	{
		openGraphFromFile(graphFile);
	}
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
				addEdge(new Edge(points.get(first-1),points.get(second-1),weight)); 
			}
			scanner.close();
		}
		catch(Exception e)
		{
			throw new IOException("Invalid file format encountered.");
		}
	}
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	public Set<Edge> getEdges()
	{
		return edges;
	}
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
	public boolean addPoint(Point point)
	{
		if(pointSet.add(point))
		{
			points.add(point);
			return true;
		}
		return false;
	}
	//adds edge to graph while keeping internals correct.
	protected abstract boolean _addEdge(Edge edge);
	//public method to add an Edge shared by all graphs.
	public boolean addEdge(Edge edge)
	{
		return _addEdge(edge);
	}
	//clears internal data structures of graph
	public void clear()
	{
		pointSet.clear();
		edges.clear();
		points.clear();
	}
	public void updateEdge(Edge e, int update)
	{
		e.updateWeight(update);
	}
	public Map<String, Path> Dijkstras(int source, ArrayList<TreeNode> history)
	{
		Map<String,Path> paths = new HashMap<String,Path>();
		DijkstraData data = new DijkstraData(points.get(source),points);
		TreeNode head = new TreeNode(data) , current;
		current = head;
		int itt = 1; 
		Point currentPoint;
		do
		{
			if(current.isValid)
			{
				currentPoint = current.point;
				if(history != null)
					history.add(new TreeNode(current));
				data.closed.add(currentPoint);
				for(Edge edge : current.point.edges)
				{
					Point otherPoint = edge.point2; //point2 is always where the edge is going to;
					int newDistance = current.distanceFromSource + edge.getWeight();
					boolean isChildValid =(data.distances[otherPoint.id] == -1 
							|| (newDistance)< data.distances[otherPoint.id]);
					TreeNode child = new TreeNode(current, otherPoint , isChildValid , edge , edge.getWeight());
					current.children.add(child);
					if(child.isValid)
					{
						current.candidates.add(child);
						current.best.put(otherPoint.id, child);
						data.distances[child.point.id] = child.distanceFromSource;
					}
				}
			}
			System.out.println("Itteration # " +itt);
			for(TreeNode candidate : current.candidates)
			{
				System.out.println(candidate.point.id + " " + candidate.distanceFromSource);
			}
			System.out.println();
			itt++;
			current = current.candidates.pollFirst();
		}
		while(data.open.size()>0 && current != null);
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
	public abstract void draw(Graphics2D g2d);
	public abstract void draw(Graphics2D g2d , Color pointColor , Color lineColor);
	//will drop
	public void saveState(ArrayList<ArrayList<Set<Point>>> pstates, ArrayList<Set<Point>> psets)
	{}
	public Set<Edge> edgesForPoint(Set<Edge> ed, Point p)
	{
		Set<Edge> edgeForPoint = new TreeSet<Edge>();
		for (Edge e : ed)
		{
			if (e.point1.equals(p))
				edgeForPoint.add(e);
		}
		return edgeForPoint;
	}// end get edgesforPoint
}
