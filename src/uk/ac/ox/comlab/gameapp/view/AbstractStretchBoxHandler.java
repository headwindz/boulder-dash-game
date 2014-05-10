package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;



abstract class AbstractStretchBoxHandler implements MouseHandler {
	protected final CaveView caveView;
	/** Holds the original corner of the stretch box. */
	protected Point stretchBoxOrigin;
	/** Holds the dragged corner of the stretch box. */
	protected Point stretchBoxTarget;	
	
	public AbstractStretchBoxHandler(CaveView caveView) {
		this.caveView = caveView;
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin == null) {
			stretchBoxOrigin = e.getPoint();
			stretchBoxTarget = stretchBoxOrigin;
			caveView.repaint();
		}
	}
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1 && stretchBoxOrigin != null) {
			stretchBoxTarget = e.getPoint();
			caveView.repaint();
			boxStretchingFinished(stretchBoxOrigin,stretchBoxTarget);
			stretchBoxOrigin = null;
			stretchBoxTarget = null;
			caveView.repaint();
		}
	}
	public void mouseDragged(MouseEvent e) {
		if (stretchBoxOrigin != null) {
			stretchBoxTarget = e.getPoint();
			caveView.repaint();
		}
	}
	
	public void mouseMoved(MouseEvent e) {
	}
	
	public void makeActive() {
		// Make sure that the handler does not contain stale state.
		stretchBoxOrigin = null;
		stretchBoxTarget = null;
	}
	/** the View's code*/
	public void paint(Graphics2D g) {
		if (stretchBoxOrigin != null) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
			g.setColor(Color.GRAY);
			int gridSize = caveView.getGridSize();
			Cave cave = caveView.getCave();
			int leftTopXCoordinate = Math.min(stretchBoxOrigin.x, stretchBoxTarget.x);
			int leftTopYCoordinate = Math.min(stretchBoxOrigin.y, stretchBoxTarget.y);
			int rightBottomXCoordinate = Math.max(stretchBoxOrigin.x , stretchBoxTarget.x);
			int rightBottomYCoordinate = Math.max(stretchBoxOrigin.y , stretchBoxTarget.y);
			for(int i = leftTopXCoordinate/gridSize; i <= rightBottomXCoordinate/gridSize ; i += 1){
				for(int j = leftTopYCoordinate/gridSize; j <= rightBottomYCoordinate/gridSize; j += 1){
					if(cave.isEmptyAt(i,j)){
						g.fillRect(i*gridSize,j*gridSize, gridSize, gridSize);
					}
					else{
						CaveElement e = cave.elementAt(i, j);
						CaveView.painters.get(e.getClass()).paintSelection(g, e);
					}
				}
			}
		}
	}
	
	protected abstract void boxStretchingFinished(Point boxOrigin, Point boxTarget);
}