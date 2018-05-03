package model;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * an extension of GameMap. this is the medium map for our game. generates
 * two enemy paths, enemy 1 and 2 takes the first, enemy 3 takes the second. many bends
 */
public class Map_Intermediate extends GameMap {

	private static int rows = 17;
	private static int cols = 17;
	private LinkedList<String> enemyPath1, enemyPath2;
	private int start;

	/**
	 * constructs are medium map
	 */
	public Map_Intermediate() {
		super(rows, cols, "intermediate");
		setUp();
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameMap#setUp()
	 */
	@Override
	public void setUp() {

		// important map points
		// shared path
			start = getSegmentVal(0); 	// q(0,0)
		int bend1 = getSegmentVal(1); 	// q(0,1)
		int bend2 = getSegmentVal(1); 	// q(1,1)
		// top path
		int bend3t = getSegmentVal(0); 	// q(1,0)
		int bend4t = getSegmentVal(2); 	// q(2,0)
		int bend5t = getSegmentVal(2); 	// q(2,2)
		int bend6t = getSegmentVal(3); 	// q(3,2)
		int bend7t = getSegmentVal(1); 	// q(3,1)
		int endt = bend7t;				// q(3,1)
		// bottom path
		int bend3b = getSegmentVal(2); 	// q(1,2)
		int bend4b = getSegmentVal(0); 	// q(0,2)
		int bend5b = getSegmentVal(3); 	// q(0,3)
		int bend6b = getSegmentVal(3);	// q(3,3)
		int endb = bend6b;				// q(3,3)

		for (int r = 0; r < rows; r++) {
			// Create blank map
			for (int c = 0; c < cols; c++) {
				Tile tile = new Tile(this, r, c);
				tile.setContent(new Tile_Empty());
				super.setTile(tile, r, c);
			}
		}
		/* hard codes the enemy paths as the roads are generated */

		// Roads:
		// Shared path:
		// from start to first bend
		for (int i = 1; i < bend1; i++) {
			super.getTile(i, start).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
			super.addToEnemyPath2("down");
		}
		// from first bend to second bend
		for (int i = start; i < bend2; i++) {
			super.getTile(bend1, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
			super.addToEnemyPath2("right");
		}
		// Top Path:
		// from second bend to third bend
		for (int i = bend1; i > bend3t; i--) {
			super.getTile(i, bend2).setContent(new Tile_Path());
			super.addToEnemyPath1("up");
		}
		// from third bend to fourth bend
		for (int i = bend2; i < bend4t; i++) {
			super.getTile(bend3t, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// from fourth to fifth bend
		for (int i = bend3t; i < bend5t; i++) {
			super.getTile(i, bend4t).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// from fifth bend to sixth bend
		for (int i = bend4t; i < bend6t; i++) {
			super.getTile(bend5t, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// from sixth bend to seventh bend
		for (int i = bend5t; i > bend7t; i--) {
			super.getTile(i, bend6t).setContent(new Tile_Path());
			super.addToEnemyPath1("up");
		}
		// from seventh bend to top end
		for (int i = bend6t; i < cols - 2; i++) {
			super.getTile(bend7t, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// Bottom Path:
		// from second bend to third bend
		for (int i = bend1; i < bend3b; i++) {
			super.getTile(i, bend2).setContent(new Tile_Path());
			super.addToEnemyPath2("down");
		}
		// from third bend to fourth bend
		for (int i = bend2; i > bend4b; i--) {
			super.getTile(bend3b, i).setContent(new Tile_Path());
			super.addToEnemyPath2("left");
		}
		// from fourth bend to fifth bend
		for (int i = bend3b; i < bend5b; i++) {
			super.getTile(i, bend4b).setContent(new Tile_Path());
			super.addToEnemyPath2("down");
		}
		// from fifth bend to sixth bend
		for (int i = bend4b; i < bend6b; i++) {
			super.getTile(bend5b, i).setContent(new Tile_Path());
			super.addToEnemyPath2("right");
		}
		// from sixth bend to end
		for (int i = bend5b; i < rows - 2; i++) {
			super.getTile(i, bend6b).setContent(new Tile_Path());
			super.addToEnemyPath2("down");
		}

		// Set Spawn
		TileContent spawn = new Tile_Spawn();
		super.getTile(1, start).setContent(spawn);
		super.setStart(spawn.getTile());
		// Set End
		TileContent destroyT = new Tile_Destroy();
		super.getTile(endt, cols -2).setContent(destroyT);
		super.setEnd(destroyT.getTile());
		TileContent destroyB = new Tile_Destroy();
		super.getTile(rows - 2, endb).setContent(destroyB);
		super.setEnd(destroyB.getTile());

		enemyPath1 = super.getEnemyPath1();
		enemyPath2 = super.getEnemyPath2();
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameMap#spawn()
	 */
	@Override
	public void spawn() {

		// clear the existing enemy list (if any)
		//super.setEnemies(new LinkedList<Enemy>());

		super.addEnemy(new Enemy1(1,start, enemyPath1));
		super.addEnemy(new Enemy2(1,start, enemyPath1));
		super.addEnemy(new Enemy3(1,start, enemyPath2));

		super.setChanged();
		super.notifyObservers();

	}

	/**
	 * adds an enemies to the super's list of enemies
	 * 
	 * @param e enemy to add
	 */
	public void addEnemyToSuper(Enemy e) {
		super.addEnemy(e);
	}

	/**
	 * gets a random segment value
	 * 
	 * @param upper bound*4 + 1
	 * @return the random segment value
	 */
	private int getSegmentVal(int segment) {
		return (int) (Math.random() * 2.75) + segment * 4 + 1;
	}
	
	/*
	 * (non-Javadoc)
	 * @see model.GameMap#returnStart()
	 */
	@Override
	public int returnStart() {
		return this.start;
	}
}
