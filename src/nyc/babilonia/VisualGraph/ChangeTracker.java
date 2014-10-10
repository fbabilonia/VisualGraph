package nyc.babilonia.VisualGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ChangeTracker 
{
	private boolean hasChanges = false;
	private static ChangeTracker _changeObject = new ChangeTracker();
	private Vector<ActionListener> listeners = new Vector<ActionListener>();
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
			fire();
	}
	public synchronized void changeRecognized()
	{
		hasChanges = false;
	}
	public void addActionListener(ActionListener l)
	{
		listeners.add(l);
	}
	public void removeActionListener(ActionListener l)
	{
		listeners.remove(l);
	}
	public void fire()
	{
		ActionEvent event = new ActionEvent(this,(int)Math.random(), "change");
		System.out.println(event.getActionCommand());
		for(ActionListener l : listeners)
		{
			l.actionPerformed(event);
		}
	}
}
