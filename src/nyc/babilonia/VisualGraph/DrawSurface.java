package nyc.babilonia.VisualGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import nyc.babilonia.VisualGraph.data.Edge;
import nyc.babilonia.VisualGraph.data.GraphObject;
import nyc.babilonia.VisualGraph.data.Path;
import nyc.babilonia.VisualGraph.data.Point;


@SuppressWarnings("serial")
public class DrawSurface extends JPanel implements MouseListener
{
	public GraphObject graph;
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	private boolean drawable = false,tranformable = false;
	Edge selected = null;
	Path path = null;
	public DrawSurface(GraphObject g , Dimension parent)
	{
		Dimension startDim = new Dimension((int)(parent.width*.75) , parent.height);
		this.setPreferredSize(startDim);
		this.addMouseListener(this);
		this.graph = g;
		this.setBackground(Color.WHITE);
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
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		graph.draw(g2d);
		if(selected!= null)
		{
			selected.draw(g2d,Color.MAGENTA);
		}
		if(path != null)
		{
			path.draw(g2d);
		}
	}//end draw
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
			graph.addPoint(new Point(graph.getPoints().size(),e.getX(),e.getY()));redraw();
		}
		if(tranformable)
		{
			for (Point p :graph.getPoints())
			{
				p.x+= (350-e.getX());
				p.y+= (500-e.getY());
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {}

}
