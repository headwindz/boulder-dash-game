package uk.ac.ox.comlab.gameapp.view;

import java.awt.Graphics2D;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Handles mouse events of the view. This class implements the
 * State pattern. Concrete states will subclass this class
 * and provide appropriate handling for different states. 
 */

interface MouseHandler extends MouseListener, MouseMotionListener {
	/**
	 * Called by the view when the handler becomes active.
	 */
	void makeActive();
	/**
	 * Called by the view to paint the handler in its current state.
	 * 
	 * @param g 		the graphics object on which painting is performed
	 */
	void paint(Graphics2D g);
}