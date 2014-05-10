package uk.ac.ox.comlab.gameapp.model;

/**
 * A cave element that represents the diamond in the game. It contains a reference to the cave,
 * the coordinates coordinates on the grid and a flag determining whether the object is falling
 */
public class DiamondElement extends GravityBoundElement {

	public DiamondElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
	}
	public boolean isAccessibleByPlayer(Direction direction) {
		return true;
	}
	public void fireElementOccupied(Direction direction) {
		cave.fireDiamondEaten();
	}
}
