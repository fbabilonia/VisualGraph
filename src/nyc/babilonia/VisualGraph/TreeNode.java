package nyc.babilonia.VisualGraph;

import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

//classed used to facilitate Dijkstra's history and shortest path tree
public class TreeNode implements Comparable<TreeNode>
{
	DijkstraData data;
	Point point;
	TreeNode parent;
	Edge edgeBetweenParent;
	boolean isValid = true;
	Map<Integer, TreeNode> best = new HashMap<Integer,TreeNode>();
	public int distanceFromSource;
	Vector<TreeNode> children = new Vector<TreeNode> ();
	TreeSet<TreeNode> candidates = new TreeSet<TreeNode>();
	public int level;
	public TreeNode (DijkstraData data)
	{
		distanceFromSource = 0;
		this.data = data;
		point = data.point;
		level = 0;
	}
	public TreeNode(TreeNode parent ,Point point, boolean valid ,Edge between , int distanceFromParent)
	{
		best= parent.best;
		candidates = parent.candidates;
		edgeBetweenParent = between;
		isValid = valid;
		this.parent = parent;
		this.point = point;
		level =  parent.level+1;
		distanceFromSource = parent.distanceFromSource + distanceFromParent;
	}
	public TreeNode(TreeNode other)
	{
		this.data = new DijkstraData(other.data);
		this.best = new HashMap<Integer,TreeNode>(other.best);
		this.distanceFromSource = other.distanceFromSource;
		this.point = other.point;
		edgeBetweenParent = other.edgeBetweenParent;
		parent = other.parent;
		candidates = new TreeSet<TreeNode>(other.candidates);
		level = other.level;
		for(TreeNode tn : other.children)
		{
			children.add(new TreeNode(tn));
		}
	}
	private void updateRec()
	{
		if(parent != null)//guard against head;
			isValid = parent.isValid && data.distances[data.point.id] == distanceFromSource; 
		for(TreeNode node : children)
			node.updateRec();
	}
	//update should only be called from the head.
	public void update()
	{
		if (parent == null)
			updateRec();
	}
	public Path getPath(Point from)
	{
		Path p = new Path(point,from,distanceFromSource);
		p.path.addPoint(point);
		TreeNode par = parent;
		Edge route = edgeBetweenParent;
		while(par!=null)
		{
			p.path.addPoint(route.point1);
			p.path.addEdge(route);
			route=par.edgeBetweenParent;
			par=par.parent;
		}
		System.out.println("Edges in path ="+p.path.getEdges().size());
		return p;
	}
	@Override
	public int compareTo(TreeNode other)
	{
		return distanceFromSource - other.distanceFromSource;
	}
}
