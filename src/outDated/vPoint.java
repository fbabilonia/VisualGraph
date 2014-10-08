package outDated;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.TreeSet;

import nyc.babilonia.VisualGraph.data.Edge;


//old point class used while in class before the breakdown
public class vPoint extends Point implements Comparable<vPoint>
{
	private static final long serialVersionUID = 5135901192710948886L;
	public int id;
	private Stroke pointStroke= new BasicStroke(15,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER);
	private static Color pointColor = Color.RED;
	public TreeSet<Edge> edges = new TreeSet<Edge>();
	public vPoint(int x ,int y , int id)
	{
		this.x=x;
		this.y=y;
		this.id=id;
	}
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
	public static void setColor(Color newColor)
	{
		pointColor = newColor;
	}
	public static Color getColor()
	{
		return pointColor;
	}
	public void setStroke(Stroke newStroke)
	{
		pointStroke = newStroke;
	}
	public void draw(Color color, Graphics2D g2d)
	{
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(pointStroke);
		Color oldColor = g2d.getColor();
		g2d.setColor(pointColor);
		g2d.drawLine(x, y, x, y);
		g2d.setStroke(oldStroke);
		g2d.setColor(oldColor);
	}
	public void draw(Graphics2D g2d)
	{
		draw(pointColor , g2d);
	}
	@Override
	public String toString()
	{return (id+1) + " " + x + " " + y;}
	//
	@Override
	public int compareTo(vPoint otherPoint)
	{
		//points are the same
		if( (x == otherPoint.x) && (y == otherPoint.y) )return 0;
		//points not the same but x coordinates differ
		else if(x != otherPoint.x) return x-otherPoint.x;
		//points not the same and share x value
		else return y - otherPoint.y;
	}	
}
