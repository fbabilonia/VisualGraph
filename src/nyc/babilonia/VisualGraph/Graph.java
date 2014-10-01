package nyc.babilonia.VisualGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Graph
{
	private Set<vPoint> pointSet = new HashSet<vPoint>();
	private Set<Edge> edges = new TreeSet<Edge>();
	private ArrayList<vPoint> points = new ArrayList<vPoint>();
	private Map<Integer, vPoint> pointMap = new HashMap<Integer, vPoint>();

	public ArrayList<vPoint> getPoints()
	{
		return points;
	}

	public Set<Edge> getEdges()
	{
		return edges;
	}

	public Graph()
	{
	}

	public Graph(File input)
	{
		capture(input);
	}// end constructor with file

	public void capture(File input)
	{
		try
		{
			Scanner scanner = new Scanner(new FileReader(input));
			vPoint p;
			int w, p1, p2, n = scanner.nextInt();
			for (int i = 0; i < n; ++i)
			{
				w = scanner.nextInt();
				p = new vPoint(scanner.nextInt(), scanner.nextInt(), w);
				// System.out.println(p);
				if (pointSet.add(p))
				{
					points.add(p);
					pointMap.put(p.id, p);
				}
			}
			while (scanner.hasNextInt())
			{
				p1 = scanner.nextInt();
				p2 = scanner.nextInt();
				w = scanner.nextInt();
				edges.add(new Edge(pointMap.get(new Integer(p1)), pointMap
						.get(new Integer(p2)), w));
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
		pointMap.clear();
	}

	public Edge getEdge(vPoint p1, vPoint p2)
	{
		Set<Edge> es = this.edgesForPoint(edges, p1);
		for (Edge e : es)
		{
			if (p1.equals(e.p1) && p2.equals(e.p2))
				return e;
		}
		return null;
	}

	public Graph createMSP()
	{
		Graph MSP = new Graph();
		ArrayList<vPoint> p = new ArrayList<vPoint>();
		Set<vPoint> ps = new HashSet<vPoint>();
		Set<Edge> es = new TreeSet<Edge>();
		System.out.println(edges.size());
		for (Edge e : edges)
		{
			// System.out.println(e.weight);
			if (ps.add(e.p1))
				p.add(e.p1);
			if (ps.add(e.p2))
				p.add(e.p2);
			es.add(e);
			// if(es.size()>2&&hasCycle(es,p)){es.remove(e);System.out.println(e);}
			if (ps.size() == points.size() && es.size() == ps.size() - 1)
				break;
		}
		MSP.edges = es;
		Collections.copy(p, points);
		MSP.points = p;
		MSP.pointSet = ps;
		// System.out.println(hasCycle(edges,points));
		return MSP;

	}

	public void saveGraph(File out)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new FileWriter(out));
			pw.println(points.size());
			for (vPoint p : points)
				pw.println(p);
			for (Edge e : edges)
				pw.println(e.p1.id + " " + e.p2.id + " " + e.weight);
			pw.flush();
			pw.close();
		} catch (IOException e)
		{/* dont do anything */
		}
	}

	public Map<String, Path> getSPath(vPoint point, ArrayList<TreeNode> history)
	{
		Map<String, Path> paths = new HashMap<String, Path>();
		Map<Integer, TreeNode> best = new HashMap<Integer, TreeNode>();
		int[] distances = new int[points.size() + 1];
		Set<vPoint> visited = new HashSet<vPoint>(), open = new TreeSet<vPoint>();
		TreeSet<TreeNode> candidates = new TreeSet<TreeNode>();
		visited.add(point);
		for (vPoint m : pointSet)
		{
			if (m.id != point.id)
			{
				distances[m.id] = -1;
				open.add(m);
			}

		}
		TreeNode current = new TreeNode(0, distances, edgesForPoint(edges,
				point), null, visited, open, true, best, this, candidates, null);
		TreeNode head = current;
		current.point = point;
		ArrayList<TreeNode> toRemove = new ArrayList<TreeNode>();
		if (history != null)
			history.add(new TreeNode(head));
		do
		{
			if (current.isValid)
			{
				visited.add(current.point);
				open.remove(current.point);
				current.activate();
				for (TreeNode child : current.children)
				{
					if (child.isValid)
					{
						if (!visited.contains(child.point))
							candidates.add(child);
					}
				}// end add children to candidates
				for (vPoint pt : visited)
				{
					System.out.print(pt.id + " ");
				}
				System.out.println();
				for (TreeNode candidate : candidates)
				{
					System.out.print(candidate.point.id + " ");
					if (visited.contains(candidate.point))
					{
						toRemove.add(candidate);
					}
				}
				System.out.println();

				if (history != null)
					history.add(new TreeNode(head));
				System.out.println(toRemove.size());
				for (TreeNode rem : toRemove)
				{
					candidates.remove(rem);
				}
				toRemove.clear();
			}
			// System.out.println(candidates);
			current = candidates.pollFirst();
			// head.update();
		} while (open.size() > 0 && current != null);
		Iterator<Entry<Integer, TreeNode>> it = best.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry<Integer, TreeNode> pairs = (Entry<Integer, TreeNode>) it
					.next();
			Path p = pairs.getValue().getPath(point);
			paths.put(p.toString(), p);
		}
		return paths;
	}

	public boolean hasCycle(Set<Edge> edg, ArrayList<vPoint> point)
	{
		boolean[] visited = new boolean[point.size()];
		vPoint p = point.get(0);
		Set<Edge> es = new TreeSet<Edge>();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		do
		{
			// System.out.println("EDGES size ="+edg.size());
			Set<Edge> tempset = edgesForPoint(edg, p);
			for (Edge e : tempset)
			{
				if (es.add(e))
					edges.add(e);
			}
			visited[point.indexOf(p)] = true;
			Edge e = edges.get(0);
			edges.remove(e);
			if (e.p1.equals(p))
				p = e.p2;
			else
				p = e.p1;
			if (visited[point.indexOf(p)])
				return true;
		} while (edges.size() > 0);
		return false;
	}// end has cycle

	public Set<Edge> edgesForPoint(Set<Edge> ed, vPoint p)
	{
		Set<Edge> edgeForPoint = new TreeSet<Edge>();
		for (Edge e : ed)
		{
			if (e.p1.equals(p))
				edgeForPoint.add(e);
		}
		return edgeForPoint;
	}// end get edgesforPoint

	public void addPoint(vPoint p)
	{
		if (pointSet.add(p))
			points.add(p);
	}

	public void addEdge(Edge e)
	{
		edges.add(e);
	}

	public void updateEdge(Edge e, int update)
	{
		e.weight = update;
	}

	public void makeGraph(File file)
	{
		this.clear();
		this.capture(file);
	}// end makeGraph
}
