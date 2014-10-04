package nyc.babilonia.VisualGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//for simplicity everything will be made public
//Class just holds information of current state for
//use when performing Dijkstra's single source
//shortest path algorithm.
public class DijkstraData
{
	int [] distances;
	Point point;
	Set<Point> open = new HashSet<Point>();
	Set<Point> closed = new HashSet<Point>();
	
	public DijkstraData(Point p , ArrayList<Point> points)
	{
		point = p;
		distances = new int[points.size()];
		for(int index = 0 ; index < points.size() ; ++index)
		{
			if(index != (point.id))
				distances[index] = -1;
		}
		open.addAll(points);
		open.remove(p);
	}
	public DijkstraData(DijkstraData other)
	{
		this.distances = java.util.Arrays.copyOf(other.distances, other.distances.length); //need a deep copy 
		open = new HashSet<Point>(other.open);
		closed = new HashSet<Point>(other.closed);
		point = other.point;
	}
}
