package model;

import java.util.LinkedList;
import java.util.Observable;
import javafx.scene.image.Image;

/**
 * abstract class for the 3 possible enemies. This class handles movement, storing
 * their direction list and the basic getters and setters.
 */
public abstract class Enemy extends Observable {
	private int speed;
	private int health;
	private int armor;
	private int positionX;
	private int positionY;
	private String name;
	private EnemyStatus status = EnemyStatus.NORMAL;
	private LinkedList<String> direction;// This list is for hard code moving
	private int originalHealth;
	private int amtAppliedEffect;
	private Tower attackedBy;

	/**
	 * constructs the enemy given their stats and their direction list for movement.
	 * @param speed speed of enemy in int
	 * @param health health of enemy in int
	 * @param armor armor of enemy in int
	 * @param xPos x coordinate of enemy in int
	 * @param yPos y coordinate of enemy in int
	 * @param dir enemy movement direction in LinkedList of String
	 * @param name String representation of enemy name
	 */
	public Enemy(int speed, int health, int armor, int xPos, int yPos, LinkedList<String> dir, String name) {
		this.speed = speed;
		this.health = health;
		this.armor = armor;
		this.positionX = xPos;
		this.positionY = yPos;
		this.direction = (LinkedList<String>) dir.clone();// It is Hard Code how
															// to move enemy
		this.name = name;
		this.originalHealth = health;
		this.amtAppliedEffect = 0;
		this.attackedBy = null;
	}

	/**
	 * getter for originalHealth
	 * 
	 * @return returns the enemy's starting(original) health
	 */
	public int getOriginalHealth() {
		return originalHealth;
	}

	/**
	 * returns the enemy's normal speed
	 * 
	 * @return returns speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * sets the enemy's speed
	 * 
	 * @param input desired speed
	 */
	public void setSpeed(int input) {
		this.speed = input;
	}

	/**
	 * returns the enemy's name. i.e. bat, skeleton, ghost
	 * 
	 * @return this object's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns this enemy's armor, however armor went unimplemented
	 * 
	 * @return returns this object's armor
	 */
	public int getArmor() {
		return this.armor;
	}

	/**
	 * sets the enemy's armor to the desired value
	 * 
	 * @param input the desired armor value
	 */
	public void setArmor(int input) {
		this.armor = input;
	}

	/**
	 * returns the enemy's image to be drawn based on if it is moving left, right, up or down, depending on the type of enemy
	 * 
	 * @return the correct image for the instance of enemy depending on its moving direction
	 */
	public abstract Image getImage();

	/**
	 * returns the enemy's current health
	 * 
	 * @return this objects health value
	 */
	public int getHealth() {
		return this.health;
	}

	/**
	 * sets the enemy's health for when it gets attacked. if the health becomes 0 or less, set enemy to dead.
	 * 
	 * @param newHealth desired health value
	 */
	public void setHealth(int newHealth) {
		this.health = newHealth;
		if (newHealth <= 0)
			this.setStatus(EnemyStatus.DIED);
		setChanged();
		notifyObservers();
	}

	/**
	 * sets the enemy's position on the GameMap
	 * 
	 * @param inputX row coord
	 * @param inputY column coord
	 */
	public void setPos(int inputX, int inputY) {
		this.positionX = inputX;
		this.positionY = inputY;
		setChanged();
		notifyObservers();
	}

	/**
	 * sets the enemy's status (dead, slowed, escaped etc)
	 * 
	 * @param eStatus enemy's desired status
	 */
	public void setStatus(EnemyStatus eStatus) {
		this.status = eStatus;
		setChanged();
		notifyObservers();
	}

	/**
	 * sets which tower the enemy is being attacked by. used for drawing the tower effect GIF  at the correct location
	 * 
	 * @param tower set the tower that's attacking this enemy
	 */
	public void isAttackedBy(Tower tower) {
		this.attackedBy = tower;
		setChanged();
		notifyObservers();
	}

