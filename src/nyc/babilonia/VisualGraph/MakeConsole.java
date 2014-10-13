package nyc.babilonia.VisualGraph;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import nyc.babilonia.data.Edge;
import nyc.babilonia.data.GraphObject;
import nyc.babilonia.data.Path;
import nyc.babilonia.data.Point;
import nyc.babilonia.data.TreeNode;


@SuppressWarnings("serial")
public class MakeConsole extends JPanel implements ActionListener,MouseListener
{

	private GraphObject graph;
	private DrawSurface surface;
	JButton drawableButton,makeEdgeButton,pathButton;
	JList<String> edgesList,pathList;
	JTextField weightField;
	JLabel edgeLabel,pathLabel,centerLabel;
	Checkbox visualizeCheck;
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	JComboBox<String> p1Box,p2Box,pathBox;
	Vector<String> points = new Vector<String>(),edges = new Vector<String>(),paths = new Vector<String>();
	ArrayList<Edge> edgeList = new ArrayList<Edge>();
	Map<String,Path> pathMap;
	
	public MakeConsole (GraphObject g, DrawSurface d , Dimension parent)
	{
		graph=g;
		surface = d;
		this.changeTracker.addActionListener(this);
		Dimension startDim = new Dimension((int)(parent.width*.25) , parent.height);
		this.setSize(startDim);
		this.setMaximumSize(startDim);
		initGUI();
	}
	public void updatePoints()
	{
		points.clear();
		for(int i=points.size();i<graph.getPoints().size();++i)
		{
			points.add("p"+(i+1));
		}
		repaint();
	}
	public void updateEdge()
	{
		edges.clear();
		edgeList.clear();
		for(Edge e : graph.getEdges())
		{
			edges.add("From:p"+(e.point1.id+1)+" To:p" +(e.point2.id+1)+" Weight:"+e.getWeight());
			edgeList.add(e);
		}
		edgesList.updateUI();
		repaint();
	}

