package uk.ac.ox.comlab.gameapp.model;

public enum Direction {
	LEFT(-1, 0), DOWN(0, 1), RIGHT(1, 0), UP(0, -1), BOTTOM_LEFT(-1,1),BOTTOM_RIGHT(1,1);

	public final int deltaX;
	public final int deltaY;
	
	private Direction(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
}
