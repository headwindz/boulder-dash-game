package uk.ac.ox.comlab.gameapp.controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JComboBox;

import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManagerListener;
import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager.States;
import uk.ac.ox.comlab.gameapp.model.CaveElement;
import uk.ac.ox.comlab.gameapp.model.WallElement;

public class ChangeWallColor implements ActionListener, SelectionManagerListener{
	/** 
	 * a mapping from Color to corresponding String.
	 * An alternative is to simply add a switch clause and set the color
	 * correspondingly. We do find using a mapping is more suitable and will make
	 * the application easier to extend. Simply imagine that later we decide that we
	 * want to have 10 colors for the wall. Instead of changing the body code, we only
	 * need to add these colors and their corresponding string into the map at the
	 * beginning of the class.
	 */
	protected static final Map<Color,String> colorToString = new HashMap<Color,String>();
	static {
		colorToString.put(Color.RED, "RED");
		colorToString.put(Color.GREEN, "GREEN");
		colorToString.put(Color.BLUE, "BLUE");
	}
	/** a mapping from String to corresponding Color */
	protected static final Map<String,Color> stringToColor = new HashMap<String,Color>();
	static {
		stringToColor.put("RED",Color.RED);
		stringToColor.put("GREEN",Color.GREEN);
		stringToColor.put("BLUE",Color.BLUE);
	}
	
	protected final SelectionManager selectionManager;
	protected final JComboBox comboBox;
	protected final String[] colors;
	
	public ChangeWallColor(SelectionManager selectionManager, JComboBox comboBox,String[] colors) {
		this.selectionManager = selectionManager;
		this.comboBox = comboBox;
		this.colors = colors;
		selectionManager.addListener(this);
		comboBox.addActionListener(this);
		updateCombox();
	}
	public void objectsSelected(Collection<? extends CaveElement> objects) {
		updateCombox();
	}
	public void objectsUnselected(Collection<? extends CaveElement> objects) {
	}
	public void selectionCleared() {
		updateCombox();
	}
	public void currentStateChanged(States newCurrentState) {
		updateCombox();
	}
	public void currentGoalChanged(int goal) {
	}
	public void invalidInputEntered(String s) {
	}
	public void currentCaveChanged() {
		updateCombox();
	}
	public void actionPerformed(ActionEvent e) {
		String color = (String) comboBox.getSelectedItem();
		Iterator<CaveElement> iterator = selectionManager.getSelection();
		while (iterator.hasNext()) {
			WallElement wall = (WallElement)iterator.next();
			wall.setColor(stringToColor.get(color));
		}
	}
	public void updateCombox(){
		comboBox.setVisible(selectionManager.getCurrentState() == SelectionManager.States.EDIT_STATE);
		comboBox.setEnabled(!selectionManager.isSelectionEmpty() && isAllSelectionWalls());
		if(comboBox.isEnabled()){
			comboBox.removeActionListener(this);
			comboBox.setSelectedIndex(indexOfColor(colorToString.get(getColor())));
			comboBox.addActionListener(this);
		}
		else{
			comboBox.removeActionListener(this);
			comboBox.setSelectedIndex(0);
			comboBox.addActionListener(this);
		}
	}
	public Color getColor(){
		if(!isAllSelectioinSameColor()){
			return null;
		}
		Iterator<CaveElement> iterator = selectionManager.getSelection();
		WallElement firstWall = (WallElement)(iterator.next());
		return firstWall.getColor();
	}
	public int indexOfColor(String color){
		for(int i = 0; i < colors.length; i++){
			if(colors[i].equals(color)){
				return i;
			}
		}
		return 0;
	}
	/** check whether all the selected elements are of same color */
	public boolean isAllSelectioinSameColor(){
		if(!isAllSelectionWalls()){
			return false;
		}
		if(selectionManager.isSelectionEmpty()){
			return false;
		}
		Iterator<CaveElement> iterator = selectionManager.getSelection();
		WallElement firstWall = (WallElement)(iterator.next());
		Color color = firstWall.getColor();
		while (iterator.hasNext()) {
			WallElement wall = (WallElement)(iterator.next());
			if(!wall.getColor().equals(color)){
				return false;
			}
		}
		return true;
	}
	/** check whether all the selected elements are of type wallElement */
	public boolean isAllSelectionWalls(){
		Iterator<CaveElement> iterator = selectionManager.getSelection();
		try{
			while (iterator.hasNext()) {
				WallElement wall = (WallElement)(iterator.next());
			}
			return true;
		}
		catch(ClassCastException cce){
			return false;
		}
	}
}



