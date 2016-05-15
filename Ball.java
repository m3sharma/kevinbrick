/*Kevin Chen
ICS4U1
November 21, 2013
Moving Ball Class
Creates and draws a moving ball*/

import java.awt.*;

public class Ball extends Rectangle implements GameConstants{
	//initialize variables
    private double dx;
	private double dy;
	private double angle;
	private double degrees;
	private int speed;

    //ball constructor class
    public Ball(int x, int y, int width, int height){
    	super(x, y, width, height);
    	//generate random angle
    	degrees = 150 * Math.random() + 15;
    	angle = Math.toRadians(degrees);
    	
    	//set speed
        speed = 10;  
        
        //get the x and y components of the ball's velocity
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }

    //draw ball
    public void draw(Graphics g){
        g.setColor(Color.red);
        g.fillOval(x, y, width, height);
		g.setColor(Color.black);
		g.drawOval(x, y, width, height);
	}

    //change ball's position (move)
    public void move(int minx, int miny, int maxx, int maxy){
        //if the ball moves off the screen, it will bounce back
        
        if (x<minx || x+width>maxx){
            dx = dx*-1;
        }
        if (y<miny){
            dy = dy*-1;
        }
        
        //new x and y values
        x = x + (int)dx;
        y = y + (int)dy;
    }
    
    //handles collision against paddle
    public void paddleCollision(double objectx, double objecty, double objectWidth, double objectHeight){
    	if (x > objectx-width && x < objectx && y > objecty-height/2 && y <= objecty+objectHeight-height/2){
    		//ball collides with left side of paddle
    		dx = -Math.abs(dx);
    	} else if (x > objectx+objectWidth-width && x < objectx+objectWidth && y >= objecty-height/2 && y <= objecty+objectHeight-height/2){
    		//ball collides with right side of paddle
    		dx = Math.abs(dx);
    	}else if (x > objectx-width && x < objectx+objectWidth && y > objecty-height && y <= objecty+objectHeight-height){
    		//checks if the ball is colliding with the top of the paddle
    		//calculate the new angle and horizontal/vertical velocity components of the ball depending on where on the paddle the ball collides with
    		angle = Math.atan2(getCenterY()-(objecty+objectHeight/2), getCenterX()-(objectx+objectWidth/2));
        	dx = Math.cos(angle) * speed;
            dy = Math.sin(angle) * speed;
    	}
    }
    
    //handles collision against bricks
    public void brickCollision(double objectx, double objecty, double objectWidth, double objectHeight){
    	if (x > objectx-width && x < objectx-width/2 && y > objecty-height && y < objecty-height/2){
    		//ball collides with top left corner of brick
    		dx = -Math.abs(dx);
    		dy = -Math.abs(dy);
    	} else if (x >= objectx-width/2 && x <= objectx+objectWidth-width/2 && y > objecty-height && y < objecty){
    		//ball collides with top of brick
    		dy = -Math.abs(dy);
    	} else if (x > objectx+objectWidth-width/2 && x < objectx+objectWidth && y > objecty-height && y < objecty-height/2){
    		//ball collides with top right corner of brick
    		dx = Math.abs(dx);
    		dy = -Math.abs(dy);
    	} else if (x > objectx-width && x < objectx && y >= objecty-height/2 && y <= objecty+objectHeight-height/2){
    		//ball collides with left side of brick
    		dx = -Math.abs(dx);
    	} else if (x > objectx+objectWidth-width && x < objectx+objectWidth && y >= objecty-height/2 && y <= objecty+objectHeight-height/2){
    		//ball collides with right side of brick
    		dx = Math.abs(dx);
    	} else if (x > objectx-width && x < objectx-width/2 && y > objecty+objectHeight-height/2 && y < objecty+objectHeight){
    		//ball collides with bottom left corner of brick
    		dx = -Math.abs(dx);
    		dy = Math.abs(dy);
    	} else if (x >= objectx-width/2 && x <= objectx+objectWidth-width/2 && y > objecty+objectHeight-height && y < objecty+objectHeight){
    		//ball collides with bottom of brick
    		dy = Math.abs(dy);
    	} else if (x > objectx+objectWidth-width/2 && x < objectx+objectWidth && y > objecty+objectHeight-height/2 && y < objecty+objectHeight){
    		//ball collides with bottom right corner of brick
    		dx = Math.abs(dx);
    		dy = Math.abs(dy);
    	}
    }
}