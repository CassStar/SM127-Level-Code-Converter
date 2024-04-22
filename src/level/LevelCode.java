package level;

import java.util.ArrayList;

import objects.LevelObject;
import tiles.LevelTile;
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
			
			LevelTile tile = new LevelTile(tileGroups[i]);
			
			tiles[i] = tile;
		}
		
		return tiles;
	}
	
	public LevelObject[] parseObjects(String data) throws Exception {
		
		LevelObject[] objects;
		
		data = data.replace('|','<');
		String[] objectStrings = data.split("<");
		
		
		objects = new LevelObject[objectStrings.length];
		
		for (int i = 0;i < objectStrings.length;i++) {
			
			LevelObject object = new LevelObject(objectStrings[i]);
			
			object.setConversionType(conversionType);
			
			objects[i] = object;
		}
		
		return objects;
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
				LevelObject destinationDoor = new LevelObject(doorGrouping[j].stringData);
				
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
