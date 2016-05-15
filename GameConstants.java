/*Kevin Chen
ICS4U1
November 21, 2013
Constants class
Interface containing constants*/

public interface GameConstants{
	//Main Window Constants
	public final int WIN_WIDTH = 640;
	public final int WIN_HEIGHT = 480;
	public final String WIN_TITLE = "One Piece Brick Breaker";
	
	//Map Creator Window Constants
	public final int MAP_WIN_WIDTH = 640;
	public final int MAP_WIN_HEIGHT = 480;
	public final String MAP_WIN_TITLE = "Custom Map Creator";
	
	//Game Setup Constants
	public final int NUM_BRICK_COLUMNS = 10;
	public final int NUM_BRICK_ROWS = 10;
	public final int BRICK_WIDTH = 40;
	public final int BRICK_HEIGHT = 20;
	public final int BRICK_START_X = WIN_WIDTH/2 - NUM_BRICK_COLUMNS/2*BRICK_WIDTH;
	public final int BRICK_START_Y = (WIN_HEIGHT-55)/3 - NUM_BRICK_ROWS/2*BRICK_HEIGHT;
	
	//Ball Constants
	public final int BALL_WIDTH = 15;
	public final int BALL_HEIGHT = 15;
	public final int BALL_START_X = WIN_WIDTH/2 - BALL_WIDTH/2;
	public final int BALL_START_Y = WIN_HEIGHT-100;
	
	//Paddle Constants
	public final int PADDLE_WIDTH = 80;
	public final int PADDLE_HEIGHT = 15;
	public final int PADDLE_START_X = WIN_WIDTH/2 - PADDLE_WIDTH/2;
	public final int PADDLE_START_Y = WIN_HEIGHT - (99-BALL_HEIGHT);
}