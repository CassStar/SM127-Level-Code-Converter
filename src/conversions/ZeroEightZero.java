package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import objects.*;
import tiles.*;
import util.ConversionUtility;
import util.Utility;

public class ZeroEightZero implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.8.0";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroEightZero(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
		this.conversionType = conversionType;
		this.numberOfWarpPipes = numberOfWarpPipes;
		this.postConversionAdditions = postConversionAdditions;
	}
	
	@Override
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int backerBG = (int) fromArea.getBackerBG();
			
			switch(backerBG) {
			
			// Underwater (Light streaming in top, plants and rocks on bottom)
			case 7:
				
				// Set Backer BG to Midday Clouds
				toArea.setBackerBG(1);
				break;
			
			// Dusk/Dawn Sky with Stars on Top and Clouds on Bottom
			case 8:
				
				// Set Backer BG to Clouds Against a Red-Coloured Background
				toArea.setBackerBG(5);
				break;
				
			// Blue and Black Gradient
			case 9:
				
				// Set Backer BG to Black Screen
				toArea.setBackerBG(0);
				break;
			
			// Underwater with Sparkles
			case 10:
				
				// Set Backer BG to Starry Sky
				toArea.setBackerBG(2);
				break;
			
			// Galaxy, Planets, and Stars
			case 11:
				
				// Set Backer BG to Dark Starry Sky
				toArea.setBackerBG(3);
				break;
			}
		}
		
		return toArea;
	}
	
	@Override
	public AreaCode convertFronterBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int fronterBG = (int) fromArea.getFronterBG();
			
			switch(fronterBG) {
			
			// Yoshi's Island Styled Forest
			case 14:
				
				// Set Fronter BG to Large Green Pine Trees
				toArea.setFronterBG(11);
				// Green palette.
				toArea.setBGPallete(1);
				break;
			
			// Ocean with Small Islands and Spaced-Out Clouds
			case 15:
				
				// Set Fronter BG to Green Hills Surrounded by Tropical Setting
				toArea.setFronterBG(8);
				break;
			
			// Green Grassland and Forest with Castle and Rocks in Back
			case 16:
				
				// Set Fronter BG to Large Green Hills With Green Trees
				toArea.setFronterBG(13);
				break;
			
			// Rocky Waterbed Surrounded by Dense Vegetation
			case 17:
				
				// Set Fronter BG to Green Hills Surrounded by Tropical Setting
				toArea.setFronterBG(8);
				break;
			
			// Silhouette Trees
			case 18:
				
				// Set Fronter BG to Large Blue Pine Trees
				toArea.setFronterBG(11);
				break;
			
			// Volcano with Lava Waterfalls
			case 19:
				
				// Set Fronter BG to Magma Caverns
				toArea.setFronterBG(9);
				break;
			
			// Choco Island with Blue Trees
			case 20:
				
				// Set Fronter BG to Crystal Filled Cave
				toArea.setFronterBG(3);
				break;
			
			// Underwater Rocks and Coral
			case 21:
				
				int bgPallete = (int) fromArea.getBGPallete();
				
				switch(bgPallete) {
				
				// Blue-Coloured palette.
				case 4:
					
					// Set Fronter BG to Sharp, Icy Mountains
					toArea.setFronterBG(5);
					// Set palette to the default.
					toArea.setBGPallete(0);
					break;
				
				// All other palettes.
				default:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
				}
				
				break;
			}
		}
		
		return toArea;
	}
	
	@Override
	public AreaCode convertMusicIDs(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int musicID = (int) fromArea.getMusicID();
			
			switch(musicID) {
			
			// Metallic Mario (Legacy)
			case 65:
				
				// Set MusicID to Metallic Mario
				toArea.setMusicID(25);
				break;
			
			// Piranha Plant Lullaby
			case 66:
				
				// Set MusicID to Deep Sea of Mare
				toArea.setMusicID(41);
				break;
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
				
				if (tile instanceof BookCaseTile) {
					
					// Change ID to Full-Tile Wooden Plank Block
					tile.tileID = WoodenPlankTile.getTileID(tile.tileID,true);
					
				} else if (tile instanceof StoneFenceTile) {
					
					// Change ID to New Cave
					tile.tileID = NewCaveTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof TreeBlockTile) {
					
					// Change ID to Volcano
					tile.tileID = VolcanoTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof CloudBlockTile) {
					
					// Change ID to Snow
					tile.tileID = SnowTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof WoodenFenceTile) {
					
					// Change ID to Wooden Plank Block
					tile.tileID = WoodenPlankTile.getTileID(tile.tileID,false);
					
				}
				
				tileArray[i] = tile;
			}
		}
		
		return tileArray;
	}
	
	@Override
	public Object[] convertObjects(AreaCode toArea,LevelObject[] objectArray,boolean[] conversionsDone) throws Exception {
		
		int arrayLength = objectArray.length;
		
		for (int i = 0;i < arrayLength;i++) {
			
			LevelObject object = objectArray[i];
			
			// Testing if object exists in version converting to.
			if (object.objectID > conversionType.maxObjectID[1]) {
				
				// Change object to placeholder if it doesn't exist.
				object = new SignObject("14,"+object.objectData[1].toString()+","+
						object.objectData[2].toString()+","+
						object.objectData[3].toString()+","+
						object.objectData[4].toString()+","+
						object.objectData[5].toString()+","+
						object.objectData[6].toString()+","+
						"STThis%20sign%20represents%20an%20object%20that%20wasn%27t%20"
						+ "converted%2C%20since%20it%20doesn%27t%20exist%20in%20this%20"
						+ "version%20of%20the%20game.",conversionType);
			}
			
			switch (conversionType.gameVersionTo) {
			
			case "0.6.0":
				
				if (!conversionsDone[0]) {
					
					object = ConversionUtility.convertDownToZeroSixZero(object,conversionType);
					
					conversionsDone[0] = true;
				}
				
			case "0.6.1":
				
				if (!conversionsDone[1]) {
					
					Object[] data = ConversionUtility.convertDownToZeroSixOne(object,conversionType,toArea,i,numberOfWarpPipes);
					
					object = (LevelObject) data[0];
					numberOfWarpPipes = (int) data[1];
					
					conversionsDone[1] = true;
				}
				
			case "0.7.0":
				
				if (!conversionsDone[2]) {
					
					object = ConversionUtility.convertDownToZeroSevenZero(object,conversionType);
					
					conversionsDone[2] = true;
				}
			
				
			case "0.7.1","0.7.2":
				
				if (!conversionsDone[3]) {
					
					object = ConversionUtility.convertDownToZeroSevenTwo(object,conversionType);
					
					conversionsDone[3] = true;
				}
				
				break;
				
			case "0.9.1":
				
				if (!conversionsDone[6]) {
					
					object = ConversionUtility.convertUpToZeroNineOne(object,conversionType);
					
					conversionsDone[6] = true;
				}	
				
			case "0.9.0":
				
				if (!conversionsDone[5]) {
					
					object = ConversionUtility.convertUpToZeroNineZero(object,conversionType);
					
					conversionsDone[5] = true;
				}
			}
			
			objectArray[i] = object;
		}
		
		return new Object[]  {objectArray,conversionsDone};
	}	

}
