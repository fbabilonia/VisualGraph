package nyc.babilonia.VisualGraph;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import nyc.babilonia.data.DijkstraData;
import nyc.babilonia.data.Edge;
import nyc.babilonia.data.GraphObject;
import nyc.babilonia.data.Path;
import nyc.babilonia.data.Point;


public class AnimateGraph extends JPanel
{
	private static final long serialVersionUID = 3592156953842604547L;
	Path selected = null;
	DijkstraData data;
	private Set<Point> usedPoints = new HashSet<Point>();
	GraphObject graph;
	public AnimateGraph(DijkstraData d,GraphObject g)
	{
		this.setSize(750,1000);
		this.setMinimumSize(new Dimension(750,1000));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setMaximumSize(new Dimension(750,1000));
		this.setBackground(Color.white);
		graph = g;
		data = d;
		setState(d);
	}
	public void setState(DijkstraData d)
	{
		this.data = d;
		this.redraw();
	}
	private void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		usedPoints.clear();
		Color pointColor;
		if(graph.getPoints()!=null&&graph.getEdges()!=null)
		{
			for(Point p :graph.getPoints())
			{
				if(data.distances[p.id]==0)
					pointColor = Color.YELLOW;
				else if(data.open.contains(p))
				{
					pointColor = Color.GRAY;
				}
				else 
					pointColor = Color.GREEN;
				p.draw(g2d,pointColor);

			}
			for(Edge e :graph.getEdges())
			{
				e.draw(g2d);
			}
//			for(Point p :graph.getPoints())
//			{
//				g2d.setColor(infoColor);
//				if(distances[p.id]!=-1)wInfo = Integer.toString(distances[p.id]);
//				else wInfo = Character.toString((char)8734);
//				g2d.drawString("p"+p.id+"[ "+wInfo+" ]", p.x, p.y);
//
//			}
//			if(selected!=null)
//			{
//				g2d.setStroke(pointStroke);
//				for(Point p :selected.path.getPoints())
//				{
//					if(distances[p.id]==0)g2d.setColor(Color.PINK);
//					else g2d.setColor(Color.MAGENTA);
//					g2d.drawLine(p.x, p.y, p.x, p.y);
//				}
//				g2d.setStroke(lineStroke);
//				for(Edge e :selected.path.getEdges())
//				{
//					int ARR_SIZE=8;
//					AffineTransform old = g2d.getTransform();
//					//System.out.println(e);
//					g2d.setColor(Color.MAGENTA);
//					double dx,dy,angle;
//					dx=e.point2.x-e.point1.x;
//					dy=e.point2.y-e.point1.y;
//					angle=Math.atan2(dy, dx);
//					int len = (int)Math.sqrt((dx*dx)+(dy*dy));
//					 AffineTransform at = AffineTransform.getTranslateInstance(e.point1.x, e.point1.y);
//		             at.concatenate(AffineTransform.getRotateInstance(angle));
//		             g2d.drawLine(e.point1.x,e.point1.y, e.point2.x, e.point2.y);
//		             g2d.transform(at);
//		             g2d.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
//	                         new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
//		            g2d.setTransform(old);
//				}
//			}
		}//only draw if not null
	}//end draw
	@Override
    public void paintComponent(Graphics g) 
	{    
        super.paintComponent(g);
        draw(g);
    }//end paintCOmponent
	public void redraw(){this.revalidate();this.repaint();}
}
