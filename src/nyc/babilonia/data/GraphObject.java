package nyc.babilonia.data;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Fernando Babilonia
 * GraphObject to be shared by all GUI JPanels in order to facilitate sharing.
 * Will keep track of current graph type and provide methods to call
 * underlying graph method calls.
 * Also adds some logic for the file names when saving and opening graphs.
 */

public class GraphObject
{
	private Graph graph;
	public final int DIRECTED = 1983 , UNDIRECTED = 6891;
	private int state;
	/**
	 * default creates undirected graph
	 */
	public GraphObject()
	{
		changeGraphType(DIRECTED);
	}
	/**
	 * Creates a GraphObject with the type given.
	 * @param type - Type of graph please use one of the constant values of the class.
	 */
	public GraphObject(int type)
	{
		changeGraphType(type);
	}
	/**
	 * Changes the type of the graph being used. takes an int to specify the type.
	 * If an incorrect type is given, nothing is done.
	 * @param type - Type of graph to be converted to.  Please use one of the GraphObject constants.
	 */
	public void changeGraphType(int type)
	{
		if(type == DIRECTED || type == UNDIRECTED)
		{
			if(type == DIRECTED)
				graph = new DirectedGraph();
			else 
				graph = new UndirectedGraph();
			state = type;
		}
	}
	/**
	 * Returns the current state (Type of graph) of the GraphObject
	 * @return - current state of the GraphObject
	 */
	public int getState()
	{
		return state;
	}
	//methods that allow access to underlying data
	/**@see Graph#getPoints()*/
	public ArrayList<Point> getPoints()
	{
		return graph.getPoints();
	}
	/**@see Graph#getEdges()*/
	public Set<Edge> getEdges()
	{
		return graph.getEdges();
	}
	/**@see Graph#addEdge(Edge)*/
	public boolean addEdge(Edge edge)
	{
		return graph.addEdge(edge);
	}
	/**@see Graph#addPoint(Point)*/
	public boolean addPoint(Point point)
	{
		return graph.addPoint(point);
	}
	/**@see Graph#updateEdge(Edge, int)*/
	public void updateEdge(Edge toUpdate , int newWeight)
	{
		graph.updateEdge(toUpdate, newWeight);
	}
	/**@see Graph#clear()*/
	public void clear()
	{
		graph.clear();
	}
	/**
	 * Saves a graph to a file.  If the current file does not end in a graph extension,
	 * the appropriate extension will be added.
	 * @param outFile - The file where the graph will be saved.
	 * @throws IOException
	 */
	public void saveGraph(File outFile) throws IOException
	{
		String path = outFile.toString();
		String extension = state == DIRECTED ? ".directed" : ".undirected";
		if(!path.endsWith(extension))
			path+=extension;
		File correctOutFile = new File(path);
		graph.saveGraph(correctOutFile);
		
	}
	/**
	 * Opens a graph from the given file.  Will check to make sure proper the file 
	 * is of the correct file extension, if not an IOException is thrown.  An IOException
	 * may also be thrown from the underlying graph if the file is not in the correct Format.
	 * If the current type of graph is not the same as the file, then a call to changeGraphType
	 * is made before opening the file.
	 * @param graphFile - the saved graph file.
	 * @throws IOException
	 */
	public void openGraphFromFile(File graphFile) throws IOException
	{
		String file = graphFile.toString();
		System.out.println(file);
		int type;
		if(file.toString().endsWith("undirected"))
			type = UNDIRECTED;
		else if(file.toString().endsWith("directed"))
			type = DIRECTED;
		else
			throw new IOException("Incorrect File Extension");
		if(type != state)
		{
			changeGraphType(type);
		}
		graph.openGraphFromFile(graphFile);
	}
	/**@see Graph#Dijkstras(int, ArrayList)*/
	public Map<String, Path> Dijkstras(int source, ArrayList<TreeNode> history)
	{
		return graph.Dijkstras(source, history);
	}
	/**@see Graph#draw(Graphics2D)*/
	public void draw(Graphics2D g2d)
	{
		graph.draw(g2d);
	}
	/**@see Graph#draw(Graphics2D, Color, Color)*/
	public void draw(Graphics2D g2d, Color pointColor , Color lineColor)
	{
		graph.draw(g2d, pointColor, lineColor);
	}
	/**@see Graph#deletePoint(Point) */
	public void deletePoint(Point point)
	{
		graph.deletePoint(point);
	}
}
