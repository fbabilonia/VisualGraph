package nyc.babilonia.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * @author Fernando Babilonia
 * Data structure to hold a paths information.  Uses an internal Directed Graph as the path representation. 
 *
 */


public class Path extends GraphComponent
{
	public Graph path = new DirectedGraph();
	public int cost;
	public Point from=null,to=null;
	
	public Path(Point destination,Point f ,int tc)
	{
		this.to=destination;
		this.cost = tc; 
		from = f;
		color = Color.YELLOW;
		stroke = new BasicStroke(2);
	}
	@Override
	public String toString()
	{return "Path From P" + (from.id + 1) + " to P" + (to.id + 1) +" with a cost of " + this.cost + ".";}
	@Override
	public void _draw(Graphics2D g2d, Color color)
	{
		for(Edge e : path.edges)
		{
			e.drawWithArrow(g2d,color);
		}
	}
}
