package uk.ac.ox.comlab.gameapp.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JPanel;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager.States;
import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.CaveListener;
import uk.ac.ox.comlab.gameapp.model.KeyboardSensitiveElement;

public class ReactToKeyboard implements CaveListener,KeyListener, SelectionManagerListener{
	protected final SelectionManager selectionManager;
	protected final JPanel contentPane;
	protected Cave cave;
	
	public ReactToKeyboard(SelectionManager selectionManager, JPanel contentPane) {
		this.selectionManager = selectionManager;
		this.contentPane = contentPane;
		selectionManager.addListener(this); 
		contentPane.addKeyListener(this);
		cave = selectionManager.getCurrentCave();
		if (cave != null) {
			cave.addCaveListener(this);
		}
		updateKeyboardFocus();
	}	
	public void currentStateChanged(States newCurrentState) {
		updateKeyboardFocus();
	}
	public void keyReleased(KeyEvent e) {
		Iterator<CaveElement> elements = selectionManager.getCurrentCave().getKeyboardSensitiveElements();
		int keyCode = e.getKeyCode();
		while(elements.hasNext()){
			KeyboardSensitiveElement keyboardElement = (KeyboardSensitiveElement)elements.next();
			keyboardElement.reactToKeyboard(keyCode);
		}
	}
	public void updateKeyboardFocus(){
		if(selectionManager.getCurrentState() == SelectionManager.States.PLAY_STATE){
			contentPane.setFocusable(true);
			contentPane.requestFocusInWindow();
		}
	}
	public void gameWon() {
		contentPane.setFocusable(false);
	}
	public void gameLost() {
		contentPane.setFocusable(false);
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
		updateCave();
	}
	public void keyPressed(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
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
	protected void updateCave() {
	}
	public void fireNumberConstraintViolated(CaveElement caveElement,int numberConstraint) {	
	}
}
