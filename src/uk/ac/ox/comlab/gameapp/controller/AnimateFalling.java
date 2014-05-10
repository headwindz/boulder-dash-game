package uk.ac.ox.comlab.gameapp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.Timer;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager.States;
import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.CaveListener;

public class AnimateFalling implements CaveListener,SelectionManagerListener{
	protected final SelectionManager selectionManager;
	protected final Timer timer;
	protected Cave cave;
	
	public AnimateFalling(SelectionManager sel) {
		this.selectionManager = sel;
		selectionManager.addListener(this);
		cave = selectionManager.getCurrentCave();
		if (cave != null) {
			cave.addCaveListener(this);
		}
		timer = new Timer(2000,new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selectionManager.getCurrentCave().step();
			}
		});
	}
	public void objectsSelected(Collection<? extends CaveElement> objects) {
	}
	public void objectsUnselected(Collection<? extends CaveElement> objects) {
	}
	public void selectionCleared() {
	}
	public void currentStateChanged(States newCurrentState) {
		if(selectionManager.getCurrentState() == SelectionManager.States.PLAY_STATE){
			timer.restart();
		}
		else{
			timer.stop();
		}
	}
	public void currentGoalChanged(int goal) {
	}
	public void invalidInputEntered(String s) {
	}
	public void currentCaveChanged() {
	}
	public void caveElementAdded(Cave cave, CaveElement caveElement) {
	}
	public void caveElementRemoved(Cave cave, CaveElement caveElement) {
	}
	public void caveElementChanged(Cave cave, CaveElement caveElement) {
	}
	public void caveElementWillMove(Cave cave, CaveElement caveElement) {
	}
	public void caveElementMoved(Cave cave, CaveElement caveElement, int oldX,int oldY, int newX, int newY) {
	}
	public void diamondEaten() {
	}
	public void gameWon() {
		timer.stop();
	}
	public void gameLost() {
		timer.stop();
	}
	protected void updateCave() {
	}
	public void fireNumberConstraintViolated(CaveElement caveElement,int numberConstraint) {
	}
}



