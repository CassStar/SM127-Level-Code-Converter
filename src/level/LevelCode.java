package level;

import java.util.ArrayList;

import objects.*;
import tiles.*;
import tools.superMario127.Converter.ConversionType;
import util.Utility;

public class LevelCode {
	
	public String levelData;
	String codeVersion,levelName,emptyArray,areaData,musicData;
	int areaBackerBG,areaFronterBG,musicID,areaBGPallete;
	int[] tileTypeSeperationIndexes;
	double areaGravity;
	double[] areaDimensions;
	LevelTile[] groundTiles,foreGroundTiles,backGround0Tiles,backGround1Tiles,allTiles;
	LevelObject[] areaObjects;
	
	boolean areDoorsNormalized = false;
	
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public LevelCode(String code) {
		
		setLevelData(code);
	}
	
	public LevelCode(LevelCode base) {
		
		setLevelData(base.getLevelData());
		setCodeVersion(base.getCodeVersion());
		setLevelName(base.getLevelName());
		setEmptyArray(base.getEmptyArray());
		setAreaData(base.getAreaData());
		setAreaDimensions(base.getAreaDimensions());
		setAreaBackerBG(base.getAreaBackerBG());
		setAreaFronterBG(base.getAreaFronterBG());
		setAreaBGPallete(base.getAreaBGPallete());
		setMusicID(base.getMusicID());
		setMusicData(base.getMusicData());
		setAreaGravity(base.getAreaGravity());
		setGroundTiles(base.getGroundTiles());
		setForeGroundTiles(base.getForeGroundTiles());
		setBackGround0Tiles(base.getBackGround0Tiles());
		setBackGround1Tiles(base.getBackGround1Tiles());
		setAreaObjects(base.getAreaObjects());
	}
	
	String getLevelData() {
		
		return levelData;
	}
	
	void setLevelData(String value) {
		
		levelData = new String(value);
	}
	
	String getCodeVersion() {
		
		return codeVersion;
	}
	
	public void setCodeVersion(String value) {
		
		codeVersion = new String(value);
	}
	
	String getLevelName() {
		
		return levelName;
	}
	
	public void setLevelName(String value) {
		
		levelName = new String(value);
	}
	
	String getEmptyArray() {
		
		return emptyArray;
	}
	
	public void setEmptyArray(String value) {
		
		emptyArray = new String(value);
	}
	
	String getAreaData() {
		
		return areaData;
	}
	
	public void setAreaData(String value) {
		
		areaData = new String(value);
	}
	
	double[] getAreaDimensions() {
		
		return areaDimensions.clone();
	}
	
	public void setAreaDimensions(double[] value) {
		
		areaDimensions = value.clone();
	}
	
	public int getAreaBackerBG() {
		
		return Integer.valueOf(areaBackerBG);
	}
	
	public void setAreaBackerBG(int value) {
		
		areaBackerBG = Integer.valueOf(value);
	}
	
	public int getAreaFronterBG() {
		
		return Integer.valueOf(areaFronterBG);
	}
	
	public void setAreaFronterBG(int value) {
		
		areaFronterBG = Integer.valueOf(value);
	}
	
	public int getAreaBGPallete() {
		
		return Integer.valueOf(areaBGPallete);
	}
	
	public void setAreaBGPallete(int value) {
		
		areaBGPallete = Integer.valueOf(value);
	}
	
	public int getMusicID() {
		
		return Integer.valueOf(musicID);
	}
	
	public void setMusicID(int value) {
		
		musicID = Integer.valueOf(value);
	}
	
	String getMusicData() {
		
		return musicData;
	}
	
	public void setMusicData(String value) {
		
		if (value == null) {
			return;
		}
		musicData = new String(value);
	}
	
	double getAreaGravity() {
		
		return Double.valueOf(areaGravity);
	}
	
	public void setAreaGravity(double value) {
		
		areaGravity = Double.valueOf(value);
	}
	
	LevelTile[] getGroundTiles() {
		
		return groundTiles.clone();
	}
	
	public void setGroundTiles(LevelTile[] value) {
		
		groundTiles = value.clone();
	}
	
