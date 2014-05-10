package uk.ac.ox.comlab.gameapp.model;

/**
 * A cave element that represents the boulder in the game. It contains a reference to the cave,
 * the coordinates coordinates on the grid and a flag determining whether the object is falling
 */
public class BoulderElement extends GravityBoundElement {
	
	public BoulderElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
	}
	public boolean isAccessibleByPlayer(Direction direction) {
		int newX = getX() + direction.deltaX;
		int newY = getY() + direction.deltaY;
		//out of boundary
		if(newX < 0 || newX > cave.getColumn() - 1){
			return false;
		}
		boolean LeftOrRight = direction == Direction.LEFT || direction == Direction.RIGHT;
		return LeftOrRight && cave.isEmptyAt(newX,newY);
	}
	public void fireElementOccupied(Direction direction) {
		int newX = getX() + direction.deltaX;
		int newY = getY() + direction.deltaY;
		moveTo(newX,newY);
	}
}
