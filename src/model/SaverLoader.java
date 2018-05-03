package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/*
 * File: save.txt 
 * Map Type: Map_Easy/ Map_Intermediate/ Map_Hard
 * Wave numer: int
 * Gold: int
 * Health: int
 * Score: int
 * TowerNum: int
 * loop{
 * 	Tower version: int
 * 	Range:	int
 * 	damage:	int
 * 	speed:	int
 * 	Type:  A/B/C	 Stinrg
 * 	xPos int
 * 	yPos int
 * }
 * enemyPath: linkedList<String>
 * enemy numer: int
 * enemy Loop{
 * 	speed: int
 * 	health: int
 * 	armor: int
 * 	xPos: int
 * 	yPos: int
 * 	name: String
 * 	status: EnemyStatus
 * }
 * map loop{
 * 	startNum: int
 *	Start loop{
 *		start Xpos: int
 *		start Ypos: int
 *		type: TileType
 *	}
 *	endNum: int
 *	end loop{
 *		end Xpos: int
 *		end Ypos: int
 *		type: TileType
 *	}
 *	all loop{
 *		type: Tile Type
 *	}
 * 	grid: Tile[][]
 *}
 *
 */

/**
 * handles all of the loading and saving functions
 */
public class SaverLoader {
	private static BufferedReader br;
	private static FileReader fileReader;
	private static FileWriter fileWriter;
	static LinkedList<String> enemyPath;
	private static int start;

	/**
	 * attempts to save the game given a GameMap
	 * 
	 * @param m1 the map to be saved
	 * @throws IOException may throw IOException when writing out to file
	 */
	public static void saveFile(GameMap m1) throws IOException {
		FileWriter file = new FileWriter("save.txt");
		Player p1 = m1.getPlayer();
		m1.setPlayer(p1);
		file.write(m1.getClass().getName().substring(6) + "\n");
		file.write(m1.getWaveNum() + "\n");
		file.write(p1.getGold() + "\n");
		file.write(p1.getHealth() + "\n");
		file.write(p1.getScore() + "\n");
		file.write(p1.getTowers().size() + "\n");
		List<Tower> towers = p1.getTowers();
		// Tower Loop
		for (int i = 0; i < p1.getTowers().size(); i++) {
			file.write(towers.get(i).getVersion() + "\n");
			file.write(towers.get(i).getRange() + "\n");
			file.write(towers.get(i).getDamage() + "\n");
			file.write(towers.get(i).getSpeed() + "\n");
			file.write(towers.get(i).getTowerType().toString() + "\n");
			file.write(towers.get(i).getTile().getXpos() + "\n");
			file.write(towers.get(i).getTile().getYpos() + "\n");
		}
		// Enemy direction
		if(m1.getClass().getName().substring(6).compareToIgnoreCase("Map_Easy") == 0){
			saveEasyEnemyPath(file,m1);
		}
		else{
			saveNonEasyEnemyPath(file,m1);
		}
		file.write(m1.getEnemies().size() + "\n");
		// Enemy Loop
		LinkedList<Enemy> enemies = m1.getEnemies();
		for (int i = 0; i < enemies.size(); i++) {
			file.write(enemies.get(i).getSpeed() + "\n");
			file.write((int)enemies.get(i).getHealth() + "\n");
			file.write(enemies.get(i).getArmor() + "\n");
			file.write(enemies.get(i).getXPos() + "\n");
			file.write(enemies.get(i).getYPos() + "\n");
			start= m1.returnStart();
			file.write(m1.returnStart() + "\n");
			file.write(enemies.get(i).getName() + "\n");
			file.write(enemies.get(i).getStatus().toString() + "\n");
		}
		// Map loop
			int startNumber = m1.getStart().size();
			file.write(startNumber  + "\n");
		for(int i=0; i<startNumber; i++){
			file.write(m1.getStart().get(i).getXpos() + "\n");
			file.write(m1.getStart().get(i).getYpos() + "\n");
			file.write(m1.getStart().get(i).getContent().getType().toString() + "\n");
		}
			int endNumber = m1.getEnd().size();
			file.write(endNumber + "\n");
		for(int i=0; i<endNumber; i++){
			file.write(m1.getEnd().get(i).getXpos() + "\n");
			file.write(m1.getEnd().get(i).getYpos() + "\n");
			file.write(m1.getEnd().get(i).getContent().getType().toString() + "\n");
		}
		for(int j=0; j<17; j++){
			for(int i=0; i<17; i++){
				file.write(m1.getTile(i, j).getContent().getType().toString() + "\n");
			}
		}
		file.close();

	}