	LevelTile[] getForeGroundTiles() {
		
		return foreGroundTiles.clone();
	}
	
	public void setForeGroundTiles(LevelTile[] value) {
		
		foreGroundTiles = value.clone();
	}
	
	LevelTile[] getBackGround0Tiles() {
		
		return backGround0Tiles.clone();
	}
	
	public void setBackGround0Tiles(LevelTile[] value) {
		
		backGround0Tiles = value.clone();
	}
	
	LevelTile[] getBackGround1Tiles() {
		
		return backGround1Tiles.clone();
	}
	
	public void setBackGround1Tiles(LevelTile[] value) {
		
		backGround1Tiles = value.clone();
	}
	
	public LevelTile[] getAllTiles() {
		
		return allTiles.clone();
	}
	
	public void setAllTiles(LevelTile[] value) {
		
		allTiles = value.clone();
	}
	
	public LevelObject[] getAreaObjects() {
		
		return areaObjects.clone();
	}
	
	public void setAreaObjects(LevelObject[] value) {
		
		areaObjects = value.clone();
	}
	
	public void setConversionType(ConversionType type) {
		
		conversionType = type;
	}
	
	public void setPostConversionAdditions(ArrayList<LevelObject> list) {
		
		
	}
	
	/*
	 * ^ Getters and Setters ^
	 * 
	 * v Functional Methods v
	 */
	
	public LevelTile[] parseTiles(String data) {
		
		LevelTile[] tiles;
		
		String[] tileGroups = data.split(",");
		
		tiles = new LevelTile[tileGroups.length];
		
		for (int i = 0;i < tileGroups.length;i++) {
			
			int tileID = LevelTile.getTileID(tileGroups[i]);
			
			tiles[i] = getTileFromIDAndData(tileID,tileGroups[i]);
		}
		
		return tiles;
	}
	
	LevelTile getTileFromIDAndData(int ID,String data) {
		
		switch (ID) {
		
		case 0:
			
			return new AirBlockTile(data);
		
		case 10,12,13:
			
			return new ColouredBrickTile(data);
		
		case 20,21,22,23:
			
			return new GrassTile(data);
		
		case 30,32,33:
			
			return new GreenBrickTile(data);
		
		case 40,42,43:
			
			return new RedBrickTile(data);
		
		case 50,52,53:
			
			return new YellowBrickTile(data);
		
		case 60:
			
			return new InvisibleBlockTile(data);
			
		case 70,71,72,73:
			
			return new SnowTile(data);
		
		case 80,81:
			
			return new OldCaveTile(data);
		
		case 90:
			
			return new GlassPaneTile(data);
			
		case 100,102,103:
			
			return new WoodenCabinTile(data);
		
		case 110:
			
			return new ColouredGemTile(data);
			
		case 120:
			
			return new YellowGemTile(data);
			
		case 130:
			
			return new GreenGemTile(data);
			
		case 140:
			
			return new BlueGemTile(data);
			
		case 150,152,153:
			
			return new LightStoneBrickTile(data);
		
		case 160,162,163:
			
			return new RoughStoneBrickTile(data);
		
		case 170,172,173:
			
			return new MagicBlockTile(data);
		
		case 180,181,182,183:
			
			return new WarpedGroundTile(data);
		
		case 190:
			
			return new GraniteBlockTile(data);
			
		case 200:
			
			return new CrackedStoneTile(data);
			
		case 210:
			
			return new SolidColourTile(data);
			
		case 220:
			
			return new CheckerBoardTile(data);
			
		case 230:
			
			return new CarpetBlockTile(data);
			
		case 240:
			
			return new WoodenPlankTile(data);
			
		case 250:
			
			return new CastleBrickTile(data);
			
		case 260,262,263:
			
			return new CastleRoofTile(data);
		
		case 270,271,272,273:
			
			return new SandTile(data);
		
		case 280,282,283:
			
			return new SandStoneTile(data);
		
		case 290,291,292,293:
			
			return new VolcanoTile(data);
		
		case 300:
			
			return new CastlePillarTile(data);
			
		case 310,312,313:
			
			return new VolcanicBrickTile(data);
		
		case 320,322,323:
			
			return new DarkBrickTile(data);
		
		case 330:
			
			return new CabinWindowTile(data);
			
		case 340,341,342,343:
			
			return new YIGrassTile(data);
		
		case 350,351,352,353:
			
			return new NewCaveTile(data);
		
		default:
			
			return new LevelTile(data);
		}
	}
	
