package uk.ac.ox.comlab.gameapp.view;

import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.CaveElement;

/**
 * We have a specific painter for each cave element. If the application is extended with
 * new kinds of objects, we need to have specific painters for those kinds of objects by
 * extending CaveElementPainter. 
 * 
 * The painter draws the cave elements. It implements the View of the
 * Model-View-Controller pattern. 
 */
public abstract class CaveElementPainter {
	/** Determines the amount by which the selection handles go outsize the shape. */
	public static final int SELECTION_HANDLES_EXCESS = 3;
	public abstract void paint(Graphics2D g, CaveElement e);
	public abstract void paintSelection(Graphics2D g, CaveElement e);
}
