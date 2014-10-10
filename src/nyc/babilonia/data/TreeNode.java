package nyc.babilonia.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;



/**
 * @author Fernando Babilonia
 * classed used to facilitate Dijkstra's history and shortest path tree
 *
 */
public class TreeNode implements Comparable<TreeNode>
{
	public DijkstraData data;
	public Point point;
	public TreeNode parent;
	public Edge edgeBetweenParent;
	public boolean isValid = true;
	public Map<Integer, TreeNode> best = new HashMap<Integer,TreeNode>();
	public int distanceFromSource;
	public Vector<TreeNode> children = new Vector<TreeNode> ();
	public TreeSet<TreeNode> candidates = new TreeSet<TreeNode>();
	public int level;
	public TreeNode (DijkstraData data ,Point p)
	{
		distanceFromSource = 0;
		this.data = data;
		point = p;
		level = 0;
	}
	public TreeNode(TreeNode parent, boolean valid ,Edge between)
	{
		best= parent.best;
		data = parent.data;
		candidates = parent.candidates;
		edgeBetweenParent = between;
		isValid = valid;
		this.parent = parent;
		this.point = between.point2;
		level =  parent.level+1;
		distanceFromSource = parent.distanceFromSource + between.getWeight();
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
		this.isValid = other.isValid;
		for(TreeNode tn : other.children)
		{
			children.add(new TreeNode(tn));
		}
	}
	private void updateRec()
	{
		if(parent != null)//guard against head;
			isValid = parent.isValid && data.distances[point.id] == distanceFromSource; 
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
	private void getStructureRec(ArrayList<ArrayList<TreeNode>> structure)
	{
		if(structure.size()<this.level+1 )structure.add(new ArrayList<TreeNode>());
		structure.get(level).add(this);
		for(TreeNode child : children)
		{
			child.getStructureRec(structure);
		}
	}
	public ArrayList<ArrayList<TreeNode>> getStructure()
	{
		ArrayList<ArrayList<TreeNode>> structure = new ArrayList<ArrayList<TreeNode>>();
		if(parent ==null)
		{
			getStructureRec(structure);
		}
		return structure;
	}
	@Override
	public int compareTo(TreeNode other)
	{
		return distanceFromSource - other.distanceFromSource;
	}
	@Override
	public String toString()
	{
		return "P"+(point.id+1)+ " with distance from source of "+this.distanceFromSource;
	}
}
