package test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import model.Enemy;
import model.Enemy1;
import model.Enemy2;
import model.Enemy3;

/**
 * JUnit Tests the enemies, mainly the moving function and the storing of their path(s)
 */
public class EnemyTest {
	LinkedList<String> moveBack;
	LinkedList<String> moveForward;

	/**
	 * initializes variables for appropriate tests
	 */
	@Before
	public void initialize() {
		LinkedList<String> moveBack = new LinkedList<String>();
		moveBack.add("right");
		moveBack.add("left");
		moveBack.add("up");
		moveBack.add("down");

		LinkedList<String> moveForward = new LinkedList<String>();
		moveForward.add("right");
		moveForward.add("down");
		moveForward.add("right");
		moveForward.add("down");
	}

	/**
	 * tests the basic moving of enemies
	 */
	@Test
	public void testBasicMoving() {
		LinkedList<String> moveBack = new LinkedList<String>();
		moveBack.add("right");
		moveBack.add("down");
		moveBack.add("left");
		moveBack.add("up");
		Enemy e1 = new Enemy1(1, 3, moveBack);
		while (e1.getDirectionList().size() != 0) {
			System.out.printf("e1(%d, %d)\n", e1.getXPos(), e1.getYPos());
		}
		System.out.printf("e1(%d, %d)\n", e1.getXPos(), e1.getYPos());
		System.out.println(e1.toString());
	}

}
