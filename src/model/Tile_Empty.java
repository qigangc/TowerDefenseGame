package model;

/**
 * an extension of TileContent. This tile is of type empty. this tile is buildable for towers
 */
public class Tile_Empty extends TileContent {

	/**
	 * construct this tile and sets super
	 */
	public Tile_Empty() {
		super(TileType.EMPTY);
	}

	/*
	 * (non-Javadoc)
	 * @see model.TileContent#toString()
	 */
	@Override
	public String toString() {
		return ",";
	}

}