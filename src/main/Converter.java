package main;

import static java.nio.file.StandardOpenOption.*;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

import level.*;
import main.ProgramLogger.LogType;
import objects.*;
import tiles.*;
import util.Utility;

public class Converter {
	
	String directory;
	Path directoryPath,inputDirectory,outputDirectory,logDirectory;
	String[] fileNames;
	ConversionType conversionType;
	LevelCode fromLevel,toLevel;
	
	FileHandler fileHandler = FileHandler.getInstance();
	
	int numberOfWarpPipes = 0;
	ArrayList<LevelObject> postConversionAdditions = new ArrayList<LevelObject>();
	
	static boolean isDebug;
	
	Scanner input = new Scanner(System.in);
	
	Converter() {
		
		// For debugging or troubleshooting, uncomment the below line.
		// isDebug = true;
		
		String workingDirectory = System.getProperty("user.dir");
		
		directoryPath = Paths.get(workingDirectory);
		
		inputDirectory = directoryPath.resolve("input");
		outputDirectory = directoryPath.resolve("output");
		logDirectory = directoryPath.resolve("logs");
		
		ProgramLogger.setLogDirectory(logDirectory);
		
		ProgramLogger.checkForLogFile();
		
		fileHandler.setupDirectories(new Path[] {inputDirectory,outputDirectory});
		
		getfileNames(inputDirectory.toFile());
		
		if (fileNames == null) {
			
			ProgramLogger.logMessage("Couldn't find any text files to convert!\n"
					+ "Did you remember to put them into the 'input' folder?",LogType.ERROR);
			
			ProgramLogger.logMessage("Quitting program.",LogType.INFO);
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
			
			ProgramLogger.logMessage("Found file for conversion: "+file.toString(),
					LogType.INFO);
		}
	}
	
	void start() throws Exception {
		
		for (String file:fileNames) {
			
			conversionType = getConversionType(file);
			
			convertFile(file);
		}
		
		
		System.out.print("\n\nFinished converting files. Press enter to quit...");
		input.nextLine();		
	}

	public static void main(String[] args) {
		
		new Converter();
	}
	
	void saveLevelCode(LevelCode code,String fileName) {
		
		code.generateCode();
		
		String levelData = code.levelData;
		
		Path outputFile = outputDirectory.resolve(fileName);
		
		// Write the level data to file.
		fileHandler.writeToFile(levelData,outputFile,new OpenOption[] {CREATE,
				TRUNCATE_EXISTING,WRITE});
	}
	
	ConversionType getConversionType(String file) {
		
		String codeVersion = getCodeVersion(file);
		
		ConversionType temp = new ConversionType(false,codeVersion,codeVersion);
		
		System.out.println();
		
		System.out.printf("Please enter which game version you want to convert to for file:"
				+ " %s%n",file);
		ProgramLogger.logMessage("Level was created using game version: "+
				temp.getGameVersionFrom(),LogType.INFO);
		System.out.println();
		
		
		System.out.print("Enter game version: ");
		
		String tempInput;
		
		tempInput = input.nextLine();
		
		ConversionType conversion = new ConversionType(true,temp.gameVersionFrom,tempInput);
		
		while (!conversion.isValid ||
				conversion.areSameGameVersions(conversion.gameVersionFrom,conversion.gameVersionTo)) {
			
			System.out.println();
			
			if (!conversion.isValid) {
				
				ProgramLogger.logMessage("Invalid game version entered!",LogType.ERROR);
				
			} else {
				
				ProgramLogger.logMessage("The level was made in the game version specified! "
						+ "Please choose a different game version.",LogType.ERROR);
			}
			
			System.out.printf("%nPlease enter which game version you want to convert to for"
					+ " file: %s%nValid game versions: %s%n%n",file,
					temp.listValidGameVersions());
			
			System.out.print("Enter game version: ");
			
			tempInput = input.nextLine();
			
			conversion = new ConversionType(true,temp.gameVersionFrom,tempInput);
		}
		
		return conversion;
	}
	
