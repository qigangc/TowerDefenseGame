package model;

import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/*
 * Health = 5;
 * Armor= 1;
 * Gold = 1;
 * Speed = 3;
 * */

/**
 * enemy 1 is an extension of the Enemy super class. it is a fast speed, low health
 * enemy drawn as a bat
 */
public class Enemy1 extends Enemy {
	private static String name = "bat";
	
	/**
	 * constructor for enemy1
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @param dir direction list
	 */
	public Enemy1(int x, int y, LinkedList<String> dir) {
		super(3,5,0, x, y, dir, name);
	}

	/*
	 * (non-Javadoc)
	 * @see model.Enemy#giveGold()
	 */
	public int giveGold() {
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * @see model.Enemy#getName()
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * (non-Javadoc)
	 * @see model.Enemy#getImage()
	 */
	public Image getImage() {
		
		if (getDirectionList().size()==0)
			return null;
		
		Image image = new Image("images/Enemy/Skeleton/Enemy_Tomb-Down.gif"); // normal stance is going south.
		
		if (getDirectionList().get(0).equals("right"))
			image = new Image("images/Enemy/Skeleton/Enemy_Tomb-Right.gif");
		else if (getDirectionList().get(0).equals("left"))
			image = new Image("images/Enemy/Skeleton/Enemy_Tomb-Left.gif");
		else if (getDirectionList().get(0).equals("up"))
			image = new Image("images/Enemy/Skeleton/Enemy_Tomb-Up.gif");

		return image;
	}

}
