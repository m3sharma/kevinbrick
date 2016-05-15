/*Kevin Chen
ICS4U1
November 21, 2013
Custom Map Creator
Allows user to create their own Brick Breaker maps*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MapCreator extends JFrame implements GameConstants{
	//initialize things
	private MapCreatorPanel editPanel;
	private JPanel buttonPanel;
	private JPanel buttonPanelTop;
	private JPanel buttonPanelCenter;
	private JPanel buttonPanelBottom;
	private JPanel editPanelBorder;
	
	private JButton placeButton1;
	private JButton placeButton2;
	private JButton placeButton3;
	private JButton deleteButton;
	private JButton clearButton;
	private JButton saveButton;
	private JButton loadButton;
	
	//constructor
	public MapCreator(){
		//load panels, buttons, action listeners
		super(MAP_WIN_TITLE);
		
		Container container = getContentPane();
		
		editPanel = new MapCreatorPanel();
		buttonPanel = new JPanel();
		buttonPanelTop = new JPanel();
		buttonPanelCenter = new JPanel();
		buttonPanelBottom = new JPanel();
		editPanelBorder = new JPanel(new GridBagLayout());
		
		placeButton1 = new JButton("Brick 1");
		placeButton2 = new JButton("Brick 2");
		placeButton3 = new JButton("Brick 3");
		deleteButton = new JButton("Delete Brick");
		clearButton = new JButton("Clear Bricks");
		saveButton = new JButton("Save");
		loadButton = new JButton("Load");
		
		editPanel.setLayout(new GridLayout(NUM_BRICK_ROWS, NUM_BRICK_COLUMNS, 0, 0));
		buttonPanel.setLayout(new GridLayout(3, 1, 15, 0));
		buttonPanelTop.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanelCenter.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPanelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		buttonPanelTop.add(placeButton1);
		buttonPanelTop.add(placeButton2);
		buttonPanelTop.add(placeButton3);
		buttonPanelCenter.add(deleteButton);
		buttonPanelCenter.add(clearButton);
		buttonPanelBottom.add(saveButton);
		buttonPanelBottom.add(loadButton);
		
		placeButton1.addActionListener(editPanel);
		placeButton2.addActionListener(editPanel);
		placeButton3.addActionListener(editPanel);
		deleteButton.addActionListener(editPanel);
		clearButton.addActionListener(editPanel);
		saveButton.addActionListener(editPanel);
		loadButton.addActionListener(editPanel);
		
		buttonPanel.add(buttonPanelTop);
		buttonPanel.add(buttonPanelCenter);
		buttonPanel.add(buttonPanelBottom);
		
		editPanelBorder.add(editPanel);
		
		container.add(editPanelBorder, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
		
		//if the user closes the window before saving, prompt the user to save and then close the window
		this.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					editPanel.closingSave();
					System.exit(0);
				}
			}
		);
	}
	
	public static void main(String[]args){
		//create new window, set size, set visible, disable resizability
		MapCreator creatorFrame = new MapCreator();
		creatorFrame.setSize(MAP_WIN_WIDTH, MAP_WIN_HEIGHT);
		creatorFrame.setVisible(true);
		creatorFrame.setResizable(false);
	}
}