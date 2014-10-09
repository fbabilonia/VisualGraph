package nyc.babilonia.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * for simplicity everything will be made public
 * @author Fernando Babilonia
 * Class just holds information of current state for
 * use when performing Dijkstra's single source
 * shortest path algorithm.
 */

public class DijkstraData
{
	public int [] distances;
	public Set<Point> open = new HashSet<Point>();
	public Set<Point> closed = new HashSet<Point>();
	
	/**
	 * Constructor for DijkstraData. The start Point is needed in order to calculate starting
	 * distance as 0 for this point. Keeps the distances array, as well as the open and closed sets for 
	 * Dijkstra's
	 * @param point - the starting point for the algorithm. 
	 * @param points - the ArrayList of Points in the graph used to make open Set.
	 */
	public DijkstraData(Point point , ArrayList<Point> points)
	{
		distances = new int[points.size()];
		for(int index = 0 ; index < points.size() ; ++index)
		{
			if(index != (point.id))
				distances[index] = -1;
		}
		open.addAll(points);
	}
	/**
	 * copy constructor for DijkstraData, will make deep copy of all data structures.
	 * @param other - other DijkstraData object to copy from.
	 */
	public DijkstraData(DijkstraData other)
	{
		distances = new int [other.distances.length];
		this.distances = java.util.Arrays.copyOf(other.distances, other.distances.length); //need a deep copy 
		open = new HashSet<Point>(other.open);
		closed = new HashSet<Point>(other.closed);
	}
}
