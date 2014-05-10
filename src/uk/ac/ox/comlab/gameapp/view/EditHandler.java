package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;


/**
 * A MouseHandler that handles selections of elements.
 */
class EditHandler extends AbstractStretchBoxHandler {
	/** Holds the dragging origin. */
	protected Point dragOrigin;
	/** Holds the dragging target. */
	protected Point dragTarget;

	public EditHandler(CaveView caveView) {
		super(caveView);
	}
	public void mousePressed(MouseEvent e) {
		Cave cave = caveView.getCave();
		// The following line tests whether the dragging or stretching should be started
		if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin == null && dragOrigin == null) {
			CaveElement clickedElement = cave.elementAt(e.getX()/caveView.getGridSize(), e.getY()/caveView.getGridSize());
			// If it's a free square or an object that is not selected
			if (!caveView.getSelectionManager().isSelected(clickedElement)) {
				// ...then call the superclass to start box stretching.
				// Before this is done, adjust the selection according to the status of the SHIFT key.
				if ((e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != MouseEvent.SHIFT_DOWN_MASK)
					caveView.getSelectionManager().clear();
				super.mousePressed(e);
			}
			else if (!caveView.getSelectionManager().isSelectionEmpty()) {
					dragOrigin = e.getPoint();
					dragTarget = e.getPoint();
					caveView.repaint();
			}
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && dragOrigin != null) {
			dragTarget = e.getPoint();
			caveView.repaint();
			dragFinished(dragOrigin,dragTarget);
			dragOrigin = null;
			dragTarget = null;
			caveView.repaint();
		}
		super.mouseReleased(e);
	}
	public void mouseDragged(MouseEvent e) {
		if (dragOrigin != null) {
			dragTarget = e.getPoint();
			caveView.repaint();
		}
		super.mouseDragged(e);
	}
	/**the View's code*/
	public void paint(Graphics2D g) {
		if (dragOrigin != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			g.setColor(Color.GRAY);
			int gridSize = caveView.getGridSize();
			Cave cave = caveView.getCave();
			int moveX = dragTarget.x/gridSize - dragOrigin.x/gridSize;
			int moveY = dragTarget.y/gridSize - dragOrigin.y/gridSize;
			Iterator<CaveElement> iterator = caveView.getSelectionManager().getSelection();
			while(iterator.hasNext()){
				CaveElement element = iterator.next();
				int newX = element.getX() + moveX;
				int newY = element.getY() + moveY;
				if(cave.isEmptyAt(newX,newY)){
					g.fillRect(newX*gridSize,newY*gridSize, gridSize, gridSize);
				}
				else{
					CaveElement e = cave.elementAt(newX, newY);
					CaveView.painters.get(e.getClass()).paintSelection(g, e);
				}
			}
		}
		// Call the superclass to paint the stretching box
		super.paint(g);
	}
	protected void boxStretchingFinished(Point boxOrigin,Point boxTarget) {
		int leftTopXCoordinate = Math.min(boxOrigin.x, boxTarget.x);
		int leftTopYCoordinate = Math.min(boxOrigin.y, boxTarget.y);
		int rightBottomXCoordinate = Math.max(boxOrigin.x, boxTarget.x);
		int rightBottomYCoordinate = Math.max(boxOrigin.y, boxTarget.y);
		int gridSize = caveView.getGridSize();
		for(int i = leftTopXCoordinate/gridSize; i <= rightBottomXCoordinate/gridSize ; i += 1){
			for(int j = leftTopYCoordinate/gridSize; j <= rightBottomYCoordinate/gridSize; j += 1){
				if(!caveView.getCave().isEmptyAt(i, j)){
					caveView.getSelectionManager().toggleObjectSelection(caveView.getCave().elementAt(i, j));
				}
			}
		}
	}
	protected void dragFinished(Point dragOrigin, Point dragTarget) {
		int gridSize = caveView.getGridSize();
		int moveX = dragTarget.x / gridSize - dragOrigin.x / gridSize;
		int moveY = dragTarget.y / gridSize - dragOrigin.y / gridSize;
		int moveWidth = dragTarget.x  - dragOrigin.x ;
		int moveHeight = dragTarget.y  - dragOrigin.y ;
		
		if(isValidMove(moveX,moveY,moveWidth,moveHeight)){
			Iterator<CaveElement> iterator = caveView.getSelectionManager().getSelection();
			while (iterator.hasNext()) {
				CaveElement e = iterator.next();
				e.moveTo(e.getX() + moveX,e.getY()  + moveY);
			}
		}
	}
	/**
	 * check whether all selected objects can be moved to the new position when
	 * dragging is finished
	 */
	protected boolean isValidMove(int moveX, int moveY,int moveWidth,int moveHeigh){
		int row = caveView.getCave().getRow();
		int column = caveView.getCave().getColumn();
		int size = caveView.getGridSize();
		Iterator<CaveElement> i = caveView.getCave().caveElements();
		if(Math.abs(moveWidth) < size && Math.abs(moveHeigh) < size){
			return false;
		}
		while (i.hasNext()) {
			CaveElement e = i.next();
			if (caveView.getSelectionManager().isSelected(e)){
				int x = e.getX()  + moveX ;
				int y = e.getY()  + moveY ;
				if(x < 0 || x > column - 1){
					return false;
				}
				if(y < 0 || y > row - 1){
					return false;
				}
			}
		}
		return true;
	}
}
