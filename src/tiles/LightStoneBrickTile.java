package tiles;

import level.LevelTile;

public class LightStoneBrickTile extends LevelTile {

	public LightStoneBrickTile(String tileData) {
		super(tileData);
	}
	
	public static int getTileID(int base,boolean flatten) {
		
		int ID = 150;
		
		if (flatten) {
			return ID;
		}
		
		String sBase = String.valueOf(base);
		
		return ID+Integer.valueOf(sBase.substring(sBase.length()-1));
	}
}
