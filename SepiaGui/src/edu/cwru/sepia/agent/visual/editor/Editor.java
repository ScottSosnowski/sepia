/**
 *  Strategy Engine for Programming Intelligent Agents (SEPIA)
    Copyright (C) 2012 Case Western Reserve University

    This file is part of SEPIA.

    SEPIA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SEPIA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SEPIA.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.cwru.sepia.agent.visual.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import edu.cwru.sepia.agent.Agent;
import edu.cwru.sepia.agent.visual.GamePanel;
import edu.cwru.sepia.agent.visual.GameScreen;
import edu.cwru.sepia.model.persistence.StateAdapter;
import edu.cwru.sepia.model.persistence.generated.XmlState;
import edu.cwru.sepia.model.state.ResourceNode;
import edu.cwru.sepia.model.state.ResourceNodeType;
import edu.cwru.sepia.model.state.ResourceType;
import edu.cwru.sepia.model.state.State;
import edu.cwru.sepia.model.state.State.StateBuilder;
import edu.cwru.sepia.model.state.Template;
import edu.cwru.sepia.model.state.Unit;
import edu.cwru.sepia.model.state.UnitTemplate;
import edu.cwru.sepia.util.SerializerUtil;
import edu.cwru.sepia.util.TypeLoader;

public class Editor extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameScreen screen;
	GamePanel gamePanel;
	State state;
	JComboBox<String> templateSelector;
	JComboBox<String> playerSelector;
	JComboBox<String> resourceSelector;
	JButton addPlayer;
	ButtonGroup cursorGroup;
	JTextField resourceAmount;
	JRadioButton selectPointer;
	JRadioButton selectUnit;
	JRadioButton selectResource;
	JRadioButton selectRemove;
	ButtonGroup fogOfWar;
	JRadioButton fogOn;
	JRadioButton fogOff;
	ButtonGroup revealResources;
	JRadioButton revealResourcesOn;
	JRadioButton revealResourcesOff;
	JTextField xSize;
	JTextField ySize;
	JButton setSize;
	JButton save;
	JTextArea error;

	public Editor(GameScreen screen, GamePanel gamePanel, State state, String templatefilename) {
		this.screen = screen;
		this.gamePanel = gamePanel;
		this.state = state;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(screen.getLocation().x + screen.getWidth() + 1, screen.getLocation().y);
		setSize(200, 450);
		setTitle("Editor");
		setLayout(new BorderLayout());
		JPanel inputs = new JPanel();
		GroupLayout layout = new GroupLayout(inputs); 
		inputs.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		gamePanel.addMouseListener(this.new EditorMouseListener());

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>(new String[] {});
		playerSelector = new JComboBox<String>(model);
		addPlayer = new JButton("Add");
		addPlayer.addActionListener(new ActionListener() {
			DefaultComboBoxModel<String> model;
			String templatefilename;
			State state;

			public ActionListener setOutsideInformationAndClick(DefaultComboBoxModel<String> model,
					String templatefilename, State state) {
				this.model = model;
				this.templatefilename = templatefilename;
				this.state = state;
				actionPerformed(null); // click it once so it starts with one
										// player
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if(model.getSize() < 8) {
					int newPlayerNum = model.getSize();
					try {
						List<Template<?>> newPlayerTemplates = TypeLoader.loadFromFile(
								templatefilename, newPlayerNum, state);
						for(Template<?> t : newPlayerTemplates) {
							state.addTemplate(t);
						}
					} catch(FileNotFoundException e1) {
						e1.printStackTrace();
					} catch(JAXBException e1) {
						e1.printStackTrace();
					}
					model.addElement("Player " + newPlayerNum);
				}
			}
		}.setOutsideInformationAndClick(model, templatefilename, state));
		cursorGroup = new ButtonGroup();
		selectPointer = new JRadioButton("Pointer");
		selectPointer.setSelected(true);
		cursorGroup.add(selectPointer);
		selectUnit = new JRadioButton("Unit");
		cursorGroup.add(selectUnit);
		selectResource = new JRadioButton("Resource");
		cursorGroup.add(selectResource);
		selectRemove = new JRadioButton("Remove");
		cursorGroup.add(selectRemove);

		// Make a button group for fog of war, one radio button for on and one
		// for off
		// and make listeners to set the state
		// then make it default to off
		fogOfWar = new ButtonGroup();
		fogOn = new JRadioButton("on");
		fogOfWar.add(fogOn);
		fogOn.addActionListener(new ActionListener() {
			State state;
			Editor ed;

			public ActionListener setThings(State state, Editor ed) {
				this.state = state;
				this.ed = ed;
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setFogOfWar(true);
				ed.updateScreen();
			}
		}.setThings(state, this));
		fogOff = new JRadioButton("off");
		fogOfWar.add(fogOff);
		fogOff.addActionListener(new ActionListener() {
			State state;
			Editor ed;

			public ActionListener setThings(State state, Editor ed) {
				this.state = state;
				this.ed = ed;
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				state.setFogOfWar(false);
				ed.updateScreen();
			}
		}.setThings(state, this));
		fogOff.setSelected(true);

		ArrayList<String> unitnames;
		List<Template<?>> alltemplates = null;
		try {
			alltemplates = TypeLoader.loadFromFile(templatefilename, -3, new State());
		} catch(FileNotFoundException e1) {
			e1.printStackTrace();
		} catch(JAXBException e1) {
			e1.printStackTrace();
		}
		if(alltemplates == null)
			alltemplates = new ArrayList<Template<?>>(0);
		unitnames = new ArrayList<String>();
		for(int i = 0; i < alltemplates.size(); i++) {
			if(alltemplates.get(i) instanceof UnitTemplate)
				unitnames.add(alltemplates.get(i).getName());
		}
		templateSelector = new JComboBox<String>(unitnames.toArray(new String[0]));
		templateSelector.addActionListener(new RadioButtonSelector(selectUnit));

		resourceSelector = new JComboBox<String>(new DefaultComboBoxModel<String>(new String[] {
				"GOLD_MINE, GOLD", "TREE, WOOD" }));
		resourceSelector.setEditable(true);
		resourceSelector.addActionListener(new RadioButtonSelector(selectResource));
		resourceSelector.addActionListener(new ActionListener() {
			private JComboBox<String> comboBox;

			public ActionListener setComboBox(JComboBox<String> comboBox) {
				this.comboBox = comboBox;
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String value = (String) comboBox.getSelectedItem();
				DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
				if(value.contains(",") && model.getIndexOf(value) < 0)
					model.addElement(value);
			}

		}.setComboBox(resourceSelector));

		error = new JTextArea(8, 10);
		error.setForeground(Color.RED);
		error.setEditable(false);
		error.setLineWrap(true);
		error.setWrapStyleWord(true);
		resourceAmount = new JTextField(4);
		resourceAmount.setText("100");

		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			State state;

			public ActionListener setState(State state) {
				this.state = state;
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				jfc.setCurrentDirectory(new File("."));
				int result = jfc.showSaveDialog(Editor.this);
				if(result != JFileChooser.APPROVE_OPTION)
					return;
				SerializerUtil.storeState(jfc.getSelectedFile().toString(), state);
			}

		}.setState(state));

		// Add a couple of text fields and a button to resize the map
		// x size
		xSize = new JTextField(3);
		xSize.setText(Integer.toString(state.getXExtent()));
		// y size
		ySize = new JTextField(3);
		ySize.setText(Integer.toString(state.getYExtent()));
		// button to set size
		setSize = new JButton("Set Size");
		// make a listener on the text fields to activate the button when you
		// press enter from the fields
		ActionListener pressSetSize = new ActionListener() {
			JButton button;

			public ActionListener setButtonToPress(JButton button) {
				this.button = button;
				return this;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				button.doClick();
			}
		}.setButtonToPress(setSize);
		xSize.addActionListener(pressSetSize);
		ySize.addActionListener(pressSetSize);
		// make a listener for the button that checks the text fields for
		// validity of their numbers
		// and if it would make it so small that it would exclude some
		// units/resources that are placed
		// then it needs to confirm it before deleting those units/nodes
		setSize.addActionListener(new ActionListener() {
			State state;
			JTextField xSize;
			JTextField ySize;
			JTextArea errorOut;
			Editor editor;

			public ActionListener setStateAndFields(State state, JTextField xSize,
					JTextField ySize, JTextArea errorOut, Editor editor) {
				this.state = state;
				this.xSize = xSize;
				this.ySize = ySize;
				this.errorOut = errorOut;
				this.editor = editor;
				return this;
			}

			/**
			 * Reset the text fields to the actual size of the state
			 */
			private void resetTextFields() {
				xSize.setText(Integer.toString(state.getXExtent()));
				ySize.setText(Integer.toString(state.getYExtent()));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				int newxsize = -1;
				int newysize = -1;
				// make sure that they are integers
				try {
					newxsize = Integer.parseInt(xSize.getText());
				} catch(NumberFormatException nfe) {
					errorOut.setText("x Size: " + xSize.getText() + " is not an integer");
					resetTextFields();
					return;
				}
				try {
					newysize = Integer.parseInt(ySize.getText());
				} catch(NumberFormatException nfe) {
					errorOut.setText("y Size: " + ySize.getText() + " is not an integer");
					resetTextFields();
					return;
				}
				if(newxsize < 1) {
					errorOut.setText("x Size: must be positive");
					resetTextFields();
					return;
				}
				if(newysize < 1) {
					errorOut.setText("y Size: must be positive");
					resetTextFields();
					return;
				}
				// get all units and resources that would be out of bounds
				List<Integer> unitsToRemove = new LinkedList<Integer>();
				for(Unit u : state.getUnits().values()) {
					if(u.getXPosition() >= newxsize || u.getYPosition() >= newysize) {
						unitsToRemove.add(u.id);
					}
				}
				List<Integer> resourcesToRemove = new LinkedList<Integer>();
				for(ResourceNode r : state.getResources()) {
					if(r.getxPosition() >= newxsize || r.getyPosition() >= newysize) {
						resourcesToRemove.add(r.id);
					}
				}
				if(unitsToRemove.size() + resourcesToRemove.size() > 0) {
					int response = JOptionPane.showConfirmDialog(editor, "Resizing to " + newxsize
							+ "x" + newysize + " will delete " + unitsToRemove.size()
							+ " units\n and " + resourcesToRemove.size()
							+ " resources\nAre you sure you want to do that?", "Resizing",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(response == 0)// 0 is yes, 1 is no
					{
						for(int id : unitsToRemove) {
							state.removeUnit(id);
						}
						for(int id : resourcesToRemove) {
							state.removeResourceNode(id);
						}
					} else {
						resetTextFields();
						return;
					}
				}
				state.setSize(newxsize, newysize);
				editor.updateScreen();
			}

		}.setStateAndFields(state, xSize, ySize, error, this));

		JLabel amountLabel = new JLabel("Amount");
		JLabel fogLabel = new JLabel("Fog of war");
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(templateSelector)
				.addGroup(layout.createParallelGroup()
						.addComponent(playerSelector)
						.addComponent(addPlayer)
				)
				.addComponent(resourceSelector)
				.addGroup(layout.createParallelGroup()
						.addComponent(amountLabel)
						.addComponent(resourceAmount)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(selectPointer)
						.addComponent(selectUnit)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(selectResource)
						.addComponent(selectRemove)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(fogLabel)
						.addComponent(fogOn)
						.addComponent(fogOff)
				)
				.addGroup(layout.createParallelGroup()
						.addComponent(xSize)
						.addComponent(ySize)
				)
				.addComponent(setSize)
				.addComponent(save)
		);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup()
						.addComponent(templateSelector)
						.addGroup(layout.createSequentialGroup()
								.addComponent(playerSelector)
								.addComponent(addPlayer)
						)
						.addComponent(resourceSelector)
						.addGroup(layout.createSequentialGroup()
								.addComponent(amountLabel)
								.addComponent(resourceAmount)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectPointer)
								.addComponent(selectUnit)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(selectResource)
								.addComponent(selectRemove)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(fogLabel)
								.addComponent(fogOn)
								.addComponent(fogOff)
						)
						.addGroup(layout.createSequentialGroup()
								.addComponent(xSize)
								.addComponent(ySize)
						)
						.addComponent(setSize)
						.addComponent(save)
				)
		);
		add(inputs, BorderLayout.NORTH);
		add(error, BorderLayout.SOUTH);
		/*
		add(templateSelector);
		add(playerSelector);
		add(addPlayer);
		add(resourceSelector);
		add(resourceAmount);
		add(selectPointer);
		add(selectUnit);
		add(selectResource);
		add(selectRemove);
		add(new JLabel("Fog of war"));
		add(fogOn);
		add(fogOff);
		// add(revealResourcesOn);
		// add(revealResourcesOff);
		add(xSize);
		add(ySize);
		add(setSize);
		add(save);
		add(error);*/
		setVisible(true);
		updateScreen();
	}

	private class EditorMouseListener extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			int x = gamePanel.unscaleX(e.getX());
			int y = gamePanel.unscaleY(e.getY());
			System.out.println(x + "," + y);
			int player = playerSelector.getSelectedIndex();
			if(!state.inBounds(x, y)) {
				error.setText(x + "," + y + " is out of bounds.");
				return;
			}
			if(!selectPointer.isSelected() && !selectRemove.isSelected()
					&& (state.unitAt(x, y) != null || state.resourceAt(x, y) != null)) {
				error.setText("Cannot place on top of existing object.");
				return;
			}

			if(selectUnit.isSelected()) {
				String name = (String) templateSelector.getSelectedItem();
				Unit u = ((UnitTemplate) state.getTemplate(player, name)).produceInstance(state);
				state.addUnit(u, x, y);
				error.setText("Unit id " + u.id + " " + name + " (player:" + player
						+ ") placed at " + x + "," + y);
			} else if(selectResource.isSelected()) {
				int amount;
				try {
					amount = Integer.parseInt(resourceAmount.getText());
				} catch(Exception ex) {
					error.setText("Invalid resource quantity.");
					return;
				}
				String selected = (String) resourceSelector.getSelectedItem();
				String[] names = selected.split(",");
				if(names.length != 2) {
					error.setText("Invalid resource input. Must follow format NODE_NAME, NODE");
					return;
				}
				ResourceNodeType type = new ResourceNodeType(names[0].trim(), new ResourceType(names[1].trim()));
				ResourceNode node = new ResourceNode(type, x, y, amount, state.nextTargetId());
				state.addResource(node);
				error.setText(names[0] + " producing " + names[1] + " with id " + node.id
						+ "placed at " + x + "," + y);
			} else if(selectRemove.isSelected()) {
				// Remove something on that position
				// Try to grab a resource first
				ResourceNode resourcethere = state.resourceAt(x, y);
				if(resourcethere != null) // if there was a resource there
				{// then remove it
					state.removeResourceNode(resourcethere.id);
					error.setText("Resource id " + resourcethere.id + " removed from " + x + ","
							+ y);
				} else // otherwise, see about units
				{
					Unit unitthere = state.unitAt(x, y);
					if(unitthere != null)// if there was a unit there
					{// then remove it
						state.removeUnit(unitthere.id);
						error.setText("Unit id " + unitthere.id + " removed from " + x + "," + y);
					}
				}

			}
			updateScreen();
		}

	}

	public void updateScreen() {
		gamePanel.updateState(state.getView(Agent.OBSERVER_ID), null);
	}

	public static void main(String[] args) throws FileNotFoundException, JAXBException {
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}

		
		String templates = "data/templates.xml";

		State state = null;
		for(int argInd = 0; argInd < args.length; argInd++) {
			if(args[argInd].equals("--loadstate")) {
				if(argInd + 1 >= args.length) {
					System.err.println("--loadstate must be followed by a file name");
					System.exit(ERROR);
				}
				argInd++;
				try {
					JAXBContext context = JAXBContext.newInstance(XmlState.class);
					XmlState xmlState = (XmlState)context.createUnmarshaller().unmarshal(new File(args[argInd]));
					state = new StateAdapter().fromXml(xmlState);
				} catch(Exception e) {
					System.err.println("Problem loading state.");
					e.printStackTrace();
				}
			}
			if(args[argInd].equals("--templates")) {
				if(argInd + 1 >= args.length) {
					System.err.println("--templates must be followed by a file name");
					System.exit(ERROR);
				}
				argInd++;
				templates = args[argInd];
			}
		}
		if(state == null) {
			StateBuilder builder = new StateBuilder();
			builder.setSize(25, 19);
			state = builder.build();
		}
		final State fstate = state;
		final GamePanel gamePanel = new GamePanel(null);
		final GameScreen screen = new GameScreen(gamePanel);
		final String ftemplates = templates;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				screen.pack();
				Editor editor = new Editor(screen, gamePanel, fstate, ftemplates);
				editor.setVisible(true);
			}
		});
	}/*
	 * private static void printUsage(PrintStream err) { err.println(
	 * "Usage is Editor [--loadstate statefilename] [--templates templatefilename]"
	 * ); err.println(
	 * "The order is interchangable and they default to a new 25x19 blank map and data/unit_templates respectively"
	 * ); }
	 */

	private static class RadioButtonSelector implements ActionListener {
		private JRadioButton button;

		public RadioButtonSelector(JRadioButton button) {
			this.button = button;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			button.setSelected(true);
		}
	}
}
