package nyc.babilonia.VisualGraph;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import nyc.babilonia.data.TreeNode;


public class AnimateTree extends JPanel
{
	ArrayList<ArrayList<TreeNode>> tree;
	private final int LEVEL_GAP =100;
	public TreeNode selected =null;
	Color lineColor = Color.blue, fontColor= Color.BLACK;
	Stroke lineStroke = new BasicStroke(2), pointStroke= new BasicStroke(40,BasicStroke.CAP_ROUND,BasicStroke.JOIN_MITER);
	private static final long serialVersionUID = -4165058204759509524L;
	public AnimateTree()
	{
		this.setSize(750,1000);
		this.setMinimumSize(new Dimension(750,1000));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		//this.setMaximumSize(new Dimension(750,1000));
		//this.setBackground(Color.WHITE);
	}
	public void setState(ArrayList<ArrayList<TreeNode>> nTree)
	{
		tree=nTree;
		redraw();
		System.out.println(nTree.get(0).get(0).parent);
	}
	public boolean drawTree(Graphics2D g2d, int leftx,int rightx,int numOfCP,int myNum,TreeNode node)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		int x=(leftx+rightx)/2,y=20+node.level*this.LEVEL_GAP;
		boolean selectedBelow = false;
		if(node.parent == null)g2d.setColor(Color.YELLOW);
		else if(node.isValid)
		{
			if(node.data.closed.contains(node.point))
				g2d.setColor(Color.GREEN);
			else 
				g2d.setColor(Color.PINK);
		}
		else g2d.setColor(Color.RED);
		g2d.setStroke(pointStroke);
		g2d.drawLine(x,y, x, y);
		g2d.setColor(fontColor);
		if(node.point!=null)g2d.drawString((node.point.id +1 )+"["+node.distanceFromSource+"]", x, y);
		else g2d.drawString("Source["+node.distanceFromSource+"]", x, y);
		if(node.children.size()>0)
		{
			int range = (rightx-leftx)/node.children.size();
			int left = leftx, right = leftx+range;
					for(int c=0;c<node.children.size();++c)
					{
						g2d.setStroke(lineStroke);
						g2d.setColor(lineColor);
						g2d.drawLine(x, y+20,(left+right)/2 ,20+(node.level+1)*this.LEVEL_GAP);
						if(drawTree(g2d,left,right,node.children.size(),c+1,node.children.get(c)))
						{
							selectedBelow = true;
							g2d.setStroke(new BasicStroke(8));
							g2d.setColor(Color.MAGENTA);
							g2d.drawLine(x, y+20,(left+right)/2 ,20+(node.level+1)*this.LEVEL_GAP);
						}
						left+=range;
						right+=range;
					}
		}
		if(selected!=null &&node.point.id == selected.point.id && node.distanceFromSource== selected.distanceFromSource)selectedBelow=true;
		return selectedBelow;	
	}
	private void draw(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		if(tree!=null && tree.get(0).get(0)!=null)
		{
			drawTree(g2d,0,this.getSize().width,0,1,tree.get(0).get(0));
		}//only draw if not null
	}//end draw
	public void paintComponent(Graphics g) 
	{    
        super.paintComponent(g);
        draw(g);
    }//end paintCOmponent
	public void redraw(){this.revalidate();this.repaint();}
}
