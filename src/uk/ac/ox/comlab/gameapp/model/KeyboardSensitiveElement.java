package uk.ac.ox.comlab.gameapp.model;

/**
 * Represents an element of the cave. This is a base class for the elements
 * that can react to keyboard events.
 */
public abstract class KeyboardSensitiveElement extends CaveElement{
	
	public KeyboardSensitiveElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
	}
	/**
	 * whether the object's move is valid, i.e whether the object can move in
	 * a supplied direction
	 */
	public abstract boolean isValidMove(Direction dir);
	/**
	 * what to do in response to the key event.
	 */
	public abstract void reactToKeyboard(int keyCode);
}
