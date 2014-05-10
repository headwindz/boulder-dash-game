package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager.States;
import uk.ac.ox.comlab.gameapp.model.BoulderElement;
import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.CaveListener;
import uk.ac.ox.comlab.gameapp.model.DiamondElement;
import uk.ac.ox.comlab.gameapp.model.Direction;
import uk.ac.ox.comlab.gameapp.model.DirtElement;
import uk.ac.ox.comlab.gameapp.model.PlayerElement;
import uk.ac.ox.comlab.gameapp.model.Position;
import uk.ac.ox.comlab.gameapp.model.WallElement;

/**
 * Displays the cave and its elements. It implements the View and the Controller parts of the
 * Model-View-Controller pattern. 
 */
public class CaveView extends JPanel{
	protected static final int SHADOW_WIDTH = 1;
	
	/** 
	 * Assigns a painter to a class. 
	 * If we would to extend with other types of objects, we need to write a specific
	 * Painter class for the element, and add the corresponding map here
	 */
	protected static final Map<Class<? extends CaveElement>,CaveElementPainter> painters = new HashMap<Class<? extends CaveElement>,CaveElementPainter>();
	static {
		painters.put(BoulderElement.class, BoulderElementPainter.INSTANCE);
		painters.put(WallElement.class, WallElementPainter.INSTANCE);
		painters.put(PlayerElement.class, PlayerElementPainter.INSTANCE);
		painters.put(DirtElement.class, DirtElementPainter.INSTANCE);
		painters.put(DiamondElement.class, DiamondElementPainter.INSTANCE);
	}
	/** Assigns a handler to a state. */
	protected final Map<SelectionManager.States,MouseHandler> stateToHandler;
	protected final CaveViewListener caveViewListener;
	protected final SelectionManager selectionManager;
	protected MouseHandler currentMouseHandler;
	protected Cave cave;
	protected int gridSize;
	protected boolean isGameWon;
	protected boolean isGameLost;

	public CaveView(SelectionManager selectionManager,int gridSize) {
		MouseEventForwarder forwarder = new MouseEventForwarder();
		addMouseListener(forwarder);
		addMouseMotionListener(forwarder);
		caveViewListener = new CaveViewListener();
		this.gridSize = gridSize;
		
		/** 
		 * Assigns a handler to a state. 
		 * If we would like to extend with other types of objects, we may need to have a similar
		 * state where we are able to create that kind of object.For example, we are adding a type
		 * of object called ImageElement and need a CREATE_IMAGE_STATE state. First we need to add
		 * this state to the States enumeration is SelectionManager.States. Then we need to create
		 * a corresponding handler for creating such element. Given that i use object creation pattern
		 * here, we need to add new corresponding elements factory (can be called IMAGE_FACTORY)
		 * in CreateElementHandler class. Finally we only need to add this mapping to the folloing map.
		 * Like:  stateToHandler.put(SelectionManager.States.CREATE_IMAGE_STATE, new CreateElementHandler(this,CreateElementHandler.IMAGE_FACTORY));
		 */
		stateToHandler = new HashMap<SelectionManager.States,MouseHandler>();
		stateToHandler.put(SelectionManager.States.EDIT_STATE, new EditHandler(this));
		stateToHandler.put(SelectionManager.States.CREATE_WALL_STATE, new CreateElementHandler(this,CreateElementHandler.WALL_FACTORY));
		stateToHandler.put(SelectionManager.States.CREATE_BOULDER_STATE, new CreateElementHandler(this,CreateElementHandler.BOULDER_FACTORY));
		stateToHandler.put(SelectionManager.States.CREATE_DIAMOND_STATE, new CreateElementHandler(this,CreateElementHandler.DIAMOND_FACTORY));
		stateToHandler.put(SelectionManager.States.CREATE_DIRT_STATE, new CreateElementHandler(this,CreateElementHandler.DIRT_FACTORY));
		stateToHandler.put(SelectionManager.States.CREATE_PLAYER_STATE, new CreateElementHandler(this,CreateElementHandler.PLAYER_FACTORY));
		stateToHandler.put(SelectionManager.States.PLAY_STATE, new NullHandler());
		
		selectionManager.addListener(caveViewListener);
		this.selectionManager = selectionManager;
		cave = selectionManager.getCurrentCave();
		if (cave != null) {
			cave.addCaveListener(caveViewListener);
			setPreferredSize(new Dimension(cave.getCaveSize().width + SHADOW_WIDTH, cave.getCaveSize().height + SHADOW_WIDTH));
		}
		this.gridSize = gridSize;
		isGameWon = false;
		isGameLost = false;
		updateCave();
		updateMouseHandler();
	}
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	public Cave getCave() {
		return cave;
	}
	public int getGridSize(){
		return gridSize;
	}
	protected void updateCave() {
		updateMouseHandler();
		repaint();
	}
	protected void updateMouseHandler() {
		currentMouseHandler = stateToHandler.get(selectionManager.getCurrentState());
		currentMouseHandler.makeActive();
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D)g; 
		// The state of the Graphics object should stay the same after the painting is finished.
		Color oldColor = g2d.getColor();
		Shape oldClip = g2d.getClip();

