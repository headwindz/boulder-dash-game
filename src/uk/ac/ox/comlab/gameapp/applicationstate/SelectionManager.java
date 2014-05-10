package uk.ac.ox.comlab.gameapp.applicationstate;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.Position;

/**
 * This class keeps track of the application state which consists
 * of the currently selected objects and the currently selected
 * application mode. It implements the
 * Observer pattern and can therefore be used as a model in a
 * Model-View-Controller framework.
 */
public class SelectionManager {
	/** 
	 * Enumerates all different types of states. 
	 * If we would like to extend with other types of objects, we may need to have a similar
	 * state where we are able to create that kind of object. For example, we are adding a type
	 * of object called ImageElement, we may need a state called CREATE_IMAGE_STATE. To implement
	 * it, we add the state to the enum States.
	 */
	public static enum States { EDIT_STATE, CREATE_WALL_STATE, CREATE_BOULDER_STATE,
		CREATE_DIAMOND_STATE,CREATE_DIRT_STATE,CREATE_PLAYER_STATE,PLAY_STATE}
	
	protected final List<SelectionManagerListener> listeners;
	protected final Set<CaveElement> selection;
	protected Cave currentCave;
	protected States currentState;
	protected int goal;
	protected int goalAtGameStart;
	/** store the elements and their initial positions */
	protected final Map<CaveElement,Position> caveAtGameStart ;
	
	public SelectionManager(int gridSize) {
		listeners = new ArrayList<SelectionManagerListener>();
		selection = new HashSet<CaveElement>();
		currentState = States.PLAY_STATE;
		currentCave = new Cave(gridSize);
		setGoal(1);
		goalAtGameStart = goal;
		caveAtGameStart = new HashMap<CaveElement,Position>();
	}
	public int getGoal(){
		return goal;
	}
	public void setGoal(int goal){
		this.goal = goal;
		for (SelectionManagerListener l : listeners)
			l.currentGoalChanged(goal);
	}
	public Cave getCurrentCave(){
		return currentCave;
	}
	public Iterator<CaveElement> getSelection() {
		return selection.iterator();
	}
	/**
	 * reset the cave and move all objects to their initial positions. 
	 * fires the "cave changed" event -- i.e, it notifies the registered listeners that
	 * the cave has been changed.
	 */
	public void reverseCaveToGameStart(){
		Iterator<CaveElement> iterator = caveAtGameStart.keySet().iterator();
		while (iterator.hasNext()) {
			CaveElement element = iterator.next();
			Position position = caveAtGameStart.get(element);
			element.resetToGameStart(position.getX(), position.getY());
			if(!currentCave.contains(element)){
				currentCave.addCaveElement(element);
			}
		}
		//reset the goal to the time when game starts
		setGoal(goalAtGameStart);
		for (SelectionManagerListener l : listeners)
			l.currentCaveChanged();
	}
	
	/**
	 * record the cave's state when game starts, including the initial
	 * positions of all cave elements and the goal.
	 */
	public void reserveCave(){
		caveAtGameStart.clear();
		Iterator<CaveElement> iterator = currentCave.caveElements();
		while(iterator.hasNext()){
			CaveElement e = iterator.next();
			caveAtGameStart.put(e, new Position(e.getX(),e.getY()));
		}
		goalAtGameStart = goal;
	}
	public Map<CaveElement,Position> getCaveAtGameStart(){
		return caveAtGameStart;
	}
	public boolean isSelectionEmpty() {
		return selection.isEmpty();
	}
	public boolean isSelected(CaveElement object) {
		return selection.contains(object);
	}
	public void addListener(SelectionManagerListener l) {
		listeners.add(l);
	}
	public void removeListener(SelectionManagerListener l) {
		listeners.remove(l);
	}
	public void selectObject(CaveElement object) {
		if (selection.add(object))
			fireObjectsSelected(Collections.singleton(object));
	}
	public void selectObjects(Collection<? extends CaveElement> objects) {
		selection.addAll(objects);
		fireObjectsSelected(objects);
	}
	protected void fireObjectsSelected(Collection<? extends CaveElement> objects) {
		for (SelectionManagerListener l : listeners)
			l.objectsSelected(objects);
	}
	public void unselectObject(CaveElement object) {
		if (selection.remove(object))
			fireObjectsUnselected(Collections.singleton(object));
	}
	public void unselectObjects(Collection<? extends CaveElement> objects) {
		selection.removeAll(objects);
		fireObjectsUnselected(objects);
	}
	protected void fireObjectsUnselected(Collection<? extends CaveElement> objects) {
		for (SelectionManagerListener l : listeners)
			l.objectsUnselected(objects);
	}
	public void toggleObjectSelection(CaveElement object) {
		if (isSelected(object))
			unselectObject(object);
		else
			selectObject(object);
	}
	public void clear() {
		selection.clear();
		for (SelectionManagerListener l : listeners)
			l.selectionCleared();
	}
	public States getCurrentState() {
		return currentState;
	}
	public void setCurrentState(States newCurrentState) {
		currentState = newCurrentState;
		for (SelectionManagerListener l : listeners)
			l.currentStateChanged(newCurrentState);
	}
	/**
	 * fires the "invalid input" event -- i.e, it notifies the registered listeners that
	 * the the input entered by clients is invalid
	 */
	public void fireInvalidInput(String s){
		for (SelectionManagerListener l : listeners)
			l.invalidInputEntered(s);
	}
}
