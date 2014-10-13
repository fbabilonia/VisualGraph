package nyc.babilonia.VisualGraph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import nyc.babilonia.data.Edge;
import nyc.babilonia.data.GraphObject;
import nyc.babilonia.data.Path;
import nyc.babilonia.data.Point;


@SuppressWarnings("serial")
public class DrawSurface extends JPanel implements MouseListener ,MouseMotionListener
{
	public GraphObject graph;
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	Point draggedPoint;
	private boolean drawable = false,tranformable = false;
	Edge selected = null;
	Path path = null;
	public DrawSurface(GraphObject g , Dimension parent)
	{
		Dimension startDim = new Dimension((int)(parent.width*.75) , parent.height);
		this.setPreferredSize(startDim);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.graph = g;
		this.setBackground(Color.WHITE);
		this.changeTracker.addActionListener( new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				redraw();
			}
			
		});
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
	
	//find closest point to given point.  If no points are close null is returned.
	private Point getClosestPoint(Point point)
	{
		for(Point p : graph.getPoints())
		{
			if(p.doOverLap(point))
				return p;
		}
		return null;
	}
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
		Point mousePoint = new Point(0,e.getX(),e.getY());
		draggedPoint = getClosestPoint(mousePoint);
	}
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		if(e.isPopupTrigger())
		{
			Point mousePoint = new Point(0,e.getX(),e.getY());
			Point menuPoint = getClosestPoint(mousePoint);
			if(menuPoint != null)
			{
				GraphMenu menu = new GraphMenu(menuPoint);
				menu.show(this, mousePoint.x, mousePoint.y);
			}
		}
		else
		if(drawable)
		{
			graph.addPoint(new Point(graph.getPoints().size(),e.getX(),e.getY()));
			changeTracker.changeMade();
		}
		else
		{
			boolean doesCollide;
			do
			{
				doesCollide = false;
				for(Point p : graph.getPoints())
				{
					if(p!= draggedPoint && draggedPoint.doOverLap(p))
					{
						doesCollide = true;
						draggedPoint.x+=5;
						draggedPoint.y+=5;
					}
				}
			}
			while(doesCollide);
			changeTracker.changeMade();
			draggedPoint = null;
		}
	}
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(draggedPoint != null)
		{
			draggedPoint.x = e.getX();
			draggedPoint.y = e.getY();
			changeTracker.changeMade();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}
	private class GraphMenu extends JPopupMenu
	{
		JMenuItem deleteJMenuItem;
		public GraphMenu(final Point point)
		{
			deleteJMenuItem = new JMenuItem("Delete Point");
			deleteJMenuItem.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					graph.deletePoint(point);
					changeTracker.changeMade();
				}
				
			});
			add(deleteJMenuItem);
		}
	}
}
