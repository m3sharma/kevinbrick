/*Kevin Chen
ICS4U1
November 21, 2013
Map Creator Panel
interactive panel that shows the setup of custom maps*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class MapCreatorPanel extends JPanel implements ActionListener, MouseListener, GameConstants{
	//initialize stuff
	private JLabel[][] editableBricks = new JLabel[NUM_BRICK_ROWS][NUM_BRICK_COLUMNS];
	
	private ImageIcon background;
	private ImageIcon emptyBrick;
	private ImageIcon brick1;
	private ImageIcon brick2;
	private ImageIcon brick3;
	private ImageIcon emptyBrickHover;
	private ImageIcon brick1Hover;
	private ImageIcon brick2Hover;
	private ImageIcon brick3Hover;
	
	private JFileChooser fc = new JFileChooser();
	private File savedFile = null;
	private File loadFile = null;
	private File defaultFile = new File("levels/level_default.opbb");
	private Container saveContainer;
	private BufferedWriter bw;
	private BufferedReader br;
	
	private int[][] brickTypeArray = new int[NUM_BRICK_ROWS][NUM_BRICK_COLUMNS];
	private int brickPlaceType = 1;
	
	private int hoveringBrickX;
	private int hoveringBrickY;
	
	private boolean hovering = false;
	private boolean saved = true;
	
	//constructor
	public MapCreatorPanel(){
		//load images
		background = new ImageIcon("img/background.jpg");
		emptyBrick = new ImageIcon("img/emptybrick.png");
		brick1 = new ImageIcon("img/brick1.png");
		brick2 = new ImageIcon("img/brick2.png");
		brick3 = new ImageIcon("img/brick3.png");
		emptyBrickHover = new ImageIcon("img/emptybrick_hover.png");
		brick1Hover = new ImageIcon("img/brick1_hover.png");
		brick2Hover = new ImageIcon("img/brick2_hover.png");
		brick3Hover = new ImageIcon("img/brick3_hover.png");
		
		//create array of interactive bricks with mouse listeners
		for (int i=0; i<NUM_BRICK_ROWS; i++){
			for (int j=0; j<NUM_BRICK_COLUMNS; j++){
				editableBricks[i][j] = new JLabel();
				editableBricks[i][j].setIcon(emptyBrick);
				editableBricks[i][j].addMouseListener(this);
				this.add(editableBricks[i][j]);
			}
		}
		
		//create array of brick types and preset all elements of the array to 0
		for (int i=0; i<NUM_BRICK_ROWS; i++){
			for (int j=0; j<NUM_BRICK_COLUMNS; j++){
				brickTypeArray[i][j] = 0;
			}
		}
	}
	
	//method asks user to save their map if they close without saving
	public void closingSave(){
		if (!saved){
			//file selector window
			fc.setSelectedFile(defaultFile);
			int returnVal = fc.showSaveDialog(saveContainer);
			
			if (returnVal == JFileChooser.APPROVE_OPTION){
				savedFile = fc.getSelectedFile();
			}
			
			try{
				bw = new BufferedWriter(new FileWriter(savedFile.getAbsolutePath()));
				
				//takes the brick type array and writes the values to the file
				for (int i=0; i<NUM_BRICK_ROWS; i++){
					for (int j=0; j<NUM_BRICK_COLUMNS; j++){
						bw.write("" + brickTypeArray[i][j]);
					}
					
					bw.newLine();
				}
				
				bw.close();
			} catch (Exception ee){
				//print error message if the file was not saved properly
				System.out.println("ERROR: level was not saved successfully");
			}
		}
	}
	
	//paint method
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//draw background
		g.drawImage(background.getImage(), 0, 0, null);
		
		for (int i=0; i<NUM_BRICK_ROWS; i++){
			for (int j=0; j<NUM_BRICK_COLUMNS; j++){
				//draw the bricks
				if (brickTypeArray[i][j] == 1){
					editableBricks[i][j].setIcon(brick1);
				} else if (brickTypeArray[i][j] == 2){
					editableBricks[i][j].setIcon(brick2);
				} else if (brickTypeArray[i][j] == 3){
					editableBricks[i][j].setIcon(brick3);
				} else{
					editableBricks[i][j].setIcon(emptyBrick);
				}
				
				//if the user's mouse is hovering over a brick, then that brick will have a different image (white outline)
				if (hovering){
					if (brickTypeArray[hoveringBrickY][hoveringBrickX] == 1){
						editableBricks[hoveringBrickY][hoveringBrickX].setIcon(brick1Hover);
					} else if (brickTypeArray[hoveringBrickY][hoveringBrickX] == 2){
						editableBricks[hoveringBrickY][hoveringBrickX].setIcon(brick2Hover);
					} else if (brickTypeArray[hoveringBrickY][hoveringBrickX] == 3){
						editableBricks[hoveringBrickY][hoveringBrickX].setIcon(brick3Hover);
					} else{
						editableBricks[hoveringBrickY][hoveringBrickX].setIcon(emptyBrickHover);
					}
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e){
		//initialize objects/variables
		JButton sourceButton = (JButton)e.getSource();
		fc.setSelectedFile(defaultFile);
		int returnVal;
		
		//if the user clicked one of the buttons on the first row, perform an action depending on the button that was pressed (change the type of brick the user can place if it was one of the first 4 buttons, remove all bricks from screen if it was the fifth button)
		if (sourceButton.getText().equals("Brick 1")){
			brickPlaceType = 1;
		} else if (sourceButton.getText().equals("Brick 2")){
			brickPlaceType = 2;
		} else if (sourceButton.getText().equals("Brick 3")){
			brickPlaceType = 3;
		} else if (sourceButton.getText().equals("Delete Brick")){
			brickPlaceType = 0;
		} else if (sourceButton.getText().equals("Clear Bricks")){
			for (int i=0; i<NUM_BRICK_ROWS; i++){
				for (int j=0; j<NUM_BRICK_COLUMNS; j++){
					brickTypeArray[i][j] = 0;
				}
			}
			
			//the map has been updated
			saved = false;
		} else if (sourceButton.getText().equals("Save")){
			//if the user clicks the save button, save the brick types into a file
			returnVal = fc.showSaveDialog(saveContainer);
			
			//file selector window
			if (returnVal == JFileChooser.APPROVE_OPTION){
				savedFile = fc.getSelectedFile();
			}
			
			try{
				bw = new BufferedWriter(new FileWriter(savedFile.getAbsolutePath()));
				
				//write the values from the brick type array into the file
				for (int i=0; i<NUM_BRICK_ROWS; i++){
					for (int j=0; j<NUM_BRICK_COLUMNS; j++){
						bw.write("" + brickTypeArray[i][j]);
					}
					
					bw.newLine();
				}
				
				bw.close();
			} catch (Exception ee){
				//print error message if there was an error saving the file
				System.out.println("ERROR: level was not saved successfully");
			}
			
			//map has been saved
			saved = true;
		} else if (sourceButton.getText().equals("Load")){
			//if the user clicks the load button, load a map from a file
			returnVal = fc.showOpenDialog(saveContainer);
			
			//file selector window
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				loadFile= fc.getSelectedFile();
			}
			
			try{
				br = new BufferedReader(new FileReader(loadFile.getAbsolutePath()));
				
				//read the valeus from the file and transfer it into the brick type array
				for (int i=0; i<NUM_BRICK_ROWS; i++){
					for (int j=0; j<NUM_BRICK_COLUMNS; j++){
						brickTypeArray[i][j] = br.read() - 48;
					}
					
					br.readLine();
				}
				
				br.close();
			} catch (Exception ee){
				//print error message if the file is not loaded properly
				System.out.println("ERROR: level was not loaded successfully");
			}
			
			//map has just been loaded, no changes have been made so no need to save
			saved = true;
		}
		
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		
	}
	
	public void mouseEntered(MouseEvent e) {
		//detect the brick that the user's mouse is hovering over
		hoveringBrickX = ((JLabel)e.getSource()).getX() / 40;
		hoveringBrickY = ((JLabel)e.getSource()).getY() / 20;
		hovering = true;
		
		repaint();
	}
	
	public void mouseExited(MouseEvent e) {
		//detect when the user's mouse is no longer hovering over a brick
		hovering = false;
		
		repaint();
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		//detects when the user clicks, and place a brick/delete a brick depending on which option the user is currently on
		if (e.getButton() == MouseEvent.BUTTON1){
			if (brickPlaceType > 0){
				brickTypeArray[((JLabel)e.getSource()).getY() / 20][((JLabel)e.getSource()).getX() / 40] = brickPlaceType;
			} else{
				brickTypeArray[((JLabel)e.getSource()).getY() / 20][((JLabel)e.getSource()).getX() / 40] = 0;
			}
			
			//map has been updated and needs to be saved
			saved = false;
		}
		
		repaint();
	}
}