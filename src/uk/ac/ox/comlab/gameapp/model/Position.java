package uk.ac.ox.comlab.gameapp.model;

/**
 * A position that keeps track of the position of the cave elements in the grid.
 * It's designed deliberately to be immutable as it will be used as keys in 
 * Hashmap<Position,CaveElement> (i.e the representation of the cave).
 */
public class Position {
	protected final int x;
	protected final int y;
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public boolean equals(Object other){
		boolean result = false;
		if(other instanceof Position){
			Position that = (Position)other;
			result = (getX() == that.getX() && getY() == that.getY());
		}
		return result;
	}
	public int hashCode(){
		return x + y;
	}
}
