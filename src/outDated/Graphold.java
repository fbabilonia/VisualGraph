	package outDated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import nyc.babilonia.VisualGraph.Edge;
import nyc.babilonia.VisualGraph.Path;
import nyc.babilonia.VisualGraph.Point;
import nyc.babilonia.VisualGraph.TreeNode;

public abstract class Graphold
{
	protected Set<Point> pointSet = new HashSet<Point>(); //used so no duplicate points are entered.
	protected Set<Edge> edges = new TreeSet<Edge>();
	protected ArrayList<Point> points = new ArrayList<Point>();
	//protected Map<Integer, Point> pointMap = new HashMap<Integer, Point>();

	public ArrayList<Point> getPoints()
	{
		return points;
	}
	//returning edges works for both directed and undirected graphs as undirected graphs
	//will just have duplicates of edge from a->b and b->a used for Dijkstra's
	public Set<Edge> getEdges()
	{
		return edges;
	}

	public Graphold()
	{
	}

	public Graphold(File input)
	{
		capture(input);
	}// end constructor with file

	public void capture(File input)
	{
		try
		{
			Scanner scanner = new Scanner(new FileReader(input));
			Point p;
			int w, p1, p2, n = scanner.nextInt();
			for (int i = 0; i < n; ++i)
			{
				p = new Point(i,scanner.nextInt(), scanner.nextInt());
				// System.out.println(p);
				if (pointSet.add(p))
				{
					points.add(p);
				}
			}
			while (scanner.hasNextInt())
			{
				p1 = scanner.nextInt();
				p2 = scanner.nextInt();
				w = scanner.nextInt();
				addEdge(new Edge(points.get(p1-1), points.get(p2-1), w));//let internals handle difference
			}
			scanner.close();
		} catch (FileNotFoundException e)
		{/* don't do anything for now */
		}
		// for(Edge e :edges)System.out.println(e.weight);
	}

	public void clear()
	{
		points.clear();
		edges.clear();
		pointSet.clear();
	}
	public void saveGraph(File out)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(out));
			pw.println(points.size());
			for (Point p : points)
				pw.println(p.x  + " " + p.y);
			for (Edge e : edges)
				pw.println(e.point1.id + " " + e.point2.id + " " + e.getWeight());
			pw.flush();
			pw.close();
		} catch (IOException e)
		{/* dont do anything */
		}
	}
	public void saveState(ArrayList<ArrayList<Set<Point>>> pstates, ArrayList<Set<Point>> psets)
	{
		ArrayList<Set<Point>> tmp = new ArrayList<Set<Point>>();
		for(Set<Point> s : psets)
		{
			tmp.add(new HashSet<Point> (s));
		}
		pstates.add(tmp);
	}
//	public Map<String, Path> getSPath(Point point, ArrayList<TreeNode> history)
//	{
//		Map<String, Path> paths = new HashMap<String, Path>();
//		Map<Integer, TreeNode> best = new HashMap<Integer, TreeNode>();
//		int[] distances = new int[points.size() + 1];
//		Set<Point> visited = new HashSet<Point>(), open = new TreeSet<Point>();
//		TreeSet<TreeNode> candidates = new TreeSet<TreeNode>();
//		visited.add(point);
//		for (Point m : pointSet)
//		{
//			if (m.id != point.id)
//			{
//				distances[m.id] = -1;
//				open.add(m);
//			}
//
//		}
//	//	TreeNode current = new TreeNode(0, distances, edgesForPoint(edges,
//	//			point), null, visited, open, true, best, this, candidates, null);
//		TreeNode head = current;
//		current.point = point;
//		ArrayList<TreeNode> toRemove = new ArrayList<TreeNode>();
//		if (history != null)
//			history.add(new TreeNode(head));
//		do
//		{
//			if (current.isValid)
//			{
//				visited.add(current.point);
//				open.remove(current.point);
//				current.activate();
//				for (TreeNode child : current.children)
//				{
//					if (child.isValid)
//					{
//						if (!visited.contains(child.point))
//							candidates.add(child);
//					}
//				}// end add children to candidates
//				for (Point pt : visited)
//				{
//					System.out.print(pt.id + " ");
//				}
//				System.out.println();
//				for (TreeNode candidate : candidates)
//				{
//					System.out.print(candidate.point.id + " ");
//					if (visited.contains(candidate.point))
//					{
//						toRemove.add(candidate);
//					}
//				}
//				System.out.println();
//
//				if (history != null)
//					history.add(new TreeNode(head));
//				System.out.println(toRemove.size());
//				for (TreeNode rem : toRemove)
//				{
//					candidates.remove(rem);
//				}
//				toRemove.clear();
//			}
//			// System.out.println(candidates);
//			current = candidates.pollFirst();
//			// head.update();
//		} while (open.size() > 0 && current != null);
//		Iterator<Entry<Integer, TreeNode>> it = best.entrySet().iterator();
//		while (it.hasNext())
//		{
//			Map.Entry<Integer, TreeNode> pairs = (Entry<Integer, TreeNode>) it
//					.next();
//			Path p = pairs.getValue().getPath(point);
//			paths.put(p.toString(), p);
//		}
//		return paths;
//	}


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

	public void addPoint(Point p)
	{
		if (pointSet.add(p))
			points.add(p);
	}
	protected abstract void _addEdge(Edge e);
	public void addEdge(Edge e)
	{
		_addEdge(e);
	}
	public void updateEdge(Edge e, int update)
	{
		e.updateWeight(update);
	}

	public void makeGraph(File file)
	{
		this.clear();
		this.capture(file);
	}// end makeGraph
}
