package model;

/**
 * an extension of TileContent. This tile is of type path. this is where enemies spawn from
 */
public class Tile_Spawn extends TileContent {

	/**
	 * construct this tile and sets super
	 */
	public Tile_Spawn() {
		super(TileType.SPAWNER);
	}

	/*
	 * (non-Javadoc)
	 * @see model.TileContent#toString()
	 */
	@Override
	public String toString() {
		return "S";
	}

}
