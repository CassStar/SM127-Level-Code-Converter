package tiles;

public class LevelTile {
	
	public int tileID,tilePallete,tileAmount;
	public boolean hasPallete,noLength;
	
	public LevelTile(String tileData) {
		
		int amountIndex = tileData.indexOf('*'),palleteIndex = tileData.indexOf(':');
		noLength = false;
		
		if (amountIndex == -1) {
			
			noLength = true;
			amountIndex = tileData.length();
		}
		
		if (palleteIndex != -1) {
			
			hasPallete = true;
			
			tileID = Integer.parseInt(tileData.substring(palleteIndex+1,amountIndex));
			tilePallete = Integer.parseInt(tileData.substring(0,palleteIndex));
			tileAmount = noLength? 1 : Integer.parseInt(tileData.substring(amountIndex+1));
			
		} else {
			
			hasPallete = false;
			
			tileID = Integer.parseInt(tileData.substring(0,amountIndex));
			tilePallete = 0;
			tileAmount = noLength? 1 : Integer.parseInt(tileData.substring(amountIndex+1));
		}
	}
	
	public String toString() {
		
		String output = "";
		
		if (tileID == 0) {
			
			output += "000";
		} else {
			
			if (hasPallete) {
				
				output += tilePallete+":";
			}
			
			output += String.format("%03d",tileID);
		}
		
		if (!noLength) {
			
			output += "*"+tileAmount;
		}
		
		return output;
	}
}