	/**
	 * saves a non easy enemy path
	 * 
	 * @param file the FileWriter to be used
	 * @param m1 the GameMap
	 * @throws IOException writing out to file may cause exception
	 */
	public static void saveNonEasyEnemyPath(FileWriter file, GameMap m1) throws IOException{
		file.write(m1.getEnemyPath2().size() + "\n");
		for (int i = 0; i < m1.getEnemyPath2().size(); i++) {
			file.write(m1.getEnemyPath2().get(i) + "\n");
		}
	}
	
	/**
	 * saves an easy enemy path
	 * 
	 * @param file the FileWriter to be used
	 * @param m1 the GameMap
	 * @throws IOException writing out to file may cause exception
	 */
	public static void saveEasyEnemyPath(FileWriter file, GameMap m1) throws IOException{
		file.write(m1.getEnemyPath1().size() + "\n");
		for (int i = 0; i < m1.getEnemyPath1().size(); i++) {
			file.write(m1.getEnemyPath1().get(i) + "\n");
		}
	}
	
	/**
	 * attempts to load a file
	 * 
	 * @return the loaded file
	 * @throws Exception Reading from file may cause exception
	 */
	public static GameMap loadFile() throws Exception {
		GameMap loadMap;
		Player loadPlayer;
		fileReader = new FileReader("save.txt");
		br = new BufferedReader(fileReader);
		String content = getMapName(br);

		// initialize Map
		if (content.compareToIgnoreCase("Map_Easy") == 0)
			loadMap = new Map_Easy();
		else if (content.compareToIgnoreCase("Map_Hard") == 0)
			loadMap = new Map_Hard();
		else
			loadMap = new Map_Intermediate();

		// initialize Player
		int[] inputBasicInfo = getBasic(br);
		loadMap.setWaveNum(inputBasicInfo[0]);
		loadPlayer = new Player(loadMap);
		loadPlayer.setGold(inputBasicInfo[1]);
		loadPlayer.setHealth(inputBasicInfo[2]);
		loadPlayer.setScore(inputBasicInfo[3]);
		int towerSize = getTowerSize(br);
		Tower newTower;
		for (int i = 0; i < towerSize; i++) {
			newTower = getTower(br, loadMap);
			loadPlayer.getTowers().add(newTower);
		}
		loadMap.setPlayer(loadPlayer);
		// initialize enemies
		enemyPath = getEnemyPath(br);
		int enemyNum = Integer.parseInt(br.readLine());
		for (int i = 0; i < enemyNum; i++) {
			Enemy newEnemy = getEnemy(br, enemyPath);
			loadMap.addEnemy(newEnemy);
		}
		
		//initialize Map
		List<Tile> startList = getStart(br, loadMap);
		List<Tile> endList = getEnd(br, loadMap);
		for(Tile start: startList)
			loadMap.setStart(start);
		for(Tile end: endList)
			loadMap.setEnd(end);
		loadMap.setGrid(getGrid(br, loadMap));;
		//finished
		return loadMap;
	}

	/**
	 * gets the size of tower list from save filed
	 * 
	 * @param br BufferReader used
	 * @return number of towers on map
	 * @throws Exception
	 */
	public static int getTowerSize(BufferedReader br) throws Exception {
		return Integer.parseInt(br.readLine());
	}

