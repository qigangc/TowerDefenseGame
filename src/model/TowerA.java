package model;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * an extension of Tower. This tower has high damage and inflicts poison to the enemy.
 *
 */
public class TowerA extends Tower{
  private String type = "A";
  
  /**
   * constructs the tower on a given GameMap
   * 
   * @param map the map the tower is on
   */
  public TowerA(GameMap map){
	super(map, TowerType.A);
  }

  /**
   * returns double the damage of base amount
   * This tower is stronger than other towers
   * 
   * @return damage
   */
  public int getDamage() {
	  return super.getDamage() * 2;
  }
  
  /*
   * (non-Javadoc)
   * @see model.Tower#getImage()
   */
  public ImageView getImage() {

	  Image image = new LocatedImage("images/Tower/Tower_Normal_level_1.png");

	  if (getVersion() == 2)
		  image = new LocatedImage("images/Tower/Tower_Normal_level_2.png");
	  else if (getVersion() == 3)
		  image = new LocatedImage("images/Tower/Tower_Normal_level_3.png");
	  
	  ImageView tower = new ImageView(image);
	  Tooltip.install(tower, new Tooltip(getStats()));
	  tower.setPreserveRatio(true);
	  return tower;
  } 
  
  /*
   * (non-Javadoc)
   * @see model.Tower#getSpecialPower()
   */
  public String getSpecialPower() {
	  return "POISON";
  }
  
  /*
   * (non-Javadoc)
   * @see model.TileContent#toString()
   */
  public String toString() {
	  return type;
  }
  
}
