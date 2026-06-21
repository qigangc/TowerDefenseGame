package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SaverLoader {
	private static final String SAVE_FILE = "save.txt";
	private static final int MAP_SIZE = 17;
	static LinkedList<String> enemyPath;

	public static void saveFile(GameMap map) throws IOException {
		try (FileWriter file = new FileWriter(SAVE_FILE)) {
			Player player = map.getPlayer();
			map.setPlayer(player);
			file.write(map.getClass().getSimpleName() + "\n");
			file.write(map.getWaveNum() + "\n");
			file.write(player.getGold() + "\n");
			file.write(player.getHealth() + "\n");
			file.write(player.getScore() + "\n");
			file.write(player.getTowers().size() + "\n");

			for (Tower tower : player.getTowers()) {
				file.write(tower.getVersion() + "\n");
				file.write(tower.getRange() + "\n");
				file.write(tower.getDamage() + "\n");
				file.write(tower.getSpeed() + "\n");
				file.write(tower.getTowerType().toString() + "\n");
				file.write(tower.getTile().getXpos() + "\n");
				file.write(tower.getTile().getYpos() + "\n");
			}

			if (map instanceof Map_Easy)
				saveEasyEnemyPath(file, map);
			else
				saveNonEasyEnemyPath(file, map);

			file.write(map.getEnemies().size() + "\n");
			for (Enemy enemy : map.getEnemies()) {
				file.write(enemy.getSpeed() + "\n");
				file.write(enemy.getHealth() + "\n");
				file.write(enemy.getArmor() + "\n");
				file.write(enemy.getXPos() + "\n");
				file.write(enemy.getYPos() + "\n");
				file.write(map.returnStart() + "\n");
				file.write(enemy.getName() + "\n");
				file.write(enemy.getStatus().toString() + "\n");
			}

			file.write(map.getStart().size() + "\n");
			for (Tile tile : map.getStart()) {
				file.write(tile.getXpos() + "\n");
				file.write(tile.getYpos() + "\n");
				file.write(tile.getContent().getType().toString() + "\n");
			}

			file.write(map.getEnd().size() + "\n");
			for (Tile tile : map.getEnd()) {
				file.write(tile.getXpos() + "\n");
				file.write(tile.getYpos() + "\n");
				file.write(tile.getContent().getType().toString() + "\n");
			}

			for(int y=0; y<MAP_SIZE; y++){
				for(int x=0; x<MAP_SIZE; x++){
					file.write(map.getTile(x, y).getContent().getType().toString() + "\n");
				}
			}
		}
	}

	public static void saveNonEasyEnemyPath(FileWriter file, GameMap map) throws IOException{
		file.write(map.getEnemyPath2().size() + "\n");
		for (String direction : map.getEnemyPath2())
			file.write(direction + "\n");
	}

	public static void saveEasyEnemyPath(FileWriter file, GameMap map) throws IOException{
		file.write(map.getEnemyPath1().size() + "\n");
		for (String direction : map.getEnemyPath1())
			file.write(direction + "\n");
	}

	public static GameMap loadFile() throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(SAVE_FILE))) {
			GameMap map = createMap(getMapName(br));
			int[] basicInfo = getBasic(br);
			map.setWaveNum(basicInfo[0]);

			Player player = new Player(map);
			player.setGold(basicInfo[1]);
			player.setHealth(basicInfo[2]);
			player.setScore(basicInfo[3]);

			int towerSize = getTowerSize(br);
			for (int i = 0; i < towerSize; i++)
				player.getTowers().add(getTower(br, map));
			map.setPlayer(player);

			enemyPath = getEnemyPath(br);
			int enemyNum = readInt(br, "enemy count");
			for (int i = 0; i < enemyNum; i++)
				map.addEnemy(getEnemy(br, enemyPath));

			for(Tile start: getStart(br, map))
				map.setStart(start);
			for(Tile end: getEnd(br, map))
				map.setEnd(end);
			map.setGrid(getGrid(br, map));
			return map;
		}
	}

	public static int getTowerSize(BufferedReader br) throws IOException {
		return readInt(br, "tower count");
	}

	public static List<Tile> getStart(BufferedReader br, GameMap map) throws IOException{
		List<Tile> tiles = new LinkedList<Tile>();
		int num = readInt(br, "start tile count");
		for(int i=0; i<num; i++){
			int xPos = readInt(br, "start tile x");
			int yPos = readInt(br, "start tile y");
			readRequiredLine(br, "start tile type");
			Tile tile = new Tile(map, xPos, yPos);
			tile.setContent(new Tile_Spawn());
			tiles.add(tile);
		}
		return tiles;
	}

	public static List<Tile> getEnd(BufferedReader br, GameMap map) throws IOException{
		List<Tile> tiles = new LinkedList<Tile>();
		int num = readInt(br, "end tile count");
		for(int i=0; i<num; i++){
			int xPos = readInt(br, "end tile x");
			int yPos = readInt(br, "end tile y");
			readRequiredLine(br, "end tile type");
			Tile tile = new Tile(map, xPos, yPos);
			tile.setContent(new Tile_Destroy());
			tiles.add(tile);
		}
		return tiles;
	}

	public static Tile[][] getGrid(BufferedReader br, GameMap map) throws IOException{
		Tile[][] grid = new Tile[MAP_SIZE][MAP_SIZE];
		for(int y=0; y<MAP_SIZE; y++){
			for(int x=0; x<MAP_SIZE; x++){
				String content = readRequiredLine(br, "grid tile");
				grid[x][y] = new Tile(map, x, y);
				if(content.compareToIgnoreCase("EMPTY") == 0)
					grid[x][y].setContent(new Tile_Empty());
				else if (content.compareToIgnoreCase("Path") == 0)
					grid[x][y].setContent(new Tile_Path());
				else if (content.compareToIgnoreCase("SPAWNER") == 0)
					grid[x][y].setContent(new Tile_Spawn());
				else if (content.compareToIgnoreCase("DESTROYER") == 0)
					grid[x][y].setContent(new Tile_Destroy());
				else if (content.compareToIgnoreCase("TOWER") == 0
						|| content.compareToIgnoreCase("A") == 0
						|| content.compareToIgnoreCase("B") == 0
						|| content.compareToIgnoreCase("C") == 0)
					setTowerTile(grid[x][y], map.getPlayer().getTowers());
				else
					throw new IOException("Invalid save file: unknown tile type " + content);
			}
		}
		return grid;
	}

	public static Tower getTower(BufferedReader br, GameMap map) throws IOException {
		Tower tower;
		int towerVersion = readInt(br, "tower version");
		int towerRange = readInt(br, "tower range");
		int towerDamage = readInt(br, "tower damage");
		int towerSpeed = readInt(br, "tower speed");
		String towerType = readRequiredLine(br, "tower type");
		if (towerType.compareToIgnoreCase("A") == 0)
			tower = new TowerA(map);
		else if (towerType.compareToIgnoreCase("B") == 0)
			tower = new TowerB(map);
		else if (towerType.compareToIgnoreCase("C") == 0)
			tower = new TowerC(map);
		else
			throw new IOException("Invalid save file: unknown tower type " + towerType);
		tower.setVersion(towerVersion);
		tower.setRange(towerRange);
		tower.setSpeed(towerSpeed);
		tower.setDamage(towerDamage);
		tower.setTile(new Tile(map, readInt(br, "tower x"), readInt(br, "tower y")));
		return tower;
	}

	public static LinkedList<String> getEnemyPath(BufferedReader br) throws IOException {
		int size = readInt(br, "enemy path size");
		LinkedList<String> path = new LinkedList<String>();
		for (int i = 0; i < size; i++)
			path.add(readRequiredLine(br, "enemy path direction"));
		return path;
	}

	public static Enemy getEnemy(BufferedReader br, LinkedList<String> enemyPath) throws IOException {
		Enemy enemy;
		int speed = readInt(br, "enemy speed");
		int health = readInt(br, "enemy health");
		int armor = readInt(br, "enemy armor");
		int xPos = readInt(br, "enemy x");
		int yPos = readInt(br, "enemy y");
		int start = readInt(br, "enemy start");
		String name = readRequiredLine(br, "enemy name");
		if (name.compareToIgnoreCase("bat") == 0)
			enemy = new Enemy1(1, start, enemyPath);
		else if (name.compareToIgnoreCase("skeleton") == 0)
			enemy = new Enemy2(1, start, enemyPath);
		else if (name.compareToIgnoreCase("ghost") == 0)
			enemy = new Enemy3(1, start, enemyPath);
		else
			throw new IOException("Invalid save file: unknown enemy name " + name);
		enemy.setSpeed(speed);
		enemy.setHealth(health);
		enemy.setArmor(armor);
		enemy.setPos(xPos, yPos);
		enemy.setStatus(parseEnemyStatus(readRequiredLine(br, "enemy status")));
		return enemy;
	}

	public static String getMapName(BufferedReader br) throws IOException {
		return readRequiredLine(br, "map name");
	}

	public static int[] getBasic(BufferedReader br) throws IOException {
		int[] result = new int[4];
		for (int i = 0; i < 4; i++)
			result[i] = readInt(br, "basic info");
		return result;
	}

	public static LinkedList<String> returnEnemyPath(){
		return enemyPath;
	}

	private static GameMap createMap(String mapName) throws IOException {
		if (mapName.compareToIgnoreCase("Map_Easy") == 0)
			return new Map_Easy();
		if (mapName.compareToIgnoreCase("Map_Hard") == 0)
			return new Map_Hard();
		if (mapName.compareToIgnoreCase("Map_Intermediate") == 0)
			return new Map_Intermediate();
		throw new IOException("Invalid save file: unknown map type " + mapName);
	}

	private static void setTowerTile(Tile tile, List<Tower> towers) throws IOException {
		for (Tower tower : towers) {
			if(tower.getTile().getXpos() == tile.getXpos() && tower.getTile().getYpos() == tile.getYpos()){
				tile.setContent(tower);
				return;
			}
		}
		throw new IOException("Invalid save file: tower tile has no matching tower");
	}

	private static EnemyStatus parseEnemyStatus(String status) throws IOException {
		try {
			return EnemyStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IOException("Invalid save file: unknown enemy status " + status);
		}
	}

	private static int readInt(BufferedReader br, String field) throws IOException {
		String line = readRequiredLine(br, field);
		try {
			return Integer.parseInt(line);
		} catch (NumberFormatException e) {
			throw new IOException("Invalid save file: expected integer for " + field + ", got " + line);
		}
	}

	private static String readRequiredLine(BufferedReader br, String field) throws IOException {
		String line = br.readLine();
		if (line == null)
			throw new IOException("Invalid save file: missing " + field);
		return line;
	}
}
