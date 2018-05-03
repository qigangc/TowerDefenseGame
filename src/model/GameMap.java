package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

/**
 * this is the model of the map with extensions for each of the difficulties.
 * it is observed by the view and determines what gets drawn where. stores every tile on the map along with each tower and enemy.
 */
public abstract class GameMap extends Observable {
	private Tile[][] grid;
	private List<Tile> start;
	private List<Tile> end;
	private LinkedList<String> enemyPath1, enemyPath2;
	private Player player;
	private LinkedList<Enemy> enemies;
	private int waveNum;
	private boolean waveInProgress;
	private boolean loadMap = false;
	
	/**
	 * construct GameMap with the given size of the map and the type as String
	 * 
	 * @param row amount of rows
	 * @param col amount of columns
	 * @param map name of the map ("easy", "hard" etc)
	 */
	public GameMap(int row, int col, String map) {
		if((enemyPath1 != null) || (enemyPath2 != null) || (enemies != null))
			return;
		this.grid 	= new Tile[row][col];
		this.start 	= new LinkedList<Tile>();
		this.end 	= new LinkedList<Tile>();
		this.enemyPath1 = new LinkedList<String>();
		this.enemyPath2 = new LinkedList<String>();
		this.enemies = new LinkedList<Enemy>();
		this.waveNum = 0;
		this.waveInProgress = false;
	}
	
	/**
	 * returns true if a wave is running, else false
	 * 
	 * @return true/false whether wave is running
	 */
	public boolean isWaveInProgress(){
		System.out.println("waveInProgress: "+waveInProgress); //DEBUGGING
		return waveInProgress;
	}
	
	/**
	 * returns the tile at the given row/column
	 * 
	 * @param row row coordinate
	 * @param col column coordinate
	 * @return the Tile at the two coordinates
	 */
	public Tile getTile(int row, int col) {
		return this.grid[row][col];
	}
	
	/**
	 * returns the current wave number
	 * 
	 * @return waveNum
	 */
	public int getWaveNum(){
		return waveNum;
	}
	
	/**
	 * sets the current wave number
	 * 
	 * @param input desired waveNum
	 */
	public void setWaveNum(int input){
		this.waveNum = input;
	}
	
	/**
	 * sets the tile passed at the given coordinates
	 * 
	 * @param tile Tile to be set
	 * @param row row coordinate 
	 * @param col column coordinate
	 */
	protected void setTile(Tile tile, int row, int col) {
		this.grid[row][col] = tile;
	}

	/**
	 * sets the 2d Tile grid
	 * 
	 * @param input desired grid
	 */
	public void setGrid(Tile[][] input){
		this.grid = input;
	}
	
	/**
	 * returns the start tile
	 * 
	 * @return start
	 */
	public List<Tile> getStart() {
		return start;
	}

	/**
	 * sets the start tile
	 * @param start desired start tile
	 */
	protected void setStart(Tile start) {
		this.start.add(start);
	}

	/**
	 * returns the ending tile
	 * 
	 * @return end
	 */
	public List<Tile> getEnd() {
		return end;
	}

	/**
	 * sets the end tile
	 * 
	 * @param end desired end tile
	 */
	protected void setEnd(Tile end) {
		this.end.add(end);
	}
	
	/**
	 * returns the game grid
	 * 
	 * @return grid
	 */
	public Tile[][] getGrid(){
		return grid;
	}
	
	/**
	 * adds a direction to the first enemy path
	 * 
	 * @param dir direction to be added. ("left", "right" etc)
	 */
	public void addToEnemyPath1(String dir){
		enemyPath1.add(dir);
	}
	
	/**
	 * adds a direction to the second enemy path
	 * 
	 * @param dir direction to be added. ("left", "right" etc)
	 */
	public void addToEnemyPath2(String dir){
		enemyPath2.add(dir);
	}
	
	/**
	 * returns the first enemy path
	 * 
	 * @return enemyPath1
	 */
	public LinkedList<String> getEnemyPath1(){
		return this.enemyPath1;
	}
	
	/**
	 * sets the first enemy path
	 * 
	 * @param input desired first enemy Path
	 */
	public void setEnemyPath1(LinkedList<String> input){
		this.enemyPath1 = input;
	}
	
	/**
	 * returns the second enemy path
	 * 
	 * @return second enemy path
	 */
	public LinkedList<String> getEnemyPath2(){
		return this.enemyPath2;
	}

	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String map = " ";
		int x = 0;
		
		for (int i=0; i<this.grid.length; i++){
			map += i%10;	//mod 10 to keep formatting
		}
		map += "\n";
		for (Tile[] row : this.grid) {
			map += x%10;	//mod 10 to keep formatting
			x++;
			for (Tile tile : row) {
				map += tile.toString();
			}
			map += "\n";
		}
		return map;
	}
	
	/**
	 * sets up the map given the difficulty, randomly generates a map. hard and intermediate maps
	 * 	have two paths. enemies 1 and 2 take the first, enemy 3 the second.
	 */
	public abstract void setUp();

	/**
	 * spawns enemies based on what instance this map is
	 */
	
	public abstract void spawn();
	
	/**
	 * @return the starting point for SaverLoader
	 */
	public abstract int returnStart();
	
	/**
	 * returns the player of this map
	 * 
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * sets the player for this map
	 * 
	 * @param player to be playing on this map instance
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * returns the enemy list on this map
	 * 
	 * @return enemies
	 */
	public LinkedList<Enemy> getEnemies() {
		return enemies;
	}
	
	/**
	 * iterates through every enemy that was spawned, adding enemies that
	 * are currently alive to a linked list and returning that list.
	 * 
	 * @return linked list of alive enemies
	 */
	public LinkedList<Enemy> getAliveEnemies() {
		LinkedList<Enemy> alive = new LinkedList<Enemy>();
		for (Enemy e : getEnemies()){
			EnemyStatus s = e.getStatus();
			if (s == EnemyStatus.DIED || s == EnemyStatus.ESCAPED)
				continue;
			else
				alive.add(e);
		}
		return alive;
	}

	/**
	 * notifies observers and sets the enemy list of the map to the one passed
	 * as the parameter. generally used for passed an empty list to clear the enemies on the map
	 * for spawning a new wave
	 * 
	 * @param enemies the desired enemy list
	 */
	public void setEnemies(LinkedList<Enemy> enemies) {
		this.enemies = enemies;
		setChanged();
	    notifyObservers();
	}
	
	/**
	 * removes enemy from the list. KNOWN TO CAUSE BUGS
	 * 
	 * @param e enemy to be removed
	 */
	public void removeEnemy(Enemy e) {
		enemies.remove(e);
		setChanged();
	    this.notifyObservers();
	}
	
	/**
	 * adds an enemy to this maps enemy list
	 * 
	 * @param e enemy to be added to this maps list
	 */
	public void addEnemy(Enemy e){
		enemies.add(e);
		setChanged();
	    this.notifyObservers("addEnemy");
	}
	
	/*
	 * @return boolean loadMap status
	 */
	public boolean getLoadMap(){
		return this.loadMap;
	}

	/*
	 * @param input change boolean variable of loadMap status
	 */
	public void setLoadMap(boolean input){
		this.loadMap = input;
	}
}