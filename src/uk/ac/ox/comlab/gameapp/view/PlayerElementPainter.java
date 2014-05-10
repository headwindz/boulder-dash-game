package uk.ac.ox.comlab.gameapp.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.Direction;
import uk.ac.ox.comlab.gameapp.model.PlayerElement;

public class PlayerElementPainter extends CaveElementPainter{
	public static final PlayerElementPainter INSTANCE = new PlayerElementPainter();
	
	public void paint(Graphics2D g, CaveElement e){
		PlayerElement player = (PlayerElement)e;
		Color oldColor = g.getColor();
		g.setColor(Color.GREEN);
		int gridSize = player.getSize();
		int leftTopX = player.getX() * gridSize;
		int leftTopY = player.getY() * gridSize;
		g.drawOval(leftTopX, leftTopY, gridSize, gridSize);
		Direction direction = player.getDirection();
		int centerX = leftTopX + gridSize / 2;
		int centerY = leftTopY + gridSize / 2;
		int endX = centerX + direction.deltaX * gridSize / 2;
		int endY = centerY + direction.deltaY * gridSize / 2;
		g.drawLine(centerX, centerY, endX, endY);
		g.setColor(oldColor);
	}
	public void paintSelection(Graphics2D g, CaveElement e) {
		Color oldColor = g.getColor();
		Composite oldComposite = g.getComposite();
		PlayerElement player = (PlayerElement)e;
		int gridSize = player.getSize();
		int leftTopX = player.getX() * gridSize;
		int leftTopY = player.getY() * gridSize;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		g.setColor(Color.GRAY);
		g.fillRect(leftTopX,leftTopY,gridSize,gridSize);
		g.setComposite(oldComposite);
		g.setColor(oldColor);
	}
}
