package nyc.babilonia.VisualGraph;

public class SharedGraph
{
	enum graphType {Directed, Undirected};
	private static Graph graph = new DirectedGraph();
	private SharedGraph(){}
	public static void setGraphType(graphType type)
	{
		if(type == graphType.Directed)
			graph = new DirectedGraph();
		else if (type == graphType.Undirected)
			graph = new UndirectedGraph();
	}
}
