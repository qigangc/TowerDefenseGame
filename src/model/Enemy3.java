package model;

import java.util.LinkedList;
import javafx.scene.image.Image;

/**
 * enemy 3 is an extension of the enemy super class. it is a fast speed, low health
 * enemy drawn as a bat
 */
public class Enemy3 extends Enemy {
	private static String name = "ghost";
	
	/**
	 * constructor for enemy3
	 * 
	 * @param x row coordinate
	 * @param y column coordinate
	 * @param dir direction list
	 */
	public Enemy3(int x, int y, LinkedList<String> dir) {
		super(1, 20, 1, x, y, dir, name);
	}

	/*
	 * (non-Javadoc)
	 * @see model.Enemy#giveGold()
	 */
	public int giveGold() {
		return 10;
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
		
		Image image = new Image("images/Enemy/Ghost/Enemy_Ghost-Down.gif"); // normal stance is going south.
		
		if (getDirectionList().get(0).equals("right"))
			image = new Image("images/Enemy/Ghost/Enemy_Ghost-Right.gif");
		else if (getDirectionList().get(0).equals("left"))
			image = new Image("images/Enemy/Ghost/Enemy_Ghost-Left.gif");
		else if (getDirectionList().get(0).equals("up"))
			image = new Image("images/Enemy/Ghost/Enemy_Ghost-Up.gif");
		
		return image;
	}

}
