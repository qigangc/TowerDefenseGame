package model;

import java.util.Observable;

/**
 * 
 * models a single Tile on the GameMap being used. makes up the grid
 * 
 */
public class Tile extends Observable{
	private GameMap map;
	private TileContent content;
	private int size = 10;
	private int row, col;
	
	/**
	 * creates a tile on the map given a row and column
	 * 
	 * @param map GameMap the tile will go on
	 * @param row desired row
	 * @param col desired column
	 */
	public Tile(GameMap map, int row, int col){
		this.map = map;
		this.row = row;
		this.col = col;
	}

	/**
	 * returns what type of tile this is
	 * 
	 * @return the type of tile
	 */
	public TileContent getContent() {
		return content;
	}

	/**
	 * sets what type of tile desired
	 * 
	 * @param content desired type of tile
	 */
	public void setContent(TileContent content) {
		this.content = content;
		if (content == null){
			System.out.println("Not enough gold");
			return;
		}
		content.setTile(this);
		setChanged();
	    notifyObservers();
	}

	/**
	 * returns the size of tile (unused)
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * returns column of this tile
	 * 
	 * @return col
	 */
	public int getXpos() {
		return col;
	}

	/**
	 * returns the row of this tile
	 * 
	 * @return row
	 */
	public int getYpos() {
		return row;
	}
	
	/**
	 * returns the GameMap this tile is on
	 * 
	 * @return map
	 */
	public GameMap getMap() {
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return content.toString();
	}
}
