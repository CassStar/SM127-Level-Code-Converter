package tiles;

import level.LevelTile;

public class GrassTile extends LevelTile {

	public GrassTile(String tileData) {
		super(tileData);
	}
	
	public static int getTileID(int base,boolean flatten) {
		
		int ID = 20;
		
		if (flatten) {
			return ID;
		}
		
		String sBase = String.valueOf(base);
		
		return ID+Integer.valueOf(sBase.substring(sBase.length()-1));
	}
}
