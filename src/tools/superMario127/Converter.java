package tools.superMario127;

import static java.nio.file.StandardOpenOption.READ;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

import level.*;
import objects.*;
import tiles.*;
import util.Utility;

public class Converter {
	
	public enum ConversionType {OLD_TO_NEW(1),NEW_TO_OLD(2);
		
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
			
			fileNames = Utility.expandStringArray(fileNames,1);
			fileNames[fileNames.length-1] = file.toString();
			
			System.out.printf("Found file for conversion: %s%n",file.toString());
		}
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
			if (object instanceof DoorObject) {
				
				object = new DoorObject(""+object.objectID+","+
						Utility.vector2DToString(
								(double[]) object.objectData[2],false)+","+
						Utility.vector2DToString(
								(double[]) object.objectData[3],false)+","+
						object.dataTypes[4]+object.objectData[4]+","+
						Utility.booleanToString((boolean) object.objectData[5])+","+
						Utility.booleanToString((boolean) object.objectData[6])+","+
						"IT0,"+object.objectData[8]+",BL0");
			}
			
			toLevel.addObject(object);
		}
		
		String newFileName = file.substring(0,file.length()-4)+
				" [Converted to V"+conversionType.gameVersionTo+"].txt";
		
		saveLevelCode(toLevel,newFileName);
		
		System.out.printf("Finished converting level data!%nYou can find the converted level"
				+ " in the output folder with the filename: %s%n",newFileName);
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
			
			double[] areaDimensions = Utility.parseVector2D(dimensionData);
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
			
			int areaBackerBG = Utility.parseInteger(temp);
			
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
			
			int areaFronterBG = Utility.parseInteger(temp);
			
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
			
			String musicDataType = Utility.getDataType(temp),musicData = null;
			
			if (!(musicDataType.equals("ST") || musicDataType.equals("IT"))) {
				
				System.out.println("Unkown music data structure. Setting to default value.");
				musicID = 1;
				
				System.out.println("Area Music ID: "+musicID+"\n");
				
				fromLevel.setMusicID(musicID);
				
			} else if (musicDataType.equals("IT")) {
				
				musicID = Utility.parseInteger(temp);
				
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
			
			double areaGravity = Utility.parseFloat(temp);
			
			System.out.println("Area Gravity: "+areaGravity+"\n");
			
			fromLevel.setAreaGravity(areaGravity);
			
			if (conversionType == ConversionType.NEW_TO_OLD) {
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf('~');
				
				temp = areaData.substring(currentIndex,nextIndex);
				
				System.out.println("Area BG Pallete Data: "+temp);
				
				int areaBGPallete = Utility.parseInteger(temp);
				
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
					
				} else if (tile instanceof YIGrassTile) {
					
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
					
				} else if (tile instanceof NewCaveTile) {
					
					
					if (tile.tileID > 351) {
						
						// Change ID to Old Cave half-tile.
						tile.tileID = OldCaveTile.getTileID(tile.tileID,true)+1;
						
					} else {
						
						// Change ID to Old Cave half-tile.
						tile.tileID = OldCaveTile.getTileID(tile.tileID,true);
					}
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
			
			if (object instanceof MetalPlatformObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new MetalPlatformObject(object.stringData+",CL1x1x1");
					
				} else {
					
					object = new MetalPlatformObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object instanceof MushroomTopObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new MushroomTopObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new MushroomTopObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object instanceof SignObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new SignObject(object.stringData+",BL0,BL0");
					
				} else {
					
					object = new SignObject(object.stringData.substring(0,
						object.stringData.length()-",BL0,BL0".length()));
				}
				
			} else if (object instanceof TwistedTreeTopObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new TwistedTreeTopObject(object.stringData+",CL0.97x0.5x0.16");
					
				} else {
					
					object = new TwistedTreeTopObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object instanceof WarpPipeTopObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					double[] pipeDestinationCoordinates = (double[]) object.objectData[8],
							pipeCoordinates = (double[]) object.objectData[2];
					
					object = new WarpPipeTopObject(""+object.objectID+","+
							Utility.vector2DToString(
									(double[]) object.objectData[2],false)+","+
							Utility.vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							Utility.booleanToString((boolean) object.objectData[5])+","+
							Utility.booleanToString((boolean) object.objectData[6])+","+
							object.dataTypes[7]+object.objectData[7]+","+
							"STWarpPipe"+numberOfWarpPipes+","+
							"CL0x1x0,"+"BL1");
					
					if (!Utility.areSameVectors(pipeCoordinates,pipeDestinationCoordinates)) {
						
						LevelObject destinationPipe = new WarpPipeTopObject(object.stringData);
						
						destinationPipe.objectData[2] = new double[] {
								pipeDestinationCoordinates[0],
								pipeDestinationCoordinates[1]
						};
						
						destinationPipe.setConversionType(conversionType);
						
						postConversionAdditions.add(destinationPipe);
					}
				} else {
					
					LevelObject destinationPipe = toLevel.findMatchingPipe(object,i);
					
					object = new WarpPipeTopObject(""+object.objectID+",0,"+
							Utility.vector2DToString(
									(double[]) object.objectData[2],false)+","+
							Utility.vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							Utility.booleanToString((boolean) object.objectData[5])+","+
							Utility.booleanToString((boolean) object.objectData[6])+","+
							object.dataTypes[7]+object.objectData[7]+","+
							(destinationPipe == null?
							Utility.vector2DToString((double[]) object.objectData[2],false):
							Utility.vector2DToString((double[]) destinationPipe.objectData[2],
									false))+
							",BL1");
				}
				
				numberOfWarpPipes++;
				
			} else if (object instanceof GoombaObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new GoombaObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new GoombaObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object instanceof DoorObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					toLevel.normalizeDoors();
					
					object = new DoorObject(""+object.objectID+","+
							Utility.vector2DToString(
									(double[]) object.objectData[2],false)+","+
							Utility.vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							Utility.booleanToString((boolean) object.objectData[5])+","+
							Utility.booleanToString((boolean) object.objectData[6])+","+
							"IT0,"+object.objectData[8]+",BL0");
				} else {
					
					object = new DoorObject(""+object.objectID+",0,"+
							Utility.vector2DToString(
									(double[]) object.objectData[2],false)+","+
							Utility.vector2DToString(
									(double[]) object.objectData[3],false)+","+
							object.dataTypes[4]+object.objectData[4]+","+
							Utility.booleanToString((boolean) object.objectData[5])+","+
							Utility.booleanToString((boolean) object.objectData[6])+","+
							object.objectData[8]+","+
							object.objectData[8]);
				}
				
			} else if (object instanceof ArrowObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new ArrowObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new ArrowObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object instanceof WoodenPlatformObject) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					object = new WoodenPlatformObject(object.stringData+",CL1x0x0");
					
				} else {
					
					object = new WoodenPlatformObject(object.stringData.substring(
							0,object.stringData.indexOf("CL")-1));
				}
				
			} else if (object.objectID > 69) {
				
				object = new LevelObject("14,"+object.objectData[1]+","+
						Utility.vector2DToString(
								(double[]) object.objectData[2],false)+","+
						Utility.vector2DToString(
								(double[]) object.objectData[3],false)+","+
						object.dataTypes[4]+object.objectData[4]+","+
						Utility.booleanToString((boolean) object.objectData[5])+","+
						Utility.booleanToString((boolean) object.objectData[6])+","+
						"STThis%20sign%20represents%20an%20object%20that%20wasn%27t%20"
						+ "converted%2C%20since%20it%20doesn%27t%20exist%20in%20this%20"
						+ "version%20of%20the%20game.");
			}
			
			object.setConversionType(conversionType);
			
			objectArray[i] = object;
		}
		
		toLevel.setAreaObjects(objectArray);
	}
	
	ConversionType getConversionType() {
		
		return conversionType;
	}
	
}