	void convertFile(String file) throws Exception {
		
		System.out.println();
		ProgramLogger.logMessage("Checking code sturcture...",LogType.INFO);
		System.out.println();
		
		boolean isValid = checkCodeStructure(file);
		
		ProgramLogger.logMessage("Finished checking code structure.",LogType.INFO);
		System.out.println();
		
		if (!isValid) {
			
			ProgramLogger.logMessage("Code structure for file: "+file+" does not match known "
					+ "code structure for game version: "+conversionType.gameVersionFrom,
					LogType.ERROR);
			System.out.println();
			return;
		}
		
		ProgramLogger.logMessage("Converting level data...",LogType.INFO);
		System.out.println();
		
		// Copying all data to the new level.
		toLevel = new LevelCode(fromLevel);
		
		toLevel.setConversionType(conversionType);
		toLevel.setPostConversionAdditions(postConversionAdditions);
		
		// Updating code version.
		toLevel.setCodeVersion(conversionType.codeVersionTo);
		
		ProgramLogger.logMessage("Converting Backgrounds...",LogType.INFO);
		// Area Backer BG Conversion.
		convertBackerBG();
		
		// Area Fronter BG Conversion.
		convertFronterBG();
		
		ProgramLogger.logMessage("Converting Music...",LogType.INFO);
		// Area Music Conversion.
		convertMusicIDs();
		
		ProgramLogger.logMessage("Converting Tiles...",LogType.INFO);
		// Tile Conversions.
		convertTiles();
		
		ProgramLogger.logMessage("Converting Objects...",LogType.INFO);
		// Object Conversions.
		convertObjects();
		
		System.out.println();
		
		for (LevelObject object:postConversionAdditions) {
			
			// Post-conversion additions need to actually be converted as well.
			if (object instanceof DoorObject) {
				
				object = new DoorObject(""+object.objectID+","+
						Utility.vector2DToString(
								(double[]) object.objectData[2].getValue(),false)+","+
						Utility.vector2DToString(
								(double[]) object.objectData[3].getValue(),false)+","+
						object.objectData[4].getType()+object.objectData[4].getValue()+","+
						Utility.booleanToString((boolean) object.objectData[5].getValue())+","+
						Utility.booleanToString((boolean) object.objectData[6].getValue())+","+
						"IT0,"+object.objectData[8]+",BL0",conversionType);
			}
			
			toLevel.addObject(object);
		}
		
		String newFileName = file.substring(0,file.length()-4)+
				" [Converted to V"+conversionType.gameVersionTo+"].txt";
		
		saveLevelCode(toLevel,newFileName);
		
		ProgramLogger.logMessage("Finished converting level data!",LogType.INFO);
		ProgramLogger.logMessage("You can find the converted level in the output folder with "
				+ "the filename: "+newFileName,LogType.INFO);
	}
	