	/**
	 * returns start tile
	 * 
	 * @param br BufferedReader used
	 * @param loadMap GameMap to be loaded
	 * @return start tile
	 * @throws Exception
	 */
	public static List<Tile> getStart(BufferedReader br, GameMap loadMap) throws Exception{
		List<Tile> newTileList = new LinkedList<Tile>();
		int num = Integer.parseInt(br.readLine());
		for(int i=0; i<num; i++){
			int xPos = Integer.parseInt(br.readLine());
			int yPos = Integer.parseInt(br.readLine());
			String type = br.readLine();
			Tile newTile = new Tile(loadMap, xPos, yPos);
			newTile.setContent(new Tile_Spawn());
			newTileList.add(newTile);
		}
		return newTileList;
	}
	
	/**
	 * returns start tile
	 * 
	 * @param br BufferedReader used
	 * @param loadMap GameMap to be loaded
	 * @return end tile
	 * @throws Exception
	 */
	public static List<Tile> getEnd(BufferedReader br, GameMap loadMap) throws Exception{
		List<Tile> newTileList = new LinkedList<Tile>();
		int num = Integer.parseInt(br.readLine());
		for(int i=0; i<num; i++){
			int xPos = Integer.parseInt(br.readLine());
			int yPos = Integer.parseInt(br.readLine());
			String type = br.readLine();
			Tile newTile = new Tile(loadMap, xPos, yPos);
			newTile.setContent(new Tile_Destroy());
			newTileList.add(newTile);
		}
		return newTileList;
	}
	
	/**
	 * returns end tile
	 * 
	 * @param br BufferedReader used
	 * @param loadMap GameMap to be loaded
	 * @return loadMap's grid
	 * @throws Exception
	 */
	public static Tile[][] getGrid(BufferedReader br, GameMap loadMap) throws Exception{
		Tile[][] grid = new Tile[17][17];
		for(int j=0; j<17; j++){
			for(int i=0; i<17; i++){
				String content = br.readLine();
				grid[i][j] = new Tile(loadMap, i, j);
				if(content.compareToIgnoreCase("EMPTY") == 0)
					grid[i][j].setContent(new Tile_Empty());
				else if (content.compareToIgnoreCase("Path") == 0)
					grid[i][j].setContent(new Tile_Path());
				else if (content.compareToIgnoreCase("SPAWNER") == 0)
					grid[i][j].setContent(new Tile_Spawn());
				else if (content.compareToIgnoreCase("DESTROYER") == 0)
					grid[i][j].setContent(new Tile_Destroy());
				else{
				List<Tower> towers= loadMap.getPlayer().getTowers();
					for(int q=0; q<towers.size(); q++){
						Tower t;
						if((towers.get(q).getTile().getXpos() == i) && (towers.get(q).getTile().getYpos() == j)){
							if(towers.get(q).toString().compareToIgnoreCase("A") == 0){
								t = towers.get(q);
								grid[i][j].setContent(new Tile_Empty());
								grid[i][j].setContent(t);
							}
							else if(towers.get(q).toString().compareToIgnoreCase("B") == 0){
								t = towers.get(q);
								grid[i][j].setContent(new Tile_Empty());
								grid[i][j].setContent(t);
							}
							else{
								t = new TowerC(loadMap);
								grid[i][j].setContent(new Tile_Empty());
								grid[i][j].setContent(t);
							}
						}
					}
				}
			}
		}
		
		return grid;
	}
	
	/**
	 * returns Tower
	 * 
	 * @param br BufferedReader used
	 * @param loadMap GameMap to be loaded
	 * @return tower from the save file
	 * @throws Exception
	 */
	public static Tower getTower(BufferedReader br, GameMap loadMap) throws Exception {
		Tower newTower;
		int towerVersion = Integer.parseInt(br.readLine());
		int towerRange = Integer.parseInt(br.readLine());
		int towerDamage = Integer.parseInt(br.readLine());
		int towerSpeed = Integer.parseInt(br.readLine());
		String towerType = br.readLine();
		if (towerType.compareToIgnoreCase("A") == 0) {
			newTower = new TowerA(loadMap);
		} else if (towerType.compareToIgnoreCase("B") == 0) {
			newTower = new TowerB(loadMap);
		} else
			newTower = new TowerC(loadMap);
		newTower.setVersion(towerVersion);
		newTower.setRange(towerRange);
		newTower.setSpeed(towerSpeed);
		newTower.setDamage(towerDamage);
		Tile newTile = new Tile(loadMap, Integer.parseInt(br.readLine()), Integer.parseInt(br.readLine()));
		newTower.setTile(newTile);

		return newTower;
	}

