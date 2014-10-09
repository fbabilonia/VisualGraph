package nyc.babilonia.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.TreeSet;



public class Point extends GraphComponent implements Comparable<Point>
{
	//for simplicity keeping id as an int to make life easier looking it up.
	public int x,y,id;
	public TreeSet<Edge> edges = new TreeSet<Edge>();
	/**
	 * Creates a new point with and id, and an x , y coordinate.
	 * @param id - meant to be place points place in ArrayList for easy lookup.
	 * @param x - the x coordinate.
	 * @param y - the y coordinate.
	 */
	public Point(int id, int x ,int y)
	{
		stroke= new BasicStroke(30,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER);
		color = Color.RED;
		fontColor = Color.BLACK;
		this.x=x;
		this.y=y;
		this.id=id;
	}
	/**
	 * checks to see if points overlap (so points do not get draw on top of each other
	 * @param otherPoint - A point which will be tested.
	 * @return True if points overlap when drawn.  False otherwise.
	 */
	public boolean doOverLap( Point otherPoint)
	{
		int circumference = (int)((BasicStroke) getStroke()).getLineWidth();
		double distance = Math.sqrt(Math.pow(x - otherPoint.x, 2) + Math.pow(y -otherPoint.y,2));
		return circumference > distance ;
	}
	/**
	 * Adds an edge to points Edge set, which stores edges associated with this point.
	 * @param e - an edge to be added to the points adjacency set.
	 */
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
	@Override
	protected void _draw(Graphics2D g2d , Color drawColor)
	{
		Stroke oldStroke = g2d.getStroke();
		Color oldColor = g2d.getColor();
		g2d.setStroke(stroke);
		g2d.setColor(drawColor);
		g2d.drawLine(x, y, x, y);
		g2d.setColor(fontColor);
		g2d.drawString("p" + (id+1), x-6, y+6);
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
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