	public LevelObject[] parseObjects(String data) throws Exception {
		
		LevelObject[] objects;
		
		data = data.replace('|','<');
		String[] objectStrings = data.split("<");
		
		
		objects = new LevelObject[objectStrings.length];
		
		for (int i = 0;i < objectStrings.length;i++) {
			
			int objectID = LevelObject.getObjectID(objectStrings[i]);
			
			LevelObject object = getObjectFromIDAndData(objectID,objectStrings[i]);
			
			object.setConversionType(conversionType);
			
			objects[i] = object;
		}
		
		return objects;
	}
	
	LevelObject getObjectFromIDAndData(int ID,String data) throws Exception {
		
		switch (ID) {
		
		case 0:
			
			return new MarioSpawnObject(data);
		
		case 1:
			
			return new YellowCoinObject(data);
			
		case 2:
			
			return new ShineSpriteObject(data);
			
		case 3:
			
			return new MetalPlatformObject(data);
			
		case 4:
			
			return new SM64TreeObject(data);
			
		case 5:
			
			return new LuigiSpawnObject(data);
			
		case 6:
			
			return new MushroomTopObject(data);
			
		case 7:
			
			return new MushroomStemObject(data);
			
		case 8:
			
			return new MushroomEndObject(data);
			
		case 9:
			
			return new CloudPlatformObject(data);
			
		case 10:
			
			return new FallingLogObject(data);
			
		case 11:
			
			return new LargeBushesObject(data);
			
		case 12:
			
			return new SmallBushesObject(data);
			
		case 13:
			
			return new BonePlatformObject(data);
			
		case 14:
			
			return new SignObject(data);
			
		case 15:
			
			return new GreenDemonObject(data);
			
		case 16:
			
			return new TwistedTreeTopObject(data);
			
		case 17:
			
			return new TwistedTreeBodyObject(data);
			
		case 18:
			
			return new TwistedTreeAngleObject(data);
			
		case 19:
			
			return new HoverFluddObject(data);
			
		case 20:
			
			return new FluddNozzleObject(data);
			
		case 21:
			
			return new SmallWaterBottleObject(data);
			
		case 22:
			
			return new LargeWaterBottleObject(data);
			
		case 23:
			
			return new WarpPipeTopObject(data);
			
		case 24:
			
			return new SmallSnowyTreeObject(data);
			
		case 25:
			
			return new BulletBillObject(data);
			
		case 26:
			
			return new BulletBillBlasterObject(data);
			
		case 27:
			
			return new BobOmbObject(data);
			
		case 28:
			
			return new KoopaTroopaObject(data);
			
		case 29:
			
			return new GoombaObject(data);
			
		case 30:
			
			return new RedCoinObject(data);
			
		case 31:
			
			return new UnkownObject(data);
			
		case 32:
			
			return new SnowyPlatformObject(data);
			
		case 33:
			
			return new BreakableBlockObject(data);
			
		case 34:
			
			return new EnchantedGearObject(data);
			
		case 35:
			
			return new MetalBrickBlockObject(data);
			
		case 36:
			
			return new BigSteelySpawnerObject(data);
			
		case 37:
			
			return new BigSteelyObject(data);
			
		case 38:
			
			return new RocketFluddObject(data);
			
		case 39:
			
			return new TurboFluddObject(data);
			
		case 40:
			
			return new BlueCoinObject(data);
			
		case 41:
			
			return new RainbowStarObject(data);
			
		case 42:
			
			return new RainbowBrickBlockObject(data);
			
		case 43:
			
			return new GhostPepperObject(data);
			
		case 44:
			
			return new IronBarsObject(data);
			
		case 45:
			
			return new ShineShardObject(data);
			
		case 46:
			
			return new CannonObject(data);
			
		case 47:
			
			return new SeesawPlatformObject(data);
			
		case 48:
			
			return new DoorObject(data);
			
		case 49:
			
			return new MovingPlatformObject(data);
			
		case 50:
			
			return new CarouselPlatformObject(data);
			
		case 51:
			
			return new PearlPlatformObject(data);
		
		case 52:
			
			return new StarCoinObject(data);
			
		case 53:
			
			return new ArrowObject(data);
			
		case 54:
			
			return new SmallCaveTreeObject(data);
			
		case 55:
			
			return new LargeCaveTreeObject(data);
			
		case 56:
			
			return new WoodenPlatformObject(data);
			
		case 57:
			
			return new SuperFeatherObject(data);
			
		case 58:
			
			return new NoteblockObject(data);
			
		case 59:
			
			return new LargeSnowyTreeObject(data);
			
		case 60:
			
			return new ShortSnowyPillarObject(data);
			
		case 61:
			
			return new TallSnowyPillarObject(data);
			
		case 62:
			
			return new SmallPineTreeObject(data);
			
		case 63:
			
			return new LargePineTreeObject(data);
			
		case 64:
			
			return new CavePlantObject(data);
			
		case 65:
			
			return new TallCaveSporeObject(data);
			
		case 66:
			
			return new ShortCaveSporeObject(data);
			
		case 67:
			
			return new SnowyLogObject(data);
			
		case 68:
			
			return new CastleWindowObject(data);
			
		case 69:
			
			return new JelectroObject(data);
			
		case 70:
			
			return new GoonieObject(data);
			
		case 71:
			
			return new ButterflyObject(data);
			
		case 72:
			
			return new WaterObject(data);
			
		case 73:
			
			return new LargeStoneObject(data);
			
		case 74:
			
			return new CheepCheepObject(data);
			
		case 75:
			
			return new LavaObject(data);
			
		case 76:
			
			return new SmallBeachTreeObject(data);
			
		case 77:
			
			return new CactusObject(data);
			
		case 78:
			
			return new FlowerBedObject(data);
			
		case 79:
			
			return new PSwitchBlockObject(data);
			
		case 80:
			
			return new PSwitchObject(data);
			
		case 81:
			
			return new FluidControllerObject(data);
			
		case 82:
			
			return new CheckpointObject(data);
			
		case 83:
			
			return new FirePillarObject(data);
			
		case 84:
			
			return new CharredTreeObject(data);
			
		case 85:
			
			return new BooObject(data);
			
		case 86:
			
			return new LargeBeachTreeObject(data);
			
		case 87:
			
			return new RoundBuildingWindowObject(data);
			
		case 88:
			
			return new SpikeTrapObject(data);
			
		case 89:
			
			return new PipeBodySegmentObject(data);
			
		case 90:
			
			return new PipeMetalConnectorObject(data);
			
		case 91:
			
			return new RecoveryHeartObject(data);
			
		case 92:
			
			return new OnOffSwitchObject(data);
			
		case 93:
			
			return new OnOffControlledBlockObject(data);
		
		case 94:
			
			return new OnOffControlledMovingPlatformObject(data);
			
		case 95:
			
			return new OnOffButtonObject(data);
			
		case 96:
			
			return new OnOffControlledCarouselPlatformObject(data);
			
		case 97:
			
			return new CoralObject(data);
			
		default:
			
			return new LevelObject(data);
		}
	}
	
