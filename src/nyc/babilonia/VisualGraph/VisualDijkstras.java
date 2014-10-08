package nyc.babilonia.VisualGraph;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import nyc.babilonia.VisualGraph.data.GraphObject;
import nyc.babilonia.VisualGraph.data.Path;
import nyc.babilonia.VisualGraph.data.TreeNode;


public class VisualDijkstras extends JFrame
{
	private static final long serialVersionUID = -3417311569513353106L;
	ArrayList<TreeNode> history;
	Map<String,Path> paths;
	GraphObject graph;
	public VisualDijkstras(ArrayList<TreeNode> his,Map<String,Path> p , GraphObject g)
	{
		history=his; 
		graph = g;
		paths=p;initGUI();
	}
	public void initGUI()
	{
		setTitle("Visualize Paths");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(new Dimension(1750,1000));
        this.setVisible(true);
//System.out.println(paths);
        this.setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        AnimateGraph aGraph = new AnimateGraph(history.get(0).data , graph);
        AnimateTree aTree = new AnimateTree();
        add(new DijkstraInfo(history,aGraph,aTree,paths , graph));
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setSize(750,1000);
        treeScroll.setMaximumSize(new Dimension(750,1000));
        treeScroll.setViewportView(aTree);
        add(aGraph);
        add(treeScroll);
	}

}
