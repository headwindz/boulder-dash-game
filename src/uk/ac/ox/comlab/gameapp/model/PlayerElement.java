package uk.ac.ox.comlab.gameapp.model;

import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * A cave element that represents the player in the game. It contains a reference to the cave,
 * the coordinates coordinates on the grid and the direction of last move
 */
public class PlayerElement extends KeyboardSensitiveElement{
	protected Direction direction;
	/**
	 * a mapping from Keyboard event code to Direction
	 * It's designed for potential extensions. Say if later we decide to 
	 * allow A S D W keys to represent LEFT, DOWN, RIGHT, UP respectively. We
	 * only need to add this extension information to the following map instead of
	 * updating the code body.
	 */
	protected static final HashMap<Integer,Direction> KeyboardToDirection
	= new HashMap<Integer,Direction>();
	static{
		KeyboardToDirection.put(new Integer(KeyEvent.VK_KP_LEFT), Direction.LEFT);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_LEFT), Direction.LEFT);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_KP_RIGHT), Direction.RIGHT);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_RIGHT), Direction.RIGHT);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_KP_UP), Direction.UP);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_UP), Direction.UP);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_KP_DOWN), Direction.DOWN);
		KeyboardToDirection.put(new Integer(KeyEvent.VK_DOWN), Direction.DOWN);
	}
	
	public PlayerElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
		direction = Direction.LEFT;
	}
	public void setDirection(Direction d){
		direction = d;
		cave.fireCaveElementChanged(this);
	}
	public Direction getDirection(){
		return direction;
	}
	public boolean isValidMove(Direction direction){
		int newX = getX() + direction.deltaX;
		int newY = getY() + direction.deltaY;
		//out of the boundary
		if(newX < 0 || newX > cave.getColumn() - 1 || newY < 0 || newY > cave.getRow() - 1){
				return false;
		}
		if(cave.isEmptyAt(newX,newY)){
			return true;
		}
		CaveElement element = cave.elementAt(newX, newY);
		return element.isAccessibleByPlayer(direction);
	}
	public boolean isAccessibleByPlayer(Direction direction) {
		return false;
	}
	public void reactToKeyboard(int keyCode){
		Direction direction = KeyboardToDirection.get(new Integer(keyCode));
		//if a valid key is pressed, interpret it and move in corresponding direction
		if(direction != null){
			setDirection(direction);
			if(isValidMove(direction)){
				move(direction);
			}
		}
	}
	public void resetToGameStart(int gameStartX, int gameStartY){
		moveTo(gameStartX,gameStartY);
		setDirection(Direction.LEFT);
	}
	public void reactToObjectAbove(CaveElement element){
		try{
			GravityBoundElement gb_ele =  (GravityBoundElement)element;
			//if the supplied element is not falling
			if(!gb_ele.isFalling()){
				return;
			}
			else{
				gb_ele.setFallingDirection(Direction.DOWN);
			}
		}
		catch(ClassCastException cce){
			//the supplied element is not of type that can fall, do nothing.
		}
	}
	public void fireElementOccupied(Direction direction){
		cave.fireGameLost();
	}
	public String toString(){
		return "Player";
	}
	public void landsOnEmptySquare() {	
	}
	public void landsOnCaveBottom() {
	}
}
