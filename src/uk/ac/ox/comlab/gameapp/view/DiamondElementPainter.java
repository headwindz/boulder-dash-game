package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.DiamondElement;


public class DiamondElementPainter extends CaveElementPainter{
	public static final DiamondElementPainter INSTANCE = new DiamondElementPainter();
	
	public void paint(Graphics2D g, CaveElement e){
		Color oldColor = g.getColor();
		DiamondElement diamond = (DiamondElement)e;
		
		int gridSize = diamond.getSize();
		int leftTopX = diamond.getX() * gridSize;
		int leftTopY = diamond.getY() * gridSize;
		g.setColor(Color.MAGENTA);
		g.drawLine(leftTopX + 2, leftTopY + 2 , leftTopX + gridSize/2, leftTopY + gridSize -2);
		g.drawLine(leftTopX + gridSize-2, leftTopY+ 2 , leftTopX + gridSize/2, leftTopY + gridSize-2);
		g.drawLine(leftTopX + 2, leftTopY+ 2 , leftTopX + gridSize-2, leftTopY+ 2);
		g.setColor(oldColor);
	}
	public void paintSelection(Graphics2D g, CaveElement e) {
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		DiamondElement diamond = (DiamondElement)e;
		int gridSize = diamond.getSize();
		int leftTopX = diamond.getX() * gridSize;
		int leftTopY = diamond.getY() * gridSize;
		g.setColor(Color.GRAY);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		g.fillRect(leftTopX,leftTopY , gridSize, gridSize);
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}
}
