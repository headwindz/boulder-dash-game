package uk.ac.ox.comlab.gameapp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.model.CaveElement;

public class SelectState implements ActionListener, SelectionManagerListener{
	protected final SelectionManager selectionManager;
	protected final AbstractButton button;
	protected final SelectionManager.States state;
	
	public SelectState(SelectionManager stateManager, 
			AbstractButton button, SelectionManager.States state, JPanel contentPane) {
		this.selectionManager = stateManager;
		this.button = button;
		this.state = state;
		stateManager.addListener(this);
		button.addActionListener(this);
		updateButton();
	}
	
	public void actionPerformed(ActionEvent e) {
		//it was in play mode and going to another mode, reset the cave
		if(selectionManager.getCurrentState() == SelectionManager.States.PLAY_STATE){
			selectionManager.reverseCaveToGameStart();
			selectionManager.getCurrentCave().initiateFallingAttributes();
		}
		selectionManager.setCurrentState(state);
		//it is in play mode, record the cave
		if(selectionManager.getCurrentState() == SelectionManager.States.PLAY_STATE){
			selectionManager.reserveCave();
			selectionManager.getCurrentCave().initiateFallingAttributes();
		}
	}
	public void currentStateChanged(SelectionManager.States newCurrentState) {
		updateButton();
	}
	protected void updateButton() {
		button.setSelected(selectionManager.getCurrentState() == state);
	}
	public void objectsSelected(Collection<? extends CaveElement> objects) {
	}
	public void objectsUnselected(Collection<? extends CaveElement> objects) {
	}
	public void selectionCleared() {
	}
	public void currentGoalChanged(int goal) {
	}
	public void invalidInputEntered(String s) {
	}
	public void currentCaveChanged() {
		updateButton();
	}
}
