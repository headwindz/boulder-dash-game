package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.WallElement;

public class WallElementPainter extends CaveElementPainter{
	public static final WallElementPainter INSTANCE = new WallElementPainter();
	
	public void paint(Graphics2D g, CaveElement e){
		Color oldColor = g.getColor();
		WallElement wall = (WallElement)e;
		g.setColor(wall.getColor());
		int gridSize = wall.getSize();
		int leftTopX = wall.getX() * gridSize;
		int leftTopY = wall.getY() * gridSize;
		g.fillRect(leftTopX, leftTopY, gridSize, gridSize);
		g.setColor(oldColor);	
	}
	public void paintSelection(Graphics2D g, CaveElement e) {
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
		WallElement wall = (WallElement)e;
		g.setColor(Color.WHITE);
		int gridSize = wall.getSize();
		int leftTopX = wall.getX() * gridSize;
		int leftTopY = wall.getY() * gridSize;
		g.setStroke(new BasicStroke(2));
		g.drawRect(leftTopX + 2, leftTopY + 2, gridSize - 4, gridSize - 4);
		g.setColor(oldColor);
		g.setComposite(oldComposite);
	}
}
