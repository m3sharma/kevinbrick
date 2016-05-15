/*Kevin Chen
ICS4U1
November 21, 2013
Paddle Class
Creates and draws the paddle*/

import java.awt.*;

public class Paddle extends Rectangle implements GameConstants{
	//initialize variables
	private int speed;
	
	//constructor
	public Paddle(int x, int y, int width, int height){
		super(x, y, width, height);
		speed = 3;
	}
	
	//method draws the paddle
	public void draw(Graphics g){
		g.setColor(new Color(51, 102, 153));
		g.fillRect(x, y, width, height);
		g.setColor(new Color(0, 51, 102));
		g.drawRect(x, y, width, height);
	}
	
	//method moves paddle
	public void move(int minx, int maxx, boolean left){
		//if paddle is currently moving left, move the paddle left until left border of screen
		if (left && x>minx){
			x = x - speed;
		}
		//if paddle is currently moving right, move the paddle right until right border of screen
		if (!left && x+width<maxx){
			x = x + speed;
		}
	}
	
	//method slowly increments the speed of the paddle as the user presses down on the button
	public void speedIncrement(){
		if (speed < 16){
			speed++;
		}
	}
	
	//resets the speed of the paddle back to minimum after the user releases the button
	public void speedReset(){
		speed = 3;
	}
}