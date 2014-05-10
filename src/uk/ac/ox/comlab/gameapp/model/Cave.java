package uk.ac.ox.comlab.gameapp.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a cave as a collection of wall, dirt, diamond, boulder
 * and player elements. It implements the Observable pattern and thus 
 * can be used as part of the Model-View-Controller pattern.  
 */
public class Cave {
	/** 
	 * a collectioin of class types that can fall so that we can identify them
	 * easily on the cave. To extend with new kinds of objects we need to update
	 * it correspondingly depending on whether the new kinds of objects can fall
	 * or not
	 */
	protected static final HashSet<Class<? extends CaveElement>> fallingTypes
	= new HashSet<Class<? extends CaveElement>>();
	static {
		fallingTypes.add(BoulderElement.class);
		fallingTypes.add(DiamondElement.class);
	}
	
	/** 
	 * a collection of class types that can react to keyboard event 
	 * so that we can identify them easily on the cave.To extend with new kinds 
	 * of objects we need to update it correspondingly depending on whether 
	 * the new kinds of objects can react to keyboard event
	 */
	protected static final HashSet<Class<? extends CaveElement>> reactToKeyboardTypes
	= new HashSet<Class<? extends CaveElement>>();
	static {
		reactToKeyboardTypes.add(PlayerElement.class);
	}
	
	/** 
	 * a mapping from class types with their number constraints on the cave.
	 * To extend with new kinds of objects we need to update it correspondingly depending on whether 
	 * the new kinds of objects have number limits. Say if we are going to
	 * add a kind of element called ImageElement and pose a limit that there can at most
	 * be 2 ImageElement(s) on the cave.Then we add to add the following code:
	 * constraints.put(ImageElement.Class , 2)
	 */
	protected static final HashMap<Class<? extends CaveElement>, Integer> constraints
	= new HashMap<Class<? extends CaveElement>,Integer>();
	static {
		constraints.put(PlayerElement.class,1);
	}
	
	/** Contains all registered cave listeners. */
	protected final List<CaveListener> caveListeners;
	/** 
	 * Contains all elements on the cave. Here i used the hashmap data structure
	 * because it's easy and efficient in search, i.e we can easily find an element
	 * with the position pair (x,y) where x and y are the coordinates of the element on the grid
	 * correspondingly. Another reason is that we can ensure that there is only one
	 * element on one grid square.
	 */
	protected final HashMap<Position,CaveElement> caveElements;
	/** Contains the size of the cave */
	private Dimension caveSize;
	/** The outstanding number of diamonds to collect to win*/
	protected int numberOfDiamondsLeft;
	/** 
	 * The cave is understood as an n * m grid
	 * row is the number of rows the grid contains
	 * column is the number of columns the grid contains
	 */
	protected int row;
	protected int column;
	
	public Cave(int gridSize) {
		caveListeners = new ArrayList<CaveListener>();
		caveElements = new HashMap<Position,CaveElement>();
		numberOfDiamondsLeft = 0;
		row = 10;
		column = 20;
		caveSize = new Dimension(column * gridSize, row * gridSize);
	}
	
	/**
	 * Returns the size of this cave. Given that Dimension is a mutable object,
	 * clients may call getCaveSize() and then change the returned object without
	 * cave's notice. In order to solving this unexpected aliasing problems, 
	 * we keep a private internal copy of caveSize and never return it to clients
	 */
	public Dimension getCaveSize(){
		return new Dimension(caveSize.width,caveSize.height);
	}
	public int getNumberOfDiamondsLeft(){
		return numberOfDiamondsLeft;
	}	
	public void setNumberOfDiamondsLeft(int n){
		numberOfDiamondsLeft = n;
	}
	public int getRow(){
		return row;
	}
	public int getColumn(){
		return column;
	}
	public Iterator<CaveElement> caveElements() {
		return caveElements.values().iterator();
	}
	
