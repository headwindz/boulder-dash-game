package uk.ac.ox.comlab.gameapp.model;


/**
 * Represents an element of the cave. This is a base class for the falling elements.
 */
public abstract class GravityBoundElement extends CaveElement{
	protected boolean isFalling;
	protected Direction fallingDirection;
	
	public GravityBoundElement(Cave cave, int x, int y, int size) {
		super(cave, x, y, size);
		setFalling(false);
		fallingDirection = Direction.DOWN;
	}
	/**
	 * Even though it's not stated in the problem specification, it makes sense
	 * to believe that a gravity bound element can't slip without be falling.
	 * For example (P,B,W stands for player, boulder and wall respectively):
	 * 			PB
	 * 			 WB
	 * 			  W	
	 * when player pushes the boulder right to it to the right, the cave becomes:
	 * 			 PB
	 * 			 WB
	 * 			  W	
	 * In the case, the boulder on top can't slip to the right because it's not falling.
	 * According to the specification, a gravity bound element begins falling when the
	 * square below it becomes empty and stops falling when it reaches dirt, wall or the bottom
	 * of the cave. Another example of the case is:
	 * 			B					B
	 * 			W		----->		W			(Note:B will not slip here as it's not falling at all!)
	 * 
	 * But
	 * 			B					
	 * 								B						
	 * 			B		----->		B			----->		BB		---->		B
	 * 			W					W						W					WB		(Note: B slips bacase it's falling)
	 * 
	 * 
	 * Another thing i would like to clarify here is the slipping of the gravity bound element.
	 * In the problem specification, it says that the gravity bound element can slip when
	 * the squre to the right(left) of the falling object is empty and if the square to the lower
	 * right(left) of the falling is empty. Here I treat "empty" as the real empty square, which means
	 * that a square containing the player (even though it's valid for the element to go inside that square)
	 * is not considered as "empty". For example
	 * 
	 * 			WB						W
	 * 			W						WB
	 * 			WBP			------->	WBP			(Note: B will not slip because lower right is not empty and thus the falling stops)
	 * 			WWWWWW					WWWWWW
	 * 
	 * 
	 * A special scenario is that: because we animate the falling objects in a predefined time interval.
	 * If the player moves into the area in the time interval, the players should be caught and gamve should end.
	 * For example
	 * 
	 * 			WB						W
	 * 			W						WB
	 * 			WB P		------->	WB P		
	 * 			WWWWWW					WWWWWW
	 * (Note: The B on the top will slip to the lower right. If during the time interval(Before B finishes the movement
	 * to lower right)), P takes a step left, then P will be hit by the B.
	 */
	public void reactToObjectAbove(CaveElement element){
		try{
			GravityBoundElement gb_ele =  (GravityBoundElement)element;
			int x = gb_ele.getX();
			int y = gb_ele.getY();
			//if the supplied element is not falling
			if(!gb_ele.isFalling()){
				return;
			}
			if((x+1) < cave.column && cave.isEmptyAt(x + 1, y) && 
					cave.isEmptyAt(x + 1, y + 1)){
				gb_ele.setFalling(true);
				gb_ele.setFallingDirection(Direction.BOTTOM_RIGHT);
			}
			else if((x-1) >= 0 && cave.isEmptyAt(x - 1, y) &&
					cave.isEmptyAt(x - 1, y + 1)){
				gb_ele.setFalling(true);
				gb_ele.setFallingDirection(Direction.BOTTOM_LEFT);
			}
			else{
				gb_ele.setFalling(false);
			}	
		}
		catch(ClassCastException cce){
			//the supplied element is not of type that can fall, do nothing.
		}
	}
	public  boolean isFalling(){
		return isFalling;
	}
	public void setFalling(boolean isFalling){
		this.isFalling = isFalling;
	}
	public void setFallingDirection(Direction fallingDirection){
		this.fallingDirection = fallingDirection;
	}
	public Direction getFallingDirectioin(){
		return fallingDirection;
	}
	public void landsOnEmptySquare() {
		fallingDirection = Direction.DOWN;
		setFalling(true);
	}
	public void landsOnCaveBottom(){
		setFalling(false);
	}
	public void resetToGameStart(int gameStartX, int gameStartY){
		setFalling(false);
		moveTo(gameStartX,gameStartY);
	}
}
