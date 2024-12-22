package level;

import java.util.ArrayList;

import main.ProgramLogger;
import main.ProgramLogger.LogType;
import objects.*;
import tiles.*;
import types.BooleanType;
import types.DataType;
import util.Utility;

public class AreaCode {
	
	String areaData,musicData;
	int backerBG,fronterBG,musicID,BGPallete;
	int[] tileTypeSeperationIndexes;
	double gravity,timer;
	double[] dimensions;
	LevelTile[] groundTiles,foreGroundTiles,backGround0Tiles,backGround1Tiles,allTiles;
	LevelObject[] objects;
	LevelCode levelCode;
	
	boolean areDoorsNormalized = false;
	ArrayList<LevelObject> postConversionAdditions;
	
	AreaCode(LevelCode base) {
		
		levelCode = base;
	}
	
	String getAreaData() {
		
		return areaData;
	}
	
	public void setAreaData(String value) {
		
		areaData = new String(value);
	}
	
	double[] getDimensions() {
		
		return dimensions.clone();
	}
	
	public void setDimensions(double[] value) {
		
		dimensions = value.clone();
	}
	
	public int getBackerBG() {
		
		return Integer.valueOf(backerBG);
	}
	
	public void setBackerBG(int value) {
		
		backerBG = Integer.valueOf(value);
	}
	
	public int getFronterBG() {
		
		return Integer.valueOf(fronterBG);
	}
	
	public void setFronterBG(int value) {
		
		fronterBG = Integer.valueOf(value);
	}
	
	public int getBGPallete() {
		
		return Integer.valueOf(BGPallete);
	}
	
