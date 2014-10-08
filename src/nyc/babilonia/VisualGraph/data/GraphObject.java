package nyc.babilonia.VisualGraph.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;



public class GraphObject
{
	private Graph graph;
	public final int DIRECTED = 0 , UNDIRECTED = 1;
	private int state;
	//default creates undirected graph
	public GraphObject()
	{
		changeGraphType(DIRECTED);
	}
	public GraphObject(int type)
	{
		changeGraphType(type);
	}
	public void changeGraphType(int type)
	{
		if(type == DIRECTED)
			graph = new DirectedGraph();
		else
			graph = new UndirectedGraph();
		state = type;
		System.out.println(type);
	}
	public int getState()
	{
		return state;
	}
	//accessors to allow access to underlying data
	public ArrayList<Point> getPoints()
	{
		return graph.getPoints();
	}
	public Set<Edge> getEdges()
	{
		return graph.getEdges();
	}
	public boolean addEdge(Edge edge)
	{
		return graph.addEdge(edge);
	}
	public boolean addPoint(Point point)
	{
		return graph.addPoint(point);
	}
	public void updateEdge(Edge toUpdate , int newWeight)
	{
		graph.updateEdge(toUpdate, newWeight);
	}
	public void clear()
	{
		graph.clear();
	}
	public void saveGraph(File outFile) throws IOException
	{
		String path = outFile.toString();
		String extension = state == DIRECTED ? ".directed" : ".undirected";
		if(!path.endsWith(extension))
			path+=extension;
		File correctOutFile = new File(path);
		graph.saveGraph(correctOutFile);
		
	}
	public void openGraphFromFile(File graphFile) throws IOException
	{
		String file = graphFile.toString();
		System.out.println(file);
		int type = file.endsWith("undirected") ? UNDIRECTED : DIRECTED ;
		System.out.println(type + " type");
		if(type != state)
		{
			changeGraphType(type);
		}
		graph.openGraphFromFile(graphFile);
	}
	public Map<String, Path> Dijkstras(int source, ArrayList<TreeNode> history)
	{
		return graph.Dijkstras(source, history);
	}
	public void draw(Graphics2D g2d)
	{
		graph.draw(g2d);
	}
	public void draw(Graphics2D g2d, Color pointColor , Color lineColor)
	{
		graph.draw(g2d, pointColor, lineColor);
	}
}
