package model;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * an extension of GameMap. this is the easy map for our game. spawns one
 * path with fews bends.
 */
public class Map_Easy extends GameMap {

	private static int rows = 17;
	private static int cols = 17;
	private LinkedList<String> enemyPath;
	private int start;

	/**
	 * constructs an easy map
	 */
	public Map_Easy() {
		super(rows, cols, "easy");
		setUp();
		if(super.getLoadMap()){
			try {
				System.out.println("#" + SaverLoader.returnEnemyPath().toString());
				this.setEnemyPath(SaverLoader.returnEnemyPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameMap#setUp()
	 */
	@Override
	public void setUp() {
		
		// important map points
			start = getSegmentVal(0); // q(0,0)
		int bend1 = getSegmentVal(0); // q(0,0)
		int bend2 = getSegmentVal(3); // q(3,0)
		int bend3 = getSegmentVal(2); // q(3,2)
		int bend4 = getSegmentVal(2); // q(2,2)
		int bend5 = getSegmentVal(1); // q(2,1)
		int bend6 = getSegmentVal(0); // q(0,1)
		int bend7 = getSegmentVal(2); // q(0,2)
		int bend8 = getSegmentVal(1); // q(1,2)
		int bend9 = getSegmentVal(3); // q(1,3)
		int bendF = getSegmentVal(3); // q(3,3)
		int end = bendF;			  // q(3,3)

		// Create blank map
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				Tile tile = new Tile(this, r, c);
				tile.setContent(new Tile_Empty());
				super.setTile(tile, r, c);
			}
		}
		/* hard codes the enemy paths as the roads are generated */

		// Roads:
		// from start to first bend
		for (int i = 1; i < bend1; i++) {
			super.getTile(i, start).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// from first bend to second bend
		for (int i = start; i < bend2; i++) {
			super.getTile(bend1, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// from second bend to third bend
		for (int i = bend1; i < bend3; i++) {
			super.getTile(i, bend2).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// from third bend to fourth bend
		for (int i = bend2; i > bend4; i--) {
			super.getTile(bend3, i).setContent(new Tile_Path());
			super.addToEnemyPath1("left");
		}
		// from fourth to fifth bend
		for (int i = bend3; i > bend5; i--) {
			super.getTile(i, bend4).setContent(new Tile_Path());
			super.addToEnemyPath1("up");
		}
		// from fifth bend to sixth bend
		for (int i = bend4; i > bend6; i--) {
			super.getTile(bend5, i).setContent(new Tile_Path());
			super.addToEnemyPath1("left");
		}
		// from sixth bend to seventh bend
		for (int i = bend5; i < bend7; i++) {
			super.getTile(i, bend6).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// from seventh bend to eighth bend
		for (int i = bend6; i < bend8; i++) {
			super.getTile(bend7, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// from eighth bend to ninth bend
		for (int i = bend7; i < bend9; i++) {
			super.getTile(i, bend8).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}
		// from seventh bend to final bend
		for (int i = bend8; i < bendF; i++) {
			super.getTile(bend9, i).setContent(new Tile_Path());
			super.addToEnemyPath1("right");
		}
		// from final bend to end
		for (int i = bend9; i < rows - 2; i++) {
			super.getTile(i, bendF).setContent(new Tile_Path());
			super.addToEnemyPath1("down");
		}

		// Set Spawn
		TileContent spawn = new Tile_Spawn();
		super.getTile(1, start).setContent(spawn);
		super.setStart(spawn.getTile());
		// Set End
		TileContent destroy = new Tile_Destroy();
		super.getTile(rows - 2, end).setContent(destroy);
		super.setEnd(destroy.getTile());

		enemyPath = super.getEnemyPath1();
	}

	/*
	 * (non-Javadoc)
	 * @see model.GameMap#spawn()
	 */
	@Override
	public void spawn() {
		
		if(super.getLoadMap()){
			try {
				this.setEnemyPath(SaverLoader.returnEnemyPath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.setLoadMap(false);
			super.setChanged();
			super.notifyObservers();
			return;
		}
		super.addEnemy(new Enemy1(1,start, enemyPath));
		super.addEnemy(new Enemy2(1,start, enemyPath));
		super.addEnemy(new Enemy3(1,start, enemyPath));
		super.setChanged();
		super.notifyObservers();

	}

	/**
	 * returns a random segment value
	 */
	private int getSegmentVal(int segment) {
		return (int) (Math.random() * 2.75) + segment * 4 + 1;
	}
	
	/*
	 * /(non-Javadoc)
	 * @see model.GameMap#returnStart()
	 */
	public int returnStart(){
		return this.start;
	}
	
	/*
	 * setting EnemyPath for loadGame
	 */
	public void setEnemyPath(LinkedList<String> input){
		this.enemyPath = input;
	}
}
