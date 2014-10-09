package nyc.babilonia.VisualGraph;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import nyc.babilonia.data.GraphObject;
import nyc.babilonia.data.Path;
import nyc.babilonia.data.Point;
import nyc.babilonia.data.TreeNode;


public class DijkstraInfo extends JPanel implements ActionListener , MouseListener
{
	private static final long serialVersionUID = -7262192529381632081L;
	ArrayList<TreeNode> states;
	int currentState = 0;
	JLabel distanceLabel,closedLabel,openLabel,stateLabel,candidateLabel;
	JList<String> closedList,openList,candidateList,pathList;
	GraphObject graph;
	JButton nextButton,previousButton;
	Map<String,Path> pathMap;
	AnimateGraph aGraph;
	AnimateTree aTree;
	JTable distanceTable;
	Vector<String> closed = new Vector<String>(), open = new Vector<String>(),header = new Vector<String>(),candidates = new Vector<String>(), paths = new Vector<String>();
	public DijkstraInfo(ArrayList<TreeNode> st,AnimateGraph a,AnimateTree t,Map<String,Path> p , GraphObject g)
	{
		states=st; 
		aGraph = a;
		aTree=t;
		pathMap= p;
		graph = g;
		initGUI();}
	public void initGUI()
	{
		this.setSize(250,1000);
		this.setMinimumSize(new Dimension(250,1000));
		this.setMaximumSize(new Dimension(250,1000));
		this.setVisible(true);
		this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		//this.setAlignmentX(LEFT_ALIGNMENT);
		//this.setAlignmentY(TOP_ALIGNMENT);
		
		JPanel statePanel = new JPanel();
		statePanel.setLayout(new BoxLayout(statePanel,BoxLayout.X_AXIS));
		stateLabel = new JLabel( currentState + " of ");
		statePanel.add(stateLabel);
		nextButton = new JButton("Next Step");
		nextButton.setActionCommand("next");
		nextButton.addActionListener(this);
		previousButton = new JButton("Previous");
		previousButton.setActionCommand("back");
		previousButton.addActionListener(this);
		previousButton.setEnabled(false);
		statePanel.add(nextButton);
		statePanel.add(previousButton);
		add(statePanel);
		
		JPanel distancePanel = new JPanel();
		distancePanel.setLayout(new BoxLayout(distancePanel,BoxLayout.Y_AXIS));
		distanceLabel = new JLabel("Current Distances");
		distancePanel.add(distanceLabel);
		JScrollPane distanceScroll = new JScrollPane();
		for (Point p: graph.getPoints())
		{header.add("p"+ (p.id +1));}
		distanceTable = new JTable(new DefaultTableModel(header,1));
		distanceScroll.setViewportView(distanceTable);
		distancePanel.add(distanceScroll);
		add(distancePanel);
		
		JPanel closedPanel = new JPanel();
		closedPanel.setLayout(new BoxLayout(closedPanel,BoxLayout.Y_AXIS));
		closedLabel = new JLabel("Nodes currently in closed list:");
		closedPanel.add(closedLabel);
		JScrollPane closedScroll = new JScrollPane();
		closedList = new JList<String>(closed);
		closedScroll.setViewportView(closedList);
		closedPanel.add(closedScroll);
		add(closedPanel);
		
		JPanel openPanel = new JPanel();
		openPanel.setLayout(new BoxLayout(openPanel,BoxLayout.Y_AXIS));
		openLabel = new JLabel("Nodes currently in open list:");
		openPanel.add(openLabel);
		JScrollPane openScroll = new JScrollPane();
		openList = new JList<String>(open);
		openScroll.setViewportView(openList);
		openPanel.add(openScroll);
		add(openPanel);
		
		JPanel candidatePanel = new JPanel();
		candidatePanel.setLayout(new BoxLayout(candidatePanel,BoxLayout.Y_AXIS));
		candidateLabel = new JLabel("Tree nodes waiting to be expanded");
		candidatePanel.add(candidateLabel);
		JScrollPane candidateScroll = new JScrollPane();
		candidateList = new JList<String>(candidates);
		candidateScroll.setViewportView(candidateList);
		candidatePanel.add(candidateScroll);
		add(candidatePanel);
		System.out.println(pathMap);
		for(String s : pathMap.keySet()){paths.add(s);}
		pathList = new JList<String>(paths);
		pathList.setEnabled(false);
		JScrollPane pathScroll = new JScrollPane();
		pathScroll.setViewportView(pathList);
		pathList.addMouseListener(this);
		add(pathScroll);
		updateAll();
	}//end initGUI
	public void updateAll()
	{
		stateLabel.setText((currentState+1)+" of "+ states.size()+" states");
		header.clear();
		Vector<Vector<String>>distances = new Vector<Vector<String>>();
		distances.add(new Vector<String>());
		TreeNode cState = states.get(currentState);
		for(int i = 0; i<cState.data.distances.length;++i)
		{
			int distance = cState.data.distances[i];
			if (distance != -1)distanceTable.getModel().setValueAt(distance, 0, i);
			else distanceTable.getModel().setValueAt((char)8734, 0, i);
		}
		
		closed.clear();
		for(Point p : cState.data.closed)
		{closed.add("p"+p.id);}
		open.clear();
		for(Point p : cState.data.open){open.add("p"+p.id);}
		candidates.clear();
		for(TreeNode tn :cState.candidates){candidates.add(tn.toString());}
		openList.updateUI();
		closedList.updateUI();
		distanceTable.updateUI();
		candidateList.updateUI();
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("next"))
		{
			if(!previousButton.isEnabled())previousButton.setEnabled(true);
			if(currentState+1!=states.size())currentState++;
			updateAll();
			aGraph.setState(states.get(currentState).data);
			aTree.setState(states.get(currentState).getStructure());
			if(currentState+1== states.size()){nextButton.setEnabled(false);pathList.setEnabled(true);}
		}//end next
		else if(e.getActionCommand().equals("back"))
		{
			if(!nextButton.isEnabled())nextButton.setEnabled(true);
			--currentState;
			updateAll();
			aGraph.setState(states.get(currentState).data);
			aTree.setState(states.get(currentState).getStructure());
			if(currentState== 0)previousButton.setEnabled(false);
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		aGraph.selected=pathMap.get(pathList.getSelectedValue());
		aTree.selected = states.get(0).best.get(pathMap.get(pathList.getSelectedValue()).to.id);
		aGraph.redraw();
		aTree.redraw();
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {	}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent e) 
	{
		aGraph.selected=pathMap.get(pathList.getSelectedValue());
		aTree.selected = states.get(0).best.get(pathMap.get(pathList.getSelectedValue()).to.id);
		aGraph.redraw();
		aTree.redraw();
	}
}