		// paints the background
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		Dimension caveSize = cave.getCaveSize();
		// paints the shadow behind the cave
		g2d.setColor(Color.BLACK);
		g2d.fillRect(SHADOW_WIDTH, SHADOW_WIDTH, caveSize.width, caveSize.height);
		// paints the cave background
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, caveSize.width, caveSize.height);
		// paints the cave frame
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, caveSize.width, caveSize.height);
		
		// A clip rectangle is like a mask specifying which part of the canvas can be changed.
		// The following call ensures that no subsequent drawing call can draw outside the
		// area taken by the cave.
		g2d.clipRect(1, 1, caveSize.width - 2, caveSize.height - 2);
		
		// Now paint the cave elements. This is done by invoking the painter for each element.
		Iterator<CaveElement> i = cave.caveElements();
		while (i.hasNext()) {
			CaveElement e = i.next();
			painters.get(e.getClass()).paint(g2d, e);
		}
		// Now paint the selection if necessary
		if (!selectionManager.isSelectionEmpty()
			&& selectionManager.getCurrentState() == SelectionManager.States.EDIT_STATE) {
			i = cave.caveElements();
			while (i.hasNext()) {
				CaveElement e = i.next();
				if (selectionManager.isSelected(e)){
					painters.get(e.getClass()).paintSelection(g2d, e);
				}
			}
		}
		// paints the dragged box, if necessary
		currentMouseHandler.paint(g2d);
		// restore the original state of the Graphics object
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
		if(isGameWon){
			drawGameOverNotification(g2d,"Game won!", caveSize);
		}
		if(isGameLost){
			drawGameOverNotification(g2d,"Game lost!", caveSize);
		}
	}
	protected void drawGameOverNotification(Graphics2D g2d, String string,Dimension caveSize){
		g2d.setColor(Color.GRAY);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		g2d.fillRect(60,60, caveSize.width - 120, caveSize.height - 120);
		g2d.setStroke(new BasicStroke(3));
		g2d.setColor(Color.WHITE);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
		g2d.drawRect(70,70, caveSize.width - 140, caveSize.height - 140);
		Font font = new Font("Arial", Font.PLAIN, 70);
		g2d.setFont(font);
		g2d.drawString(string, caveSize.width/2 - 180, caveSize.height/2 + 20);
	}
	
	protected class CaveViewListener implements CaveListener, SelectionManagerListener {

		public void objectsSelected(Collection<? extends CaveElement> objects) {			
		}
		public void objectsUnselected(Collection<? extends CaveElement> objects) {
		}
		public void selectionCleared() {
			repaint();
		}
		public void currentStateChanged(States newCurrentState) {
			updateMouseHandler();
			repaint();
		}
		public void currentCaveChanged() {
			isGameWon = false;
			isGameLost = false;
			updateCave();
		}
		public void currentGoalChanged(int goal) {
			cave.setNumberOfDiamondsLeft(goal);
		}
		public void invalidInputEntered(String s) {
			JOptionPane.showMessageDialog(CaveView.this,
				    " ' " + s + " ' " + " is an invalid input. Please enter a positive integer",
				    "Inane error",
				    JOptionPane.ERROR_MESSAGE);
		}
		public void caveElementAdded(Cave cave, CaveElement caveElement) {
			repaint(caveElement.getX()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					caveElement.getY()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS);
		}
		public void caveElementRemoved(Cave cave, CaveElement caveElement) {
			repaint(caveElement.getX()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					caveElement.getY()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS);
		}
		public void caveElementChanged(Cave cave, CaveElement caveElement) {
			repaint(caveElement.getX()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					caveElement.getY()*gridSize - CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS, 
					gridSize + 2*CaveElementPainter.SELECTION_HANDLES_EXCESS);
		}
		public void caveElementWillMove(Cave cave, CaveElement caveElement) {
			if(cave.elementAt(caveElement.getX(),caveElement.getY()) == caveElement){
				cave.removeCaveElement(caveElement);
			}
		}
		public void updateOldPositionSurrounding(int oldX,int oldY){
			if(cave.isEmptyAt(oldX, oldY) && !cave.isEmptyAt(oldX, oldY - 1)){
				CaveElement elementAbove = cave.elementAt(oldX, oldY - 1);
				elementAbove.landsOnEmptySquare();
			}
		}
		public void updateNewPositionSurrounding(int newX,int newY,CaveElement caveElement){
			//update the above element
			if(!cave.isEmptyAt(newX, newY - 1)){
				CaveElement elementAbove = cave.elementAt(newX, newY - 1);
				elementAbove.landsOn(caveElement);
			}
			//update the supplied element according to type of object below it
			if(!cave.isEmptyAt(newX, newY + 1)){
				CaveElement elementBelow = cave.elementAt(newX, newY + 1);
				caveElement.landsOn(elementBelow);
			}
			else{
				//no element below the supplied element, 2 possibilities: empty or out of boundary
				if(newY == cave.getRow() - 1){
					caveElement.landsOnCaveBottom();
				}
				else{
					caveElement.landsOnEmptySquare();
				}
			}
		}
		public void caveElementMoved(Cave cave, CaveElement caveElement,int oldX,int oldY,int newX,int newY) {
			cave.addCaveElement(caveElement);
			updateOldPositionSurrounding(oldX, oldY);
			updateNewPositionSurrounding(newX,newY, caveElement);
		}
		public void gameWon() {
			isGameWon = true;
			repaint();
		}		
		public void gameLost() {
			isGameLost = true;
			repaint();
		}
		public void fireNumberConstraintViolated(CaveElement caveElement,int numberConstraint) {
			JOptionPane.showMessageDialog(CaveView.this,
				    "More Than " + numberConstraint + " " + caveElement + " " + "Added",
				    "Inane error",
				    JOptionPane.ERROR_MESSAGE);
		}
		public void diamondEaten() {
			selectionManager.setGoal(selectionManager.getGoal() - 1);
			cave.checkGameStatus();
		}
	}
	
	protected class MouseEventForwarder implements MouseListener, MouseMotionListener {
		public void mouseEntered(MouseEvent e) {
			currentMouseHandler.mouseEntered(e);
		}
		public void mouseExited(MouseEvent e) {
			currentMouseHandler.mouseExited(e);
		}
		public void mouseClicked(MouseEvent e) {
			currentMouseHandler.mouseClicked(e);
		}
		public void mousePressed(MouseEvent e) {
			currentMouseHandler.mousePressed(e);
		}
		public void mouseReleased(MouseEvent e) {
			currentMouseHandler.mouseReleased(e);
		}
		public void mouseDragged(MouseEvent e) {
			currentMouseHandler.mouseDragged(e);
		}
		public void mouseMoved(MouseEvent e) {
			currentMouseHandler.mouseMoved(e);
		}
	}
}

