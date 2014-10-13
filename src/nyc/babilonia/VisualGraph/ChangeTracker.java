package nyc.babilonia.VisualGraph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
/**
 * 
 * @author Fernando Babilonia
 * Class to keep of any changes made among the different JPanels that might require
 * changes to be saved.  Uses the singleton pattern in order to allow only one
 * instance to be made.
 *
 */
public class ChangeTracker 
{
	private boolean hasChanges = false;
	private static ChangeTracker _changeObject = new ChangeTracker();
	private Vector<ActionListener> listeners = new Vector<ActionListener>();
	private ChangeTracker(){}
	/**
	 * Gets the one instance of the ChangeTracker Object
	 * @return - The sole instance of the ChangeTracker Object
	 */
	public static ChangeTracker getTracker()
	{
		return _changeObject;
	}
	/**
	 * @return - Checks if changes have been registered.
	 */
	public synchronized boolean hasChanges()
	{
		return hasChanges;
	}
	/**
	 * Notifys object that changes have been made to the structure.
	 */
	public synchronized void changeMade()
	{
			hasChanges = true;
			fire();
	}
	/**
	 * In one way or another a change has been recognized and no longer needs to be kept track of.
	 */
	public synchronized void changeRecognized()
	{
		hasChanges = false;
	}
	/**
	 * Adds a listener to this Object.
	 * @param l - The ActionListener being added to the Object.
	 */
	public void addActionListener(ActionListener l)
	{
		listeners.add(l);
	}
	/**
	 * Removes the supplied ActionListener from the list of listeners.
	 * @param l - ActionListener to be removed.
	 */
	public void removeActionListener(ActionListener l)
	{
		listeners.remove(l);
	}
	 
	private void fire()
	{
		ActionEvent event = new ActionEvent(this,(int)Math.random(), "change");
		for(ActionListener l : listeners)
		{
			l.actionPerformed(event);
		}
	}
}
