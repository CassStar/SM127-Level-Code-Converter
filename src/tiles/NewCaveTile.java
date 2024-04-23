package tiles;

import level.LevelTile;

public class NewCaveTile extends LevelTile {

	public NewCaveTile(String tileData) {
		super(tileData);
	}
	
	public static int getTileID(int base,boolean flatten) {
		
		int ID = 350;
		
		if (flatten) {
			return ID;
		}
		
		String sBase = String.valueOf(base);
		
		return ID+Integer.valueOf(sBase.substring(sBase.length()-1));
	}
}
