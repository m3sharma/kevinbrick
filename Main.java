/*Kevin Chen
ICS4U1
November 21, 2013
Main
Brick Breaker frame class*/

import java.awt.*;
import javax.swing.*;

public class Main extends JFrame implements GameConstants{
	//initialize things
	private BrickBreaker myPanel;
	private JPanel buttonPanel;
	
	private JButton button1;
	private JButton button2;
	private JButton exitButton;
	
	//constructor
	public Main(){
		//load panels, buttons, and action/key listeners
		super(WIN_TITLE);
		
		Container container = getContentPane();
		
		myPanel = new BrickBreaker();
		buttonPanel = new JPanel();
		
		button1 = new JButton("Load");
		button2 = new JButton("Background");
		exitButton = new JButton("Pause");
		
		buttonPanel.setLayout(new GridLayout(1, 3, 0, 0));
		
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(exitButton);
		
		button1.addActionListener(myPanel);
		button2.addActionListener(myPanel);
		exitButton.addActionListener(myPanel);
		
		button1.setFocusable(false);
		button2.setFocusable(false);
		exitButton.setFocusable(false);
		addKeyListener(myPanel);
		
		container.add(myPanel, BorderLayout.CENTER);
		container.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[]args){
		//create new window, set size, set visible, and disable resizability
		Main myFrame = new Main();
		myFrame.setSize(WIN_WIDTH, WIN_HEIGHT);
		myFrame.setVisible(true);
		myFrame.setResizable(false);
		myFrame.setDefaultCloseOperation(myFrame.EXIT_ON_CLOSE);
	}
}