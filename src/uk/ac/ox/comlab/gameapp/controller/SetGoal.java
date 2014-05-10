package uk.ac.ox.comlab.gameapp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JTextField;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.model.CaveElement;

public class SetGoal implements ActionListener, SelectionManagerListener {
	protected final SelectionManager selectionManager;
	protected final JTextField goalNumberField;
	
	public SetGoal(SelectionManager selectionManager, JTextField goalNumberField) {
		this.selectionManager = selectionManager;
		this.goalNumberField = goalNumberField;
		selectionManager.addListener(this);
		goalNumberField.addActionListener(this);
		updateTextField();
	}
	public void actionPerformed(ActionEvent e) {
		String goalNumberText = goalNumberField.getText();
		try {
			int numberOfDiamondsLeft = Integer.parseInt(goalNumberText);
			if(numberOfDiamondsLeft <= 0){
				throw new Exception("Invalid input. Please enter again");
			}
			selectionManager.setGoal(numberOfDiamondsLeft);
		}
		catch (Exception nfe) {
			selectionManager.fireInvalidInput(goalNumberText);
		}
	}
	public void objectsSelected(Collection<? extends CaveElement> objects) {
	}
	public void objectsUnselected(Collection<? extends CaveElement> objects) {
	}
	public void selectionCleared() {
	}
	public void currentStateChanged(SelectionManager.States newCurrentState) {
		updateTextField();
	}
	public void currentGoalChanged(int newCurrentPage) {
		updateTextField();
	}
	public void invalidInputEntered(String invalidInput) {
	}	
	protected void updateTextField() {
		goalNumberField.setEnabled(selectionManager.getCurrentState() == SelectionManager.States.EDIT_STATE);
		goalNumberField.setText(String.valueOf(selectionManager.getGoal()));
		goalNumberField.selectAll();
	}
	public void currentCaveChanged() {
		updateTextField();
	}

}
