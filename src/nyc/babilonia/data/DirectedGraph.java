package nyc.babilonia.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Set;



public class DirectedGraph extends Graph
{

	@Override
	protected boolean _addEdge(Edge e)
	{
		if(edges.add(e))
		{
			e.point1.addEdge(e);
			return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics2D g2d)
	{
		for(Edge e : edges)
		{
			e.drawWithArrow(g2d);
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
			e.drawWithArrow(g2d , lineColor);
		}
		for(Point p :points)
		{
			p.draw(g2d , pointColor);
		}
	}

	@Override
	public int prim(Set<Point> mstPoints, Set<Edge> mstEdges,
			ArrayList<Set<Edge>> eh, ArrayList<Set<Point>> ph) 
	{
		try
		{
			throw new Exception("Prims algorithm should not be run on a directed graph.");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int kruskal(ArrayList<ArrayList<Set<Point>>> pstates,
			ArrayList<Set<Edge>> estates, ArrayList<Point> gobble)
	{
		try
		{
			throw new Exception("Kruskal algorithm should not be run on a directed graph.");
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public void deletePoint(Point point)
	{
		if(points.remove(point))
		{
			ArrayList<Edge> toRemove = new ArrayList<Edge>();
			for(Edge e : edges)
			{
				if(e.point1.id == point.id || e.point2.id == point.id)
					toRemove.add(e);
			}
			edges.removeAll(toRemove);
			for(int i = point.id ; i < points.size() ; ++i)
			{
				--points.get(i).id;
			}
		}	
	}
}
