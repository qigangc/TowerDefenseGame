package model;
import java.util.Scanner;

/**
 * command line version of the game
 */
public class Game {
  /**
   * Was used for testing model.
   * Left unused for the GUI version of the game.
   * @param args reads input
   */
  public static void main(String [] args) {
    Scanner scan = new Scanner(System.in);

    //only implemented for easy map right now
    System.out.println("Pick difficulty level: ");
    System.out.println("Easy - Intermediate - Hard");
    GameMap map = null;
    while (true){
      String difficulty = scan.nextLine().toLowerCase();
      if (difficulty.equals("intermediate")){
        map = new Map_Intermediate();
        break;
      }
      else if (difficulty.equals("hard")){
        map = new Map_Hard();
        break;
      }
      else if (difficulty.equals("easy")){
        map = new Map_Easy();
        break;
      }
      else
    	  System.out.println("Input must either 'easy', 'intermediate' or 'hard'");
    }

    Player player = new Player(map); // null for single-player mode
    map.setPlayer(player);

    System.out.println("\nType in these commands: ");
    System.out.println("Play - Shop - Stats - Map - Quit");

    while (scan.hasNextLine()){

      String command = scan.nextLine().trim();
      if (command.length() == 0)
    	  	continue;

      // User can exit out of the game
      if (command.equalsIgnoreCase("Quit")){
          System.exit(1);

      // Once player buys a tower, he/she is able to start the game
      } else if (command.equalsIgnoreCase("Play")){

      // Player buys towers and places the tower on the map
      } else if (command.equalsIgnoreCase("Shop")){

        System.out.println("a - b - c\n");
        String towerType = scan.next().toLowerCase();
        Tower purchasedTower = null; 
        System.out.println("Now where would you like to place the tower?");
        System.out.println(player.getMap().toString());
        System.out.println("Type in the ROW coordinate:");
        int x = scan.nextInt();
        System.out.println("Type in the COLUMN coordinate:");
        int y = scan.nextInt();
 
        if (map.getGrid().length <= x || map.getGrid()[0].length <= y)
        		System.out.printf("Invalid coordinates: (%d,%d)\n", x,y);    
        	else if (map.getGrid()[x][y].getContent().toString().equals(",")){
        		
	        	if (towerType.equals("a"))
	             purchasedTower = player.buyTower(new TowerA(map));
	        else if(towerType.equals("b"))
	             purchasedTower = player.buyTower(new TowerB(map));
	        else if (towerType.equals("c"))
	             purchasedTower = player.buyTower(new TowerC(map));
	        else
	             System.out.println("Wrong tower type.");
        }
        else {
	        	System.out.println("Can't place tower here!");
        }
        
        if (purchasedTower != null) {
	        	Tile t = new Tile(map, x,y);
	        	t.setContent(purchasedTower);
	        	map.setTile(t,x,y);
	        	System.out.println("\nTower "+purchasedTower.toString()+" placed succsesfully\n");
        } else
        		System.out.println("\nError placing tower\n");


      // player can select a tower to upgrade
      // player has the option to choose one of the three attributes of the tower to upgrade
      // Upgraded towers are upper case letters on the map.
      } else if (command.equalsIgnoreCase("Upgrade")) {
        if (player.getTowers().size() == 0)
          System.out.println("\nYou have not purchased any towers for an upgrade.\n");
        else {
            System.out.println(player.getMap().toString());
            System.out.println("Type in the X coordinate of the tower you want to upgrade:\n");
            int x = scan.nextInt();
            System.out.println("Type in the Y coordinate of the tower you want to upgrade:\n");
            int y = scan.nextInt();  
            Tower towerToUpgrade = null;
            for (Tower t : player.getTowers()) {
            		if (x == t.getTile().getXpos() && y == t.getTile().getYpos()) {
            			towerToUpgrade = t;
            			break;
            		}
            }
            if (towerToUpgrade == null)
            		System.out.println("Wrong x,y coordinate");
            	else {             
	          System.out.println("\nWhat would you like to upgrade?");
	          System.out.println("range - damage - speed");
	          String upgrade = scan.next().toLowerCase();
	          boolean range = false;
	          boolean damage = false;
	          boolean speed = false;
	          if (upgrade.equals("range"))
	        	  	range = true;
	          else if (upgrade.equals("damage"))
	        	  	damage = true;
	          else if (upgrade.equals("speed"))
	        	  	speed = true;
	          if (player.upgradeTower(towerToUpgrade, range, damage, speed))
	            System.out.println("\nYou have successfully upgraded " + towerToUpgrade + "!\n");
	          else
	            System.out.println("\nTower could not be upgraded.\n");
            	}
        }

      // when user wants to move the towers around
      // or when user tried to place tower on enemy path or outside of map x,y coordinates
      // user will have to re-set the tower's x,y coordinates using this option
      } else if (command.equalsIgnoreCase("Rearrange")) {
          if (player.getTowers().size() == 0)
              System.out.println("You have not purchased any towers to rearrange them.\n");
          else {
              System.out.println(player.getMap().toString());
              System.out.println("Type in the X coordinate of the tower you want to move:\n");
              int x = scan.nextInt();
              System.out.println("Type in the Y coordinate of the tower you want to move:\n");
              int y = scan.nextInt();  
              Tower towerToMove = null;
              for (Tower t : player.getTowers()) {
              		if (x == t.getTile().getXpos() && y == t.getTile().getYpos()) {
              			towerToMove = t;
              			break;
              		}
              }
              if (towerToMove == null)
            	  	System.out.println("You don't have a tower at (" + x + "," + y + ")");
	          else {
	              System.out.println("Where would you like to place the tower?");
	              System.out.println(player.getMap().toString());
	              System.out.println("Type in the new X coordinate:");
	              x = scan.nextInt();
	              System.out.println("Type in the new Y coordinate:");
	              y = scan.nextInt();
	              if (x == 0 || map.getGrid().length <= x || map.getGrid()[0].length <= y)
	          		System.out.printf("Invalid coordinates: (%d,%d)\n", x,y);    
	              else if (map.getGrid()[x][y].getContent().toString().equals(",")){
	            	  	towerToMove.getTile().setContent(null);
	            	  	Tile tile = new Tile(map, x, y);
	            	  	towerToMove.setTile(tile);
	            	  	System.out.println("\nYou have successfully placed the tower at (" + x + "," + y + ")!\n");
	              }
	          }
          }

      // Prints out the player's stats
      } else if (command.equalsIgnoreCase("Stats")){
    	  System.out.println("\n" + player);

      // prints out the map in String form
      } else if (command.equalsIgnoreCase("Map")) {
    	  System.out.println("\n" + player.getMap());

      // if user types in something else other than the known commands
      } else {
    	  System.out.println("\nInvalid command.\n");
      }

      System.out.println("Type in one of these commands: ");
      System.out.println("Play - Shop - Stats - Upgrade - Rearrange - Map - Quit");
    }
    scan.close();
  }
}
