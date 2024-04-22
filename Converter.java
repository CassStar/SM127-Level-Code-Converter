package tools.superMario127;

import static java.nio.file.StandardOpenOption.READ;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Converter {
	
	enum ConversionType {OLD_TO_NEW(1),NEW_TO_OLD(2);
		
		int type;
		String gameVersionFrom,gameVersionTo,codeVersionFrom,codeVersionTo;
		
		ConversionType(int value) {
			
			type = value;
			
			switch(type) {
			case 1:
				
				gameVersionFrom = "0.6.1";
				gameVersionTo = "0.7.2";
				codeVersionFrom = "0.4.5";
				codeVersionTo = "0.4.9";
				
				break;
			case 2:
				
				gameVersionFrom = "0.7.2";
				gameVersionTo = "0.6.1";
				codeVersionFrom = "0.4.9";
				codeVersionTo = "0.4.5";
			}
		}
	}
	
	String directory;
	Path directoryPath,inputDirectory,outputDirectory;
	String[] fileNames;
	ConversionType conversionType;
	LevelCode fromLevel,toLevel;
	
	int maxAreaBackerBG,maxAreaFronterBG,maxMusicID,numberOfWarpPipes = 0;
	ArrayList<LevelObject> postConversionAdditions = new ArrayList<LevelObject>();
	
	Scanner input = new Scanner(System.in);
	
	Converter() {
		
//		String workingDirectory = System.getProperty("user.dir");
		String workingDirectory = "C:\\Users\\27funkj\\Downloads\\Super Mario 127\\"
				+ "Code Converter";
		directoryPath = Paths.get(workingDirectory);
		
		inputDirectory = directoryPath.resolve("input");
		outputDirectory = directoryPath.resolve("output");
		
		getfileNames(inputDirectory.toFile());
		
		if (fileNames == null) {
			
			System.out.printf("Couldn't find any text files to convert!%n"
					+ "Did you remember to put them into the 'input' folder?%n"
					+ "Quitting program.");
			input.nextLine();
			System.exit(0);
		}
		
		try {
			start();
		} catch (Exception e) {
			
			e.printStackTrace();
			input.nextLine();
		}
		
	}
	
	void getfileNames(File folder) {
		
		for (File file:folder.listFiles()) {
			
			addFile(file.getName());
		}
	}
	
	void addFile(String file) {
		
		if (file.endsWith(".txt")) {
			
			fileNames = expandStringArray(fileNames,1);
			fileNames[fileNames.length-1] = file.toString();
			
			System.out.printf("Found file for conversion: %s%n",file.toString());
		}
	}
	
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on String arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	String[] expandStringArray(String[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new String[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		String[] newArray = new String[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	void start() throws Exception {
		
		for (String file:fileNames) {
			
			System.out.println();
			
			conversionType = getConversionType(file);
			
			convertFile(file);
		}
		
		
		System.out.println("\n\nFinished converting files. Press enter to quit...");
		input.nextLine();		
	}

	public static void main(String[] args) {
		
		new Converter();
	}
	
	void saveLevelCode(LevelCode code,String fileName) {
		
		code.generateCode();
		
		String levelData = code.levelData;
		
		Path outputFile = outputDirectory.resolve(fileName);
		
		// Beginning output to file.
		try (OutputStream out = new BufferedOutputStream(
			      Files.newOutputStream(outputFile))) {
			
			// Writing the data to file.
			out.write(levelData.getBytes());
			
			// Making sure everything gets written to file before closing it.
			out.flush();
			
		} catch (IOException e) {
			
			System.out.println("Couldn't write converted level code data to file!\n"
					+ "Detailed error below:");
			e.printStackTrace();
		}
	}
	
	ConversionType getConversionType(String file) {
		
		System.out.printf("Please enter the conversion type to use for file: %s%n%n"
				+ "Enter '1' to convert from 0.6.1 to 0.7.2%n"
				+ "Enter '2' to convert from 0.7.2 to 0.6.1%n%n",file);
		
		System.out.print("Enter conversion type: ");
		
		String tempInput;
		
		tempInput = input.nextLine();
		
		while (!(tempInput.equals("1") || tempInput.equals("2"))) {
			
			System.err.println("\nInvalid conversion type entered!\n");
			
			System.out.printf("Please enter the conversion type to use for file: %s%n%n"
					+ "Enter '1' to convert from 0.6.1 to 0.7.2%n"
					+ "Enter '2' to convert from 0.7.2 to 0.6.1%n%n",file);
			
			System.out.print("Enter conversion type: ");
			
			tempInput = input.nextLine();
		}
		
		switch (tempInput) {
		case "1":
			return ConversionType.OLD_TO_NEW;
		}
		return ConversionType.NEW_TO_OLD;
	}
	
	void convertFile(String file) throws Exception {
		
		System.out.println("\nChecking code sturcture...\n");
		boolean isValid = checkCodeStructure(file);
		System.out.println("Finished checking code structure.\n");
		
		if (!isValid) {
			
			System.err.printf("Code structure for file: %s does not match known code structure"
					+ "for game version: %s%n",file,conversionType.gameVersionFrom);
			return;
		}
		
		System.out.println("Converting level data...\n");
		
		// Copying all data to the new level.
		toLevel = new LevelCode(fromLevel);
		
		// Updating code version.
		toLevel.setCodeVersion(conversionType.codeVersionTo);
		
		// Area Backer BG Conversion.
		convertBackerBG();
		
		// Area Fronter BG Conversion.
		convertFronterBG();
		
		// Area Music Conversion.
		convertMusicIDs();
		
		// Tile Conversions.
		convertTiles();
		
		// Object Conversions.
		convertObjects();
		
		for (LevelObject object:postConversionAdditions) {
			
			// Post-conversion additions need to actually be converted as well.
			if (object.objectID == 48) {
				
				object = new LevelObject(""+object.objectID+","+
						vector2DToString(
								(double[]) object.objectData[2],false)+","+
						vector2DToString(
								(double[]) object.objectData[3],false)+","+
						object.dataTypes[4]+object.objectData[4]+","+
						booleanToString((boolean) object.objectData[5])+","+
						booleanToString((boolean) object.objectData[6])+","+
						"IT0,"+object.objectData[8]+",BL0");
			}
			
			toLevel.addObject(object);
		}
		
		String newFileName = file.substring(0,file.length()-4)+
				" [Converted to V"+conversionType.gameVersionTo+"].txt";
		
		saveLevelCode(toLevel,newFileName);
		
		System.out.printf("Finished converting level data!%nYou can find the converted level in"
				+ " the output folder with the filename: %s%n",newFileName);
	}
	
	boolean checkCodeStructure(String fileName) {
		
		
		Path dataFile = inputDirectory.resolve(fileName);
		int currentIndex = 0,nextIndex = 0;
		
		int minAreaBackerBG = 0,minAreaFronterBG = 0;
		int minMusicID = 0,musicID = 0;
		
		switch(conversionType) {
		case OLD_TO_NEW:
			
			maxAreaBackerBG = 5;
			maxAreaFronterBG = 6;
			maxMusicID = 39;
			break;
			
		case NEW_TO_OLD:
			
			maxAreaBackerBG = 7;
			maxAreaFronterBG = 13;
			maxMusicID = 66;
			break;
		}
		
		try (InputStream in = new BufferedInputStream(Files.newInputStream(dataFile,READ))) {
			
			String levelData = new String(in.readAllBytes());
			
			int numberOfBrackets = 0,index = 0;
			
			while (levelData.substring(index,levelData.length()).indexOf('[') != -1) {
				
				index += levelData.substring(index,levelData.length()).indexOf('[')+1;
				numberOfBrackets++;
			}
			
			if (numberOfBrackets > 2) {
				
				System.out.println("Level code has multiple areas. Multiple areas are not "
						+ "supported by this converter yet. File will be skipped.");
				return false;
			}
			
			fromLevel = new LevelCode(levelData);
			
//			System.out.println("Level Code: "+levelData+"\n");
			
			nextIndex = levelData.indexOf(',');
			String codeVersion = levelData.substring(currentIndex,nextIndex);
			
			System.out.println("Code Version: "+codeVersion+"\n");
			
			if (!codeVersion.equals(conversionType.codeVersionFrom)) {
				
				System.err.printf("Invalid level code version! Expected: %s Got: %s%n",
						conversionType.codeVersionFrom,codeVersion);
				
				return false;
			}
			
			fromLevel.setCodeVersion(codeVersion);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			nextIndex = levelData.indexOf(',');
			
			String levelName = levelData.substring(currentIndex,nextIndex);
			
			System.out.println("Level Name: "+levelName+"\n");
			
			fromLevel.setLevelName(levelName);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			nextIndex = levelData.indexOf(',');
			
			String emptyArray = levelData.substring(currentIndex,nextIndex);
			
			System.out.println("Empty Array: "+emptyArray+"\n");
			
			if (!emptyArray.equals("[]")) {
				
				System.err.println("Couldn't find empty array in level code.\nAdding one"
						+ " where it would be expected.");
				
				emptyArray = "[]";
			}
			
			fromLevel.setEmptyArray(emptyArray);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			
			String areaData = levelData.substring(currentIndex,levelData.length());
			
//			System.out.println("Area Data: "+areaData+"\n");
			
			if (!(areaData.startsWith("[") && areaData.endsWith("]"))) {
				
				System.err.println("Invalid structure for area data detected.");
				return false;
			}
			
			fromLevel.setAreaData(areaData);
			
			nextIndex = areaData.indexOf(',');
			
			String dimensionData = areaData.substring(currentIndex+1,nextIndex);
			
			System.out.println("Area Dimension data: "+dimensionData);
			
			double[] areaDimensions = parseVector2D(dimensionData);
			double[] minimumDimensions = {24,14};
			double[] defaultDimensions = {80,30};
			
			if (areaDimensions == null) {
				
				areaDimensions = defaultDimensions.clone();
				System.out.println("Setting level dimensions to default values.");
				
			} else {
				
				if (areaDimensions[0] < minimumDimensions[0] ||
						areaDimensions[1] < minimumDimensions[1]) {
					
					System.out.println("Level dimensions are too small! Clamping low "
							+ "values.");
					
					areaDimensions[0] = Math.max(areaDimensions[0],minimumDimensions[0]);
					areaDimensions[1] = Math.max(areaDimensions[1],minimumDimensions[1]);
				}
			}
			
			System.out.printf("Area Dimensions: %.0f,%.0f%n%n",areaDimensions[0],
					areaDimensions[1]);
			
			fromLevel.setAreaDimensions(areaDimensions);
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			String temp = areaData.substring(currentIndex,nextIndex);
			
			System.out.println("Area backer BG Data: "+temp);
			
			int areaBackerBG = parseInteger(temp);
			
			if (areaBackerBG < minAreaBackerBG || areaBackerBG > maxAreaBackerBG) {
				
				System.out.println("Area backer BG value outside of expected range! "
						+ "Clamping value.");
				
				areaBackerBG = Math.min(areaBackerBG,maxAreaBackerBG);
				areaBackerBG = Math.max(areaBackerBG,minAreaBackerBG);
			}
			
			System.out.println("Area backer BG: "+areaBackerBG+"\n");
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			System.out.println("Area fronter BG Data: "+temp);
			
			int areaFronterBG = parseInteger(temp);
			
			if (areaFronterBG < minAreaFronterBG || areaFronterBG > maxAreaFronterBG) {
				
				System.out.println("Area fronter BG value outside of expected range! "
						+ "Clamping value.");
				
				areaFronterBG = Math.min(areaFronterBG,maxAreaFronterBG);
				areaFronterBG = Math.max(areaFronterBG,minAreaFronterBG);
			}
			
			System.out.println("Area fronter BG: "+areaFronterBG+"\n");
			
			fromLevel.setAreaBackerBG(areaBackerBG);
			fromLevel.setAreaFronterBG(areaFronterBG);
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			System.out.println("Area Music Data: "+temp);
			
			String musicDataType = getDataType(temp),musicData = null;
			
			if (!(musicDataType.equals("ST") || musicDataType.equals("IT"))) {
				
				System.out.println("Unkown music data structure. Setting to default value.");
				musicID = 1;
				
				System.out.println("Area Music ID: "+musicID+"\n");
				
				fromLevel.setMusicID(musicID);
				
			} else if (musicDataType.equals("IT")) {
				
				musicID = parseInteger(temp);
				
				if (musicID < minMusicID || musicID > maxMusicID) {
					
					System.out.println("Music ID outside of expected range! "
							+ "Clamping value.");
					
					musicID = Math.min(musicID,maxMusicID);
					musicID = Math.max(musicID,minMusicID);
				}
				
				System.out.println("Area Music ID: "+musicID+"\n");
				
				fromLevel.setMusicID(musicID);
				
			} else {
				
				musicData = new String(temp);
				
				System.out.println();
				
				fromLevel.setMusicData(musicData);
			}
			
			switch (conversionType) {
			case OLD_TO_NEW:
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf('~');
				break;
				
			case NEW_TO_OLD:
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf(',');
				break;
			}
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			System.out.println("Area Gravity Data: "+temp);
			
			double areaGravity = parseFloat(temp);
			
			System.out.println("Area Gravity: "+areaGravity+"\n");
			
			fromLevel.setAreaGravity(areaGravity);
			
			if (conversionType == ConversionType.NEW_TO_OLD) {
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf('~');
				
				temp = areaData.substring(currentIndex,nextIndex);
				
				System.out.println("Area BG Pallete Data: "+temp);
				
				int areaBGPallete = parseInteger(temp);
				
				System.out.println("Area BG Pallete: "+areaBGPallete+"\n");
				
				fromLevel.setAreaBGPallete(areaBGPallete);
			}
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
//			System.out.println("Area Ground Tile Data: "+temp+"\n");
			
			fromLevel.setGroundTiles(fromLevel.parseTiles(temp));
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// ForeGround Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
//			System.out.println("Area ForeGround Tile Data: "+temp+"\n");
			
			fromLevel.setForeGroundTiles(fromLevel.parseTiles(temp));
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// BackGround0 Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
//			System.out.println("Area BackGround0 Tile Data: "+temp+"\n");
			
			fromLevel.setBackGround0Tiles(fromLevel.parseTiles(temp));
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// BackGround1 Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
//			System.out.println("Area BackGround1 Tile Data: "+temp+"\n");
			
			fromLevel.setBackGround1Tiles(fromLevel.parseTiles(temp));
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.length()-1;
			
			temp = areaData.substring(currentIndex,nextIndex);
			
//			System.out.println("Area Objects Data: "+temp+"\n");
			
			fromLevel.setAreaObjects(fromLevel.parseObjects(temp));
			
		} catch (IOException e) {
			
			System.err.printf("An error occured when reading from the file: %s.%n"
					+ "Cannot determine if code structure is valid, so file will be "
					+ "skipped. Detailed error below:%n",fileName);
			
			e.printStackTrace();
			return false;
			
		} catch (Exception e) {
			
			System.err.println("Invalid code structure detected. Detailed error below:\n");
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	void convertBackerBG() {
		
		if (toLevel.getAreaBackerBG() == maxAreaBackerBG) {
			
			System.out.println("Converting Silhouette Mode.");
			
			if (conversionType == ConversionType.OLD_TO_NEW) {
				
				toLevel.setAreaBackerBG(7);
				
			} else {
				
				toLevel.setAreaBackerBG(5);
			}
		} else if (toLevel.getAreaBackerBG() > maxAreaBackerBG) {
			
			toLevel.setAreaBackerBG(4);
		}
	}
	
	void convertFronterBG() {
		
		if (conversionType == ConversionType.NEW_TO_OLD) {
			
			if (toLevel.getAreaFronterBG() == 1 && toLevel.getAreaBGPallete() == 1) {
				
				toLevel.setAreaFronterBG(2);
			}
			
			toLevel.setAreaBGPallete(0);
			
			switch (toLevel.getAreaFronterBG()) {
			case 7,8,10,12,13:
				
				toLevel.setAreaFronterBG(1);
				break;
				
			case 9:
				
				toLevel.setAreaFronterBG(3);
				break;
				
			case 11:
				
				toLevel.setAreaFronterBG(2);
				break;
			}
		}
	}
	
	void convertMusicIDs() {
		
		if (conversionType == ConversionType.OLD_TO_NEW && toLevel.getMusicID() == maxMusicID) {
			
			toLevel.setMusicID(66);
			
		} else if (conversionType == ConversionType.NEW_TO_OLD) {
			
			switch(toLevel.getMusicID()) {
			
			case 66:
				
				toLevel.setMusicID(39);
				break;
				
			case 39:
				
				toLevel.setMusicID(12);
				break;
				
			case 40,41,42,43,64:
				
				toLevel.setMusicID(9);
				break;
				
			case 44,45,46,47,48,49,50,51:
				
				toLevel.setMusicID(16);
				break;
				
			case 52,53,54,55:
				
				toLevel.setMusicID(4);
				break;
				
			case 56,57,58,59,63:
				
				toLevel.setMusicID(20);
				break;
				
			case 60:
				
				toLevel.setMusicID(5);
				break;
				
			case 61:
				
				toLevel.setMusicID(19);
				break;
				
			case 62:
				
				toLevel.setMusicID(34);
				break;
				
			case 65:
				
				toLevel.setMusicID(1);
				break;
			}
		}
	}
	
	void convertTiles() {
		
		// Combine all the tiles into one large array.
		toLevel.setupAllTiles();
		
		LevelTile[] tileArray = toLevel.getAllTiles();
		
		if (conversionType == ConversionType.NEW_TO_OLD) {
			
			int arrayLength = tileArray.length;
			
			for (int i = 0;i < arrayLength;i++) {
				
				LevelTile tile = tileArray[i];
				
				switch (tile.tileID) {
				
				// Coloured Brick Blocks
				case 10,12,13:
					
					if (tile.hasPallete) {
						
						switch (tile.tilePallete) {
							
						// Red Pallete
						case 1:
							
							// Change ID to Red Brick Block.
							tile.tileID += 30;
							break;
							
						// Yellow Pallete
						case 2:
							
							// Change ID to Yellow Brick Block.
							tile.tileID += 40;
							break;
							
						// Green Pallete,Purple Pallete
						case 3,4:
							
							// Change ID to Green Brick Block.
							tile.tileID += 20;
							break;
						}
					}
					break;
				
				// Coloured Gem Blocks
				case 110:
					
					if (tile.hasPallete) {
						
						switch (tile.tilePallete) {
						
						// Yellow Pallete
						case 1:
							
							// Change ID to Yellow Gem Block.
							tile.tileID += 10;
							break;
							
						// Green Pallete
						case 2:
							
							// Change ID to Green Gem Block.
							tile.tileID += 20;
							break;
							
						// Blue Pallete,Purple Pallete,Light Blue Pallete,Black Pallete
						case 3,4,5,6:
							
							// Change ID to Blue Gem Block.
							tile.tileID += 30;
							break;
						}
					}
					break;
				
				// Castle Roof Tiles
				case 260,262,263:
					
					// Change ID to Red Gem Block.
					tile.tileID = 110;
					break;
					
				// Snow Tiles
				case 270,271,272,273:
					
					// Change ID to Grass Block.
					tile.tileID -= 250;
					break;
					
				// SandStone Tiles
				case 280,282,283:
					
					// Change ID to Yellow Brick Block.
					tile.tileID -= 230;
					break;
					
				// Volcano Tiles
				case 290,291,292,293:
					
					// Change ID to Warped Ground Block.
					tile.tileID -= 110;
					break;
					
				// Castle Pillar Tiles
				case 300:
					
					// Change ID to Castle Brick Block.
					tile.tileID -= 50;
					break;
					
				// Volcanic Brick Tiles
				case 310,312,313:
					
					// Change ID to Red Brick Block.
					tile.tileID -= 270;
					break;
					
				// Dark Brick Tiles
				case 320,322,323:
					
					if (tile.hasPallete) {
						
						switch (tile.tilePallete) {
							
						// Red Pallete
						case 1:
							
							// Change ID to Red Brick Block.
							tile.tileID -= 280;
							break;
							
						// Yellow Pallete
						case 2:
							
							// Change ID to Yellow Brick Block.
							tile.tileID -= 270;
							break;
							
						// Green Pallete,Purple Pallete
						case 3,4:
							
							// Change ID to Green Brick Block.
							tile.tileID -= 290;
							break;
						}
					}
					break;
					
				// Cabin Window Tiles
				case 330:
					
					// Change ID to Glass Pane Block.
					tile.tileID -= 240;
					break;
					
				// YI Grass Tiles
				case 340,341,342,343:
					
					// Change ID to Grass Block.
					tile.tileID -= 320;
					break;
					
				// New Cave Tiles
				case 350,351:
					
					// Change ID to Old Cave Block.
					tile.tileID -= 270;
					break;
					
				// New Cave Slope Tiles
				case 352,353:
					
					// Change ID to Old Cave Half-Tile Block.
					tile.tileID = 81;
					break;
				}
				
				tile.tilePallete = 0;
				tile.hasPallete = false;
				
				tileArray[i] = tile;
			}
		}
		
		toLevel.setAllTiles(tileArray);
		
		// Seperate the large tile array back into the different types.
		toLevel.seperateAllTilesArray();
	}
	
	void convertObjects() throws Exception {
		
		LevelObject[] objectArray = toLevel.getAreaObjects();
		int arrayLength = objectArray.length;
		
		for (int i = 0;i < arrayLength;i++) {
			
			LevelObject object = objectArray[i];
			
			switch(object.objectID) {
			
			// Metal Platform Object
			case 3:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL1x1x1");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			// Mushroom Top Object
			case 6:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			// Sign Object
			case 14:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",BL0,BL0");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(0,
						object.stringData.length()-",BL0,BL0".length()));
				}
				break;
				
			// Twited Tree Top Object
			case 16:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL0.97x0.5x0.16");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			// Warp Pipe Top Object
			case 23:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					double[] pipeDestinationCoordinates = (double[]) object.objectData[8],
							pipeCoordinates = (double[]) object.objectData[2];
					
					object = new LevelObject(""+object.objectID+","+
							vector2DToString(
									(double[]) object.objectData[2],false)+","+
							vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							booleanToString((boolean) object.objectData[5])+","+
							booleanToString((boolean) object.objectData[6])+","+
							object.dataTypes[7]+object.objectData[7]+","+
							"STWarpPipe"+numberOfWarpPipes+","+
							"CL0x1x0,"+"BL1");
					
					if (!areSameVectors(pipeCoordinates,pipeDestinationCoordinates)) {
						
						LevelObject destinationPipe = new LevelObject(object.stringData);
						
						destinationPipe.objectData[2] = new double[] {
								pipeDestinationCoordinates[0],
								pipeDestinationCoordinates[1]
						};
						
						postConversionAdditions.add(destinationPipe);
					}
				} else {
					
					LevelObject destinationPipe = toLevel.findMatchingPipe(object,i);
					
					object = new LevelObject(""+object.objectID+",0,"+
							vector2DToString(
									(double[]) object.objectData[2],false)+","+
							vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							booleanToString((boolean) object.objectData[5])+","+
							booleanToString((boolean) object.objectData[6])+","+
							object.dataTypes[7]+object.objectData[7]+","+
							(destinationPipe == null?
							vector2DToString((double[]) object.objectData[2],false):
							vector2DToString((double[]) destinationPipe.objectData[2],false))+
							",BL1");
				}
				
				numberOfWarpPipes++;
				break;
				
			// Goomba Object
			case 29:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			// Door Object
			case 48:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					toLevel.normalizeDoors();
					
					object = new LevelObject(""+object.objectID+","+
							vector2DToString(
									(double[]) object.objectData[2],false)+","+
							vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							booleanToString((boolean) object.objectData[5])+","+
							booleanToString((boolean) object.objectData[6])+","+
							"IT0,"+object.objectData[8]+",BL0");
				} else {
					
					object = new LevelObject(""+object.objectID+",0,"+
							vector2DToString(
									(double[]) object.objectData[2],false)+","+
							vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							booleanToString((boolean) object.objectData[5])+","+
							booleanToString((boolean) object.objectData[6])+","+
							object.objectData[8]+","+
							object.objectData[8]);
				}
				break;
				
			// Arrow Object
			case 53:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			// Wooden Platform Object
			case 56:
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new LevelObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new LevelObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				break;
				
			default:
				// All other objects (New Objects)
				if (object.objectID > 69) {
					
					object = new LevelObject("14,"+object.objectData[1]+","+
							vector2DToString(
									(double[]) object.objectData[2],false)+","+
							vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							booleanToString((boolean) object.objectData[5])+","+
							booleanToString((boolean) object.objectData[6])+","+
							"STThis%20sign%20represents%20an%20object%20that%20wasn%27t%20"
							+ "converted%2C%20since%20it%20doesn%27t%20exist%20in%20this%20"
							+ "version%20of%20the%20game.");
				}
			}
			
			objectArray[i] = object;
		}
		
		toLevel.setAreaObjects(objectArray);
	}
	
	
	String getDataType(String data) {
		
		return data.substring(0,2);
	}
	
	
	boolean isValidType(String type) {
		
		if (type.length() != 2) {
			return false;
		}
		
		String[] validDataTypes = {"V2","C2","FL","BL","CL","IT","ST"};
		boolean isValid = false;
		
		for (String value:validDataTypes) {
			
			if (type.equals(value)) {
				isValid = true;
			}
		}
		
		return isValid;
	}
	
	
	double[] parseVector2D(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("V2")) {
			
			System.err.printf("Found invalid prefix while parsing Vector2D data type.%n"
					+ "Expected: V2 Got: %s%n",prefix);
			
			return null;
		}
		
		data = data.replace(prefix,"");
		
		double xValue = 0,yValue = 0;
		
		try {
			
			xValue = Double.parseDouble(data.substring(0,data.indexOf('x')));
			yValue = Double.parseDouble(data.substring(data.indexOf('x')+1));
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Vector2D data type. Details below:");
			e.printStackTrace();
			
			return null;
		}
		
		return new double[] {xValue,yValue};
	}
	
	
	int parseInteger(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("IT")) {
			
			System.err.printf("Found invalid prefix while parsing Integer data type.%n"
					+ "Expected: IT Got: %s%n",prefix);
			
			return 0;
		}
		
		data = data.replace(prefix,"");
		
		try {
			
			return Integer.parseInt(data);
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Integer data type. Details below:");
			e.printStackTrace();
			
			return 0;
		}
	}
	
	double parseFloat(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("FL")) {
			
			System.err.printf("Found invalid prefix while parsing Float data type.%n"
					+ "Expected: FL Got: %s%n",prefix);
			
			return 0;
		}
		
		data = data.replace(prefix,"");
		
		try {
			
			return Double.parseDouble(data);
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Float data type. Details below:");
			e.printStackTrace();
			
			return 0;
		}
	}
	
	boolean parseBoolean(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("BL")) {
			
			System.err.printf("Found invalid prefix while parsing Boolean data type.%n"
					+ "Expected: BL Got: %s%n",prefix);
			
			return false;
		}
		
		data = data.replace(prefix,"");
		
		if (data.equals("1")) {
			
			return true;
			
		} if (data.equals("0")) {
			
			return false;
		}
		
		System.err.printf("Invalid value for Boolean data type.%nExpected: 0 or 1 Got: %s%n"
				+ "Will set value to 0.%n",data);
		
		return false;
	}
	
	boolean areSameVectors(double[] vector1,double[] vector2) {
		
		if (vector1 == null && vector2 == null) {
			
			return true;
			
		} else if (vector1 == null || vector2 == null) {
			
			return false;
		}
		
		if (vector1.length != vector2.length) {
			
			return false;
		}
		
		for (int i = 0;i < vector1.length;i++) {
			
			if (vector1[i] != vector2[i]) {
				
				return false;
			}
		}
		
		return true;
	}
	
	String vector2DToString(double[] vector,boolean flattenValues) {
		
		Object[] parsedVector = new Object[vector.length];
		
		for (int i = 0;i < vector.length;i++) {
			
			if ((int) vector[i] == vector[i]) {
				
				parsedVector[i] = (int) vector[i];
			} else {
				parsedVector[i] = vector[i];
			}
		}
		
		if (flattenValues) {
			
			return new String("V2"+(int) vector[0]+"x"+(int) vector[1]);
		}
		
		return new String("V2"+parsedVector[0]+"x"+parsedVector[1]);
	}
	
	String booleanToString(boolean value) {
		
		return value? "BL1":"BL0";
	}
	
	class LevelCode {
		
		String levelData,codeVersion,levelName,emptyArray,areaData,musicData;
		int areaBackerBG,areaFronterBG,musicID,areaBGPallete;
		int[] tileTypeSeperationIndexes;
		double areaGravity;
		double[] areaDimensions;
		LevelTile[] groundTiles,foreGroundTiles,backGround0Tiles,backGround1Tiles,allTiles;
		LevelObject[] areaObjects;
		
		boolean areDoorsNormalized = false;
		
		LevelCode(String code) {
			
			setLevelData(code);
		}
		
		LevelCode(LevelCode base) {
			
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
		
		void setCodeVersion(String value) {
			
			codeVersion = new String(value);
		}
		
		String getLevelName() {
			
			return levelName;
		}
		
		void setLevelName(String value) {
			
			levelName = new String(value);
		}
		
		String getEmptyArray() {
			
			return emptyArray;
		}
		
		void setEmptyArray(String value) {
			
			emptyArray = new String(value);
		}
		
		String getAreaData() {
			
			return areaData;
		}
		
		void setAreaData(String value) {
			
			areaData = new String(value);
		}
		
		double[] getAreaDimensions() {
			
			return areaDimensions.clone();
		}
		
		void setAreaDimensions(double[] value) {
			
			areaDimensions = value.clone();
		}
		
		int getAreaBackerBG() {
			
			return Integer.valueOf(areaBackerBG);
		}
		
		void setAreaBackerBG(int value) {
			
			areaBackerBG = Integer.valueOf(value);
		}
		
		int getAreaFronterBG() {
			
			return Integer.valueOf(areaFronterBG);
		}
		
		void setAreaFronterBG(int value) {
			
			areaFronterBG = Integer.valueOf(value);
		}
		
		int getAreaBGPallete() {
			
			return Integer.valueOf(areaBGPallete);
		}
		
		void setAreaBGPallete(int value) {
			
			areaBGPallete = Integer.valueOf(value);
		}
		
		int getMusicID() {
			
			return Integer.valueOf(musicID);
		}
		
		void setMusicID(int value) {
			
			musicID = Integer.valueOf(value);
		}
		
		String getMusicData() {
			
			return musicData;
		}
		
		void setMusicData(String value) {
			
			if (value == null) {
				return;
			}
			musicData = new String(value);
		}
		
		double getAreaGravity() {
			
			return Double.valueOf(areaGravity);
		}
		
		void setAreaGravity(double value) {
			
			areaGravity = Double.valueOf(value);
		}
		
		LevelTile[] getGroundTiles() {
			
			return groundTiles.clone();
		}
		
		void setGroundTiles(LevelTile[] value) {
			
			groundTiles = value.clone();
		}
		
		LevelTile[] getForeGroundTiles() {
			
			return foreGroundTiles.clone();
		}
		
		void setForeGroundTiles(LevelTile[] value) {
			
			foreGroundTiles = value.clone();
		}
		
		LevelTile[] getBackGround0Tiles() {
			
			return backGround0Tiles.clone();
		}
		
		void setBackGround0Tiles(LevelTile[] value) {
			
			backGround0Tiles = value.clone();
		}
		
		LevelTile[] getBackGround1Tiles() {
			
			return backGround1Tiles.clone();
		}
		
		void setBackGround1Tiles(LevelTile[] value) {
			
			backGround1Tiles = value.clone();
		}
		
		LevelTile[] getAllTiles() {
			
			return allTiles.clone();
		}
		
		void setAllTiles(LevelTile[] value) {
			
			allTiles = value.clone();
		}
		
		LevelObject[] getAreaObjects() {
			
			return areaObjects.clone();
		}
		
		void setAreaObjects(LevelObject[] value) {
			
			areaObjects = value.clone();
		}
		
		/*
		 * ^ Getters and Setters ^
		 * 
		 * v Functional Methods v
		 */
		
		LevelTile[] parseTiles(String data) {
			
			LevelTile[] tiles;
			
			String[] tileGroups = data.split(",");
			
			tiles = new LevelTile[tileGroups.length];
			
			for (int i = 0;i < tileGroups.length;i++) {
				
				LevelTile tile = new LevelTile(tileGroups[i]);
				
				tiles[i] = tile;
			}
			
			return tiles;
		}
		
		LevelObject[] parseObjects(String data) throws Exception {
			
			LevelObject[] objects;
			
			data = data.replace('|','<');
			String[] objectStrings = data.split("<");
			
			
			objects = new LevelObject[objectStrings.length];
			
			for (int i = 0;i < objectStrings.length;i++) {
				
				LevelObject object = new LevelObject(objectStrings[i]);
				
				objects[i] = object;
			}
			
			return objects;
		}
		
		void addObject(LevelObject object) {
			
			LevelObject[] currentObjects = getAreaObjects();
			int length = currentObjects.length;
			LevelObject[] newObjects = new LevelObject[length+1];
			
			for (int i = 0;i < length;i++) {
				
				newObjects[i] = currentObjects[i];
			}
			
			newObjects[length] = object;
			
			setAreaObjects(newObjects);
		}
		
		LevelObject findMatchingPipe(LevelObject pipe,int indexFrom) {
			
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
		
		void normalizeDoors() throws Exception {
			
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
		
		void setupAllTiles() {
			
			setAllTiles(
					combineTileArrays(new LevelTile[][] {getGroundTiles(),getForeGroundTiles(),
				getBackGround0Tiles(),getBackGround1Tiles()}));
		}
		
		void seperateAllTilesArray() {
			
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
		
		void generateCode() {
			
			generateAreaData();
			
			setLevelData(codeVersion+","+
					levelName+","+
					emptyArray+","+
					areaData);
		}
		
		void generateAreaData() {
			
			setAreaData("["+vector2DToString(areaDimensions,true)+","+
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
	
	class LevelTile {
		
		int tileID,tilePallete,tileAmount;
		boolean hasPallete,noLength;
		
		LevelTile(String tileData) {
			
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
	
	class LevelObject {
		
		int objectID;
		String stringData;
		Object[] objectData;
		String[] dataTypes;
		
		LevelObject(String data) throws Exception {
			
			stringData = data;
			
			if (conversionType == ConversionType.OLD_TO_NEW) {
				
				// Add pallete of 0.
				data = data.substring(0,data.indexOf(',')+1)+"0,"+
						data.substring(data.indexOf(',')+1);
				
			}
			
			String[] splitData = data.split(",");
			
			objectData = new Object[splitData.length];
			dataTypes = new String[splitData.length];
			
			int startIndex = 2;
			
			objectID = Integer.parseInt(splitData[0]);
			objectData[0] = objectID;
			dataTypes[0] = "ID";
			
			objectData[1] = splitData[1];
			dataTypes[1] = "PL";
			
			for (int i = startIndex;i < splitData.length;i++) {
				
				String dataType = getDataType(splitData[i]);
				
				boolean isValid = isValidType(dataType);
				
				// Special case for On/Off Switch Controlled Moving Platform.
				if (objectID == 94 && dataType.equals("Nu")) {
					
					isValid = true;
				}
				
				if (!isValid) {
					
					throw new Exception("Invalid data type found while parsing an object!\n"
							+ "Value of: "+dataType+" is not a recognized data type.");
				}
				
				dataTypes[i] = dataType;
				
				switch (dataType) {
				case "V2":
					
					objectData[i] = parseVector2D(splitData[i]);
					break;
					
				case "C2","CL","ST":
					
					objectData[i] = splitData[i];
					break;
					
				case "FL":
					
					objectData[i] = parseFloat(splitData[i]);
					break;
					
				case "BL":
					
					objectData[i] = parseBoolean(splitData[i]);
					break;
					
				case "IT":
					
					objectData[i] = parseInteger(splitData[i]);
					break;
					
				case "Nu":
					
					dataTypes[i] = "BL";
					objectData[i] = false;
				}
			}
		}
		
		void removeLastValue() {
			
			objectData = expandObjectArray(objectData,-1);
			dataTypes = expandStringArray(dataTypes,-1);
		}
		
		/**
		 * <dl>
		 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
		 * method works exclusively on Object arrays.</dd><hr>
		 * 
		 * @param array		The array to expand/shrink.
		 * @param amount	The amount to expand the array by. Negative values will shrink the
		 * 					array.
		 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
		 * 			shrunk arrays may lose data at removed indexes.
		 */
		Object[] expandObjectArray(Object[] array,int amount) {
			
			// Null check.
			if (array == null) {
				array = new Object[0];
			}
			
			// Making sure the amount to shrink by doesn't exceed the current array length.
			if (array.length+amount < 0) {
				amount = array.length*-1;
			}
			
			// Create a new array with increased/decreased capacity.
			Object[] newArray = new Object[array.length+amount];
			
			// Copy values from the old array to the new array.
			for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
				newArray[i] = array[i];
			}
			
			// Return the new expanded/shrunk array.
			return newArray.clone();
		}
		
		public boolean equals(Object o) {
			
			if (o == null) {
				return false;
			}
			if (!(o instanceof LevelObject)) {
				return false;
			}
			
			LevelObject l = (LevelObject) o;
			
			if (!stringData.equals(l.stringData)) {
				return false;
			}
			
			return true;
		}
		
		public String toString() {
			
			String output = "";
			
			for (int i = 0;i < objectData.length;i++) {
				
				if (dataTypes[i].equals("V2")) {
					
					output += vector2DToString(
							(double[]) objectData[i],false)+",";
					
				} else if (dataTypes[i].equals("PL")) {
					
					if (conversionType == ConversionType.OLD_TO_NEW) {
						
						output += objectData[i]+",";
					}
					
				} else if (dataTypes[i].equals("ID") || dataTypes[i].equals("ST") ||
						dataTypes[i].equals("CL") || dataTypes[i].equals("C2")) {
					
					output += objectData[i]+",";
					
				} else if (dataTypes[i].equals("BL")) {
					
					output += dataTypes[i]+((boolean) objectData[i]? "1,":"0,");
					
				} else {
					
					output += dataTypes[i]+objectData[i]+",";
				}
			}
			
			output = output.substring(0,output.length()-1);
			
			return output;
		}
	}
}
