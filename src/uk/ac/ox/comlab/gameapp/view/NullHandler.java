package uk.ac.ox.comlab.gameapp.view;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * A mouse handler that does nothing. This object implements the Null Object design pattern.
 * It's used when the game is in PALY state, the moment when mouse events make no effect 
 */
public class NullHandler implements MouseHandler{

	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public void mouseDragged(MouseEvent e) {
	}
	public void mouseMoved(MouseEvent e) {
	}
	public void makeActive() {
	}
	public void paint(Graphics2D g) {
	}
}
