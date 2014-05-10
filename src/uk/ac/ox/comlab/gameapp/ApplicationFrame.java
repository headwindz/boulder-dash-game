package uk.ac.ox.comlab.gameapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;


import uk.ac.ox.comlab.gameapp.applicationstate.SelectionManager;
import uk.ac.ox.comlab.gameapp.controller.AnimateFalling;
import uk.ac.ox.comlab.gameapp.controller.ChangeWallColor;
import uk.ac.ox.comlab.gameapp.controller.DeleteSelectedElements;
import uk.ac.ox.comlab.gameapp.controller.ReactToKeyboard;
import uk.ac.ox.comlab.gameapp.controller.SelectState;
import uk.ac.ox.comlab.gameapp.controller.SetGoal;
import uk.ac.ox.comlab.gameapp.view.CaveView;


public class ApplicationFrame extends JFrame {
	protected final SelectionManager selectionManager;
	protected final CaveView caveView;

	public ApplicationFrame() {
		super("Boulder Dash");
		int gridSize = 40;
		selectionManager = new SelectionManager(gridSize);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		caveView = new CaveView(selectionManager,gridSize);
		
		// The cave view is displayed within a scroll pane.
		JScrollPane caveViewScrollPane = new JScrollPane(caveView, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// The frame contains a toolbar and the cave view (wrapped in a scroll pane).
		// These are wrapped in a content pane, which uses a border layout.
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(BorderLayout.NORTH, getToolBar(contentPane));
		JPanel gamePane = new JPanel();
		gamePane.add(caveViewScrollPane);
		contentPane.add(BorderLayout.CENTER, gamePane);
		contentPane.setPreferredSize(new Dimension(850, 500));
		
		// set content pane into the frame.
		setContentPane(contentPane);

		// The following call computes the layout of the frame accordingly. 
		pack();
	}
	
	protected JToolBar getToolBar(JPanel contentPane) {
		JToggleButton selectEditStateButton = new JToggleButton("Edit");
		new SelectState(selectionManager, selectEditStateButton, SelectionManager.States.EDIT_STATE,contentPane);
		
		JToggleButton selectCreateWallElementStateButton = new JToggleButton("Create Wall");
		new SelectState(selectionManager, selectCreateWallElementStateButton, SelectionManager.States.CREATE_WALL_STATE,contentPane);
		
		JToggleButton selectCreateBoulderElementStateButton = new JToggleButton("Create Boulder");
		new SelectState(selectionManager, selectCreateBoulderElementStateButton, SelectionManager.States.CREATE_BOULDER_STATE,contentPane);
		
		JToggleButton selectCreateDiamondElementStateButton = new JToggleButton("Create Diamond");
		new SelectState(selectionManager, selectCreateDiamondElementStateButton, SelectionManager.States.CREATE_DIAMOND_STATE,contentPane);
		
		JToggleButton selectCreateDirtElementStateButton = new JToggleButton("Create Dirt");
		new SelectState(selectionManager, selectCreateDirtElementStateButton, SelectionManager.States.CREATE_DIRT_STATE,contentPane);
		
		JToggleButton selectCreatePlayerElementStateButton = new JToggleButton("Create Player");
		new SelectState(selectionManager, selectCreatePlayerElementStateButton, SelectionManager.States.CREATE_PLAYER_STATE,contentPane);
		
		JToggleButton selectPlayStateButton = new JToggleButton("Play");
		new SelectState(selectionManager, selectPlayStateButton, SelectionManager.States.PLAY_STATE,contentPane);
		
		JButton deleteButton = new JButton("Delete");
		new DeleteSelectedElements(selectionManager, deleteButton);
		
		String[] colors = {" ","RED","GREEN","BLUE"};
		JComboBox comboBox = new JComboBox(colors);
		comboBox.setPreferredSize(new Dimension(100,15));
		new ChangeWallColor(selectionManager, comboBox,colors);
		
		final JTextField goalNumberField = new JTextField("0");
		goalNumberField.setPreferredSize(new Dimension(50,15));
		new SetGoal(selectionManager, goalNumberField);
		
		new ReactToKeyboard(selectionManager,contentPane);
		new AnimateFalling(selectionManager);

		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(10,10,10,10));
		
		toolBar.add(selectEditStateButton);
		toolBar.add(selectCreateWallElementStateButton);
		
		toolBar.add(selectCreateBoulderElementStateButton);
		toolBar.add(selectCreateDiamondElementStateButton);
		toolBar.add(selectCreateDirtElementStateButton);
		toolBar.add(selectCreatePlayerElementStateButton);
		toolBar.add(selectPlayStateButton);
		toolBar.addSeparator();
		toolBar.add(deleteButton);
		toolBar.add(comboBox);
		toolBar.add(goalNumberField);
		return toolBar;
	}
	
	public static void main(String[] args)   {
		ApplicationFrame applicationFrame = new ApplicationFrame();
		applicationFrame.setLocation(100, 100);
		applicationFrame.setVisible(true);
	}
	
}
