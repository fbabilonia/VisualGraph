package nyc.babilonia.VisualGraph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DrawSurface extends JPanel implements MouseListener
{
	public Graph graph;
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	private boolean drawable = false,tranformable = false;
	private double increment=0;
	private Dimension startDim = new Dimension(750,1000);
	private int dx=0,dy=0;
	private vPoint dPoint= new vPoint(0,0,0);
	private Set<vPoint> usedPoints = new HashSet<vPoint>();
	Edge selected = null;
	Path path = null;
	public DrawSurface(Graph g )
	{
		this.setPreferredSize(startDim);
		this.addMouseListener(this);
		this.graph = g;
	}
	public void toggleTranform()
	{
		tranformable = !tranformable;
	}
	public void clear()
	{
		graph.clear();
		redraw();
	}

	
	public boolean toggleDrawable()
	{
		drawable = !drawable; 
		return drawable;
	}
	public void setSelected(Edge e)
	{
		selected = e;
	}
	public void setPaths(Path p)
	{
		path=p;
	}
	private void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		Color lineColor = Color.blue, fontColor= Color.BLACK,pathColor = Color.YELLOW;
		Stroke lineStroke = new BasicStroke(2);
		if(graph.getPoints()!=null&&graph.getEdges()!=null)
		{
			AffineTransform ats = g2d.getTransform();
			
			ats.scale(ats.getScaleX()+increment, ats.getScaleY()+increment);
			if(dPoint.x!=0 && dPoint.y!=0)
			{
				dx =(int) (((((this.getSize().width)/2)*(1+increment))-(dPoint.x*(1+increment))));
				dy = (int)(((((this.getSize().height)/2)*(1+increment)) - (dPoint.y*(1+increment))));
			}
			else
			{
				dx=0;dy=0;
			}
			ats.translate(dx, dy);
			g2d.setTransform(ats);
			usedPoints.clear();
			for(vPoint p :graph.getPoints())
			{
				p.draw(g2d);
				g2d.setColor(fontColor);
				g2d.drawString("p"+p.id, p.x, p.y-8);
			}
			for(Edge e :graph.getEdges())
			{
				e.draw(lineColor, g2d);
				g2d.setColor(fontColor);
				int x= ((e.p1.x+e.p2.x)/2), y =((e.p1.y+e.p2.y)/2),xr,yr;
				double theta = 5;
				xr=(int) ((10*Math.cos(Math.toRadians(theta)))-(10*Math.sin(Math.toRadians(theta))));
				yr = (int) ((10*Math.sin(Math.toRadians(theta)))+(10*Math.cos(Math.toRadians(theta))));
				xr+=x;
				yr+=y;
				while(!usedPoints.add(new vPoint(xr,yr,1))){xr+=10;yr+=10;}
				g2d.drawString(String.valueOf(e.weight), xr, yr	);	
			}
			if(path!=null)
			{
				for(vPoint p :path.path.getPoints())
				{
					g2d.setColor(pathColor);
					g2d.drawLine(p.x, p.y, p.x, p.y);
				}
				g2d.setStroke(lineStroke);
				for(Edge e :path.path.getEdges())
				{
					int ARR_SIZE=8;
					AffineTransform old = g2d.getTransform();
					//System.out.println(e);
					g2d.setColor(pathColor);
					double dx,dy,angle;
					dx=e.p2.x-e.p1.x;
					dy=e.p2.y-e.p1.y;
					angle=Math.atan2(dy, dx);
					int len = (int)Math.sqrt((dx*dx)+(dy*dy));
					 AffineTransform at = AffineTransform.getTranslateInstance(e.p1.x, e.p1.y);
		             at.concatenate(AffineTransform.getRotateInstance(angle));
		             g2d.drawLine(e.p1.x,e.p1.y, e.p2.x, e.p2.y);
		             g2d.transform(at);
		             g2d.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
	                         new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
		            g2d.setTransform(old);
				}
			}
			if(selected!= null)
			{
				g2d.setColor(Color.MAGENTA);
				g2d.drawLine(selected.p1.x, selected.p1.y, selected.p2.x, selected.p2.y);
			}
		}//only draw if not null
	}//end draw
	public void setGraph(Graph g)
	{
		graph = g; redraw();
	}
	@Override
    public void paintComponent(Graphics g) 
	{    
        super.paintComponent(g);
        draw(g);
    }//end paintCOmponent
	public void redraw()
	{
		this.revalidate();
		this.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent e) 
	{}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent e) 
	{
		if(drawable)
		{
			changeTracker.changeMade();
			graph.addPoint(new vPoint(e.getX(),e.getY(),graph.getPoints().size()+1));redraw();
		}
		if(tranformable)
		{
			for (vPoint p :graph.getPoints())
			{
				p.x+= (350-e.getX());
				p.y+= (500-e.getY());
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}

}
