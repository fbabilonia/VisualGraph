package nyc.babilonia.data;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;



public class Edge extends GraphComponent implements Comparable<Edge>
{
	public Point point1, point2, drawPoint1, drawPoint2 , textPoint;
	private int weight;
	/**
	 * Creates a new Edge between point a, and point b with weight w
	 * @param a - First point in Edge. (Edge goes from this point)
	 * @param b - Second point in Edge. (Edge goes to this point)
	 * @param w - the weight between for the Edge.
	 */
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
	/**
	 * Returns the weight of the Edge
	 * @return - the weight of the Edge.
	 */
	public int getWeight()
	{
		return weight;
	}
	/**
	 * Used to change the current weight of the Edge.
	 * @param newWeight - new weight for the Edge.
	 */
	public void updateWeight(int newWeight)
	{
		weight = newWeight;
	}
	/*
	 * Used to calculate how the edge gets drawn.
	 * will make sure not to be drawn over points.
	 */
	private void calculateDrawPoints()
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
	//calculates where to draw the weight for the Edge.
	private void calculateTextPoint()
	{
		textPoint = new Point(0,(point1.x+point2.x)/2,(point1.y+point2.y)/2);
		textPoint.x+=6;
		textPoint.y-=6;
	}
	/**
	 * Draws the edge with an arrow will use default color if none is given.
	 * @param g2d - Graphics2D object of current application.
	 */
	public void drawWithArrow(Graphics2D g2d)
	{
		drawWithArrow(g2d, color);
	}
	/**
	 * Draws the Edge with an arrow and a specific Color.
	 * @param g2d - Graphic2D object of current application.
	 * @param drawColor - Color the Edge will be drawn in .
	 */
	public void drawWithArrow(Graphics2D g2d , Color drawColor)
	{
		_draw(g2d, drawColor);
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
	//draws the Edge with given color.
	protected void _draw(Graphics2D g2d, Color drawColor)
	{
		Stroke oldStroke = g2d.getStroke();
		Color oldColor = g2d.getColor();
		g2d.setStroke(stroke);
		g2d.setColor(drawColor);
		g2d.drawLine(drawPoint1.x , drawPoint1.y , drawPoint2.x , drawPoint2.y);
		g2d.setColor(fontColor);
		g2d.drawString(weight+"",textPoint.x,textPoint.y);
		g2d.setColor(oldColor);
		g2d.setStroke(oldStroke);
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
	@Override
	public int compareTo(Edge other) 
	{
		if(point1.equals(other.point1)&&point2.equals(other.point2))
			return 0;
		else if(weight>other.weight) 
			return 1;
		else if (weight<other.weight)
			return -1;
		else
		{
			if(point1 != other.point1)return -1;
			else return 1;
		}
	}

}
