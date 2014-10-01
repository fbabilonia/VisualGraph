package nyc.babilonia.VisualGraph;


public class Path
{
	public Graph path = new Graph();
	public int cost;
	public vPoint from=null,to=null;
	
	public Path(vPoint destination,vPoint f ,int tc)
	{this.to=destination;this.cost = tc; from = f;}
	@Override
	public String toString()
	{return "Path From P" +from.id+ " to P"+to.id+" with a cost of "+this.cost+".";}
}
