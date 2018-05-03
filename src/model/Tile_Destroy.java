package model;

/**
 * an extension of TileContent. This tile is of type destroy. this is the enemies goal to move to
 */
public class Tile_Destroy extends TileContent{

	/**
	 * construct this tile and sets super
	 */
	public Tile_Destroy() {
		super(TileType.DESTROYER);
	}

	/*
	 * (non-Javadoc)
	 * @see model.TileContent#toString()
	 */
	@Override
	public String toString() {
		return "D";
	}
	
}
