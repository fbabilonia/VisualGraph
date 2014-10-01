package nyc.babilonia.VisualGraph;

public class ChangeTracker
{
	private boolean hasChanges = false;
	private static ChangeTracker _changeObject = new ChangeTracker();;
	private ChangeTracker(){}
	public static ChangeTracker getTracker()
	{
		return _changeObject;
	}
	public synchronized boolean hasChanges()
	{
		return hasChanges;
	}
	public synchronized void changeMade()
	{
			hasChanges = true;
	}
	public synchronized void changeRecognized()
	{
		hasChanges = false;
	}
}
