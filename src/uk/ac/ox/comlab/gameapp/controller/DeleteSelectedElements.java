package uk.ac.ox.comlab.gameapp.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JButton;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager.States;
import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;

public class DeleteSelectedElements implements ActionListener, SelectionManagerListener{
	protected final SelectionManager selectionManager;
	protected final JButton button;
	
	public DeleteSelectedElements(SelectionManager selectionManager, JButton button){
		this.selectionManager = selectionManager;
		this.button = button;
		selectionManager.addListener(this);
		button.addActionListener(this);
		updateButton();
	}
	public void objectsSelected(Collection<? extends CaveElement> objects) {
		updateButton();
	}
	public void objectsUnselected(Collection<? extends CaveElement> objects) {
	}
	public void selectionCleared() {
		updateButton();
	}
	public void currentStateChanged(States newCurrentState) {
		updateButton();
	}
	public void currentGoalChanged(int goal) {
	}
	public void invalidInputEntered(String s) {
	}
	public void currentCaveChanged() {
		updateButton();
	}
	public void actionPerformed(ActionEvent arg0) {
		Iterator<CaveElement> iterator = selectionManager.getSelection();
		Cave cave = selectionManager.getCurrentCave();
		while (iterator.hasNext()) {
			CaveElement e = iterator.next();
			cave.removeCaveElement(e);
		}
		selectionManager.clear();
	}
	protected void updateButton() {
		button.setVisible(selectionManager.getCurrentState() == SelectionManager.States.EDIT_STATE);
		button.setEnabled(!selectionManager.isSelectionEmpty());
	}
}