	public void addObject(LevelObject object) {
		
		LevelObject[] currentObjects = getAreaObjects();
		int length = currentObjects.length;
		LevelObject[] newObjects = new LevelObject[length+1];
		
		for (int i = 0;i < length;i++) {
			
			newObjects[i] = currentObjects[i];
		}
		
		newObjects[length] = object;
		
		setAreaObjects(newObjects);
	}
	
	public LevelObject findMatchingPipe(LevelObject pipe,int indexFrom) {
		
		LevelObject[] objects = getAreaObjects();
		LevelObject match = null;
		
		for (int i = indexFrom;i >= 0;i--) {
			
			LevelObject compare = objects[i];
			
			if (!pipe.equals(compare) && compare.objectID == 23) {
				
				double[] scale = (double[]) compare.objectData[3];
				String tag = (String) pipe.objectData[8],
						compareTag = (String) compare.objectData[8];
				
				if (scale[0] == 1 && scale[1] == 1 && tag.equals(compareTag)) {
					
					match = compare;
				}
			}
		}
		
		if (match != null) {
			return match;
		}
		
		for (int i = indexFrom;i < objects.length;i++) {
			
			LevelObject compare = objects[i];
			
			if (!pipe.equals(compare) && compare.objectID == 23) {
				
				double[] scale = (double[]) compare.objectData[3];
				String tag = (String) pipe.objectData[8],
						compareTag = (String) compare.objectData[8];
				
				if (scale[0] == 1 && scale[1] == 1 && tag.equals(compareTag)) {
					
					return compare;
				}
			}
		}
		
		return match;
	}
	