	boolean checkCodeStructure(String fileName) {
		
		
		int currentIndex = 0,nextIndex = 0;
		
		int musicID = 0;
		
		Path file = inputDirectory.resolve(fileName);
		
		String levelData = fileHandler.readFromFile(file);
		
		if (levelData == null) {
			
			ProgramLogger.logMessage("Cannot determine if code structure is valid, so file "
					+ "will be skipped.",LogType.WARNING);
			System.out.println();
			
			return false;
		}
		
		try {
			
			int numberOfBrackets = 0,index = 0;
			
			while (levelData.substring(index,levelData.length()).indexOf('[') != -1) {
				
				index += levelData.substring(index,levelData.length()).indexOf('[')+1;
				numberOfBrackets++;
			}
			
			if (numberOfBrackets > 2) {
				
				ProgramLogger.logMessage("Level code has multiple areas. Multiple areas are"
						+ " not supported by this converter yet. File will be skipped.",
						LogType.WARNING);
				System.out.println();
				return false;
			}
			
			fromLevel = new LevelCode(levelData);
			
			fromLevel.setConversionType(conversionType);
			
			ProgramLogger.logMessage("Level Code: "+levelData+"\n",LogType.DEBUG);
			
			nextIndex = levelData.indexOf(',');
			String codeVersion = levelData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Level Code Version: "+codeVersion,LogType.INFO);
			
			if (!codeVersion.equals(conversionType.codeVersionFrom)) {
				
				ProgramLogger.logMessage("Invalid level code version! Expected: "+
						conversionType.codeVersionFrom+" Got: "+codeVersion,LogType.ERROR);
				
				return false;
			}
			System.out.println();
			
			fromLevel.setCodeVersion(codeVersion);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			nextIndex = levelData.indexOf(',');
			
			String levelName = levelData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Level Name: "+levelName,LogType.INFO);
			System.out.println();
			
			fromLevel.setLevelName(levelName);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			nextIndex = levelData.indexOf(',');
			
			String emptyArray = levelData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Empty Array: "+emptyArray+"\n",LogType.DEBUG);
			
			if (!emptyArray.equals("[]")) {
				
				ProgramLogger.logMessage("Couldn't find empty array in level code. Adding one"
						+ " where it would be expected.",LogType.WARNING);
				System.out.println();
				
				emptyArray = "[]";
			}
			
			fromLevel.setEmptyArray(emptyArray);
			
			levelData = levelData.substring(nextIndex+1,levelData.length());
			
			String areaData = levelData.substring(currentIndex,levelData.length());
			
			ProgramLogger.logMessage("Area Data: "+areaData+"\n",LogType.DEBUG);
			
			if (!(areaData.startsWith("[") && areaData.endsWith("]"))) {
				
				ProgramLogger.logMessage("Invalid structure for area data detected.",
						LogType.ERROR);
				System.out.println();
				return false;
			}
			
			fromLevel.setAreaData(areaData);
			
			nextIndex = areaData.indexOf(',');
			
			String dimensionData = areaData.substring(currentIndex+1,nextIndex);
			
			ProgramLogger.logMessage("Area Dimension data: "+dimensionData,LogType.INFO);
			
			double[] areaDimensions = Utility.parseVector2D(dimensionData);
			double[] minimumDimensions = {24,14};
			double[] defaultDimensions = {80,30};
			
			if (areaDimensions == null) {
				
				ProgramLogger.logMessage("Area dimensions are null!",LogType.WARNING);
				
				areaDimensions = defaultDimensions.clone();
				ProgramLogger.logMessage("Setting level dimensions to default values.",
						LogType.INFO);
				
			} else {
				
				if (areaDimensions[0] < minimumDimensions[0] ||
						areaDimensions[1] < minimumDimensions[1]) {
					
					ProgramLogger.logMessage("Area dimensions are too small! Clamping low "
							+ "values.",LogType.WARNING);
					
					areaDimensions[0] = Math.max(areaDimensions[0],minimumDimensions[0]);
					areaDimensions[1] = Math.max(areaDimensions[1],minimumDimensions[1]);
				}
			}
			
			String dimensions = Utility.vector2DToString(areaDimensions,true);
			dimensions = dimensions.replace("x",",");
			dimensions = dimensions.substring(2);
			
			ProgramLogger.logMessage("Area Dimensions: "+dimensions,LogType.INFO);
			System.out.println();
			
			fromLevel.setAreaDimensions(areaDimensions);
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			String temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area backer BG Data: "+temp,LogType.INFO);
			
			int areaBackerBG = Utility.parseInteger(temp);
			
			if (areaBackerBG < conversionType.minBackerBG[0] ||
					areaBackerBG > conversionType.maxBackerBG[0]) {
				
				ProgramLogger.logMessage("Area backer BG value outside of expected range! "
						+ "Clamping value.",LogType.WARNING);
				
				areaBackerBG = Math.min(areaBackerBG,conversionType.maxBackerBG[0]);
				areaBackerBG = Math.max(areaBackerBG,conversionType.minBackerBG[0]);
				
			} else if (areaBackerBG == conversionType.maxBackerBG[0]) {
				
//				ProgramLogger.logMessage("Silhouette Mode detected.",LogType.INFO);
			}
			
			ProgramLogger.logMessage("Area backer BG: "+areaBackerBG,LogType.INFO);
			System.out.println();
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area fronter BG Data: "+temp,LogType.INFO);
			
			int areaFronterBG = Utility.parseInteger(temp);
			
			if (areaFronterBG < conversionType.minFronterBG[0] ||
					areaFronterBG > conversionType.maxFronterBG[0]) {
				
				ProgramLogger.logMessage("Area fronter BG value outside of expected range! "
						+ "Clamping value.",LogType.WARNING);
				
				areaFronterBG = Math.min(areaFronterBG,conversionType.maxFronterBG[0]);
				areaFronterBG = Math.max(areaFronterBG,conversionType.minFronterBG[0]);
			}
			
			ProgramLogger.logMessage("Area fronter BG: "+areaFronterBG,LogType.INFO);
			System.out.println();
			
			fromLevel.setAreaBackerBG(areaBackerBG);
			fromLevel.setAreaFronterBG(areaFronterBG);
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area Music Data: "+temp,LogType.INFO);
			
			String musicDataType = Utility.getDataType(temp),musicData = null;
			
			if (!(musicDataType.equals("ST") || musicDataType.equals("IT"))) {
				
				ProgramLogger.logMessage("Unkown music data structure! Setting to default"
						+ " value.",LogType.WARNING);
				musicID = 1;
				
				ProgramLogger.logMessage("Area Music ID: "+musicID,LogType.INFO);
				System.out.println();
				
				fromLevel.setMusicID(musicID);
				
			} else if (musicDataType.equals("IT")) {
				
				musicID = Utility.parseInteger(temp);
				
				if (musicID < conversionType.minMusicID[0] ||
						musicID > conversionType.maxMusicID[0]) {
					
					ProgramLogger.logMessage("Music ID outside of expected range! "
							+ "Clamping value.",LogType.WARNING);
					
					musicID = Math.min(musicID,conversionType.maxMusicID[0]);
					musicID = Math.max(musicID,conversionType.minMusicID[0]);
				}
				
				ProgramLogger.logMessage("Area Music ID: "+musicID,LogType.INFO);
				System.out.println();
				
				fromLevel.setMusicID(musicID);
				
			} else {
				
				musicData = new String(temp);
				
				System.out.println();
				
				fromLevel.setMusicData(musicData);
			}
			
			switch (conversionType.codeVersionFrom) {
			case "0.4.5":
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf('~');
				break;
				
			case "0.4.9":
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf(',');
				break;
			}
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area Gravity Data: "+temp,LogType.INFO);
			
			double areaGravity = Utility.parseFloat(temp);
			
			ProgramLogger.logMessage("Area Gravity: "+areaGravity,LogType.INFO);
			System.out.println();
			
			fromLevel.setAreaGravity(areaGravity);
			
			if (conversionType.codeVersionFrom == "0.4.9") {
				
				areaData = areaData.substring(nextIndex+1,areaData.length());
				nextIndex = areaData.indexOf('~');
				
				temp = areaData.substring(currentIndex,nextIndex);
				
				ProgramLogger.logMessage("Area BG Pallete Data: "+temp,LogType.INFO);
				
				int areaBGPallete = Utility.parseInteger(temp);
				
				ProgramLogger.logMessage("Area BG Pallete: "+areaBGPallete,LogType.INFO);
				System.out.println();
				
				fromLevel.setAreaBGPallete(areaBGPallete);
			}
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area Ground Tile Data: "+temp+"\n",LogType.DEBUG);
			
			fromLevel.setGroundTiles(fromLevel.parseTiles(temp));
			
			ProgramLogger.logMessage("Number of Ground tile groups in level: "+
					fromLevel.getGroundTiles().length,LogType.INFO);
			System.out.println();
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// ForeGround Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area ForeGround Tile Data: "+temp+"\n",LogType.DEBUG);
			
			fromLevel.setForeGroundTiles(fromLevel.parseTiles(temp));
			
			ProgramLogger.logMessage("Number of ForeGround tile groups in level: "+
					fromLevel.getForeGroundTiles().length,LogType.INFO);
			System.out.println();
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// BackGround0 Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area BackGround0 Tile Data: "+temp+"\n",LogType.DEBUG);
			
			fromLevel.setBackGround0Tiles(fromLevel.parseTiles(temp));
			
			ProgramLogger.logMessage("Number of BackGround0 tile groups in level: "+
					fromLevel.getBackGround0Tiles().length,LogType.INFO);
			System.out.println();
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			// BackGround1 Tiles.
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area BackGround1 Tile Data: "+temp+"\n",LogType.DEBUG);
			
			fromLevel.setBackGround1Tiles(fromLevel.parseTiles(temp));
			
			ProgramLogger.logMessage("Number of BackGround1 tile groups in level: "+
					fromLevel.getBackGround1Tiles().length,LogType.INFO);
			System.out.println();
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.length()-1;
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area Objects Data: "+temp+"\n",LogType.DEBUG);
			
			fromLevel.setAreaObjects(fromLevel.parseObjects(temp));
			
			ProgramLogger.logMessage("Number of objects in level: "+
					fromLevel.getAreaObjects().length,LogType.INFO);
			System.out.println();
			
		} catch (Exception e) {
			
			ProgramLogger.logMessage("Invalid code structure detected. Detailed error "
					+ "below:",LogType.ERROR);
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	void convertBackerBG() {
		
		if (fromLevel.getAreaBackerBG() == conversionType.maxBackerBG[0]) {
			
			toLevel.setAreaBackerBG(conversionType.maxBackerBG[1]);
			
		} else if (fromLevel.getAreaBackerBG() > conversionType.maxBackerBG[1]) {
			
			toLevel.setAreaBackerBG(conversionType.maxBackerBG[1]-1);
		}
	}
	
	void convertFronterBG() {
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			if (fromLevel.getAreaFronterBG() == 1 && fromLevel.getAreaBGPallete() == 1) {
				
				toLevel.setAreaFronterBG(Math.min(2,conversionType.maxFronterBG[1]));
			}
			
			toLevel.setAreaBGPallete(0);
			
			switch (fromLevel.getAreaFronterBG()) {
			case 7,8,10,12,13:
				
				toLevel.setAreaFronterBG(Math.min(1,conversionType.maxFronterBG[1]));
				break;
				
			case 9:
				
				toLevel.setAreaFronterBG(Math.min(3,conversionType.maxFronterBG[1]));
				break;
				
			case 11:
				
				toLevel.setAreaFronterBG(Math.min(2,conversionType.maxFronterBG[1]));
				break;
			}
		}
		
		if (toLevel.getAreaFronterBG() == conversionType.maxFronterBG[0]) {
			
			toLevel.setAreaFronterBG(conversionType.maxFronterBG[1]);
			
		} else if (toLevel.getAreaFronterBG() > conversionType.maxFronterBG[1]) {
			
			toLevel.setAreaFronterBG(conversionType.maxFronterBG[1]-1);
		}
	}
	
	void convertMusicIDs() {
		
		if (fromLevel.getMusicID() == conversionType.maxMusicID[0]) {
			
			toLevel.setMusicID(conversionType.maxMusicID[1]);
		}
		
		switch (conversionType.gameVersionFrom) {
		
		case "0.7.2":
			
			// This is the same as MusicID 1 in all other versions.
			if (fromLevel.getMusicID() == 65) {
				
				toLevel.setMusicID(1);
			}
			
		case "0.7.1":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.7.0":
				
				switch(fromLevel.getMusicID()) {
				
				// Yoshi's Island - Underground (Remix)
				case 62:
					
					// Set to Underground Yoshi's New Island
					toLevel.setMusicID(34);
					break;
				
				// Waltz of the Boos (Remix)
				case 63:
					
					// Set to Waltz of the Boos Super Mario Galaxy
					toLevel.setMusicID(56);
					break;
				
				// Super Mario Sunshine - Sky & Sea
				case 64:
					
					// Set to Deep Sea of Mare Super Mario Sunshine
					toLevel.setMusicID(41);
				}
			
			}
			
		case "0.7.0":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.6.1":
				
				switch(fromLevel.getMusicID()) {
				
				// Ice Ice Outpost
				case 39:
					
					// Set to Snow Rise Paper Mario: Sticker Star
					toLevel.setMusicID(12);
					break;
				
				// Water themes
				case 40,41,42,43,64:
					
					// Set to Buoy Base Galaxy Super Mario Galaxy
					toLevel.setMusicID(9);
					break;
					
				// Desert themes, Waltz of the Boos (Remix)
				case 44,45,46,47,48,63:
					
					// Set to Princess Peach's Castle Super Smash Bros. Melee
					toLevel.setMusicID(1);
					break;
					
				// Beach themes
				case 49,50,51:
					
					// Set to Yoshi Star Galaxy Super Mario Galaxy 2
					toLevel.setMusicID(16);
					break;
					
				// Fire themes
				case 52,53,54,55:
					
					// Set to Speedy Comet Super Mario Galaxy
					toLevel.setMusicID(8);
					break;
					
				// Ghost themes
				case 56,57,58,59:
					
					// Set to SMW Underground Super Mario Maker
					toLevel.setMusicID(32);
					break;
					
				// Secret Course
				case 60:
					
					// Set to Mario's Pwnd Slide Remix by m477zorz
					toLevel.setMusicID(5);
					break;
					
				// Sammer's Kingdom
				case 61:
					
					// Set to Champion's Road Super Mario 3D World
					toLevel.setMusicID(7);
					break;
				
				// Yoshi's Island - Underground (Remix)
				case 62:
					
					// Set to Underground Yoshi's New Island
					toLevel.setMusicID(34);
					break;
				}
			}
			
		case "0.6.1":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.6.0":
				
				switch(fromLevel.getMusicID()) {
				
				// Ice Ice Outpost
				case 39:
					
					// Set to Snow Rise Paper Mario: Sticker Star
					toLevel.setMusicID(12);
					break;
				
				// Water themes
				case 40,41,42,43,64:
					
					// Set to Buoy Base Galaxy Super Mario Galaxy
					toLevel.setMusicID(9);
					break;
					
				// Desert themes, Waltz of the Boos (Remix)
				case 44,45,46,47,48,63:
					
					// Set to Princess Peach's Castle Super Smash Bros. Melee
					toLevel.setMusicID(1);
					break;
					
				// Beach themes
				case 49,50,51:
					
					// Set to Yoshi Star Galaxy Super Mario Galaxy 2
					toLevel.setMusicID(16);
					break;
					
				// Fire themes
				case 52,53,54,55:
					
					// Set to Speedy Comet Super Mario Galaxy
					toLevel.setMusicID(8);
					break;
					
				// Ghost themes
				case 56,57,58,59:
					
					// Set to SMW Underground Super Mario Maker
					toLevel.setMusicID(32);
					break;
					
				// Secret Course
				case 60:
					
					// Set to Mario's Pwnd Slide Remix by m477zorz
					toLevel.setMusicID(5);
					break;
					
				// Sammer's Kingdom
				case 61:
					
					// Set to Champion's Road Super Mario 3D World
					toLevel.setMusicID(7);
					break;
				
				// Yoshi's Island - Underground (Remix)
				case 62:
					
					// Set to Underground Yoshi's New Island
					toLevel.setMusicID(34);
					break;
				}
			}
		}
		
	}
	
	void convertTiles() {
		
		// Combine all the tiles into one large array.
		toLevel.setupAllTiles();
		
		LevelTile[] tileArray = toLevel.getAllTiles();
		
		int arrayLength = tileArray.length;
		
		for (int i = 0;i < arrayLength;i++) {
			
			LevelTile tile = tileArray[i];
			
			switch(conversionType.gameVersionFrom) {
			
			case "0.7.2","0.7.1":
				
				if (Utility.versionGreaterThanVersion("0.7.1",conversionType.gameVersionTo)) {
					
					if (tile instanceof NewCaveTile) {
						
						if (tile.tileID > 351) {
							
							// Change ID to Old Cave half-tile.
							tile.tileID = OldCaveTile.getTileID(tile.tileID,true)+1;
							
						} else {
							
							// Change ID to Old Cave full-tile.
							tile.tileID = OldCaveTile.getTileID(tile.tileID,true);
						}
					}
				}
				
			case "0.7.0":
				
				if (Utility.versionGreaterThanVersion("0.7.0",conversionType.gameVersionTo)) {
					
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
						
					}
				}
				
			case "0.6.1":
				
				if (Utility.versionGreaterThanVersion("0.6.1",conversionType.gameVersionTo)) {
					
					if (tile instanceof SolidColourTile) {
						
						// Change ID to Old Cave full-tile.
						tile.tileID = OldCaveTile.getTileID(tile.tileID,true);
						
					} else if (tile instanceof CheckerBoardTile || tile instanceof CastleBrickTile) {
						
						// Change ID to Light Stone Brick Block.
						tile.tileID = LightStoneBrickTile.getTileID(tile.tileID,true);
						
					} else if (tile instanceof CarpetBlockTile) {
						
						// Change ID to Red Gem Block.
						tile.tileID = ColouredGemTile.getTileID(tile.tileID,true);
						
					} else if (tile instanceof WoodenPlankTile) {
						
						// Change ID to Wooden Cabin Block.
						tile.tileID = WoodenCabinTile.getTileID(tile.tileID,true);
						
					}
				}
			}
			
			tile.tilePallete = 0;
			tile.hasPallete = false;
			
			tileArray[i] = tile;
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
			
			switch (conversionType.gameVersionFrom) {
			
			case "0.7.2","0.7.1":
				
				if (Utility.versionGreaterThanVersion("0.7.1",conversionType.gameVersionTo)) {
					
					if (object instanceof MetalPlatformObject) {
						
						object = new MetalPlatformObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof TwistedTreeTopObject) {
						
						object = new TwistedTreeTopObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						object = new WarpPipeTopObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								object.objectData[7].toString()+","+
								object.objectData[8].toString()+","+
								object.objectData[9].toString(),
								conversionType);
						
					} else if (object instanceof GoombaObject) {
						
						object = new GoombaObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof DoorObject) {
						
						object = new DoorObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								object.objectData[7].toString()+","+
								object.objectData[8].toString(),conversionType);
						
					} else if (object instanceof WoodenPlatformObject) {
						
						object = new WoodenPlatformObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					}
					
				}
			
			case "0.7.0":
				
				if (Utility.versionGreaterThanVersion(conversionType.gameVersionTo,"0.7.0")) {
					
					if (object instanceof MetalPlatformObject) {
						
						object = new MetalPlatformObject(object.stringData+",CL1x1x1",
								conversionType);
						
					} else if (object instanceof TwistedTreeTopObject) {
						
						object = new TwistedTreeTopObject(object.stringData+",CL0.97x0.5x0.16",
								conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						object = new WarpPipeTopObject(object.stringData+",BL1",
								conversionType);
						
					} else if (object instanceof GoombaObject) {
						
						object = new GoombaObject(object.stringData+",CL1x0x0",conversionType);
						
					} else if (object instanceof DoorObject) {
						
						object = new DoorObject(object.stringData+",BL1",
								conversionType);
						
					} else if (object instanceof WoodenPlatformObject) {
						
						object = new WoodenPlatformObject(object.stringData+",CL1x0x0",conversionType);
					}
					
				} else if (Utility.versionGreaterThanVersion("0.7.0",conversionType.gameVersionTo)) {
					
					if (object instanceof MushroomTopObject) {
						
						object = new MushroomTopObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof SignObject) {
						
						object = new SignObject(object.stringData.substring(0,
								object.stringData.length()-",BL0,BL0".length()),conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						LevelObject destinationPipe = toLevel.findMatchingPipe(object,i);
						
						object = new WarpPipeTopObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								object.objectData[7].toString()+","+
								(destinationPipe == null?
								object.objectData[2].toString():
								destinationPipe.objectData[2].toString())+
								",BL1",conversionType);
						
						numberOfWarpPipes++;
						
					} else if (object instanceof DoorObject) {
						
						object = new DoorObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								object.objectData[8].toString()+","+
								object.objectData[8].toString(),conversionType);
						
					} else if (object instanceof ArrowObject) {
						
						object = new ArrowObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
					}
				}
				
			case "0.6.1":
				
				if (Utility.versionGreaterThanVersion(conversionType.gameVersionTo,"0.6.1")) {
					
					if (object instanceof MushroomTopObject) {
						
						object = new MushroomTopObject(object.stringData+",CL1x0x0",
								conversionType);
						
					} else if (object instanceof SignObject) {
						
						object = new SignObject(object.stringData+",BL0,BL0",conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						double[] pipeDestinationCoordinates = (
								double[]) object.objectData[8].getValue(),
								pipeCoordinates = (double[]) object.objectData[2].getValue();
						
						object = new WarpPipeTopObject(""+object.objectID+","+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								object.objectData[7].toString()+","+
								"STWarpPipe"+numberOfWarpPipes+","+
								"CL0x1x0,"+"BL1",conversionType);
						
						if (!Utility.areSameVectors(pipeCoordinates,pipeDestinationCoordinates)) {
							
							LevelObject destinationPipe = new WarpPipeTopObject(object.stringData,
									conversionType);
							
							destinationPipe.objectData[2].setValue(new double[] {
									pipeDestinationCoordinates[0],
									pipeDestinationCoordinates[1]
							});
							
							postConversionAdditions.add(destinationPipe);
						}
						
						numberOfWarpPipes++;
						
					} else if (object instanceof DoorObject) {
						
						toLevel.normalizeDoors();
						
						object = new DoorObject(""+object.objectID+","+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								"IT0,"+object.objectData[8].toString()+",BL0",conversionType);
						
					} else if (object instanceof ArrowObject) {
						
						object = new ArrowObject(object.stringData+",CL1x0x0",conversionType);
					}
					
				} else if (Utility.versionGreaterThanVersion("0.6.1",conversionType.gameVersionTo)) {
					
					if (object instanceof EnchantedGearObject) {
						
						object = new EnchantedGearObject(object.stringData.substring(0,
								object.stringData.length()-",BL0".length()),conversionType);
						
					} else if (object instanceof RainbowStarObject) {
						
						object = new RainbowStarObject(object.stringData.substring(0,
								object.stringData.length()-",BL0".length()),conversionType);
						
					} else if (object instanceof GhostPepperObject) {
						
						object = new GhostPepperObject(object.stringData.substring(0,
								object.stringData.length()-",BL0".length()),conversionType);
						
					} else if (object instanceof SuperFeatherObject) {
						
						object = new SuperFeatherObject(object.stringData.substring(0,
								object.stringData.length()-",BL0".length()),conversionType);
					}
				}
				
			case "0.6.0":
				
				if (Utility.versionGreaterThanVersion(conversionType.gameVersionTo,"0.6.0")) {
					
					if (object instanceof EnchantedGearObject) {
						
						object = new EnchantedGearObject(object.stringData+"BL1",conversionType);
						
					} else if (object instanceof RainbowStarObject) {
						
						object = new RainbowStarObject(object.stringData+"BL1",conversionType);
						
					} else if (object instanceof GhostPepperObject) {
						
						object = new GhostPepperObject(object.stringData+"BL1",conversionType);
						
					} else if (object instanceof SuperFeatherObject) {
						
						object = new SuperFeatherObject(object.stringData+"BL1",conversionType);
					}
				}
			}
			
			objectArray[i] = object;
		}
		
		toLevel.setAreaObjects(objectArray);
	}
	
	String getCodeVersion(String file) {
		
		Path filePath = inputDirectory.resolve(file);
		
		String levelData = fileHandler.readFromFile(filePath);
		
		int currentIndex = 0,nextIndex = 0;
		
		nextIndex = levelData.indexOf(',');
		String codeVersion = levelData.substring(currentIndex,nextIndex);
		
		return codeVersion;
	}
	
	ConversionType getConversionType() {
		
		return conversionType;
	}
	
	static boolean getIsDebug() {
		
		return Boolean.valueOf(isDebug);
	}
	
}
