package tiles;

import level.LevelTile;

public class GreenBrickTile extends LevelTile {

	public GreenBrickTile(String tileData) {
		super(tileData);
	}
	
	public static int getTileID(int base,boolean flatten) {
		
		int ID = 30;
		
		if (flatten) {
			return ID;
		}
		
		String sBase = String.valueOf(base);
		
		return ID+Integer.valueOf(sBase.substring(sBase.length()-1));
	}
}