	/**
	 * returns enemy path
	 * 
	 * @param br BufferedReader used
	 * @return enemy path
	 * @throws Exception
	 */
	public static LinkedList<String> getEnemyPath(BufferedReader br) throws Exception {
		int size = Integer.parseInt(br.readLine());
		LinkedList<String> loadEnemyPath = new LinkedList<String>();
		for (int i = 0; i < size; i++)
			loadEnemyPath.add(br.readLine());
		return loadEnemyPath;
	}

	/**
	 * returns an enemy from the save file
	 * 
	 * @param br BufferedReader to be used
	 * @param enemyPath the enemy path
	 * @return an enemy from the save file
	 * @throws Exception
	 */
	public static Enemy getEnemy(BufferedReader br, LinkedList<String> enemyPath) throws Exception {
		Enemy loadEnemy;
		int speed = Integer.parseInt(br.readLine());
		int health = Integer.parseInt(br.readLine());
		int armor = Integer.parseInt(br.readLine());
		int xPos = Integer.parseInt(br.readLine());
		int yPos = Integer.parseInt(br.readLine());
		int start = Integer.parseInt(br.readLine());
		String name = br.readLine();
		if (name.compareToIgnoreCase("bat") == 0){
			loadEnemy = new Enemy1(1, start, enemyPath);}
		else if (name.compareToIgnoreCase("skeleton") == 0)
			loadEnemy = new Enemy2(1, start, enemyPath);
		else
			loadEnemy = new Enemy3(1, start, enemyPath);
		loadEnemy.setSpeed(speed);
		loadEnemy.setHealth(health);
		loadEnemy.setArmor(armor);
		String status = br.readLine();
		if (status.compareToIgnoreCase("SLOWDOWN") == 0)
			loadEnemy.setStatus(EnemyStatus.SLOWDOWN);
		else if (status.compareToIgnoreCase("POISON") == 0)
			loadEnemy.setStatus(EnemyStatus.POISON);
		else if (status.compareToIgnoreCase("FREEZE") == 0)
			loadEnemy.setStatus(EnemyStatus.FREEZE);
		else if (status.compareToIgnoreCase("DIED") == 0)
			loadEnemy.setStatus(EnemyStatus.DIED);
		else if (status.compareToIgnoreCase("ESCAPED") == 0)
			loadEnemy.setStatus(EnemyStatus.ESCAPED);
		else if (status.compareToIgnoreCase("NORMAL") == 0)
			loadEnemy.setStatus(EnemyStatus.NORMAL);
		return loadEnemy;
	}

	/**
	 * returns GameMap instance's name
	 * 
	 * @param br BufferedReader to be used
	 * @return map's name
	 * @throws Exception
	 */
	public static String getMapName(BufferedReader br) throws Exception {
		String mapName = br.readLine();
		return mapName;
	}

	/**
	 * return basic representation of load/save file
	 * 
	 * @param br BufferedReader to be used
	 * @return basic representation of load/save file
	 * @throws Exception
	 */
	public static int[] getBasic(BufferedReader br) throws Exception {
		int[] result = new int[4];
		for (int i = 0; i < 4; i++)
			result[i] = Integer.parseInt(br.readLine());
		return result;
	}
	
	/**
	 * returns enemy path
	 * 
	 * @return enemy path
	 * @throws Exception
	 */
	public static LinkedList<String> returnEnemyPath() throws Exception{
		return enemyPath;
	}
	
}