	public void normalizeDoors() throws Exception {
		
		// Don't do anything if doors have already been normalized.
		if (areDoorsNormalized) {
			return;
		}
		
		// Getting all level objects.
		LevelObject[] levelObjects = getAreaObjects();
		int length = levelObjects.length;
		
		// 2D arrays containing groupings of doors that have matching Teleport To and
		// Teleport From tags.
		LevelObject[][] doorToGroupings = new LevelObject[0][],
				doorFromGroupings = new LevelObject[0][];
		
		// Loop over all the objects.
		for (int i = 0;i < length;i++) {
			
			LevelObject object = levelObjects[i];
			
			// Object is a door.
			if (object.objectID == 48) {
				
				// Boolean values for keeping track of if the door tags match any of the
				// current groupings.
				boolean doorMatchesToGrouping = false,doorMatchesFromGrouping = false;
				
				// Loop over all of the current groupings.
				for (int j = 0;j < doorToGroupings.length;j++) {
					
					// Get the Teleport To and Teleport From tags of the object.
					String doorTeleportToTag = (String) object.objectData[8],
							doorTeleportFromTag = (String) object.objectData[7],
					
					// Get the Teleport To and Teleport From tags of the first door in the
					// grouping.
					compareTeleportToTag = (String) doorToGroupings[j][0].objectData[8],
					compareTeleportFromTag = (String) doorToGroupings[j][0].objectData[7];
					
					// Check if the Teleport To tags match.
					if (doorTeleportToTag.equals(compareTeleportToTag)) {
						
						// Found a match.
						doorMatchesToGrouping = true;
						
						// Expand the grouping and add the door to the grouping.
						doorToGroupings[j] = expandObjectArray(doorToGroupings[j],1);
						doorToGroupings[j][doorToGroupings[j].length-1] = object;
					}
					
					// Check if the Teleport From tags match.
					if (doorTeleportFromTag.equals(compareTeleportFromTag)) {
						
						// Found a match.
						doorMatchesFromGrouping = true;
						
						// Expand the grouping and add the door to the grouping.
						doorFromGroupings[j] = expandObjectArray(doorFromGroupings[j],1);
						doorFromGroupings[j][doorFromGroupings[j].length-1] = object;
					}
				}
				
				// Check if the door's Teleport To tag didn't match any grouping.
				if (!doorMatchesToGrouping) {
					
					// Expand the total groupings array.
					doorToGroupings = Expand2DObjectArray(doorToGroupings,1);
					
					// Create a new grouping.
					LevelObject[] doorGrouping = new LevelObject[] {object};
					
					// Add the grouping to the full list.
					doorToGroupings[doorToGroupings.length-1] = doorGrouping;
				}
				
				// Check if the door's Teleport From tag didn't match any grouping.
				if (!doorMatchesFromGrouping) {
					
					// Expand the total groupings array.
					doorFromGroupings = Expand2DObjectArray(doorFromGroupings,1);
					
					// Create a new grouping.
					LevelObject[] doorGrouping = new LevelObject[] {object};
					
					// Add the grouping to the full list.
					doorFromGroupings[doorFromGroupings.length-1] = doorGrouping;
				}
			}
		}
		
		
		for (int i = 0;i < doorToGroupings.length;i++) {
			
			LevelObject[] doorGrouping = doorToGroupings[i];
			
			if (doorGrouping.length == 1) {
				
				// Set Teleport To value to be same as Teleport From value.
				doorGrouping[0].objectData[8] = doorGrouping[0].objectData[7];
				
				// Searching for single matches.
				for (int j = 0;j < doorFromGroupings.length;j++) {
					
					LevelObject[] fromGrouping = doorFromGroupings[j];
					
					if (fromGrouping.length == 1) {
						
						LevelObject door = fromGrouping[0];
						
						// This door's Teleport To value is equal the equal to the other
						// door's Teleport From value, meaning they go to each other.
						if (door.objectData[8].equals(doorGrouping[0].objectData[7]) &&
								!door.equals(doorGrouping[0])) {
							
							// Setting the Teleport From vale.
							door.objectData[7] = doorGrouping[0].objectData[7];
						}
					}
				}
				
				// Nothing else needs to be done.
				continue;
			}
			
			for (int j = 1;j < doorGrouping.length;j++) {
				
				// Create a new destination door to lead to.
				LevelObject destinationDoor = new DoorObject(doorGrouping[j].stringData);
				
				// Find the door object that we want to position the destination at.
				LevelObject positioner = findPositioner(doorToGroupings,i,
						doorGrouping[j].objectData[8]);
				
				// Copy Teleport To value from start door.
				destinationDoor.objectData[8] = doorGrouping[j].objectData[8];
				
				// Disable the positioner door.
				positioner.objectData[5] = false;
				// Make the positioner door invisible.
				positioner.objectData[5] = false;
				
				// Place the destination door where it should be.
				destinationDoor.objectData[2] = positioner.objectData[2];
				
				// Set Teleport To value to be same as Teleport From value.
				destinationDoor.objectData[8] = destinationDoor.objectData[7];
				
				destinationDoor.setConversionType(conversionType);
				
				postConversionAdditions.add(destinationDoor);
				
				// Set Teleport To value to be same as Teleport From value.
				doorGrouping[j].objectData[8] = doorGrouping[j].objectData[7];
			}
			
			// Set Teleport From value to be same as Teleport To value.
			doorGrouping[0].objectData[7] = doorGrouping[0].objectData[8];
		}
		
		areDoorsNormalized = true;
	}
	
