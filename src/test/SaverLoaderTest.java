package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;

import org.junit.Test;

import model.Enemy;
import model.Enemy1;
import model.GameMap;
import model.Map_Easy;
import model.Player;
import model.SaverLoader;
import model.Tile;
import model.Tower;
import model.TowerA;

/**
 * JUnit testing class for SaverLoader
 */
public class SaverLoaderTest {

	/**
	 * tests the save function of SaverLoader
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSave() throws Exception {
		GameMap m1 = new Map_Easy();
		Player p1 = new Player(m1);
		m1.setPlayer(p1);
		Tower t1 = new TowerA(m1);
		Tile newTile = new Tile(m1, 1, 8);
		t1.setTile(newTile);
		p1.buyTower(t1);
		LinkedList<String> direction = new LinkedList<String>();
		direction.add("right");
		Enemy e1 = new Enemy1(0, 0, direction);
		m1.addEnemy(e1);
		
		SaverLoader.saveFile(m1);
	}

	/**
	 * tests the load function of SaverLoader
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLoad() throws Exception{
		GameMap m1 = new Map_Easy();
		Player p1 = new Player(m1);
		m1.setPlayer(p1);
		Tower t1 = new TowerA(m1);
		Tile newTile = new Tile(m1, 1, 8);
		t1.setTile(newTile);
		p1.buyTower(t1);
		LinkedList<String> direction = new LinkedList<String>();
		direction.add("right");
		Enemy e1 = new Enemy1(0, 0, direction);
		m1.addEnemy(e1);
		
		GameMap m2 = SaverLoader.loadFile();
		System.out.println("Map type=" + m2.getClass().toString());
		System.out.println("" + m2.getPlayer().toString());
		System.out.println("----\n" + m2.getPlayer().getTowers().get(0).getStats());
		System.out.println("-----\n" + m2.getEnemyPath1());
		System.out.println("-----\n" + m2.getEnemyPath1().size());
		System.out.println("@"+SaverLoader.returnEnemyPath());
//		System.out.println("-----\n" + m2.toString());
	}
}