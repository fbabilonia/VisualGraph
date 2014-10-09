package outDated;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;


import nyc.babilonia.data.Edge;
import nyc.babilonia.data.Graph;
import nyc.babilonia.data.Path;
import nyc.babilonia.data.Point;

public class TreeNodeold implements Comparable<TreeNodeold>
{
	public TreeNodeold parent;
	public Edge between;
	public boolean isValid;
	public int distanceFromSource;
	public Vector<TreeNodeold> children = new Vector<TreeNodeold>();
	private Set<Edge> edges;
	public Set<Point> closed,open;
	public TreeSet<TreeNodeold> candidates;
	public Graph graph;
	public Map<Integer,TreeNodeold> best;
	public Point point;
	public int level;
	int [] distances;
	public TreeNodeold(int d,int []da,Set<Edge> ed,Edge b,Set<Point> c,Set<Point>o, boolean valid,Map<Integer,TreeNodeold> be,Graph g,TreeSet<TreeNodeold> can,TreeNodeold parent)
	{
		this.distanceFromSource=d;
		this.distances=da;
		this.between = b;
		this.closed = c;
		this.edges=ed;
		this.graph = g;
		this.isValid = valid;
		this.candidates = can;
		if(between != null){this.point = getEdgePartner(between);}
		this.best = be;
		this.open = o;
		this.parent = parent;
		if(parent == null)level = 0;
		else level = parent.level+1;
	}
	@Override
	public boolean equals(Object other)
	{
		TreeNodeold otherp=(TreeNodeold)other;
		return this.point.equals(otherp.point);
	}
	
	@Override
	public String toString()
	{return "P"+point.id+ " with distance from source of "+this.distanceFromSource;}
	private void getStructureRec(ArrayList<ArrayList<TreeNodeold>> structure)
	{
		if(structure.size()<this.level+1 )structure.add(new ArrayList<TreeNodeold>());
		structure.get(level).add(this);
		for(TreeNodeold child : children)
		{
			child.getStructureRec(structure);
		}
	}
	private void updateRec(TreeNodeold node)
	{
		if(parent!=null)this.isValid = parent.isValid && distances[point.id]==this.distanceFromSource;
		else this.isValid=distances[point.id]==this.distanceFromSource;
		for(TreeNodeold child :children)updateRec(child);
	}
	public void update()
	{
		if(parent==null)updateRec(this);
	}
	public ArrayList<ArrayList<TreeNodeold>> getStructure()
	{
		ArrayList<ArrayList<TreeNodeold>> structure = new ArrayList<ArrayList<TreeNodeold>>();
		if(parent ==null)
		{getStructureRec(structure);}
		return structure;
	}
	public TreeNodeold(TreeNodeold other)
	{
		this.parent = other.parent;
		this.distanceFromSource = other.distanceFromSource;
		//this.children = other.children;
		this.point = other.point;
		this.edges = other.edges;
		this.open = new HashSet<Point>(other.open);
		this.candidates = new TreeSet<TreeNodeold>(other.candidates);
		this.closed =  new HashSet<Point>(other.closed);
		this.distances = java.util.Arrays.copyOf(other.distances, other.distances.length); //need a deep copy 
		if(between!=null)this.point = between.point2;
		this.best = other.best;
		this.graph = other.graph;
		this.level = other.level;
		this.isValid =  other.isValid;
		isValid = (this.distanceFromSource==distances[point.id]||distances[point.id]==-1);
		this.cloneChildren(other.children);
	}
	public void cloneChildren(Vector<TreeNodeold> othersChildren)
	{
		for(TreeNodeold child : othersChildren)this.children.add(new TreeNodeold(child));
	}
	private Point getEdgePartner(Edge e)
	{
		if (e.point1.compareTo(this.point) == 0)
			return e.point2;
		else 
			return e.point1;
	}
	public void activate()
	{
		for(Edge e : point.edges)
		{
			boolean valid;
			Point other = getEdgePartner(e);
			if(!closed.contains(other))
			{
				int nweight = e.getWeight()+this.distanceFromSource;
				if(distances[other.id]>nweight || distances[other.id] == -1)valid = true;
				else valid = false;
				TreeNodeold child = new TreeNodeold(nweight,distances,other.edges,e,closed,open,valid,best,graph,candidates,this);
				children.add(child);
				if (valid)
				{
					distances[other.id] = e.getWeight()+this.distanceFromSource;
					best.put(other.id, child);
				}
			}
		}
	}
	public Path getPath(Point from)
	{
		Path p = new Path(point,from,distanceFromSource);
		p.path.addPoint(point);
		TreeNodeold par = parent;
		Edge route = between;
		while(par!=null)
		{
			p.path.addPoint(route.point1);
			p.path.addEdge(route);
			route=par.between;
			par=par.parent;
		}
		System.out.println("Edges in path ="+p.path.getEdges().size());
		return p;
	}
	@Override
	public int compareTo(TreeNodeold other)
	{
		if(this.distanceFromSource == other.distanceFromSource)return 0;
		else if(this.distanceFromSource < other.distanceFromSource)return -1;
		else return 1;
	}
	
}