	public void setBGPallete(int value) {
		
		BGPallete = Integer.valueOf(value);
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
	
	double getGravity() {
		
		return Double.valueOf(gravity);
	}
	
	public void setGravity(double value) {
		
		gravity = Double.valueOf(value);
	}
	
	double getTimer() {
		
		return Double.valueOf(timer);
	}
	
	public void setTimer(double value) {
		
		timer = Double.valueOf(value);
	}
	
	public LevelTile[] getGroundTiles() {
		
		return groundTiles.clone();
	}
	
	public void setGroundTiles(LevelTile[] value) {
		
		groundTiles = value.clone();
	}
	
	public LevelTile[] getForeGroundTiles() {
		
		return foreGroundTiles.clone();
	}
	
	public void setForeGroundTiles(LevelTile[] value) {
		
		foreGroundTiles = value.clone();
	}
	
	public LevelTile[] getBackGround0Tiles() {
		
		return backGround0Tiles.clone();
	}
	
	public void setBackGround0Tiles(LevelTile[] value) {
		
		backGround0Tiles = value.clone();
	}
	
	public LevelTile[] getBackGround1Tiles() {
		
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
	
	public LevelObject[] getObjects() {
		
		return objects.clone();
	}
	
	public void setObjects(LevelObject[] value) {
		
		objects = value.clone();
	}
	
	public ArrayList<LevelObject> getPostConversionAdditions() {
		
		return postConversionAdditions;
	}
	
	public void setPostConversionAdditions(ArrayList<LevelObject> list) {
		
		postConversionAdditions = list;
	}
	
	/*
	 * ^ Getters and Setters ^
	 * 
	 * v Functional Methods v
	 */
	
	public LevelObject[] parseObjects(String data) throws Exception {
		
		LevelObject[] objects;
		
		data = data.replace('|','<');
		String[] objectStrings = data.split("<");
		
		
		objects = new LevelObject[objectStrings.length];
		
		for (int i = 0;i < objectStrings.length;i++) {
			
			int objectID = LevelObject.getObjectID(objectStrings[i]);
			
			LevelObject object = getObjectFromIDAndData(objectID,objectStrings[i]);
			
			objects[i] = object;
		}
		
		return objects;
	}
	
	LevelObject getObjectFromIDAndData(int ID,String data) throws Exception {
		
		switch (ID) {
		
		case 0:
			
			return new MarioSpawnObject(data,levelCode.conversionType);
		
		case 1:
			
			return new YellowCoinObject(data,levelCode.conversionType);
			
		case 2:
			
			return new ShineSpriteObject(data,levelCode.conversionType);
			
		case 3:
			
			return new MetalPlatformObject(data,levelCode.conversionType);
			
		case 4:
			
			return new SM64TreeObject(data,levelCode.conversionType);
			
		case 5:
			
			return new LuigiSpawnObject(data,levelCode.conversionType);
			
		case 6:
			
			return new MushroomTopObject(data,levelCode.conversionType);
			
		case 7:
			
			return new MushroomStemObject(data,levelCode.conversionType);
			
		case 8:
			
			return new MushroomEndObject(data,levelCode.conversionType);
			
		case 9:
			
			return new CloudPlatformObject(data,levelCode.conversionType);
			
		case 10:
			
			return new FallingLogObject(data,levelCode.conversionType);
			
		case 11:
			
			return new LargeBushesObject(data,levelCode.conversionType);
			
		case 12:
			
			return new SmallBushesObject(data,levelCode.conversionType);
			
		case 13:
			
			return new BonePlatformObject(data,levelCode.conversionType);
			
		case 14:
			
			return new SignObject(data,levelCode.conversionType);
			
		case 15:
			
			return new GreenDemonObject(data,levelCode.conversionType);
			
		case 16:
			
			return new TwistedTreeTopObject(data,levelCode.conversionType);
			
		case 17:
			
			return new TwistedTreeBodyObject(data,levelCode.conversionType);
			
		case 18:
			
			return new TwistedTreeAngleObject(data,levelCode.conversionType);
			
		case 19:
			
			return new HoverFluddObject(data,levelCode.conversionType);
			
		case 20:
			
			return new FluddNozzleObject(data,levelCode.conversionType);
			
		case 21:
			
			return new SmallWaterBottleObject(data,levelCode.conversionType);
			
		case 22:
			
			return new LargeWaterBottleObject(data,levelCode.conversionType);
			
		case 23:
			
			return new WarpPipeTopObject(data,levelCode.conversionType);
			
		case 24:
			
			return new SmallSnowyTreeObject(data,levelCode.conversionType);
			
		case 25:
			
			return new BulletBillObject(data,levelCode.conversionType);
			
		case 26:
			
			return new BulletBillBlasterObject(data,levelCode.conversionType);
			
		case 27:
			
			return new BobOmbObject(data,levelCode.conversionType);
			
		case 28:
			
			return new KoopaTroopaObject(data,levelCode.conversionType);
			
		case 29:
			
			return new GoombaObject(data,levelCode.conversionType);
			
		case 30:
			
			return new RedCoinObject(data,levelCode.conversionType);
			
		case 31:
			
			return new UnkownObject(data,levelCode.conversionType);
			
		case 32:
			
			return new SnowyPlatformObject(data,levelCode.conversionType);
			
		case 33:
			
			return new BreakableBlockObject(data,levelCode.conversionType);
			
		case 34:
			
			return new EnchantedGearObject(data,levelCode.conversionType);
			
		case 35:
			
			return new MetalBrickBlockObject(data,levelCode.conversionType);
			
		case 36:
			
			return new BigSteelySpawnerObject(data,levelCode.conversionType);
			
		case 37:
			
			return new BigSteelyObject(data,levelCode.conversionType);
			
		case 38:
			
			return new RocketFluddObject(data,levelCode.conversionType);
			
		case 39:
			
			return new TurboFluddObject(data,levelCode.conversionType);
			
		case 40:
			
			return new BlueCoinObject(data,levelCode.conversionType);
			
		case 41:
			
			return new RainbowStarObject(data,levelCode.conversionType);
			
		case 42:
			
			return new RainbowBrickBlockObject(data,levelCode.conversionType);
			
		case 43:
			
			return new GhostPepperObject(data,levelCode.conversionType);
			
		case 44:
			
			return new IronBarsObject(data,levelCode.conversionType);
			
		case 45:
			
			return new ShineShardObject(data,levelCode.conversionType);
			
		case 46:
			
			return new CannonObject(data,levelCode.conversionType);
			
		case 47:
			
			return new SeesawPlatformObject(data,levelCode.conversionType);
			
		case 48:
			
			return new DoorObject(data,levelCode.conversionType);
			
		case 49:
			
			return new MovingPlatformObject(data,levelCode.conversionType);
			
		case 50:
			
			return new CarouselPlatformObject(data,levelCode.conversionType);
			
		case 51:
			
			return new PearlPlatformObject(data,levelCode.conversionType);
		
		case 52:
			
			return new StarCoinObject(data,levelCode.conversionType);
			
		case 53:
			
			return new ArrowObject(data,levelCode.conversionType);
			
		case 54:
			
			return new SmallCaveTreeObject(data,levelCode.conversionType);
			
		case 55:
			
			return new LargeCaveTreeObject(data,levelCode.conversionType);
			
		case 56:
			
			return new WoodenPlatformObject(data,levelCode.conversionType);
			
		case 57:
			
			return new SuperFeatherObject(data,levelCode.conversionType);
			
		case 58:
			
			return new NoteblockObject(data,levelCode.conversionType);
			
		case 59:
			
			return new LargeSnowyTreeObject(data,levelCode.conversionType);
			
		case 60:
			
			return new ShortSnowyPillarObject(data,levelCode.conversionType);
			
		case 61:
			
			return new TallSnowyPillarObject(data,levelCode.conversionType);
			
		case 62:
			
			return new SmallPineTreeObject(data,levelCode.conversionType);
			
		case 63:
			
			return new LargePineTreeObject(data,levelCode.conversionType);
			
		case 64:
			
			return new CavePlantObject(data,levelCode.conversionType);
			
		case 65:
			
			return new TallCaveSporeObject(data,levelCode.conversionType);
			
		case 66:
			
			return new ShortCaveSporeObject(data,levelCode.conversionType);
			
		case 67:
			
			return new SnowyLogObject(data,levelCode.conversionType);
			
		case 68:
			
			return new CastleWindowObject(data,levelCode.conversionType);
			
		case 69:
			
			return new JelectroObject(data,levelCode.conversionType);
			
		case 70:
			
			return new GoonieObject(data,levelCode.conversionType);
			
		case 71:
			
			return new ButterflyObject(data,levelCode.conversionType);
			
		case 72:
			
			return new WaterObject(data,levelCode.conversionType);
			
		case 73:
			
			return new LargeStoneObject(data,levelCode.conversionType);
			
		case 74:
			
			return new CheepCheepObject(data,levelCode.conversionType);
			
		case 75:
			
			return new LavaObject(data,levelCode.conversionType);
			
		case 76:
			
			return new SmallBeachTreeObject(data,levelCode.conversionType);
			
		case 77:
			
			return new CactusObject(data,levelCode.conversionType);
			
		case 78:
			
			return new FlowerBedObject(data,levelCode.conversionType);
			
		case 79:
			
			return new PSwitchBlockObject(data,levelCode.conversionType);
			
		case 80:
			
			return new PSwitchObject(data,levelCode.conversionType);
			
		case 81:
			
			return new FluidControllerObject(data,levelCode.conversionType);
			
		case 82:
			
			return new CheckpointObject(data,levelCode.conversionType);
			
		case 83:
			
			return new FirePillarObject(data,levelCode.conversionType);
			
		case 84:
			
			return new CharredTreeObject(data,levelCode.conversionType);
			
		case 85:
			
			return new BooObject(data,levelCode.conversionType);
			
		case 86:
			
			return new LargeBeachTreeObject(data,levelCode.conversionType);
			
		case 87:
			
			return new RoundBuildingWindowObject(data,levelCode.conversionType);
			
		case 88:
			
			return new SpikeTrapObject(data,levelCode.conversionType);
			
		case 89:
			
			return new PipeBodySegmentObject(data,levelCode.conversionType);
			
		case 90:
			
			return new PipeMetalConnectorObject(data,levelCode.conversionType);
			
		case 91:
			
			return new RecoveryHeartObject(data,levelCode.conversionType);
			
		case 92:
			
			return new OnOffSwitchObject(data,levelCode.conversionType);
			
		case 93:
			
			return new OnOffControlledBlockObject(data,levelCode.conversionType);
		
		case 94:
			
			return new OnOffControlledMovingPlatformObject(data,levelCode.conversionType);
			
		case 95:
			
			return new OnOffButtonObject(data,levelCode.conversionType);
			
		case 96:
			
			return new OnOffControlledCarouselPlatformObject(data,levelCode.conversionType);
			
		case 97:
			
			return new MediumCoralObject(data,levelCode.conversionType);
			
		case 98:
			
			return new WaterBalloonObject(data,levelCode.conversionType);
			
		case 99:
			
			return new WaterSpongeObject(data,levelCode.conversionType);
			
		case 100:
			
			return new TorchObject(data,levelCode.conversionType);
			
		case 101:
			
			return new LargeCoralObject(data,levelCode.conversionType);
			
		case 102:
			
			return new SmallCoralObject(data,levelCode.conversionType);
			
		case 103:
			
			return new RadialQuarterSpiderWebObject(data,levelCode.conversionType);
			
		case 104:
			
			return new BannerObject(data,levelCode.conversionType);
			
		case 105:
			
			return new SmallCrystalObject(data,levelCode.conversionType);
			
		case 106:
			
			return new MediumCrystalObject(data,levelCode.conversionType);
			
		case 107:
			
			return new LargeCrystalObject(data,levelCode.conversionType);
			
		case 108:
			
			return new LaunchStarObject(data,levelCode.conversionType);
			
		case 109:
			
			return new SlingStarObject(data,levelCode.conversionType);
			
		case 110:
			
			return new BigSteelyDespawnerObject(data,levelCode.conversionType);
			
		case 111:
			
			return new DeathPlaneObject(data,levelCode.conversionType);
			
		case 112:
			
			return new WarpZoneObject(data,levelCode.conversionType);
			
		case 113:
			
			return new ConditionLockedDoorObject(data,levelCode.conversionType);
			
		case 114:
			
			return new SawBladeObject(data,levelCode.conversionType);
			
		case 115:
			
			return new RadialHalfSpiderWebObject(data,levelCode.conversionType);
			
		case 116:
			
			return new CornerSpiderWebObject(data,levelCode.conversionType);
			
		case 117:
			
			return new LengthWiseSpiderWebObject(data,levelCode.conversionType);
			
		case 118:
			
			return new FlowerPlatformObject(data,levelCode.conversionType);
			
		case 119:
			
			return new VineBuddedEndObject(data,levelCode.conversionType);
			
		case 120:
			
			return new VineBodyObject(data,levelCode.conversionType);
			
		case 121:
			
			return new SpikyVineObject(data,levelCode.conversionType);
			
		case 122:
			
			return new VineQuarterCircleCurveObject(data,levelCode.conversionType);
		
		case 123:
			
			return new VineCurledEndObject(data,levelCode.conversionType);
			
		case 124:
			
			return new WoodenTopFloatingPlatformObject(data,levelCode.conversionType);
			
		case 125:
			
			return new FloatingDuckPlatformObject(data,levelCode.conversionType);
			
		case 126:
			
			return new ZoomTriggerObject(data,levelCode.conversionType);
			
		case 127:
			
			return new ToadNPCObject(data,levelCode.conversionType);
			
		case 128:
			
			return new DialogueControllerObject(data,levelCode.conversionType);
			
		case 129:
			
			return new WindAreaObject(data,levelCode.conversionType);
			
		case 130:
			
			return new RexObject(data,levelCode.conversionType);
			
		case 131:
			
			return new BoneGoonieObject(data,levelCode.conversionType);
			
		case 132:
			
			return new GoggleCheepObject(data,levelCode.conversionType);
			
		case 133:
			
			return new NipperObject(data,levelCode.conversionType);
			
		case 134:
			
			return new NipperFireBallObject(data,levelCode.conversionType);
		
		case 135:
			
			return new StarBitObject(data,levelCode.conversionType);
			
		case 136:
			
			return new PropellerObject(data,levelCode.conversionType);
			
		case 137:
			
			return new PrincessPeachNPCObject(data,levelCode.conversionType);
			
		case 138:
			
			return new YoshiNPCObject(data,levelCode.conversionType);
			
		case 139:
			
			return new BobOmbNPCObject(data,levelCode.conversionType);
			
		case 140:
			
			return new DryBonesObject(data,levelCode.conversionType);
			
		case 141:
			
			return new MossyRockBunchObject(data,levelCode.conversionType);
				
		default:
			
			ProgramLogger.logMessage("Invalid ID for object detected! Converting object to Turbo Fludd object.",LogType.WARNING);
			return new TurboFluddObject(data,levelCode.conversionType);
		}
	}
	
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
	
	LevelTile parseTile(String data) {
		
		int tileID = LevelTile.getTileID(data);
		
		return getTileFromIDAndData(tileID,data);
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
		
		case 360:
			
			return new BookCaseTile(data);
			
		case 370,371,372,373:
			
			return new StoneFenceTile(data);
		
		case 380,381,382,383:
			
			return new TreeBlockTile(data);
		
		case 390,391:
			
			return new CloudBlockTile(data);
		
		case 400,402,403:
			
			return new WoodenFenceTile(data);
		
		default:
			
			return new LevelTile(data);
		}
	}
	
	public void addObject(LevelObject object) {
		
		LevelObject[] currentObjects = getObjects();
		int length = currentObjects.length;
		LevelObject[] newObjects = new LevelObject[length+1];
		
		for (int i = 0;i < length;i++) {
			
			newObjects[i] = currentObjects[i];
		}
		
		newObjects[length] = object;
		
		setObjects(newObjects);
	}
	
	public LevelObject findMatchingPipe(LevelObject pipe,int indexFrom) {
		
		LevelObject[] objects = getObjects();
		LevelObject match = null;
		
		for (int i = indexFrom;i >= 0;i--) {
			
			LevelObject compare = objects[i];
			
			if (!pipe.equals(compare) && compare.objectID == 23) {
				
				double[] scale = (double[]) compare.objectData[3].getValue();
				String tag = (String) pipe.objectData[8].getValue(),
						compareTag = (String) compare.objectData[8].getValue();
				
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
				
				double[] scale = (double[]) compare.objectData[3].getValue();
				String tag = (String) pipe.objectData[8].getValue(),
						compareTag = (String) compare.objectData[8].getValue();
				
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
		LevelObject[] levelObjects = getObjects();
		int length = levelObjects.length;
		
		// 2D arrays containing groupings of doors that have matching Teleport To and
		// Teleport From tags.
		LevelObject[][] doorToGroupings = new LevelObject[0][],
				doorFromGroupings = new LevelObject[0][];
		
		// Loop over all the objects.
		for (int i = 0;i < length;i++) {
			
			LevelObject object = levelObjects[i];
			
			// Object is a door.
			if (object instanceof DoorObject) {
				
				// Boolean values for keeping track of if the door tags match any of the
				// current groupings.
				boolean doorMatchesToGrouping = false,doorMatchesFromGrouping = false;
				
				// Loop over all of the current groupings.
				for (int j = 0;j < doorToGroupings.length;j++) {
					
					// Get the Teleport To and Teleport From tags of the object.
					String doorTeleportToTag = (String) object.objectData[8].getValue(),
							doorTeleportFromTag = (String) object.objectData[7].getValue(),
					
					// Get the Teleport To and Teleport From tags of the first door in the
					// grouping.
					compareTeleportToTag = (String)
						doorToGroupings[j][0].objectData[8].getValue(),
					compareTeleportFromTag = (String)
						doorToGroupings[j][0].objectData[7].getValue();
					
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
				
				String teleportTo = (String) doorGrouping[0].objectData[8].getValue();
				
				// Searching for single matches.
				for (int j = 0;j < doorFromGroupings.length;j++) {
					
					LevelObject[] fromGrouping = doorFromGroupings[j];
					
					if (fromGrouping.length == 1) {
						
						LevelObject door = fromGrouping[0];
						
						String teleportFrom = (String) door.objectData[7].getValue();
						
						// This door's Teleport To value is equal the equal to the other
						// door's Teleport From value, meaning they go to each other.
						if (teleportTo.equals(teleportFrom) &&
								!door.equals(doorGrouping[0])) {
							
							LevelObject destinationDoor = new DoorObject(door.stringData,levelCode.conversionType);
							
							// Setting the Teleport From and Teleport To values.
							destinationDoor.objectData[7] = doorGrouping[0].objectData[7];
							destinationDoor.objectData[8] = doorGrouping[0].objectData[7];
							
							// Disable the destination door.
							destinationDoor.objectData[5] = new BooleanType("BL0");
							// Make the destination door invisible.
							destinationDoor.objectData[6] = new BooleanType("BL0");
							
							postConversionAdditions.add(destinationDoor);
						}
					}
				}
				
				// Set Teleport To value to be same as Teleport From value.
				doorGrouping[0].objectData[8] = doorGrouping[0].objectData[7];
				
				// Nothing else needs to be done.
				continue;
			}
			
			for (int j = 1;j < doorGrouping.length;j++) {
				
				// Create a new destination door to lead to.
				LevelObject destinationDoor = new DoorObject(doorGrouping[j].stringData,
						levelCode.conversionType);
				
				// Find the door object that we want to position the destination at.
				LevelObject positioner = findPositioner(doorToGroupings,i,
						doorGrouping[j].objectData[8]);
				
				// Copy Teleport To value from start door.
				destinationDoor.objectData[8] = doorGrouping[j].objectData[8];
				
				// Disable the positioner door.
				positioner.objectData[5] = new BooleanType("BL0");
				// Make the positioner door invisible.
				positioner.objectData[6] = new BooleanType("BL0");
				
				// Place the destination door where it should be.
				destinationDoor.objectData[2] = positioner.objectData[2];
				
				// Set Teleport To value to be same as Teleport From value.
				destinationDoor.objectData[8] = destinationDoor.objectData[7];
				
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
			DataType searchValue) {
		
		LevelObject positioner = null;
		
		for (int i = indexFrom-1;i >= 0;i--) {
			
			LevelObject[] doorGrouping = doorToGroupings[i];
			
			for (int j = doorGrouping.length-1; j >= 0;j--) {
				
				LevelObject compare = doorGrouping[j];
				
				if (compare.objectData[7].getValue().equals(searchValue.getValue())) {
					
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
				
				if (compare.objectData[7].getValue().equals(searchValue.getValue())) {
					
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
	
	void generateAreaData() {
		
		StringBuilder areaData = new StringBuilder("[");
		
		areaData.append(Utility.vector2DToString(dimensions,true));
		areaData.append(",IT");
		areaData.append(backerBG);
		areaData.append(",IT");
		areaData.append(fronterBG);
		areaData.append(",");
		
		if (musicData == null) {
			
			areaData.append("IT");
			areaData.append(musicID);
			
		} else {
			
			areaData.append("ST");
			areaData.append(musicData);
		}
		
		areaData.append(",FL");
		areaData.append(gravity);
		
		if (Utility.versionGreaterThanVersion(levelCode.conversionType.gameVersionTo,"0.6.9")) {
			
			areaData.append(",IT");
			areaData.append(BGPallete);
		}
		
		if (Utility.versionGreaterThanVersion(levelCode.conversionType.gameVersionTo,"0.8.9")) {
			
			areaData.append(",FL");
			areaData.append(timer);
		}
		
		areaData.append("~");
		areaData.append(getAllTileData());
		areaData.append("~");
		areaData.append(getAllObjectData());
		areaData.append("]");
		
		this.areaData = areaData.toString();
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
		
		for (LevelObject object:objects) {
			
			data.append(object.toString());
			data.append('|');
		}
		
		data = data.deleteCharAt(data.length()-1);
		
		return data.toString();
	}
}
