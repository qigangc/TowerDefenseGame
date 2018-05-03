package model;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * 
 * an extension of Tower. This tower has faster speed and inflicts a slow to the enemy.
 *
 */
public class TowerC extends Tower{
  private String type = "C";
  
  /**
   * constructs the tower on a given GameMap
   * 
   * @param map the map the tower is on
   */
  public TowerC(GameMap map){
	super(map, TowerType.C);
  
  }

  /**
   * returns the doubled amount of base speed
   * This tower has faster speed
   * @return speed
   */
  public int getSpeed() {
	  return super.getSpeed() * 2;
  }
  
  /*
   * (non-Javadoc)
   * @see model.Tower#getImage()
   */
  public ImageView getImage() {
	  Image image = new LocatedImage("images/Tower/Tower_Wizard_level_1.png");
	  
	  if (getVersion() == 2)
		  image = new LocatedImage("images/Tower/Tower_Wizard_level_2.png");
	  else if (getVersion() == 3)
		  image = new LocatedImage("images/Tower/Tower_Wizard_level_3.png");
	  
	  ImageView tower = new ImageView(image);
	  Tooltip.install(tower, new Tooltip(getStats()));
	  tower.setPreserveRatio(true);
	  return tower;
  }
  
 /*
  *  (non-Javadoc)
  * @see model.Tower#getSpecialPower()
  */
  public String getSpecialPower() {
	  return "SLOW";
  }
  
  /*
   * (non-Javadoc)
   * @see model.TileContent#toString()
   */
  public String toString() {
	  return type;
  }

}
