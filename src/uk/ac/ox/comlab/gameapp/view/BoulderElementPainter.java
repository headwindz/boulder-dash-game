package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.BoulderElement;
import uk.ac.ox.comlab.gameapp.model.CaveElement;


public class BoulderElementPainter extends CaveElementPainter{
	public static final BoulderElementPainter INSTANCE = new BoulderElementPainter();
	
	public void paint(Graphics2D g, CaveElement e){
		Color oldColor = g.getColor();
		BoulderElement boulder = (BoulderElement)e;
		int gridSize = boulder.getSize();
		int leftTopX = boulder.getX() * gridSize;
		int leftTopY = boulder.getY() * gridSize;
		g.setColor(Color.BLACK);
		g.fillOval(leftTopX, leftTopY, gridSize, gridSize);
		g.setColor(oldColor);
	}
	public void paintSelection(Graphics2D g, CaveElement e) {
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		BoulderElement boulder = (BoulderElement)e;
		int gridSize = boulder.getSize();
		int leftTopX = boulder.getX() * gridSize;
		int leftTopY = boulder.getY() * gridSize;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		g.setColor(Color.GRAY);
		g.fillRect(leftTopX,leftTopY , gridSize, gridSize);
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}
}
