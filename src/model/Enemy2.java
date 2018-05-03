package model;

import java.util.LinkedList;
import javafx.scene.image.Image;

/**
 * enemy 2 is an extension of the enemy super class. it is a fast speed, low health
 * enemy drawn as a bat
 */
public class Enemy2 extends Enemy {
	private static String name = "skeleton";
	
	/**
	 * constructor for enemy2
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @param dir direction list
	 */
	public Enemy2(int x, int y, LinkedList<String> dir) {
		super(2,10,1, x, y, dir, name);
		// this.symbol = "#";
	}


	/*
	 * (non-Javadoc)
	 * @see model.Enemy#giveGold()
	 */
	public int giveGold() {
		return 8;
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
		
		Image image = new Image("images/Enemy/Bat/Enemy_Bat-Down.gif"); // normal stance is going south.
		
		if (getDirectionList().get(0).equals("right"))
			image = new Image("images/Enemy/Bat/Enemy_Bat-Right.gif");
		else if (getDirectionList().get(0).equals("left"))
				image = new Image("images/Enemy/Bat/Enemy_Bat-Left.gif");
		else if (getDirectionList().get(0).equals("up"))
				image = new Image("images/Enemy/Bat/Enemy_Bat-Up.gif");
		
		return image;
	}


}
