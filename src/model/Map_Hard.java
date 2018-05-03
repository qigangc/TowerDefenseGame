package model;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * an extension of GameMap. this is the hard map for our game. generates
 * two enemy paths, enemy 1 and 2 takes the first, enemy 3 takes the second. many bends
 */
public class Map_Hard extends GameMap {

	private static int rows = 17;
	private static int cols = 17;
	private LinkedList<String> enemyPath1, enemyPath2;
	private int start;

	/**
	 * constructs our hard map
	 */
	public Map_Hard() {
		super(rows, cols, "hard");
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
			start = getSegmentVal(0);	// q(0,0)
		int bend1 = getSegmentVal(1);	// q(0,1)
		int bend2 = getSegmentVal(1);	// q(1,1)
										// q(2,0)
		int bend4 = getSegmentVal(3);	// q(3,0)
		int bend5 = getSegmentVal(1);	// q(3,1)
										// q(2,2)
		// right path
		int bend7r = getSegmentVal(3);	// q(2,3)
		int endr = bend7r;				// q(3,3)
		// left path
		int bend7l = getSegmentVal(0);	// q(0,2)
		int endl = bend7l;				// q(0,3)

		for (int r = 0; r < rows; r++) {
			// Create blank map
			for (int c = 0; c < cols; c++) {
				Tile tile = new Tile(this, r, c);
				tile.setContent(new Tile_Empty());
				super.setTile(tile, r, c);
			}
		}

		/* hard codes the enemy paths as the roads are generated */

		int r, c;
		// Roads:
		// Shared Path:
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
		// from second bend to third bend
		r = bend1; c = bend2;
		for (int i = 0; i < 4; i++) {
			super.getTile(r, c).setContent(new Tile_Path());
			super.addToEnemyPath1("up");
			super.addToEnemyPath2("up");
			r--;
			super.getTile(r, c).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
			super.addToEnemyPath2("right");
			c++;
		}
		// from third bend to fourth bend
		for (int i = c; i < bend4; i++) {
			super.getTile(r, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
			super.addToEnemyPath2("right");
		}
		// from fourth bend to fifth bend
		for (int i = r; i < bend5; i++) {
			super.getTile(i, bend4).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
			super.addToEnemyPath2("down");
		}
		// from fifth bend to sixth bend
		r = bend5; c = bend4;
		for (int i = 0; i < 4; i++) {
			super.getTile(r, c).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
			super.addToEnemyPath2("down");
			r++;
			super.getTile(r, c).setContent(new Tile_Path());
			super.addToEnemyPath1("left");
			super.addToEnemyPath2("left");
			c--;
		}
		// Left Path:
		// from sixth bend to seventh bend
		for (int i = c; i > bend7l; i--) {
			super.getTile(r, i).setContent(new Tile_Path());
			super.addToEnemyPath1("left");
		}
		// from seventh bend to end
		for (int i = r; i < rows - 2; i++) {
			super.getTile(i, bend7l).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// Right Path:
		// from sixth bend to seventh bend
		for (int i = r; i < bend7r; i++) {
			super.getTile(i, c).setContent(new Tile_Path());
			super.addToEnemyPath2("down");
		}
		// from seventh bend to end
		for (int i = c; i < cols - 2; i++) {
			super.getTile(bend7r, i).setContent(new Tile_Path());
			super.addToEnemyPath2("right");
		}
		
		// Set Spawn
		TileContent spawn = new Tile_Spawn();
		super.getTile(1, start).setContent(spawn);
		super.setStart(spawn.getTile());
		// Set Ends
		TileContent destroy = new Tile_Destroy();
		super.getTile(rows - 2, endl).setContent(destroy);
		super.setEnd(destroy.getTile());
		TileContent destroyL = new Tile_Destroy();
		super.getTile(endr, cols - 2).setContent(destroyL);
		super.setEnd(destroyL.getTile());

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

		// t.cancel();
		// t.purge();
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
