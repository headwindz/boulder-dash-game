package uk.ac.ox.comlab.gameapp.view;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import uk.ac.ox.comlab.gameapp.model.BoulderElement;
import uk.ac.ox.comlab.gameapp.model.Cave;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.DiamondElement;
import uk.ac.ox.comlab.gameapp.model.DirtElement;
import uk.ac.ox.comlab.gameapp.model.PlayerElement;
import uk.ac.ox.comlab.gameapp.model.WallElement;


/**
 * A MouseHandler that creates cave elements.
 */
public class CreateElementHandler extends AbstractStretchBoxHandler{
	protected final CaveElementFactory caveElementFactory;
	
	public CreateElementHandler(CaveView caveView, CaveElementFactory caveElementFactory) {
		super(caveView);
		this.caveElementFactory = caveElementFactory;
	}
	protected void boxStretchingFinished(Point boxOrigin,Point boxTarget) {
		int gridSize = caveView.getGridSize();
		Cave cave = caveView.getCave();
		int leftTopXCoordinate = Math.min(stretchBoxOrigin.x, stretchBoxTarget.x);
		int leftTopYCoordinate = Math.min(stretchBoxOrigin.y, stretchBoxTarget.y);
		int rightBottomXCoordinate = Math.max(stretchBoxOrigin.x , stretchBoxTarget.x);
		int rightBottomYCoordinate = Math.max(stretchBoxOrigin.y , stretchBoxTarget.y);
		
		List<CaveElement> list = new ArrayList<CaveElement>();
		for(int i = leftTopXCoordinate/gridSize; i <= rightBottomXCoordinate/gridSize ; i += 1){
			for(int j = leftTopYCoordinate/gridSize; j <= rightBottomYCoordinate/gridSize; j += 1){
				CaveElement element = caveElementFactory.newCaveElement(cave, i, j, gridSize);
				list.add(element);
			}
		}
		cave.addCaveElements(list);
	}
	/**
	 * When the handler creates the cave elements, the object creation pattern is used to
	 * determine what kind of elements to create. If the application is extended with new
	 * kinds of objects, the following factory needs to be updated correspondingly by adding
	 * corresponding elements factories.
	 */
	public static interface CaveElementFactory {
		CaveElement newCaveElement(Cave cave,int x, int y, int size);
	}
	public static CaveElementFactory WALL_FACTORY = new CaveElementFactory() {
		public CaveElement newCaveElement(Cave cave,int x, int y, int size) {
			return new WallElement(cave, x, y, size);
		}
	};
	public static CaveElementFactory DIRT_FACTORY = new CaveElementFactory() {
		public CaveElement newCaveElement(Cave cave,int x, int y, int size) {
			return new DirtElement(cave, x, y, size);
		}
	};
	public static CaveElementFactory BOULDER_FACTORY = new CaveElementFactory() {
		public CaveElement newCaveElement(Cave cave,int x, int y, int size) {
			return new BoulderElement(cave, x, y, size);
		}
	};
	public static CaveElementFactory DIAMOND_FACTORY = new CaveElementFactory() {
		public CaveElement newCaveElement(Cave cave,int x, int y, int size) {
			return new DiamondElement(cave, x, y, size);
		}
	};
	public static CaveElementFactory PLAYER_FACTORY = new CaveElementFactory() {
		public CaveElement newCaveElement(Cave cave,int x, int y, int size) {
			return new PlayerElement(cave, x, y, size);
		}
	};
}
