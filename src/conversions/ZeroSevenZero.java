package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import objects.SignObject;
import tiles.*;
import util.ConversionUtility;
import util.Utility;

public class ZeroSevenZero implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.7.0";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroSevenZero(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
		this.conversionType = conversionType;
		this.numberOfWarpPipes = numberOfWarpPipes;
		this.postConversionAdditions = postConversionAdditions;
	}
	
	@Override
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			switch(fromArea.getBackerBG()) {
			
			// Clouds Against a Red-Coloured Background
			case 5:
				
				// Set Backer BG to Evening Clouds
				toArea.setBackerBG(4);
				break;
			}
		}
		
		return toArea;
	}
	
	@Override
	public AreaCode convertFronterBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			// Setting palette to zero, since versions lower than 0.7.0 don't have BG palettes.
			toArea.setBGPallete(0);
			
			switch(fromArea.getFronterBG()) {
			
			// Green Hills With Blue Trees
			case 1:
				
				switch(fromArea.getBGPallete()) {
				
				// Light Blue Hills With Blue Trees
				// Pale Pink Hills With Dark Red Trees
				case 1,4:
					
					// Set Fronter BG to Light Blue Hills With Blue Trees, without having a BG palette.
					toArea.setFronterBG(2);
					break;
				
				// Dark Purple Hills With Black Trees
				case 2:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
					
				// Organge-Yellow Hills With Brown Trees
				// This doesn't need to be changed.
//				case 3:
//					
//					break;
				}
				
				break;
			
			// Sand Dunes
			case 7:
				
				// Set Fronter BG to Green Hills With Blue Trees
				toArea.setFronterBG(1);
				break;
			
			// Green Hills Surrounded by Tropical Setting
			case 8:
				
				switch(fromArea.getBGPallete()) {
				
				// Normal Pallete
				case 0:
					
					// Set Fronter BG to Green Hills With Blue Trees
					toArea.setFronterBG(1);
					break;
				
				// Light Blue Hills Surrounded by Frozen Tropical Setting
				case 1:
					
					// Set Fronter BG to Light Blue Hills With Blue Trees
					toArea.setFronterBG(2);
					break;
				
				// Dark Purple Hills Surrounded by Dark Tropical Setting
				case 2:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
					
				// Organge-Yellow Hills Surrounded by Like-Coloured Tropical Setting
				case 3:
					
					// Set Fronter BG to Green Hills With Blue Trees
					toArea.setFronterBG(1);
					break;
				}
				
				break;
			
			// Magma Caverns
			case 9:
				
				switch(fromArea.getBGPallete()) {
				
				// Frozen Caverns
				case 3:
					
					// Set Fronter BG to Sharp, Icy Mountains
					toArea.setFronterBG(5);
					break;
				
				// Every palette except Frozen Caverns.
				default:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
					
				}
				
				break;
			
			// Stone Block Structure
			case 10:
				
				switch(fromArea.getBGPallete()) {
				
				// Dark Stone Block Structure
				case 2:
					
					// Set Fronter BG to Bowser in the Dark World Cavern
					toArea.setFronterBG(4);
					break;
				
				// Every palette except Dark Stone Block Structure.
				default:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
					
				}
				
				break;
				
			// Large Blue Pine Trees
			case 11:
				
				switch(fromArea.getBGPallete()) {
				
				// Normal Pallete
				case 0:
					
					// Set Fronter BG to Light Blue Hills With Blue Trees
					toArea.setFronterBG(2);
					break;
					
				// Every palette except the default one.
				default:
					
					// Set Fronter BG to Green Hills With Blue Trees
					toArea.setFronterBG(1);
					break;
				}
				
				break;
				
			// Inside of Wooden Mansion
			case 12:
				
				// Set Fronter BG to Bowser in the Dark World Cavern
				toArea.setFronterBG(4);
				break;
				
			// Large Green Hills With Green Trees
			case 13:
				
				switch(fromArea.getBGPallete()) {
				
				// Large Snowy Hills With Snowy Trees
				// Large Pale Pink Hills With Dark Red Trees
				case 1,4:
					
					// Set Fronter BG to Light Blue Hills With Blue Trees
					toArea.setFronterBG(2);
					break;
				
				// Large Dark Hills With Black Trees
				case 2:
					
					// Set Fronter BG to Crystal Filled Cave
					toArea.setFronterBG(3);
					break;
					
				// Large Organge-Yellow Hills With Like-Coloured Trees
				case 3:
					
					// Set Fronter BG to Green Hills With Blue Trees
					toArea.setFronterBG(1);
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
			
			switch(fromArea.getMusicID()) {
			
			// Ice Ice Outpost
			case 39:
				
				// Set to Snow Rise Paper Mario: Sticker Star
				toArea.setMusicID(12);
				break;
			
			// Dire Dire Docks
			// Deep Sea of Mare
			// Cosmic Cove Galaxy
			// Underwater Passage
			case 40,41,42,43:
				
				// Set to Buoy Base Galaxy Super Mario Galaxy
				toArea.setMusicID(9);
				break;
				
			// Gritzy Desert
			// Sinking in Quicksand
			// Slipsand Galaxy
			// Tostarena: Ruins
			// Walleye Tumble Temple
			case 44,45,46,47,48:
				
				// Set to Princess Peach's Castle Super Smash Bros. Melee
				toArea.setMusicID(1);
				break;
				
			// Beach Bowl Galaxy
			// Beach
			// Sunshine Seaside
			case 49,50,51:
				
				// Set to Yoshi Star Galaxy Super Mario Galaxy 2
				toArea.setMusicID(16);
				break;
				
			// Melty Molten Galaxy
			// Melty Monster Galaxy
			// Magma
			// Fort Fire Bros
			case 52,53,54,55:
				
				// Set to Speedy Comet Super Mario Galaxy
				toArea.setMusicID(8);
				break;
				
			// Waltz of the Boos
			// Deep Dark Galaxy
			// Boo Moon Galaxy
			// Shifty Boo Mansion
			case 56,57,58,59:
				
				// Set to SMW Underground Super Mario Maker
				toArea.setMusicID(32);
				break;
				
			// Secret Course
			case 60:
				
				// Set to Mario's Pwnd Slide Remix by m477zorz
				toArea.setMusicID(5);
				break;
				
			// Sammer's Kingdom
			case 61:
				
				// Set to Champion's Road Super Mario 3D World
				toArea.setMusicID(7);
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
				
				if (tile instanceof ColouredBrickTile || tile instanceof DarkBrickTile) {
					
					// Set a default tile ID.
					tile.tileID = ColouredBrickTile.getTileID(tile.tileID,false);
					
					if (tile.hasPallete) {
						
						switch (tile.tilePallete) {
						
						// Red Pallete
						case 1:
							
							// Change ID to Red Brick Block.
							tile.tileID = RedBrickTile.getTileID(tile.tileID,false);
							break;
							
						// Yellow Pallete
						case 2:
							
							// Change ID to Yellow Brick Block.
							tile.tileID = YellowBrickTile.getTileID(tile.tileID,false);
							break;
							
						// Green Pallete,Purple Pallete
						case 3,4:
							
							// Change ID to Green Brick Block.
							tile.tileID = GreenBrickTile.getTileID(tile.tileID,false);
							break;
						}
					}
					
				} else if (tile instanceof ColouredGemTile) {
					
					// Set a default tile ID.
					tile.tileID = ColouredGemTile.getTileID(tile.tileID,false);
					
					if (tile.hasPallete) {
						
						switch (tile.tilePallete) {
						
						// Yellow Pallete
						case 1:
							
							// Change ID to Yellow Gem Block.
							tile.tileID = YellowGemTile.getTileID(tile.tileID,false);
							break;
							
						// Green Pallete
						case 2:
							
							// Change ID to Green Gem Block.
							tile.tileID = GreenGemTile.getTileID(tile.tileID,false);
							break;
							
						// Blue Pallete,Purple Pallete,Light Blue Pallete,Black Pallete
						case 3,4,5,6:
							
							// Change ID to Blue Gem Block.
							tile.tileID = BlueGemTile.getTileID(tile.tileID,false);
							break;
						}
					}
					
				} else if (tile instanceof CastleRoofTile ||
						tile instanceof VolcanicBrickTile) {
					
					// Change ID to Red Brick Block.
					tile.tileID = RedBrickTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof SandTile ||
						tile instanceof YIGrassTile) {
					
					// Change ID to Grass Block.
					tile.tileID = GrassTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof SandStoneTile) {
					
					// Change ID to Yellow Brick Block.
					tile.tileID = YellowBrickTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof VolcanoTile) {
					
					// Change ID to Warped Ground Block.
					tile.tileID = WarpedGroundTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof CastlePillarTile) {
					
					// Change ID to Castle Brick Block.
					tile.tileID = CastleBrickTile.getTileID(tile.tileID,false);
					
				} else if (tile instanceof CabinWindowTile) {
					
					// Change ID to Glass Pane Block.
					tile.tileID = GlassPaneTile.getTileID(tile.tileID,false);
					
				}
				
				tile.tilePallete = 0;
				tile.hasPallete = false;
				
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
				
				break;
				
			case "0.9.0":
				
				if (!conversionsDone[5]) {
					
					object = ConversionUtility.convertUpToZeroNineZero(object,conversionType);
					
					conversionsDone[5] = true;
				}
				
			case "0.8.0":
				
				if (!conversionsDone[4]) {
					
					object = ConversionUtility.convertUpToZeroEightZero(object,conversionType);
					
					conversionsDone[4] = true;
				}
				
			case "0.7.1","0.7.2":
				
				if (!conversionsDone[3]) {
					
					object = ConversionUtility.convertUpToZeroSevenTwo(object,conversionType);
					
					conversionsDone[3] = true;
				}
			}
			
			objectArray[i] = object;
		}
		
		return new Object[]  {objectArray,conversionsDone};
	}
}
