package nyc.babilonia.VisualGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;


public class Edge implements Comparable<Edge> 
{
	public vPoint p1,p2;
	public int weight;
	private static Color lineColor = Color.BLUE;
	private static Stroke  lineStroke = new BasicStroke(2);
	
	public Edge(vPoint a, vPoint b, int w)
	{
		p1=a;
		p2=b;
		weight=w;
	}
	public static void setColor(Color newColor)
	{
		lineColor = newColor;
	}
	public static Color getColor()
	{
		return lineColor;
	}
	public void setStroke(Stroke newStroke)
	{
		lineStroke = newStroke;
	}
	public void draw(Color color, Graphics2D g2d)
	{
		Stroke oldStroke = g2d.getStroke();
		g2d.setStroke(lineStroke);
		Color oldColor = g2d.getColor();
		int ARR_SIZE=8;
		AffineTransform old = g2d.getTransform();
		g2d.setColor(lineColor);
		double dx,dy,angle;
		dx=p2.x-p1.x;
		dy=p2.y-p1.y;
		angle=Math.atan2(dy, dx);
		int len = (int)Math.sqrt((dx*dx)+(dy*dy));
		 AffineTransform at = AffineTransform.getTranslateInstance(p1.x, p1.y);
         at.concatenate(AffineTransform.getRotateInstance(angle));
         g2d.drawLine(p1.x,p1.y, p2.x, p2.y);
         g2d.transform(at);
         g2d.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                 new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
		g2d.setStroke(oldStroke);
		g2d.setColor(oldColor);
		g2d.setTransform(old);
	}
	@Override
	public String toString()
	{
		return "(" + p1.x + "," + p1.y + ") , "
			 + "(" + p2.x + "," + p2.y + ") , Weight :"+weight;
		}
	public int compareTo(Edge other) 
	{
		if(p1.equals(other.p1)&&p2.equals(other.p2))return 0;
		else if(weight>other.weight) return 1;
		else if (weight<other.weight)return -1;
		else
		{
			if(p1!=other.p1)return -1;
			else return 1;
		}
	}
}
