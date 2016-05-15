/*Kevin Chen
ICS4U1
November 21, 2013
Brick Class
Creates and draws bricks*/

import java.awt.*;
import javax.swing.*;

public class Brick extends Rectangle implements GameConstants{
	//initialize variables
	private boolean destroyed;
	private int brickType;
	private String brickPath;
	private ImageIcon brickImage;
	
	//constructor
	public Brick(int inputx, int inputy, int inputwidth, int inputheight, int brickType){
		super(inputx, inputy, inputwidth, inputheight);
		
		this.brickType = brickType;
		brickPath = "img/brick" + brickType + ".png";
		brickImage = new ImageIcon(brickPath);
		
		//if there is no brick at the location it is simply set as destroyed
		if (brickType == 0){
			destroyed = true;
		}
	}
	
	//returns whether the brick has been destroyed
	public boolean getDestroyed(){
		return destroyed;
	}
	
	//allows user to manually set whether the brick should be destroyed or ot
	public void setDestroyed(){
		destroyed = true;
	}
	
	//if the brick hasn't been destroyed, draw it
	public void draw(Graphics g){
		if (!destroyed){
			g.drawImage(brickImage.getImage(), this.x, this.y, brickImage.getIconWidth(), brickImage.getIconHeight(), null);
		}
	}
	
	//when the brick is hit, reduce the bricktype by one, if the bricktype becomes zero the brick has been destroyed
	public void hit(){
		brickType--;
		brickPath = "img/brick" + brickType + ".png";
		brickImage = new ImageIcon(brickPath);
		
		if (brickType == 0){
			destroyed = true;
		}
	}
}