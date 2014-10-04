package nyc.babilonia.VisualGraph;

import java.awt.Color;
import java.awt.Graphics2D;

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

}