	/** calculate the available number of the kind of supplied object
	 *  that can still be added to the cave
	 */
	public int calculateAvailableNumber(CaveElement element){
		Integer numberConstraint = constraints.get(element.getClass());
		//this type of object has number constraint
		if(numberConstraint!= null){
			int numberOfExists = 0;
			Iterator<CaveElement> iterator = caveElements();
			while(iterator.hasNext()){
				CaveElement e = iterator.next();
				if(e.getClass() == element.getClass()){
					numberOfExists++;
				}
			}
			return numberConstraint - numberOfExists;
		}
		//no constraint on the type of objects
		return Integer.MAX_VALUE;
	}
	
	public void addCaveElement(CaveElement element) {
		if(calculateAvailableNumber(element) > 0){
			Position position = new Position(element.getX(),element.getY());
			caveElements.put(position, element);
			fireCaveElementAdded(element);
		}
		else{
			//adding an invalid object, violating the number constraint
			fireNumberConstraintViolated(element,constraints.get(element.getClass()));
		}
	}
	public void addCaveElements(List<CaveElement> list) {
		if(list == null || list.size() <= 0){
			return;
		}
		// supplied list is not null and not empty
		CaveElement firstElement = list.get(0);
		int numberAvailable = calculateAvailableNumber(firstElement);
		if(numberAvailable >= list.size()){
			for (CaveElement e : list)
				addCaveElement(e);
		}
		else{
			//adding an invalid object, violating the number constraint
			fireNumberConstraintViolated(firstElement,constraints.get(firstElement.getClass()));
		}
	}
	public void removeCaveElement(CaveElement element) {
		Position position = new Position(element.getX(),element.getY());
		caveElements.remove(position);
		fireCaveElementRemoved(element);
	}
	public void removeCaveElements(List<CaveElement> list) {
		for (CaveElement e : list)
			removeCaveElement(e);
	}
	public void addCaveListener(CaveListener l) {
		caveListeners.add(l);
	}
	public void removeCaveListener(CaveListener l) {
		caveListeners.remove(l);
	}
	/**
	 * Fires the "element added" event -- i.e, it notifies the registered listeners that
	 * the supplied element has been added to the cave.
	 */
	protected void fireCaveElementAdded(CaveElement e) {
		for (CaveListener l : caveListeners)
			l.caveElementAdded(this, e);
	}
	/**
	 * Fires the "element removed" event -- i.e, it notifies the registered listeners that
	 * the supplied element has been removed from the cave.
	 */
	protected void fireCaveElementRemoved(CaveElement e) {
		for (CaveListener l : caveListeners)
			l.caveElementRemoved(this, e);
	}
	/**
	 * Fires the "element changed" event -- i.e, it notifies the registered listeners that
	 * the supplied element has changed. The view will typically respond to this event by repainting
	 * the part of the screen covered by the element. 
	 */
	protected void fireCaveElementChanged(CaveElement e){
		for (CaveListener l : caveListeners)
			l.caveElementChanged(this,e);
	}
	/**
	 * Fires the "diamond occupied by player" event -- i.e, it notifies the registered listeners that
	 * the diamond has been eaten by player.
	 */
	protected void fireDiamondEaten(){
		for (CaveListener l : caveListeners)
			l.diamondEaten();
	}
	/**
	 * Fires the "number constraint violated" event -- i.e, it notifies the registered listeners that
	 * the number constraint of the supplied object type has been violated.
	 */
	public void fireNumberConstraintViolated(CaveElement element, int numberConstraint){
		for (CaveListener l : caveListeners)
			l.fireNumberConstraintViolated(element,numberConstraint);
	}
	/**
	 * Fires the "game won" event -- i.e, it notifies the registered listeners that
	 * the the game has been won and over.
	 */
	protected void fireGameWon() {
		for (CaveListener l : caveListeners)
			l.gameWon();
	}
	/**
	 * Fires the "game lost" event -- i.e, it notifies the registered listeners that
	 * the the game has been lost and over.
	 */
	protected void fireGameLost() {
		for (CaveListener l : caveListeners)
			l.gameLost();
	}
	/**
	 * Fires the "element will move" event -- that is, it notifies the registered listeners that
	 * the supplied element will move.
	 */
	protected void fireCaveElementWillMove(CaveElement e) {
		for (CaveListener l : caveListeners)
			l.caveElementWillMove(this,e);
	}
	/**
	 * Fires the "element moved" event -- that is, it notifies the registered listeners that
	 * the supplied element has moved.
	 */
	protected void fireCaveElementMoved(CaveElement e,int oldX, int oldY, int newX, int newY) {
		for (CaveListener l : caveListeners)
			l.caveElementMoved(this,e, oldX, oldY, newX, newY);
	}
	/** determine whether the supplied element is on the cave
	 *  based on identity
	 */
	public boolean contains(CaveElement e){
		return caveElements.containsValue(e);
	}
	public boolean isEmptyAt(int x, int y){
		return !caveElements.containsKey(new Position(x,y));
	}
	public CaveElement elementAt(int x, int y){
		return caveElements.get(new Position(x,y));
	}
	public void checkGameStatus(){
		if(numberOfDiamondsLeft == 0){
			fireGameWon();
		}
	}
	/** 
	 * get all the falling elements on the cave and sort them
	 * in specified order, i.e from bottom to top and from left to right
	 * For example, if we have the following 4 falling objects.(B stands for boulder)
	 * 
	 * 			B1		B4
	 * 
	 * 			B3		B2
	 * 
	 * Then after the sorting, the falling order will be: B3  B2  B1 B4
	 * 
	 */
	public Iterator<CaveElement> getFallingElements(){
		List<CaveElement> fallingElements = new ArrayList<CaveElement>();
		Iterator<CaveElement> iterator = caveElements();
		while(iterator.hasNext()){
			CaveElement element = iterator.next();
			if(fallingTypes.contains(element.getClass())){
				fallingElements.add(element);
			}
		}
		//Sort the falling objects, i.e from bottom to top and from left to right
		//so that the order in which objects are added have no effect on how objects
		//are animated
		Collections.sort(fallingElements, new Comparator<CaveElement>(){
			public int compare(CaveElement e1, CaveElement e2){
				if(e1.getY() > e2.getY()){
					return -1;
				}
				if (e1.getY() == e2.getY()){
					if(e1.getX() < e2.getX()){
						return -1;
					}
				}
				return 1;
			}
		});
		return fallingElements.iterator();
	}
	/** get all the elements that can react to keyboard event*/
	public Iterator<CaveElement> getKeyboardSensitiveElements(){
		List<CaveElement> keyboardSensitiveObjects = new ArrayList<CaveElement>();
		Iterator<CaveElement> iterator = caveElements();
		while(iterator.hasNext()){
			CaveElement element = iterator.next();
			if(reactToKeyboardTypes.contains(element.getClass())){
				keyboardSensitiveObjects.add(element);
			}
		}
		return keyboardSensitiveObjects.iterator();
	}
	/**
	 * Determine the falling objects are falling or not and if falling which direction
	 * to fall. It's called at the beginning of the game, i.e when we enter the play
	 * state.
	 */
	public void initiateFallingAttributes(){
		Iterator<CaveElement> iterator = getFallingElements();
		while(iterator.hasNext()){
			GravityBoundElement fallingObject = (GravityBoundElement)iterator.next();
			int x = fallingObject.getX();
			int y = fallingObject.getY();
			// the below is empty and not outside the boundary of the cave
			if(isEmptyAt(x,y + 1) &&  (y+1) < row){
				fallingObject.landsOnEmptySquare();
			}
			else{
				fallingObject.setFalling(false);
			}
		}
	}
	/** 
	 * 'step' method that iterates through all falling objects in the cave and move
	 * them in accordance with the game rules. It's called in 2 seconds interval
	 */
	public void step(){
		Iterator<CaveElement> iterator = getFallingElements();
		while(iterator.hasNext()){
			GravityBoundElement fallingObject = (GravityBoundElement)iterator.next();
			if(fallingObject.isFalling()){
				fallingObject.move(fallingObject.getFallingDirectioin());
			}
		}
	}
}
