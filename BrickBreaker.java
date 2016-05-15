/*Kevin Chen
ICS4U1
November 21, 2013
Brick Breaker
Brick Breaker Game panel class*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

public class BrickBreaker extends JPanel implements ActionListener, KeyListener, GameConstants{
	//initialize variables
	private Brick[][] bricks = new Brick[NUM_BRICK_ROWS][NUM_BRICK_COLUMNS];
	private int[][] brickType = new int[NUM_BRICK_ROWS][NUM_BRICK_COLUMNS];
	private Ball ball;
	private Paddle paddle;
	private Timer clock;
	
	private JFileChooser fc = new JFileChooser();
	private File loadFile = new File("levels/level_default.opbb");
	private File bgFile = new File ("img/background.jpg");
	private Container loadContainer;
	private BufferedReader br;
	
	private JLabel pauseLabel;
	private boolean paused;
	private JLabel livesLabel;
	
	private int lives;
	private boolean gameStart;
	private boolean allDestroyed;
	private boolean paddleMoveLeft;
	private boolean paddleMoveRight;
	private int paddleMoveCount;
	
	private boolean collision;
	private double closestBrickDistance;
	private int closestBrickRow;
	private int closestBrickColumn;
	
	private JFrame winMessageFrame;
	private JFrame loseMessageFrame;
	
	private ImageIcon background;
	private JLabel victoryImage;
	private JLabel loseImage;
	
	//constructor
	public BrickBreaker(){
		//get background images
		background = new ImageIcon(bgFile.getAbsolutePath());
		victoryImage = new JLabel();
		loseImage = new JLabel();
		victoryImage.setIcon(new ImageIcon("img/victory.jpg"));
		loseImage.setIcon(new ImageIcon("img/defeat.jpg"));
		
		//get bricks
		for (int i=0; i<NUM_BRICK_ROWS; i++){
			for (int j=0; j<NUM_BRICK_COLUMNS; j++){
				bricks[i][j] = new Brick(BRICK_START_X + j*BRICK_WIDTH, BRICK_START_Y + i*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, 0);
			}
		}
		
		//set up game
		paused = false;
		lives = 3;
		pauseLabel = new JLabel("");
		livesLabel = new JLabel("Lives: " + lives);
		this.add(livesLabel);
		this.add(pauseLabel);
		restartGame();
		
		//start timer
		clock = new Timer(20, this);
		clock.start();
	}
	
	//paint method
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		//draw background
		g.drawImage(background.getImage(), 0, 0, null);
		
		//draw bricks
		for (int i=0; i<NUM_BRICK_ROWS; i++){
			for (int j=0; j<NUM_BRICK_COLUMNS; j++){
				bricks[i][j].draw(g);
			}
		}
		
		//draw ball
		ball.draw(g);
		
		//draw paddle
		paddle.draw(g);
	}
	
	public void actionPerformed(ActionEvent e){
		//move the ball for every tick of the clock
		if (e.getSource()==clock){
			//only move ball/detect collisions if the game has started
			if (gameStart){
				//reset variables
				collision = false;
				closestBrickDistance = 1000000;
				closestBrickRow = 0;
				closestBrickColumn = 0;
				
				//move ball using the ball's move class
				ball.move(0, 5, WIN_WIDTH, WIN_HEIGHT-55);
				
				//check for collision between paddle and ball
				ball.paddleCollision(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());
				
				//check for collision between each brick and ball
				for (int i=0; i<NUM_BRICK_ROWS; i++){
					for (int j=0; j<NUM_BRICK_COLUMNS; j++){
						//only check brick that have not been destroyed
						if (!bricks[i][j].getDestroyed()){
							if (ball.getBounds().intersects(bricks[i][j].getBounds())){
								collision = true;
								
								//find the brick that is closest to the ball, so the ball doesn't break several bricks at the same time
								if (Math.sqrt(Math.pow(bricks[i][j].getCenterX() - ball.getCenterX(), 2) + Math.pow(bricks[i][j].getCenterY() - ball.getCenterY(), 2)) < closestBrickDistance){
									closestBrickDistance = Math.sqrt(Math.pow(bricks[i][j].getCenterX() - ball.getCenterX(), 2) + Math.pow(bricks[i][j].getCenterY() - ball.getCenterY(), 2));
									closestBrickRow = i;
									closestBrickColumn = j;
								}
							}
						}
					}
				}
				
				if (collision){
					//change the angle of the ball's velocity and run the hit method for the brick that was hit
					ball.brickCollision(bricks[closestBrickRow][closestBrickColumn].getX(), bricks[closestBrickRow][closestBrickColumn].getY(), bricks[closestBrickRow][closestBrickColumn].getWidth(), bricks[closestBrickRow][closestBrickColumn].getHeight());
					bricks[closestBrickRow][closestBrickColumn].hit();
					
					//check if all of the bricks have been destroyed, if they have the player has won
					allDestroyed = true;
					for (int i=0; i<NUM_BRICK_ROWS; i++){
						for (int j=0; j<NUM_BRICK_COLUMNS; j++){
							if (!bricks[i][j].getDestroyed()){
								allDestroyed = false;
							}
						}
					}
					
					if (allDestroyed){
						winMessage();
					}
				}
				
				//if the ball falls out of the bottom of the screen the ball is sent back to the starting position and the player loses a life
				if (ball.y + BALL_HEIGHT > WIN_HEIGHT-55){
					//decrease the number of lives and update the counter
					lives--;
					livesLabel.setText("Lives: " + lives);
					
					//check if the player has any lives remaining
					if (lives == 0){
						loseMessage();
						
						//give the player 3 lives again and clearing all blocks from the screen (they have to load the level again)
						for (int i=0; i<NUM_BRICK_ROWS; i++){
							for (int j=0; j<NUM_BRICK_COLUMNS; j++){
								bricks[i][j].setDestroyed();
							}
						}
					}
					
					//reset game
					restartGame();
				}
				
				//if the user is currently holding the left/right arrow keys, the paddle will move left/right at increasing speeds depending on how long the user has been holding down the arrow key
				if (paddleMoveLeft){
					paddle.move(0, WIN_WIDTH, true);
					paddle.speedIncrement();
					paddleMoveCount++;
				} else if (paddleMoveRight){
					paddle.move(0, WIN_WIDTH, false);
					paddle.speedIncrement();
					paddleMoveCount++;
				}
			}
		} else{
			//initialize object
			JButton sourceButton = (JButton)e.getSource();
			
			if (sourceButton.getText().equals("Load")){
				//pause clock while the user loads a map
				clock.stop();
				
				//open file selector
				fc.setSelectedFile(loadFile);
				int returnVal = fc.showOpenDialog(loadContainer);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					loadFile= fc.getSelectedFile();
				}
				
				try{
					br = new BufferedReader(new FileReader(loadFile.getAbsolutePath()));
					
					//read the file and use the values in the file to determine the brick types for the bricks
					for (int i=0; i<NUM_BRICK_ROWS; i++){
						for (int j=0; j<NUM_BRICK_COLUMNS; j++){
							brickType[i][j] = br.read() - 48;
						}
						
						br.readLine();
					}
					
					br.close();
					
					//recreate the bricks
					for (int i=0; i<NUM_BRICK_ROWS; i++){
						for (int j=0; j<NUM_BRICK_COLUMNS; j++){
							if (brickType[i][j] < 1 || brickType[i][j] > 3){
								bricks[i][j] = new Brick(BRICK_START_X + j*BRICK_WIDTH, BRICK_START_Y + i*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, 0);
							} else{
								bricks[i][j] = new Brick(BRICK_START_X + j*BRICK_WIDTH, BRICK_START_Y + i*BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT, brickType[i][j]);
							}
						}
					}
					
					//reset the ball/paddle to the starting positions
					restartGame();
				} catch (Exception ee){
					//if there was an error loading the file display error message
					System.out.println("ERROR: level was not loaded successfully");
				}
				
				//resume clock if the game isn't paused
				if (!paused){
					clock.start();
				}
			} else if (sourceButton.getText().equals("Background")){
				//pause clock while user loads background
				clock.stop();
				
				//open background selector
				fc.setSelectedFile(bgFile);
				int returnVal = fc.showOpenDialog(loadContainer);
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					bgFile= fc.getSelectedFile();
				}
				
				//change the background
				background = new ImageIcon(bgFile.getAbsolutePath());
				
				//resume clock if the game isn't paused
				if (!paused){
					clock.start();
				}
			} else if (sourceButton.getText().equals("Pause")){
				//pause game if pause button is pressed
				if (!paused){
					clock.stop();
					paused = true;
					pauseLabel.setText("PAUSED");
				} else{
					clock.start();
					paused = false;
					pauseLabel.setText("");
				}
			}
		}
		
		repaint();
	}

	public void keyPressed(KeyEvent e) {
		//detects when the user presses the left or right arrow key
		if (e.getKeyCode() == KeyEvent.VK_LEFT && gameStart && !paused){
			paddleMoveLeft = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && gameStart){
			paddleMoveRight = true;
		}
		
		repaint();
	}

	public void keyReleased(KeyEvent e) {
		//detects when the user releases the left or right arrow key, and resets the speed of the paddle back to minimum
		if (e.getKeyCode() == KeyEvent.VK_LEFT && gameStart && !paused){
			paddleMoveLeft = false;
			paddle.speedReset();
			paddleMoveCount = 0;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT && gameStart){
			paddleMoveRight = false;
			paddle.speedReset();
			paddleMoveCount = 0;
		}
		
		repaint();
	}

	public void keyTyped(KeyEvent e) {
		//detects when the user presses the space bar and starts the game if it hasn't started yet
		if (e.getKeyChar() == KeyEvent.VK_SPACE && !gameStart && !paused){
			gameStart = true;
		}
		
		repaint();
	}
	
	public void restartGame(){
		//sets variables, redraws the ball and paddle at the starting positions
		gameStart = false;
		allDestroyed = false;
		paddleMoveLeft = false;
		paddleMoveRight = false;
		paddleMoveCount = 0;
		ball = new Ball(BALL_START_X, BALL_START_Y, BALL_WIDTH, BALL_HEIGHT);
		paddle = new Paddle(PADDLE_START_X, PADDLE_START_Y, PADDLE_WIDTH, PADDLE_HEIGHT);
	}
	
	public void winMessage(){
		//pause game while the victory message displays
		clock.stop();
		
		//create new window displaying the victory message
		winMessageFrame = new JFrame("Victory!");
		winMessageFrame.setSize(720, 540);
		winMessageFrame.setVisible(true);
		winMessageFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		winMessageFrame.add(victoryImage);
		
		winMessageFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					//when the user closes the new victory message window the ball and paddle are placed back at the starting positions and the clock resumes
					restartGame();
					clock.start();
				}
			}
		);
		
		//put the number of lives back to 3 and update counter
		lives = 3;
		livesLabel.setText("Lives: " + lives);
	}
	
	public void loseMessage(){
		//pause game while the victory message displays
		clock.stop();
		
		//create new window displaying the victory message
		loseMessageFrame = new JFrame("Defeat!");
		loseMessageFrame.setSize(720, 540);
		loseMessageFrame.setVisible(true);
		loseMessageFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		loseMessageFrame.add(loseImage);
		
		loseMessageFrame.addWindowListener(
			new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					//when the user closes the new victory message window the ball and paddle are placed back at the starting positions and the clock resumes
					clock.start();
				}
			}
		);
		
		//put the number of lives back to 3 and update counter
		lives = 3;
		livesLabel.setText("Lives: " + lives);
	}
}