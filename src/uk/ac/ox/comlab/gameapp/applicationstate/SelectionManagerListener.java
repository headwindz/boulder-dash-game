package uk.ac.ox.comlab.gameapp.applicationstate;

import java.util.Collection;

import uk.ac.ox.comlab.gameapp.model.CaveElement;


/**
 * Receives information about a change in the application state.
 */
public interface SelectionManagerListener{
	/**
	 * Notifies the listener that the specified objects have been
	 * added to the selection.
	 * 
	 * @param objects				the selected objects
	 */
	void objectsSelected(Collection<? extends CaveElement> objects);
	/**
	 * Notifies the listener that the specified objects have been
	 * removed from the selection.
	 * 
	 * @param objects				the unselected objects
	 */
	void objectsUnselected(Collection<? extends CaveElement> objects);
	/**
	 * Notifies the listener that all objects have been unselected.
	 */
	void selectionCleared();
	/**
	 * Notifies the listener that the current state has changed.
	 * 
	 * @param newCurrentState 		the new current state
	 */
	void currentStateChanged(SelectionManager.States newCurrentState);
	/**
	 * Notifies the listener that the goal has changed.
	 * 
	 * @param goal 					the new goal
	 */
	void currentGoalChanged(int goal);
	/**
	 * Notifies the listener that the invalid input is entered
	 * 
	 * @param message 					the invalid input
	 */
	void invalidInputEntered(String message);
	/**
	 * Notifies the listener that the the cave has changed
	 */
	void currentCaveChanged();
}
