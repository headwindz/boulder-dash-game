package uk.ac.ox.comlab.gameapp.model;


/**
 * This interface can be used to receive notifications about the changes in Cave.
 */
public interface CaveListener {
	void caveElementAdded(Cave cave,CaveElement caveElement);
	void caveElementRemoved(Cave cave,CaveElement caveElement);
	void caveElementChanged(Cave cave,CaveElement caveElement);
	void caveElementWillMove(Cave cave,CaveElement caveElement);
	void caveElementMoved(Cave cave,CaveElement caveElement,int oldX,int oldY, int newX, int newY);
	void diamondEaten();
	void gameWon();
	void gameLost();
	void fireNumberConstraintViolated(CaveElement caveElement,int numberConstraint);
}