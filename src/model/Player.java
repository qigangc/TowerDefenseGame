package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * models one player that is playing on a GameMap. Contains info such as player gold, currency, score, health, towers, etc. Observed
 * by the GameView
 */
public class Player extends Observable {

  private int score;
  private int health;
  private int gold;
  private List<Tower> towers;
  private GameMap map;

  /**
   * constructs our player object based on a GameMap
   * 
   * @param map GameMap to play the game on
   */
  public Player(GameMap map){
    this.map = map;
    score = 0;
    health = 30;
    gold = 100;
    towers = new ArrayList<Tower>();
  }

  /**
   * Attempts to buy a tower and add to the players tower list, given the player has the funds
   * 
   * @param tower attempt tower purchased
   * @return the tower bought
   */
  public Tower buyTower(Tower tower) {
    try {
      adjustGold(-tower.getPrice());//Buy tower, call Observers
      towers.add(tower);
	  setChanged();
      notifyObservers();
      return tower;
      
    } catch (NotEnoughGoldException e) {
      return null;
    }

  }

  /**
   * returns the player's GameMap
   * 
   * @return map
   */
  public GameMap getMap() {
	  return map;
  }
  
  /**
   * returns the player's score
   * 
   * @return score
   */
  public int getScore() {
	  return this.score;
  }
  
  /**
   * sets the player's score
   * 
   * @param input desired score
   */
  public void setScore(int input){
	  this.score = input;
  }
  
  /**
   * sets the player's gold
   * 
   * @param input desired gold
   */
  public void setGold(int input){
	  this.gold = input;
  }
  
  /**
   * adjusts the player's score
   * 
   * @param val score to be added
   */
  public void adjustScore(int val){
	  this.score += val;
	  setChanged();
      notifyObservers();
  }
  
  /**
   * deducts from the player's health
   * 
   * @param val amount to be deducted
   */
  public void loseHealth(int val){
	  this.health -= val;
	  if (health < 0)
		  health = 0;
	  setChanged();
      notifyObservers();
  }
  
  /**
   * sets the player's health
   * 
   * @param input desired health
   */
  public void setHealth(int input){
	  this.health = input;
  }
  
  /**
   * returns player's health
   * 
   * @return health
   */
  public int getHealth() {
	  return health;
  }
  
  /**
   * returns player's gold
   * 
   * @return gold
   */
  public int getGold() {
	  return gold;
  }


  /**
   * attempts to upgrade a tower's stat, returns true if tower was successfully upgraded
   * 
   * @param tower tower to be upgraded
   * @param range upgrade range?
   * @param damage upgrade damage?
   * @param speed upgrade speed?
   * @return true if upgrade successful, else false
   */
  public boolean upgradeTower(Tower tower, boolean range, boolean damage, boolean speed) {
	  if (tower.getVersion() >= 3){
		  System.out.println("Tower already maxed out!");
	  }
	  if (range || damage || speed) {
		  try {
			  adjustGold(-tower.getUpgradePrice());
			  tower.upgrade(range, damage, speed);
			  setChanged();
			  notifyObservers();
			  return true;
		  } catch (NotEnoughGoldException e) {
			  return false;
		  }
	  }
	  return false;
  }

  /**
   * add or subtract gold from player
   * 
   * @param gold amount to be adjusted
   */
  public void adjustGold(int gold) {
    if (this.gold + gold < 0)
      throw new NotEnoughGoldException(-gold, this.gold);
    this.gold += gold;
	setChanged();
    notifyObservers();
  }
  
  /**
   * return the list of towers player bought
   * 
   * @return towers
   */
  public List<Tower> getTowers(){
	  return towers;
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    String result = "Player's stats:\n";
    result += "Score : " + score + "\n";
    result += "Health: " + health  + "\n";
    result += "Gold  : " + gold;
    if (towers.size() != 0) {
    		result += "\nTowers:\n";
	    for (Tower t : towers) {
	      result += t.toString();
	    }
    }
    return result;
  }
}
