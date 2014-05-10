package uk.ac.ox.comlab.gameapp.model;

/**
 * A cave element that represents the dirt in the game. It contains a reference to the cave,
 * and the coordinates coordinates on the grid.
 */
public class DirtElement extends CaveElement {
	
	public DirtElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
	}
	public boolean isAccessibleByPlayer(Direction direction) {
		return true;
	}
	public void reactToObjectAbove(CaveElement ele){
		try{
			GravityBoundElement gb_ele =  (GravityBoundElement)ele;
			gb_ele.setFalling(false);
		}
		catch(ClassCastException cce){	
		}
	}
	public void resetToGameStart(int gameStartX, int gameStartY){
		moveTo(gameStartX,gameStartY);
	}
	public void fireElementOccupied(Direction direction) {
	}
	public void landsOnEmptySquare() {
	}
	public void landsOnCaveBottom() {
	}
}
