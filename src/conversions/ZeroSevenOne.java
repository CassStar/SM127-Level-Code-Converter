package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import tiles.*;
import util.Utility;

public class ZeroSevenOne implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.7.1";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroSevenOne(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
		this.conversionType = conversionType;
		this.numberOfWarpPipes = numberOfWarpPipes;
		this.postConversionAdditions = postConversionAdditions;
	}
	
	@Override
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int backerBG = (int) fromArea.getBackerBG();
			
			switch(backerBG) {
			
			// Bleak Clouds
			case 6:
				
				// Set Backer BG to Midday Clouds
				toArea.setBackerBG(1);
				break;
			}
		}
		
		return toArea;
	}
	
	@Override
	public AreaCode convertFronterBG(AreaCode fromArea,AreaCode toArea) {
		
		return toArea;
	}
	
	@Override
	public AreaCode convertMusicIDs(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int musicID = (int) fromArea.getMusicID();
			
			switch(musicID) {
			
			// Yoshi's Island - Underground (Remix)
			case 62:
				
				// Set to Underground Yoshi's New Island
				toArea.setMusicID(34);
				break;
			
			// Waltz of the Boos (Remix)
			case 63:
				
				// Set to Waltz of the Boos Super Mario Galaxy
				toArea.setMusicID(56);
				break;
			
			// Super Mario Sunshine - Sky & Sea
			case 64:
				
				// Set to Deep Sea of Mare Super Mario Sunshine
				toArea.setMusicID(41);
			}
		}
		
		return toArea;
	}
	
	@Override
	public LevelTile[] convertTiles(AreaCode toArea,LevelTile[] tileArray) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int arrayLength = tileArray.length;
			
			for (int i = 0;i < arrayLength;i++) {
				
				LevelTile tile = tileArray[i];
				
				if (tile instanceof NewCaveTile) {
					
					// Checking if the tile isn't a full-tile.
					if (tile.tileID > 350) {
						
						// Change ID to Half-Tile Old Cave
						tile.tileID = OldCaveTile.getTileID(tile.tileID,true)+1;
						
					} else {
						
						// Change ID to Full-Tile Old Cave
						tile.tileID = OldCaveTile.getTileID(tile.tileID,true);
					}
					
				}
				
				tileArray[i] = tile;
			}
		}
		
		return tileArray;
	}
	
	@Override
	public Object[] convertObjects(AreaCode toArea,LevelObject[] objectArray,boolean[] conversionsDone) throws Exception {
		
		return new Object[]  {objectArray,conversionsDone};
	}
}