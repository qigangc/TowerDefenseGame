package model;

import java.util.Observable;

/**
 * 
 * every Tile contains a TileContent object.
 * Tower class inherits from this class.
 *
 */
public abstract class TileContent extends Observable{
	// possibly change in future for multiple
	private Tile tile;
	private TileType type;
	
	/**
	 * constructs this object of TileType
	 * 
	 * @param type desired TileType
	 */
	public TileContent(TileType type) {
		this.type = type;
	}

	/**
	 * returns this tile
	 * 
	 * @return tile
	 */
	public Tile getTile() {
		return tile;
	}

	/**
	 * sets this tile
	 * 
	 * @param tile desired tile
	 */
	public void setTile(Tile tile) {
		this.tile = tile;
		setChanged();
	    notifyObservers();
	}

	/**
	 * returns this TileType
	 * 
	 * @return type
	 */
	public TileType getType() {
		return type;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public abstract String toString();
}
