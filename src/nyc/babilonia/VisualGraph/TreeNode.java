package nyc.babilonia.VisualGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class TreeNode implements Comparable<TreeNode>
{
	public TreeNode parent;
	public Edge between;
	public boolean isValid;
	public int distanceFromSource;
	public Vector<TreeNode> children = new Vector<TreeNode>();
	private Set<Edge> edges;
	public Set<vPoint> closed,open;
	public TreeSet<TreeNode> candidates;
	public Graph graph;
	public Map<Integer,TreeNode> best;
	public vPoint point;
	public int level;
	int [] distances;
	public TreeNode(int d,int []da,Set<Edge> ed,Edge b,Set<vPoint> c,Set<vPoint>o, boolean valid,Map<Integer,TreeNode> be,Graph g,TreeSet<TreeNode> can,TreeNode parent)
	{
		this.distanceFromSource=d;
		this.distances=da;
		this.between = b;
		this.closed = c;
		this.edges=ed;
		this.graph = g;
		this.isValid = valid;
		this.candidates = can;
		if(between != null){this.point = between.p2;}
		this.best = be;
		this.open = o;
		this.parent = parent;
		if(parent == null)level = 0;
		else level = parent.level+1;
	}
	@Override
	public boolean equals(Object other)
	{
		TreeNode otherp=(TreeNode)other;
		return this.point.equals(otherp.point);
	}
	
	@Override
	public String toString()
	{return "P"+point.id+ " with distance from source of "+this.distanceFromSource;}
	private void getStructureRec(ArrayList<ArrayList<TreeNode>> structure)
	{
		if(structure.size()<this.level+1 )structure.add(new ArrayList<TreeNode>());
		structure.get(level).add(this);
		for(TreeNode child : children)
		{
			child.getStructureRec(structure);
		}
	}
	private void updateRec(TreeNode node)
	{
		if(parent!=null)this.isValid = parent.isValid && distances[point.id]==this.distanceFromSource;
		else this.isValid=distances[point.id]==this.distanceFromSource;
		for(TreeNode child :children)updateRec(child);
	}
	public void update()
	{
		if(parent==null)updateRec(this);
	}
	public ArrayList<ArrayList<TreeNode>> getStructure()
	{
		ArrayList<ArrayList<TreeNode>> structure = new ArrayList<ArrayList<TreeNode>>();
		if(parent ==null)
		{getStructureRec(structure);}
		return structure;
	}
	public TreeNode(TreeNode other)
	{
		this.parent = other.parent;
		this.distanceFromSource = other.distanceFromSource;
		//this.children = other.children;
		this.point = other.point;
		this.edges = other.edges;
		this.open = new HashSet<vPoint>(other.open);
		this.candidates = new TreeSet<TreeNode>(other.candidates);
		this.closed =  new HashSet<vPoint>(other.closed);
		this.distances = java.util.Arrays.copyOf(other.distances, other.distances.length); //need a deep copy 
		if(between!=null)this.point = between.p2;
		this.best = other.best;
		this.graph = other.graph;
		this.level = other.level;
		this.isValid =  other.isValid;
		isValid = (this.distanceFromSource==distances[point.id]||distances[point.id]==-1);
		this.cloneChildren(other.children);
	}
	public void cloneChildren(Vector<TreeNode> othersChildren)
	{
		for(TreeNode child : othersChildren)this.children.add(new TreeNode(child));
	}
	public void activate()
	{
		for(Edge e : edges)
		{
			boolean valid;
			if(!closed.contains(e.p2))
			{
				int nweight = e.weight+this.distanceFromSource;
				if(distances[e.p2.id]>nweight || distances[e.p2.id] == -1)valid = true;
				else valid = false;
				TreeNode child = new TreeNode(nweight,distances,graph.edgesForPoint(graph.getEdges(),e.p2),e,closed,open,valid,best,graph,candidates,this);
				children.add(child);
				if (valid)
				{
					distances[e.p2.id] = e.weight+this.distanceFromSource;
					best.put(e.p2.id, child);
				}
			}
		}
	}
	public Path getPath(vPoint from)
	{
		Path p = new Path(point,from,distanceFromSource);
		p.path.addPoint(point);
		TreeNode par = parent;
		Edge route = between;
		while(par!=null)
		{
			p.path.addPoint(route.p1);
			p.path.addEdge(route);
			route=par.between;
			par=par.parent;
		}
		System.out.println("Edges in path ="+p.path.getEdges().size());
		return p;
	}
	@Override
	public int compareTo(TreeNode other)
	{
		if(this.distanceFromSource == other.distanceFromSource)return 0;
		else if(this.distanceFromSource < other.distanceFromSource)return -1;
		else return 1;
	}
	
}