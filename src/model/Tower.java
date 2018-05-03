package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.image.ImageView;

/**
 * 
 * Models a tower to be places onto a GameMap and that will attack the enemies. Each tower has different attributes
 * and cost different amounts of gold.
 *
 */
public abstract class Tower extends TileContent {

	private int price;
	private int upgradePrice;
	private int version;
	private int range;
	private int damage;
	private int speed;
	private TowerType type;
	private GameMap map;
	private boolean isAttacking;

	/**
	 * constructs our tower given the GameMap and TowerType
	 * 
	 * @param map add this tower to game map
	 * @param type decide A/B/C tower type
	 */
	public Tower(GameMap map, TowerType type) {
		super(TileType.TOWER);
		this.type = type;
		this.price = 30;
		this.upgradePrice = price/2;
		this.version = 1;
		this.range = 1;
		this.damage = 1;
		this.speed = 3;
		this.type = type;
		this.map = map;
		this.isAttacking = false;
	}

	/**
	 * returns if tower is attacking
	 * 
	 * @return if tower is attacking, it will return true.
	 */
	public boolean isAttacking() {
		return isAttacking;
	}
	
	/**
	 * sets the attack status
	 * 
	 * @param attackStatus set the tower attack status.
	 */
	public void isAttacking(boolean attackStatus) {
		isAttacking = attackStatus;
		setChanged();
	    notifyObservers();
	}	
	
	/**
	 * upgrades a single tower attribute
	 * 
	 * @param range if true, upgrade range
	 * @param damage if true, upgrade damage
	 * @param speed if true, upgrade speed
	 */
	public void upgrade(boolean range, boolean damage, boolean speed) {
		if (version < 3) {
			if (range)
				this.range += 1;
			else if (damage)
				this.damage *= 2;
			else
				this.speed *= 2;
			version++;
			setChanged();
		    notifyObservers();
		}
	}
	
	/**
	 * returns the tower's special power
	 * 
	 * @return a string explain what kind of power this tower have. For example poison/freeze/slowdown
	 */
	public abstract String getSpecialPower();
	
	/**
	 * get the map the tower is on
	 * 
	 * @return map
	 */
	public GameMap getMap(){
		return map;
	}
	
	/**
	 * returns this tower's damage
	 * 
	 * @return damage
	 */
	public int getDamage(){
		return damage;
	}
	
	/**
	 * sets this tower's damage
	 * 
	 * @param input desired damage
	 */
	public void setDamage(int input){
		this.damage = input;
	}
	
	/**
	 * returns this tower's range
	 * 
	 * @return range
	 */
	public int getRange(){
		return range;
	}

	/**
	 * sets this tower's range
	 * 
	 * @param input desired range
	 */
	public void setRange(int input){
		this.range = input;
	}
	
	/**
	 * returns this tower's price
	 * 
	 * @return price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * returns this tower's speed
	 * 
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * sets this tower's speed
	 * 
	 * @param input desired speed
	 */
	public void setSpeed(int input){
		this.speed = input;
	}
	
	/**
	 * returns this tower's version (1 to 3)
	 * 
	 * @return version
	 */
	public int getVersion() {
		return version;
	}
	
	/**
	 * sets this tower's version
	 * 
	 * @param input desired version
	 */
	public void setVersion(int input){
		this.version = input;
	}
	
	/**
	 * returns this tower's upgraded price
	 * 
	 * @return upgradePrice
	 */
	public int getUpgradePrice() {
		return upgradePrice;
	}
	
	/**
	 * returns this tower's type
	 * 
	 * @return type
	 */
	public TowerType getTowerType() {
		return type;
	}
	
	/**
	 * @return the tower status as string, used for when user clicks on tower to view its stats
	 */
	public String getStats() {
		String output = "";
		output += "Price: " + getPrice();
		output += "\nDamage: " + getDamage();
		output += "\nSpeed: " + getSpeed();
		output += "\nRange: " + getRange();
		output += "\nSpecial:\n " + getSpecialPower(); // need to add special power field
		return output;
	}
	
	/**
	 * @return ImageView of tower image
	 */
	public abstract ImageView getImage();
	
	/**
	 * attacks a single enemy. if an enemy has been killed, return that enemy. applies this towers effect
	 * on the enemy for 2 moves.
	 * 
	 * @return an enemy which been killed by this tower
	 */
	public Enemy attack() {
		int numAttacks = 0;
		LinkedList<Enemy> enemies = map.getAliveEnemies();
		for (Iterator<Enemy> it = enemies.iterator(); it.hasNext();) {
			Enemy enemy = it.next();
			if (Math.abs(getTile().getYpos() - enemy.getXPos()) <= getRange() 
				&& Math.abs(getTile().getXpos() - enemy.getYPos()) <= getRange()) {
				  numAttacks++;
				  isAttacking(true);
				  enemy.isAttackedBy(this);
				  enemy.setHealth(enemy.getHealth()-this.getDamage());
				  if (enemy.getStatus() == EnemyStatus.DIED || enemy.getHealth() <= 0)
					  return enemy;
				  if (getSpecialPower().equals("POISON"))
					  enemy.setStatus(EnemyStatus.POISON);
				  else if (getSpecialPower().equals("FREEZE"))
					  enemy.setStatus(EnemyStatus.FREEZE);
				  else
					  enemy.setStatus(EnemyStatus.SLOWDOWN);
				  System.out.println("Tower "+ this.type + " attacked " + enemy.toString()
					  		+ " Enemy now has " + enemy.getHealth() +" health.");
				  if (numAttacks > 1)
					  break;
			}
			else
				isAttacking(false);
		}
		return null; // no enemy died
	}

}
