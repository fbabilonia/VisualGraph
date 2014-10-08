package nyc.babilonia.VisualGraph.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;



public class Edge extends GraphComponent implements Comparable<Edge>
{
	public Point point1, point2, drawPoint1, drawPoint2 , textPoint;
	private int weight;
	
	public Edge(Point a, Point b, int w)
	{
		point1=a;
		point2=b;
		weight=w;
		color = Color.BLUE;
		stroke = new BasicStroke(2);
		calculateDrawPoints();
		calculateTextPoint();
	}
	public void drawWithArrow(Graphics2D g2d)
	{
		drawWithArrow(g2d, color);
	}
	public int getWeight()
	{
		return weight;
	}
	public void updateWeight(int newWeight)
	{
		weight = newWeight;
	}
	public void calculateDrawPoints()
	{
		int width = (int)((BasicStroke) point1.getStroke()).getLineWidth()/2;
		int x1,y1,x2,y2;
		double unitx , unity , distance = Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y -point2.y,2));
		unitx = Math.abs(point1.x - point2.x) / distance; 
		unity = Math.abs(point1.y - point2.y) / distance; 
		if(point1.x < point2.x)
		{
			x1 = (int)(point1.x + (unitx * width));
			x2 = (int)(point2.x - (unitx * width));
		}
		else
		{
			x1 = (int)(point1.x - (unitx * width));
			x2 = (int)(point2.x + (unitx * width));
		}
		if(point1.y < point2.y)
		{
			y1 = (int)(point1.y + (unity * width));
			y2 = (int)(point2.y - (unity * width));
		}
		else
		{
			y1 = (int)(point1.y - (unity * width));
			y2 = (int)(point2.y + (unity * width));
		}
		drawPoint1 = new Point(0 ,x1 ,y1);
		drawPoint2 = new Point (0 , x2 , y2);
	}
	private void calculateTextPoint()
	{
		textPoint = new Point(0,(point1.x+point2.x)/2,(point1.y+point2.y)/2);
		textPoint.x+=6;
		textPoint.y-=6;
	}
	public void drawWithArrow(Graphics2D g2d , Color drawColor)
	{
		draw(g2d, drawColor);
		AffineTransform old = g2d.getTransform();
		int ARR_SIZE = 8;
		double dx, dy, angle;
		dx = drawPoint2.x - drawPoint1.x;
		dy = drawPoint2.y - drawPoint1.y;
		angle = Math.atan2(dy, dx);
		int len = (int) Math.sqrt((dx * dx) + (dy * dy));
		AffineTransform at = AffineTransform.getTranslateInstance(drawPoint1.x, drawPoint1.y);
		at.concatenate(AffineTransform.getRotateInstance(angle));
		g2d.transform(at);
		g2d.fillPolygon(new int[] { len, len - ARR_SIZE, len - ARR_SIZE, len },
				new int[] { 0, -ARR_SIZE, ARR_SIZE, 0 }, 4);
		g2d.setTransform(old);
	}
	@Override
	public void _draw(Graphics2D g2d, Color drawColor)
	{
		g2d.setStroke(stroke);
		g2d.setColor(drawColor);
		g2d.drawLine(drawPoint1.x , drawPoint1.y , drawPoint2.x , drawPoint2.y);
		g2d.setColor(fontColor);
		g2d.drawString(weight+"",textPoint.x,textPoint.y);
	}
	@Override
	public String toString()
	{
		return "(" + point1.x + "," + point1.y + ") , "
			 + "(" + point2.x + "," + point2.y + ") , Weight :"+weight;
		}
	@Override
	public boolean equals(Object other)
	{
		Edge othere = (Edge) other;
		return point1.equals(othere.point1)&&point2.equals(othere.point2);
	}
	public int compareTo(Edge other) 
	{
		if(point1.equals(other.point1)&&point2.equals(other.point2))return 0;
		else if(weight>other.weight) return 1;
		else if (weight<other.weight)return -1;
		else
		{
			if(point1 != other.point1)return -1;
			else return 1;
		}
	}

}
