package model;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * an extension of Tower. This tower has high range and inflicts a slow to the enemy.
 *
 */
public class TowerB extends Tower{
  private String type = "B";

  /**
   * constructs the tower on a given GameMap
   * 
   * @param map the map the tower is on
   */
  public TowerB(GameMap map){
	super(map, TowerType.B);
}

  /**
   * returns doubled amount of base range
   * This tower has longer range than others.
   * 
   * @return range
   */
  public int getRange() {
	  return super.getRange() * 2;
  }
  
  /*
   * (non-Javadoc)
   * @see model.Tower#getImage()
   */
  public ImageView getImage() {
	  Image image = new LocatedImage("images/Tower/Tower_Magic_level_1.png");

	  if (getVersion() == 2)
		  image = new LocatedImage("images/Tower/Tower_Magic_level_2.png");
	  else if (getVersion() == 3)
		  image = new LocatedImage("images/Tower/Tower_Magic_level_3.png");
	  
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
	  return "FREEZE";
  }
  
  /*
   * (non-Javadoc)
   * @see model.TileContent#toString()
   */
  public String toString() {
	  return type;
  }
  
}
