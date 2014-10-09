package nyc.babilonia.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;



public class UndirectedGraph extends Graph
{
	public int kruskal(ArrayList<ArrayList<Set<Point>>> pstates,
			ArrayList<Set<Edge>> estates, ArrayList<Point> gobble)
	{
		TreeSet<Edge> edges = new TreeSet<Edge>(this.edges), MST = new TreeSet<Edge>();
		ArrayList<Set<Point>> pset = new ArrayList<Set<Point>>();
		for (Point p : this.points)
		{
			pset.add(new HashSet<Point>());
			pset.get(pset.size() - 1).add(p);
		}
		Edge tmp;
		estates.add(new TreeSet<Edge>(MST));
		int a = 0, b = 0, total = 0;
		while (pset.size() > 1)
		{
			tmp = edges.pollFirst();
			for (int index = 0; index < pset.size(); ++index)
			{
				if (pset.get(index).contains(tmp.point1)
						|| pset.get(index).contains(tmp.point2))
				{
					a = index;
					break;
				}
			}
			if (!(pset.get(a).contains(tmp.point1) && pset.get(a).contains(tmp.point2)))
			{
				total += tmp.getWeight();
				System.out.println(total);
				for (int index = 0; index < pset.size(); ++index)
				{
					if ((pset.get(index).contains(tmp.point1) || pset.get(index)
							.contains(tmp.point2)) && index != a)
					{
						b = index;
						break;
					}
				}
				pset.get(a).addAll(pset.get(b));
				pset.remove(b);
				gobble.add(new Point(a, b, 0));
				MST.add(tmp);
				estates.add(new TreeSet<Edge>(MST));
			}
		}
		return total;
	}
	@Override
	public int prim(Set<Point> mstPoints, Set<Edge> mstEdges,
			ArrayList<Set<Edge>> eh, ArrayList<Set<Point>> ph)
	{
		mstPoints.add(points.get(0));
		eh.add(new TreeSet<Edge>(mstEdges));
		ph.add(new HashSet<Point>(mstPoints));
		int cost = 0;
		TreeSet<Edge> cutEdges = new TreeSet<Edge>();
		while (mstPoints.size() != points.size())
		{
			cutEdges.clear();
			for (Point p : mstPoints)
			{
				for (Edge e : p.edges)
				{
					Point other;
					if (p.equals(e.point1))
						other = e.point2;
					else
						other = e.point1;
					if (!mstPoints.contains(other))
						cutEdges.add(e);
				}
			}
			Edge toAdd = cutEdges.pollFirst();
			cost += toAdd.getWeight();
			mstEdges.add(toAdd);
			mstPoints.add(toAdd.point1);
			mstPoints.add(toAdd.point2);
			eh.add(new TreeSet<Edge>(mstEdges));
			ph.add(new HashSet<Point>(mstPoints));
		}
		return cost;
	}

	@Override
	protected boolean _addEdge(Edge e)
	{
		if(!e.point1.edges.contains(e) && !e.point2.edges.contains(e))
		{
			edges.add(e);
			e.point1.addEdge(e);
			e.point2.addEdge(new Edge(e.point2 , e.point1, e.getWeight()));
			return true;
		}
		return false;
	}
	@Override
	public void draw(Graphics2D g2d)
	{
		for(Edge e : edges)
		{
			e.draw(g2d);
		}
		for(Point p :points)
		{
			p.draw(g2d);
		}
	}

	@Override
	public void draw(Graphics2D g2d, Color pointColor, Color lineColor)
	{
		for(Edge e : edges)
		{
			e.draw(g2d , lineColor);
		}
		for(Point p :points)
		{
			p.draw(g2d , pointColor);
		}
	}
}