	/**
	 * returns the tower that is attacking this enemy
	 * 
	 * @return tower attacking this enemy
	 */
	public Tower isAttackedBy() {
		return attackedBy;
	}

	/**
	 * returns this enemy's status
	 * 
	 * @return this enemy's status
	 */
	public EnemyStatus getStatus() {
		return this.status;
	}

	/**
	 * returns this enemy's row 
	 * 
	 * @return this enemy's row
	 */
	public int getXPos() {
		return this.positionX;
	}

	/**
	 * returns this enemy's column
	 * 
	 * @return this enemy's column
	 */
	public int getYPos() {
		return this.positionY;
	}

	/**
	 * if the enemy has not died or escaped, returns true, else false
	 * 
	 * @return true if enemy is alive, else false
	 */
	public boolean isAlive() {
		if (this.status == EnemyStatus.DIED || this.status == EnemyStatus.ESCAPED)
			return false;
		return true;
	}

	/**
	 * returns true if enemy has escaped, else false
	 * 
	 * @return true is enemy escaped, else false
	 */
	public boolean hasEscaped() {
		return this.status == EnemyStatus.ESCAPED;
	}

	/**
	 * returns the appropriate amount of gold rewarded for slaying this enemy
	 * 
	 * @return gold awarded for killing this enemy
	 */
	public abstract int giveGold();

	/**
	 * moves the enemy the desired number of moves down its
	 * direction path. takes into account if the enemy is dead/escaped/frozen
	 * etc. after 2 moves, any enemy effect is worn off.
	 * 
	 * @param moves the number of spaces down the path the enemy is to move
	 */
	public void move(int moves) {
		if (this.getStatus() == EnemyStatus.DIED)
			return;
		if (direction.size() <= 0 || this.hasEscaped()) {
			this.setStatus(EnemyStatus.ESCAPED);
			return;// It means the Enemy has been move to the end
		}
		for (int i = 0; i < moves; i++) {

			if (direction.size() <= 0 || this.hasEscaped()) {
				this.setStatus(EnemyStatus.ESCAPED);
				return;// It means the Enemy has been move to the end
			}

			String moveDir = "";
			if (direction.size() > 0)
				moveDir = direction.removeFirst();
			if (moveDir.equalsIgnoreCase("Right"))
				this.setPos(this.getXPos(), this.getYPos() + 1);
			else if (moveDir.equalsIgnoreCase("Left"))
				this.setPos(this.getXPos(), this.getYPos() - 1);
			else if (moveDir.equalsIgnoreCase("Up"))
				this.setPos(this.getXPos() - 1, this.getYPos());
			else if (moveDir.equalsIgnoreCase("Down"))
				this.setPos(this.getXPos() + 1, this.getYPos());
		}

		if (this.getStatus() != EnemyStatus.NORMAL)
			this.amtAppliedEffect++;

		if (this.amtAppliedEffect == 0)
			this.isAttackedBy(null);
		else if (this.amtAppliedEffect >= 3) {
			this.amtAppliedEffect = 0;
			this.setStatus(EnemyStatus.NORMAL);
		}

		setChanged();
		notifyObservers();
	}

	/**
	 * the Enemy current Speed, Status will affect enemy Speed
	 * 
	 * @return the enemies' current speed 
	 */
	public int currentSpeed() {
		if (status == EnemyStatus.FREEZE)
			return 0;
		if (status == EnemyStatus.SLOWDOWN)
			return speed - 1;
		return speed;
	}

	/**
	 * get the Enemy moving path
	 * 
	 * @return the direction list of the enemy
	 */
	public LinkedList<String> getDirectionList() {
		return this.direction;
	}

	/**
	 * returns the enemy's health, status and
	 * location in the form of string
	 * 
	 * @return String representation of all enemy info
	 */
	@Override
	public String toString() {
		return name + "(" + this.getHealth() + ")" + ": " + getStatus() + " (" + this.getXPos() + "," + this.getYPos()
				+ ")";
	}

}
