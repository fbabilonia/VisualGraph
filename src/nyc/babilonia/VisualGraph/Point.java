package nyc.babilonia.VisualGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.TreeSet;

public class Point extends GraphComponent implements Comparable<Point>
{
	//for simplicity keeping id as an int to make life easier looking it up.
	public int x,y,id;
	public TreeSet<Edge> edges = new TreeSet<Edge>();
	public Point(int id, int x ,int y)
	{
		stroke= new BasicStroke(30,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER);
		color = Color.RED;
		fontColor = Color.BLACK;
		this.x=x;
		this.y=y;
		this.id=id;
	}
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
	@Override
	public void draw(Graphics2D g2d , Color color)
	{
		g2d.setColor(color);
		g2d.drawLine(x, y, x, y);
		g2d.setColor(fontColor);
		g2d.drawString("p" + (id+1), x-6, y+6);
	}
	@Override
	public String toString()
	{return (id+1) + " " + x + " " + y;}
	
	@Override
	public boolean equals(Object other)
	{
		Point otherPoint = (Point) other;
		return (x == otherPoint.x && y == otherPoint.y);
	}
	@Override
	public int compareTo(Point otherPoint)
	{
		//points are the same
		if( (x == otherPoint.x) && (y == otherPoint.y) )return 0;
		//points not the same but x coordinates differ
		else if(x != otherPoint.x) return x-otherPoint.x;
		//points not the same and share x value
		else return y - otherPoint.y;
	}	
}