	public void initGUI()
	{
		this.setVisible(true);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		//creates the section that allows creation/editing of graph
		JPanel createSection = new JPanel();
		createSection.setLayout(new BoxLayout(createSection,BoxLayout.Y_AXIS));
		createSection.setBackground(Color.BLACK);
		createSection.setMaximumSize(new Dimension(this.getWidth(),700));
		createSection.setPreferredSize(new Dimension(this.getWidth(),700));
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.X_AXIS));
		buttonPanel.setMaximumSize(new Dimension(this.getWidth(),50));
		buttonPanel.setPreferredSize(new Dimension(this.getWidth(),50));
		buttonPanel.setBackground(new Color(240,230,140));
		drawableButton = new JButton("Start Drawing");
		drawableButton.setActionCommand("draw");
		drawableButton.setAlignmentX(LEFT_ALIGNMENT);
		drawableButton.setAlignmentY(BOTTOM_ALIGNMENT);
		drawableButton.setEnabled(false);
		drawableButton.addActionListener(this);
		buttonPanel.add(drawableButton);
		
		add(buttonPanel);
	
		
		JPanel edgePanel = new JPanel();
		edgePanel.setLayout(new BoxLayout(edgePanel,BoxLayout.X_AXIS));
		edgePanel.setMaximumSize(new Dimension(this.getWidth(),50));
		edgePanel.setPreferredSize(new Dimension(this.getWidth(),50));
		p1Box = new JComboBox<String>(points);
		p1Box.setMaximumSize(new Dimension(this.getWidth(),20));
		edgePanel.add(p1Box);
		
		p2Box = new JComboBox<String>(points);
		p2Box.setMaximumSize(new Dimension(this.getWidth(),20));
		edgePanel.add(p2Box);
		
		weightField = new JTextField();
		weightField.setActionCommand("edge");
		weightField.addActionListener(this);
		weightField.setMaximumSize(new Dimension(this.getWidth(),20));
		edgePanel.add(weightField);
		createSection.add(edgePanel);
		
		makeEdgeButton = new JButton("Make Edge");
		makeEdgeButton.setActionCommand("edge");
		makeEdgeButton.addActionListener(this);
		makeEdgeButton.setAlignmentX(RIGHT_ALIGNMENT);
		createSection.add(makeEdgeButton);
		edgeLabel = new JLabel("Edges in Graph:");
		createSection.add(edgeLabel);
		
		JScrollPane edgeScroll = new JScrollPane();
		edgesList = new JList<String>(edges);
		edgesList.setBackground(new Color(240,230,140));
		edgesList.addMouseListener(this);
		edgesList.setVisible(true);
		edgeScroll.setViewportView(edgesList);
		createSection.add(edgeScroll);
		
		add(createSection);
		
		//creates pathPanel
		Color pathColor = new Color (255,185,15);
		JPanel pathPanel = new JPanel();
		pathPanel.setMaximumSize(new Dimension(this.getWidth(),700));
		pathPanel.setPreferredSize(new Dimension(this.getWidth(),700));
		pathPanel.setLayout(new BoxLayout(pathPanel,BoxLayout.Y_AXIS));
		pathPanel.setBackground(pathColor);
		
		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel,BoxLayout.X_AXIS));
		boxPanel.setBackground(pathColor);
		boxPanel.setMaximumSize(new Dimension(this.getWidth(),700));
		pathPanel.setPreferredSize(new Dimension(this.getWidth(),700));
		pathLabel = new JLabel("Shortest Paths From");
		boxPanel.add(pathLabel);
		pathBox = new JComboBox<String>(points);
		pathBox.setMaximumSize(new Dimension(100,20));
		boxPanel.add(pathBox);
		pathPanel.add(boxPanel);
		
		JScrollPane pathScroll = new JScrollPane();
		pathList = new JList<String>(paths);
		pathList.setBackground(pathColor);
		pathList.addMouseListener(this);
		pathScroll.setViewportView(pathList);
		pathScroll.setMaximumSize(new Dimension(this.getWidth(),700));
		pathScroll.setPreferredSize(new Dimension(this.getWidth(),700));
		pathPanel.add(pathScroll);
		
		JPanel executePanel = new JPanel();
		executePanel.setMaximumSize(new Dimension(this.getWidth(),700));
		executePanel.setPreferredSize(new Dimension(this.getWidth(),700));
		executePanel.setBackground(pathColor);
		executePanel.setLayout(new BoxLayout(executePanel,BoxLayout.X_AXIS));
		executePanel.setMaximumSize(new Dimension(this.getWidth(), 200));
		visualizeCheck = new Checkbox("Visualize");
		executePanel.add(visualizeCheck);
		pathButton = new JButton("Get Paths");
		pathButton.setActionCommand("path");
		pathButton.addActionListener(this);
		executePanel.add(pathButton);
		pathPanel.add(executePanel);
		add(pathPanel);
		
		
		toggleUI(false);
	}// end initGUI
	public void toggleUI(boolean state)
	{
		p1Box.setEnabled(state);
		p2Box.setEnabled(state);
		pathBox.setEnabled(state);
		weightField.setEditable(state);
		makeEdgeButton.setEnabled(state);
		surface.setSelected(null);
		surface.setPaths(null);
		edgesList.clearSelection();
		pathButton.setEnabled(state);
	}
	public void reset()
	{
		graph.clear();
		clearAll();
		updateBoxes();
		surface.redraw();
	}
	public void clearAll()
	{
		points.clear();
		edges.clear();
		edgeList.clear();
		paths.clear();
		surface.setPaths(null);
		surface.setSelected(null);
	}
	public void updateBoxes()
	{
		p1Box.updateUI();
		p2Box.updateUI();
	}
	public void enableDraw()
	{
		drawableButton.setEnabled(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equalsIgnoreCase("draw"))
		{
			if(!surface.toggleDrawable()){drawableButton.setText("Continue Drawing"); toggleUI(true);}
			else {drawableButton.setText("Stop Drawing");toggleUI(false);}
			updatePoints();
			updateBoxes();
			pathBox.updateUI();
		}//end draw action
		else if(e.getActionCommand().equalsIgnoreCase("edge"))
		{
			try
			{
				int weight = Integer.parseInt(weightField.getText());
				if(p1Box.getSelectedIndex()!=-1 && p2Box.getSelectedIndex()!= -1 &&p1Box.getSelectedIndex()!=p2Box.getSelectedIndex())
				{
					ArrayList<Point> ep = graph.getPoints();
					Point p1=ep.get(p1Box.getSelectedIndex()), p2 = ep.get(p2Box.getSelectedIndex());
					Edge newe =new Edge(p1, p2, weight);
					if(graph.addEdge(newe))
					{
						changeTracker.changeMade();
						updateEdge();
						edgesList.updateUI();
					}
				}
			}
			catch(NumberFormatException nfe){}
			weightField.setText("");
		}//end edge case
		else if(e.getActionCommand().equalsIgnoreCase("path"))
		{
			surface.setPaths(null);
			surface.setSelected(null);
			ArrayList<TreeNode> history = null;
			if(visualizeCheck.getState())history = new ArrayList<TreeNode>();
			pathMap=graph.Dijkstras(pathBox.getSelectedIndex(),history);
			paths.clear();
			for(Path p :pathMap.values())
			{
				paths.add(p.toString());
			}
			pathList.updateUI();
			pathBox.updateUI();
			if(visualizeCheck.getState())
			{
				new VisualDijkstras(history,pathMap,graph).setVisible(true);
			}
		}//end path case
		else if(e.getActionCommand().equalsIgnoreCase("change"))
		{
			surface.selected = null;
			updatePoints();
			updateEdge();
			updateBoxes();
		}
	}//end action Listener
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(!edgeList.isEmpty())
		{
			if(e.getClickCount()==2)
			{
				try
				{
					Edge toUpdate = edgeList.get(edgesList.getSelectedIndex());
					int newWeight=Integer.parseInt(JOptionPane.showInputDialog(this, "Enter new weight"));
					graph.updateEdge(toUpdate, newWeight);
					updateEdge();
					changeTracker.changeMade();
				}
				catch(NumberFormatException nfe){}
			}
			else
			{
				if(e.getComponent().equals(edgesList))
				{
					surface.setSelected(edgeList.get(edgesList.getSelectedIndex()));
					surface.setPaths(null);
					surface.redraw();
				}
				else
				{
					surface.setSelected(null);
					Path p = pathMap.get(pathList.getSelectedValue());
					surface.setPaths(p);
					surface.redraw();	
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	
}
