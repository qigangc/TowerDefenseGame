package model;

/**
 * an extenstion of TileContent. This tile is of type path. this is the path of enemies to move on
 */
public class Tile_Path extends TileContent {

	/**
	 * construct this tile and sets super
	 */
	public Tile_Path() {
		super(TileType.PATH);
	}

	/*
	 * (non-Javadoc)
	 * @see model.TileContent#toString()
	 */
	@Override
	public String toString() {
		return "-";
	}

}
