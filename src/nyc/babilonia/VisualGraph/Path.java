package nyc.babilonia.VisualGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


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
	{return "Path From P" +from.id+ " to P"+to.id+" with a cost of "+this.cost+".";}
	@Override
	public void draw(Graphics2D g2d, Color color)
	{
		for(Edge e : path.edges)
		{
			e.drawWithArrow(g2d,color);
		}
	}
}