	LevelObject findPositioner(LevelObject[][] doorToGroupings,int indexFrom,
			Object searchValue) {
		
		LevelObject positioner = null;
		
		for (int i = indexFrom-1;i >= 0;i--) {
			
			LevelObject[] doorGrouping = doorToGroupings[i];
			
			for (int j = doorGrouping.length-1; j >= 0;j--) {
				
				LevelObject compare = doorGrouping[j];
				
				if (compare.objectData[7] == searchValue) {
					
					positioner = compare;
				}
			}
		}
		
		if (positioner != null) {
			
			return positioner;
		}
		
		for (int i = indexFrom+1;i < doorToGroupings.length;i++) {
			
			LevelObject[] doorGrouping = doorToGroupings[i];
			
			for (int j = 0; j < doorGrouping.length;j++) {
				
				LevelObject compare = doorGrouping[j];
				
				if (compare.objectData[7].equals(searchValue)) {
					
					return compare;
				}
			}
		}
		
		return positioner;
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on LevelObject arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the
	 * 					array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	LevelObject[] expandObjectArray(LevelObject[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new LevelObject[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		LevelObject[] newArray = new LevelObject[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on 2D LevelObject arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the
	 * 					array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	LevelObject[][] Expand2DObjectArray(LevelObject[][] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new LevelObject[0][];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		LevelObject[][] newArray = new LevelObject[array.length+amount][];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Trims off any null values from the given array and returns the
	 * result. This method works exclusively with LevelObject arrays.</dd><hr>
	 * 
	 * @param array	The array to trim of null values.
	 * @return	A LevelObject array with no null values in it.
	 */
	LevelObject[] getTrimmedObjectArray(LevelObject[] array) {
		
		// Variable for the new array.
		LevelObject[] newArray;
		// Length of the new array, will be changed later.
		int arrayLength = 0;
		
		// Determining the length of the new array by how many non-null values were in the
		// original.
		for (LevelObject val:array) {
			if (val != null) {
				arrayLength++;
			}
		}
		
		// Creating the new array with proper length.
		newArray = new LevelObject[arrayLength];
		// Index to put values into the new array.
		int index = 0;
		
		// Looping over every value of the old array.
		for (LevelObject val:array) {
			
			// Adding the value to the new array if it isn't null.
			if (val != null) {
				newArray[index] = val;
				index++;
			}
		}
		
		// Return the new array.
		return newArray.clone();
	}
	
	public void setupAllTiles() {
		
		setAllTiles(
				combineTileArrays(new LevelTile[][] {getGroundTiles(),getForeGroundTiles(),
			getBackGround0Tiles(),getBackGround1Tiles()}));
	}
	
	public void seperateAllTilesArray() {
		
		setGroundTiles(subArray(getAllTiles(),0,tileTypeSeperationIndexes[0]));
		setForeGroundTiles(subArray(getAllTiles(),tileTypeSeperationIndexes[0],
				tileTypeSeperationIndexes[1]));
		setBackGround0Tiles(subArray(getAllTiles(),tileTypeSeperationIndexes[1],
				tileTypeSeperationIndexes[2]));
		setBackGround1Tiles(subArray(getAllTiles(),tileTypeSeperationIndexes[2],
				getAllTiles().length));
	}
	
	LevelTile[] combineTileArrays(LevelTile[][] arrays) {
		
		LevelTile[] combined = new LevelTile[0];
		int index = 0;
		
		tileTypeSeperationIndexes = new int[arrays.length];
		
		for (int j = 0;j < arrays.length;j++) {
			
			combined = expandTileArray(combined,arrays[j].length);
			
			for (int i = 0;i < arrays[j].length;index++,i++) {
				combined[index] = arrays[j][i];
			}
			
			tileTypeSeperationIndexes[j] = Integer.valueOf(index);
		}
		
		return combined;
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on LevelTile arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the
	 * 					array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	LevelTile[] expandTileArray(LevelTile[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new LevelTile[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		LevelTile[] newArray = new LevelTile[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	LevelTile[] subArray(LevelTile[] array,int start,int end) {
		
		LevelTile[] result = new LevelTile[end-start];
		
		for (int i = start,j = 0;i < end;i++,j++) {
			
			result[j] = array[i];
		}
		
		return result;
	}
	
	public void generateCode() {
		
		generateAreaData();
		
		setLevelData(codeVersion+","+
				levelName+","+
				emptyArray+","+
				areaData);
	}
	
	void generateAreaData() {
		
		setAreaData("["+Utility.vector2DToString(areaDimensions,true)+","+
				"IT"+areaBackerBG+","+
				"IT"+areaFronterBG+","+
				(musicData == null? "IT"+musicID:"ST"+musicData)+","+
				"FL"+areaGravity+
				(codeVersion.equals("0.4.9")? ",IT"+areaBGPallete+"~":"~")+
				getAllTileData()+"~"+
				getAllObjectData()+"]");
	}
	
	String getAllTileData() {
		
		StringBuilder data = new StringBuilder();
		
		for (LevelTile tile:groundTiles) {
			
			data = data.append(tile.toString());
			data = data.append(',');
		}
		
		data = data.deleteCharAt(data.length()-1);
		data = data.append('~');
		
		for (LevelTile tile:foreGroundTiles) {
			
			data = data.append(tile.toString());
			data = data.append(',');
		}
		
		data = data.deleteCharAt(data.length()-1);
		data = data.append('~');
		
		for (LevelTile tile:backGround0Tiles) {
			
			data = data.append(tile.toString());
			data = data.append(',');
		}
		
		data = data.deleteCharAt(data.length()-1);
		data = data.append('~');
		
		for (LevelTile tile:backGround1Tiles) {
			
			data = data.append(tile.toString());
			data = data.append(',');
		}
		
		data = data.deleteCharAt(data.length()-1);
		
		return data.toString();
	}
	
	String getAllObjectData() {
		
		StringBuilder data = new StringBuilder();
		
		for (LevelObject object:areaObjects) {
			
			data.append(object.toString());
			data.append('|');
		}
		
		data = data.deleteCharAt(data.length()-1);
		
		return data.toString();
	}
}
