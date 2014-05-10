package uk.ac.ox.comlab.gameapp.model;

import java.awt.Color;

/**
 * A cave element that represents the wall in the game. It contains a reference to the cave,
 * the coordinates coordinates on the grid and its color which is defaulted to be red
 */

public class WallElement extends CaveElement {
	protected static final Color DEFAULT_COLOR = Color.RED;
	
	protected Color color;
	
	public WallElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
		setColor(DEFAULT_COLOR);
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color newColor) {
		color = newColor;
		fireCaveElementChanged();
	}
	public boolean isAccessibleByPlayer(Direction direction) {
		return false;
	}
	public void reactToObjectAbove(CaveElement element){
		try{
			GravityBoundElement gb_ele =  (GravityBoundElement)element;
			gb_ele.setFalling(false);
		}
		catch(ClassCastException cce){	
		}
	}
	public void resetToGameStart(int gameStartX, int gameStartY){
		moveTo(gameStartX,gameStartY);
	}
	public void fireElementOccupied(Direction direction){
	}
	public void landsOnEmptySquare() {
	}
	public void landsOnCaveBottom() {
	}
}
