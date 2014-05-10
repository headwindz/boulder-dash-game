package uk.ac.ox.comlab.gameapp.model;

/**
 * Represents an element of the cave. This is a base class for the hierarchy of cave elements.
 */
public abstract class CaveElement{
	protected final Cave cave;
	protected int currentX;
	protected int currentY;
	protected int gridSize;
	
	public CaveElement(Cave cave, int currentX, int currentY, int gridSize){
		this.cave = cave;
		this.currentX = currentX;
		this.currentY = currentY;
		this.gridSize = gridSize;
	}
	public int getX(){
		return currentX;
	}
	public int getY(){
		return currentY;
	}
	public int getSize(){
		return gridSize;
	}
	/** what happens when the object lands on the supplied element*/
	public void landsOn(CaveElement element){
		element.reactToObjectAbove(this);
	}
	
	/** Forwards the notification to the associated cave.*/
	protected void fireCaveElementWillMove() {
		cave.fireCaveElementWillMove(this);
	}
	/**Forwards the notification to the associated cave.*/
	protected void fireCaveElementMoved(int oldX,int oldY,int newX, int newY) {
		cave.fireCaveElementMoved(this,oldX,oldY,newX,newY);
	}
	/**Forwards the notification to the associated cave.*/
	protected void fireCaveElementChanged(){
		cave.fireCaveElementChanged(this);
	}
	/**
	 * Moves the cave element to the specified coordinates. 
	 * 
	 * @param x			the new X coordinate of the element
	 * @param y			the new Y coordinate of the element
	 */
	public void moveTo(int x, int y){
		int oldX = currentX;
		int oldY = currentY;
		fireCaveElementWillMove();
		this.currentX = x;
		this.currentY = y;
		fireCaveElementMoved(oldX,oldY,currentX,currentY);
	}
	
	/** Moves the cave element in the specified direction. */
	public void move(Direction direction){
		int newX = getX() + direction.deltaX;
		int newY = getY() + direction.deltaY;
		CaveElement occupied = cave.elementAt(newX,newY);
		moveTo(newX, newY);
		if(occupied != null){
			occupied.fireElementOccupied(direction);
		}
	}
	
	/** what happens when the supplied element lands on the object*/
	public abstract void reactToObjectAbove(CaveElement element);
	/** 
	 *  check whether the grid the object is at is accessible by the player by 
	 *  taking a move in the supplied direction. E.g a BouldeElement boulder is 
	 *  on the right of the player, then boulder.isAccessibleByPlayer(Direction.LEFT) will 
	 *  return the boolean showing whether the player can go into the boulder by taking 
	 *  a move left
	 *  
	 * @param direction			the direction the player takes
	 */
	public abstract boolean isAccessibleByPlayer(Direction direction);
	/** 
	 *  what happends when the object is occupied by the player by taking a move in the 
	 *  supplied direction
	 *  
	 * @param direction			the direction the player takes
	 */
	public abstract void fireElementOccupied(Direction direction);
	/** 
	 *  reset the element to their initial positions and states when game starts
	 *  
	 * @param direction			the direction the player takes
	 */
	public abstract void resetToGameStart(int gameStartX, int gameStartY);
	/** what happens when the object lands on an empty square*/
	public abstract void landsOnEmptySquare();
	/** what happens when the object lands on an empty square*/
	public abstract void landsOnCaveBottom();
	
}

