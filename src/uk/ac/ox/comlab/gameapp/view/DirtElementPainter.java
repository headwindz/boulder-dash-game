package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.DirtElement;

public class DirtElementPainter extends CaveElementPainter{
	public static final DirtElementPainter INSTANCE = new DirtElementPainter();
	
	public void paint(Graphics2D g, CaveElement e){
		Color oldColor = g.getColor();
		DirtElement dirt = (DirtElement)e;
		int gridSize = dirt.getSize();
		int leftTopX = dirt.getX() * gridSize;
		int leftTopY = dirt.getY() * gridSize;
		Color grown = new Color(139, 69, 19);
		g.setColor(grown);
		g.fillRect(leftTopX, leftTopY, gridSize, gridSize);
		g.setColor(Color.BLACK);
		int dirtSize = 2;
		for(int i = leftTopX + dirtSize; i < leftTopX + gridSize - dirtSize; i += dirtSize){
			for(int j = leftTopY + dirtSize; j < leftTopY + gridSize - dirtSize; j += dirtSize){
				g.fillOval(i, j, dirtSize, dirtSize);
			}
		}
		g.setColor(oldColor);
	}
	public void paintSelection(Graphics2D g, CaveElement e) {
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
		DirtElement dirt = (DirtElement)e;
		g.setColor(Color.WHITE);
		int gridSize = dirt.getSize();
		int leftTopX = dirt.getX() * gridSize;
		int leftTopY = dirt.getY() * gridSize;
		g.setStroke(new BasicStroke(2));
		g.drawRect(leftTopX + 2, leftTopY + 2, gridSize - 4, gridSize - 4);
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}
}
