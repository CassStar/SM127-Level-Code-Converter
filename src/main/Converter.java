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
	int numberOfAreas;
	AreaGrid areaGrid;
	
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
		
		fileHandler.setupDirectories(new Path[] {inputDirectory});
		
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
		
		String levelData = code.getLevelData();
		
		Path outputFile = outputDirectory.resolve(fileName);
		
		fileHandler.setupDirectories(new Path[] {outputDirectory});
		
		// Write the level data to file.
		fileHandler.writeToFile(levelData,outputFile,new OpenOption[] {CREATE,
				TRUNCATE_EXISTING,WRITE});
	}
	
	ConversionType getConversionType(String file) {
		
		String codeVersion = getCodeVersion(file);
		
		ConversionType temp = new ConversionType(false,codeVersion,codeVersion);
		
		System.out.println();
		
		ProgramLogger.logMessage("Level was created using game version: "+
				temp.getGameVersionFrom(),LogType.INFO);
		System.out.printf("Please enter which game version you want to convert to for file:"
				+ " %s%n",file);
		System.out.println();
		
		
		System.out.print("Enter game version: ");
		
		String tempInput;
		
		tempInput = input.nextLine();
		
		ConversionType conversion = new ConversionType(true,temp.gameVersionFrom,tempInput);
		
		while (!conversion.isValid ||
				Utility.areSameGameVersions(conversion.gameVersionFrom,conversion.gameVersionTo)) {
			
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
	
	@SuppressWarnings("unchecked")
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
		
		// Updating code version.
		toLevel.setCodeVersion(conversionType.codeVersionTo);
		
		for (int i = 0;i < toLevel.getNumberOfAreas();i++) {
			
			toLevel.getAreaCode(i).setPostConversionAdditions(postConversionAdditions);
			
			ProgramLogger.logMessage("Converting Backgrounds for area: "+i+"...",LogType.INFO);
			// Area Backer BG Conversion.
			convertBackerBG(i);
			
			// Area Fronter BG Conversion.
			convertFronterBG(i);
			
			ProgramLogger.logMessage("Converting Music for area: "+i+"...",LogType.INFO);
			// Area Music Conversion.
			convertMusicIDs(i);
			
			ProgramLogger.logMessage("Converting Tiles for area: "+i+"...",LogType.INFO);
			// Tile Conversions.
			convertTiles(i);
			
			ProgramLogger.logMessage("Converting Objects for area: "+i+"...",LogType.INFO);
			// Object Conversions.
			convertObjects(i);
			
			System.out.println();
			
			for (LevelObject object:postConversionAdditions) {
				
				// Post-conversion addition doors need to actually be converted as well.
				if (object instanceof DoorObject) {
					
					String data = ""+object.objectID+","+
							Utility.vector2DToString(
									(double[]) object.objectData[2].getValue(),false)+","+
							Utility.vector2DToString(
									(double[]) object.objectData[3].getValue(),false)+","+
							object.objectData[4].getType()+object.objectData[4].getValue()+","+
							Utility.booleanToString((boolean) object.objectData[5].getValue())+","+
							Utility.booleanToString((boolean) object.objectData[6].getValue())+",";
					
					if (conversionType.gameVersionTo.equals("0.7.0")) {
						
						data += "STnone,STnone,"+object.objectData[7].getType()+object.objectData[7].getValue();
						
					} else {
						
						data += "IT0,"+object.objectData[7].getType()+object.objectData[7].getValue()+",BL0";
					}
					
					object = new DoorObject(data,conversionType);
				}
				
				toLevel.getAreaCode(i).addObject(object);
			}
			
			postConversionAdditions = (ArrayList<LevelObject>) postConversionAdditions.clone();
			postConversionAdditions.clear();
		}
		
		String newFileName = file.substring(0,file.length()-4)+
				" [Converted to V"+conversionType.gameVersionTo+"].txt";
		
		saveLevelCode(toLevel,newFileName);
		
		ProgramLogger.logMessage("Finished converting level data!",LogType.INFO);
		ProgramLogger.logMessage("You can find the converted level in the output folder with "
				+ "the filename: "+newFileName,LogType.INFO);
		System.out.println("_____________________________________________________________________________________________________");
		
	}
	
	boolean checkCodeStructure(String fileName) {
		
		int currentIndex = 0,nextIndex = 0;
		
		Path file = inputDirectory.resolve(fileName);
		
		String levelData = fileHandler.readFromFile(file);
		
		if (levelData == null) {
			
			ProgramLogger.logMessage("Cannot determine if code structure is valid, file "
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
			
			numberOfAreas = numberOfBrackets-1;
			
			ProgramLogger.logMessage("Number of areas in level: "+numberOfAreas+"\n",LogType.INFO);
			
			if (numberOfAreas > 2 && Utility.versionGreaterThanVersion("0.7.0",conversionType.gameVersionTo)) {
				
				boolean defaultAreaLayout = getYesNo("Since there are multiple areas in the level, you can either choose where you "
						+ "want them placed, or leave it to the default layout.%n%nThe default layout places each area sequentially "
						+ "left-to-right starting from area ID 0.%n%nDo you want to go with the default layout? (Y/N): ",
						"%nInvalid value entered.%nDo you want to go with the default layout? (Please enter either 'Y' or 'N'): ");
				
				placeAreas(levelData,defaultAreaLayout);
				
				fromLevel = new LevelCode(levelData,areaGrid,conversionType);
				
				fromLevel.setNumberOfAreas(1);
				
				levelData = fromLevel.getLevelData();
				
			} else {
				
				fromLevel = new LevelCode(levelData,numberOfAreas,conversionType);
			}
			
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
			
			for (int i = 0;i< fromLevel.getNumberOfAreas();i++) {
				
				levelData = parseArea(levelData,i,currentIndex,nextIndex);
				
				if (levelData == null) {
					
					throw new Exception();
				}
				
				System.out.println("_____________________________________________________________________________________________________");
			}
			
			
			
		} catch (Exception e) {
			
			ProgramLogger.logMessage("Invalid code structure detected. Detailed error "
					+ "below:",LogType.ERROR);
			
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	String parseArea(String levelData,int area,int currentIndex,int nextIndex) throws Exception {
		
		int areaEnd = levelData.indexOf(']');
		
		AreaCode areaCode = fromLevel.getAreaCode(area);
		
		String areaData = levelData.substring(currentIndex,areaEnd+1);
		
		ProgramLogger.logMessage("Area Data: "+areaData+"\n",LogType.DEBUG);
		
		if (!(areaData.startsWith("[") && areaData.endsWith("]"))) {
			
			ProgramLogger.logMessage("Invalid structure for area data detected.",
					LogType.ERROR);
			System.out.println();
			return null;
		}
		
		areaCode.setAreaData(areaData);
		
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
		
		areaCode.setDimensions(areaDimensions);
		
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
			
//			ProgramLogger.logMessage("Silhouette Mode detected.",LogType.INFO);
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
		
		areaCode.setBackerBG(areaBackerBG);
		areaCode.setFronterBG(areaFronterBG);
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.indexOf(',');
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area Music Data: "+temp,LogType.INFO);
		
		String musicDataType = Utility.getDataType(temp),musicData = null;
		
		int musicID;
		
		if (!(musicDataType.equals("ST") || musicDataType.equals("IT"))) {
			
			ProgramLogger.logMessage("Unkown music data structure! Setting to default"
					+ " value.",LogType.WARNING);
			musicID = 1;
			
			ProgramLogger.logMessage("Area Music ID: "+musicID,LogType.INFO);
			System.out.println();
			
			areaCode.setMusicID(musicID);
			
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
			
			areaCode.setMusicID(musicID);
			
		} else {
			
			musicData = new String(temp);
			
			System.out.println();
			
			areaCode.setMusicData(musicData);
		}
		
		if (Utility.versionGreaterThanVersion("0.7.0",conversionType.gameVersionFrom)) {
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
		} else {
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf(',');
		}
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area Gravity Data: "+temp,LogType.INFO);
		
		double areaGravity = Utility.parseFloat(temp);
		
		ProgramLogger.logMessage("Area Gravity: "+areaGravity,LogType.INFO);
		System.out.println();
		
		areaCode.setGravity(areaGravity);
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			areaData = areaData.substring(nextIndex+1,areaData.length());
			nextIndex = areaData.indexOf('~');
			
			temp = areaData.substring(currentIndex,nextIndex);
			
			ProgramLogger.logMessage("Area BG Pallete Data: "+temp,LogType.INFO);
			
			int areaBGPallete = Utility.parseInteger(temp);
			
			ProgramLogger.logMessage("Area BG Pallete: "+areaBGPallete,LogType.INFO);
			System.out.println();
			
			areaCode.setBGPallete(areaBGPallete);
		}
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.indexOf('~');
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area Ground Tile Data: "+temp+"\n",LogType.DEBUG);
		
		areaCode.setGroundTiles(areaCode.parseTiles(temp));
		
		ProgramLogger.logMessage("Number of Ground tile groups in area: "+
				areaCode.getGroundTiles().length,LogType.INFO);
		System.out.println();
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.indexOf('~');
		
		// ForeGround Tiles.
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area ForeGround Tile Data: "+temp+"\n",LogType.DEBUG);
		
		areaCode.setForeGroundTiles(areaCode.parseTiles(temp));
		
		ProgramLogger.logMessage("Number of ForeGround tile groups in area: "+
				areaCode.getForeGroundTiles().length,LogType.INFO);
		System.out.println();
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.indexOf('~');
		
		// BackGround0 Tiles.
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area BackGround0 Tile Data: "+temp+"\n",LogType.DEBUG);
		
		areaCode.setBackGround0Tiles(areaCode.parseTiles(temp));
		
		ProgramLogger.logMessage("Number of BackGround0 tile groups in area: "+
				areaCode.getBackGround0Tiles().length,LogType.INFO);
		System.out.println();
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.indexOf('~');
		
		// BackGround1 Tiles.
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area BackGround1 Tile Data: "+temp+"\n",LogType.DEBUG);
		
		areaCode.setBackGround1Tiles(areaCode.parseTiles(temp));
		
		ProgramLogger.logMessage("Number of BackGround1 tile groups in area: "+
				areaCode.getBackGround1Tiles().length,LogType.INFO);
		System.out.println();
		
		areaData = areaData.substring(nextIndex+1,areaData.length());
		nextIndex = areaData.length()-1;
		
		temp = areaData.substring(currentIndex,nextIndex);
		
		ProgramLogger.logMessage("Area Objects Data: "+temp+"\n",LogType.DEBUG);
		
		areaCode.setObjects(areaCode.parseObjects(temp));
		
		ProgramLogger.logMessage("Number of objects in area: "+
				areaCode.getObjects().length,LogType.INFO);
		System.out.println();
		
		if (area+1 != fromLevel.getNumberOfAreas()) {
			
			levelData = levelData.substring(areaEnd+2);
			
		} else {
			
			levelData = levelData.substring(areaEnd);
		}
		
		return levelData;
	}
	
	void placeAreas(String levelData,boolean isDefault) {
		
		if (isDefault) {
			
			areaGrid = new AreaGrid(numberOfAreas);
			return;
		}
		
		String tempInput;
		
		areaGrid = new AreaGrid(0);
		areaGrid.input = input;
		
		boolean finished = false;
		
		while (!finished) {
			
			System.out.printf("%nCurrent Area Grid:%n%s",areaGrid);
			
			System.out.printf("%n%n1. Add Area%n2. Remove Area%n3. Change Area%n4. Swap Areas%n5. Manual Entry%n6. Help%n7. Finish%n");
			
			System.out.print("Enter the number of the operation you want to perform: ");
			
			tempInput = input.nextLine();
			
			System.out.println();
			
			switch (tempInput) {
			
			case "1":
				
				areaGrid.addArea();
				break;
				
			case "2":
				
				areaGrid.removeArea();
				break;
				
			case "3":
				
				areaGrid.changeArea();
				break;
				
			case "4":
				
				areaGrid.swapAreas();
				break;
				
			case "5":
				
				areaGrid.manualEntry();
				break;
				
			case "6":
				
				AreaGrid.printHelp();
				break;
				
			case "7":
				
				if (areaGrid.getNumberOfAreas() < 1) {
					
					ProgramLogger.logMessage("Area grid has no areas in it! Please add an area before finishing.",LogType.ERROR);
					continue;
				}
				
				if (getYesNo("Are you sure you're finished placing areas? (Y/N): ",
						"%nInvalid value entered.%nAre you sure you're finished placing areas? (Please enter either 'Y' or 'N'): ")) {
					
					finished = true;
				}
				break;
				
			default:
				
				ProgramLogger.logMessage("Invalid operation number entered!",LogType.ERROR);
			}
		}
	}
	
	void convertBackerBG(int area) {
		
		AreaCode fromArea = fromLevel.getAreaCode(area),toArea = toLevel.getAreaCode(area);
		
		if (fromArea.getBackerBG() == conversionType.maxBackerBG[0]) {
			
			toArea.setBackerBG(conversionType.maxBackerBG[1]);
			
		} else if (fromArea.getBackerBG() > conversionType.maxBackerBG[1]) {
			
			toArea.setBackerBG(conversionType.maxBackerBG[1]-1);
		}
	}
	
	void convertFronterBG(int area) {
		
		AreaCode fromArea = fromLevel.getAreaCode(area),toArea = toLevel.getAreaCode(area);
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			if (fromArea.getFronterBG() == 1 && fromArea.getBGPallete() == 1) {
				
				toArea.setFronterBG(Math.min(2,conversionType.maxFronterBG[1]));
			}
			
//			toArea.setBGPallete(0);
			
			switch (fromArea.getFronterBG()) {
			case 7,8,10,12,13:
				
				toArea.setFronterBG(Math.min(1,conversionType.maxFronterBG[1]));
				break;
				
			case 9:
				
				toArea.setFronterBG(Math.min(3,conversionType.maxFronterBG[1]));
				break;
				
			case 11:
				
				toArea.setFronterBG(Math.min(2,conversionType.maxFronterBG[1]));
				break;
			}
		}
		
		if (toArea.getFronterBG() == conversionType.maxFronterBG[0]) {
			
			toArea.setFronterBG(conversionType.maxFronterBG[1]);
			
		} else if (toArea.getFronterBG() > conversionType.maxFronterBG[1]) {
			
			toArea.setFronterBG(conversionType.maxFronterBG[1]-1);
		}
	}
	
	void convertMusicIDs(int area) {
		
		AreaCode fromArea = fromLevel.getAreaCode(area),toArea = toLevel.getAreaCode(area);
		
		if (fromArea.getMusicID() == conversionType.maxMusicID[0]) {
			
			toArea.setMusicID(conversionType.maxMusicID[1]);
		}
		
		switch (conversionType.gameVersionFrom) {
		
		case "0.7.2":
			
			// This is the same as MusicID 1 in all other versions.
			if (fromArea.getMusicID() == 65) {
				
				toArea.setMusicID(1);
			}
			
		case "0.7.1":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.7.0":
				
				switch(fromArea.getMusicID()) {
				
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
			
		case "0.7.0":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.6.1":
				
				switch(fromArea.getMusicID()) {
				
				// Ice Ice Outpost
				case 39:
					
					// Set to Snow Rise Paper Mario: Sticker Star
					toArea.setMusicID(12);
					break;
				
				// Water themes
				case 40,41,42,43,64:
					
					// Set to Buoy Base Galaxy Super Mario Galaxy
					toArea.setMusicID(9);
					break;
					
				// Desert themes, Waltz of the Boos (Remix)
				case 44,45,46,47,48,63:
					
					// Set to Princess Peach's Castle Super Smash Bros. Melee
					toArea.setMusicID(1);
					break;
					
				// Beach themes
				case 49,50,51:
					
					// Set to Yoshi Star Galaxy Super Mario Galaxy 2
					toArea.setMusicID(16);
					break;
					
				// Fire themes
				case 52,53,54,55:
					
					// Set to Speedy Comet Super Mario Galaxy
					toArea.setMusicID(8);
					break;
					
				// Ghost themes
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
				
				// Yoshi's Island - Underground (Remix)
				case 62:
					
					// Set to Underground Yoshi's New Island
					toArea.setMusicID(34);
					break;
				}
			}
			
		case "0.6.1":
			
			switch (conversionType.gameVersionTo) {
			
			case "0.6.0":
				
				switch(fromArea.getMusicID()) {
				
				// Ice Ice Outpost
				case 39:
					
					// Set to Snow Rise Paper Mario: Sticker Star
					toArea.setMusicID(12);
					break;
				
				// Water themes
				case 40,41,42,43,64:
					
					// Set to Buoy Base Galaxy Super Mario Galaxy
					toArea.setMusicID(9);
					break;
					
				// Desert themes, Waltz of the Boos (Remix)
				case 44,45,46,47,48,63:
					
					// Set to Princess Peach's Castle Super Smash Bros. Melee
					toArea.setMusicID(1);
					break;
					
				// Beach themes
				case 49,50,51:
					
					// Set to Yoshi Star Galaxy Super Mario Galaxy 2
					toArea.setMusicID(16);
					break;
					
				// Fire themes
				case 52,53,54,55:
					
					// Set to Speedy Comet Super Mario Galaxy
					toArea.setMusicID(8);
					break;
					
				// Ghost themes
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
				
				// Yoshi's Island - Underground (Remix)
				case 62:
					
					// Set to Underground Yoshi's New Island
					toArea.setMusicID(34);
					break;
				}
			}
		}
		
	}
	
	void convertTiles(int area) {
		
		AreaCode toArea = toLevel.getAreaCode(area);
		
		// Combine all the tiles into one large array.
		toArea.setupAllTiles();
		
		LevelTile[] tileArray = toArea.getAllTiles();
		
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
		
		toArea.setAllTiles(tileArray);
		
		// Seperate the large tile array back into the different types.
		toArea.seperateAllTilesArray();
	}
	
	void convertObjects(int area) throws Exception {
		
		AreaCode toArea = toLevel.getAreaCode(area);
		
		LevelObject[] objectArray = toArea.getObjects();
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
			
			if (conversionType.gameVersionFrom.equals("0.7.2") || conversionType.gameVersionFrom.equals("0.7.1")) {
				
				switch (conversionType.gameVersionTo) {
				
				case "0.6.0":
					
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
					
				case "0.6.1":
					
					if (object instanceof MushroomTopObject) {
						
						object = new MushroomTopObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof SignObject) {
						
						object = new SignObject(object.stringData.substring(0,
								object.stringData.length()-",BL0,BL0".length()),conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						LevelObject destinationPipe = toArea.findMatchingPipe(object,i);
						
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
						break;
						
					} else if (object instanceof ArrowObject) {
						
						object = new ArrowObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
					}
					
				case "0.7.0":
					
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
								"STnone,STnone,"+
								object.objectData[7].toString(),conversionType);
						
					} else if (object instanceof WoodenPlatformObject) {
						
						object = new WoodenPlatformObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					}
				}
				
				
			} else if (conversionType.gameVersionFrom.equals("0.7.0")) {
				
				switch (conversionType.gameVersionTo) {
				
				case "0.6.0":
					
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
					
				case "0.6.1":
					
					if (object instanceof MushroomTopObject) {
						
						object = new MushroomTopObject(object.stringData.substring(
								0,object.stringData.indexOf("CL")-1),conversionType);
						
					} else if (object instanceof SignObject) {
						
						object = new SignObject(object.stringData.substring(0,
								object.stringData.length()-",BL0,BL0".length()),conversionType);
						
					} else if (object instanceof WarpPipeTopObject) {
						
						LevelObject destinationPipe = toArea.findMatchingPipe(object,i);
						
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
					
					break;
					
				case "0.7.1","0.7.2":
					
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
				}
			} else if (conversionType.gameVersionFrom.equals("0.6.1")) {
				
				switch (conversionType.gameVersionTo) {
				
				case "0.6.0":
					
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
					
					break;
					
				case "0.7.1","0.7.2":
					
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
						break;
						
					} else if (object instanceof WoodenPlatformObject) {
						
						object = new WoodenPlatformObject(object.stringData+",CL1x0x0",conversionType);
					}
					
				case "0.7.0":
					
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
						
						toArea.normalizeDoors();
						
						object = new DoorObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								"STnone,STnone,"+
								object.objectData[8].toString(),conversionType);
						
					} else if (object instanceof ArrowObject) {
						
						object = new ArrowObject(object.stringData+",CL1x0x0",conversionType);
					}
				}
			} else if (conversionType.gameVersionFrom.equals("0.6.0")) {
				
				switch (conversionType.gameVersionTo) {
				
				case "0.7.1","0.7.2":
					
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
						break;
						
					} else if (object instanceof WoodenPlatformObject) {
						
						object = new WoodenPlatformObject(object.stringData+",CL1x0x0",conversionType);
					}
				
				case "0.7.0":
					
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
						
						toArea.normalizeDoors();
						
						object = new DoorObject(""+object.objectID+",0,"+
								object.objectData[2].toString()+","+
								object.objectData[3].toString()+","+
								object.objectData[4].toString()+","+
								object.objectData[5].toString()+","+
								object.objectData[6].toString()+","+
								"STnone,STnone,"+
								object.objectData[8].toString(),conversionType);
						
					} else if (object instanceof ArrowObject) {
						
						object = new ArrowObject(object.stringData+",CL1x0x0",conversionType);
					}
					
				case "0.6.1":
					
					if (object instanceof EnchantedGearObject) {
						
						object = new EnchantedGearObject(object.stringData+",BL1",conversionType);
						
					} else if (object instanceof RainbowStarObject) {
						
						object = new RainbowStarObject(object.stringData+",BL1",conversionType);
						
					} else if (object instanceof GhostPepperObject) {
						
						object = new GhostPepperObject(object.stringData+",BL1",conversionType);
						
					} else if (object instanceof SuperFeatherObject) {
						
						object = new SuperFeatherObject(object.stringData+",BL1",conversionType);
					}
				}
			}
			
			objectArray[i] = object;
		}
		
		toArea.setObjects(objectArray);
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
	
	boolean getYesNo(String question,String invalid) {
		
		System.out.printf(question);
		
		String tempInput = input.nextLine().toLowerCase();

		while (true) {
			
			switch (tempInput) {
			
			case "y","ye","yes":
				
				return true;
				
			case "n","no","nay":
				
				return false;
			
			default:
				
				System.out.printf(invalid);
				tempInput = input.nextLine().toLowerCase();
			}
		}
	}
	
	public static boolean getIsDebug() {
		
		return Boolean.valueOf(isDebug);
	}
	
	String[] getAreaArray() {
		
		String[] array = new String[numberOfAreas];
		
		for (int i = 0;i < numberOfAreas;i++) {
			
			array[i] = String.valueOf(i);
		}
		
		return array;
	}
	
	public class AreaGrid {
		
		int[][] grid;
		Scanner input;
		
		AreaGrid(int numberOfAreas) {
			
			grid = new int[1][numberOfAreas];
			
			for (int i = 1;i < grid[0].length;i++) {
				
				grid[0][i] = i;
			}
		}
		
		void addArea() {
			
			int areaToAdd = getValidID("Please enter the ID of the area you would like to add: "),relativeToArea = -99;
			
			if (getNumberOfAreas() == 0) {
				
				addArea(areaToAdd);
				return;
			}
			
			boolean pickRelative = true;
			
			if (getNumberOfAreas() == 1) {
				
				pickRelative = false;
				relativeToArea = grid[0][0];
			}
			
			if (pickRelative) {
				
				System.out.println();
				relativeToArea = getValidIDInGrid("Enter which area ID you want to place place this relative to: ");
			}
			
			int[] rowAndColumn;
			
			if (getNumberOfInGrid(relativeToArea) > 1) {
				
				System.out.printf("%nMultiple of that area exist in the grid. ");
				rowAndColumn = getRowAndColumn("Enter the row and column of the area you want in the format- row,column: ");
				rowAndColumn[0]--;rowAndColumn[1]--;
				
			} else {
				
				rowAndColumn = findRowAndColumn(relativeToArea,0);
			}
			
			System.out.println();
			
			String directionFromArea = getValidDirection("Please enter a direction from the relative area you want to place the new area"
					+ " at (Left/Right/Up/Down): ");
			
			switch (directionFromArea) {
			
			case "left":
				
				rowAndColumn[1]--;
				break;
				
			case "right":
				
				rowAndColumn[1]++;
				break;
				
			case "up":
				
				rowAndColumn[0]--;
				break;
				
			case "down":
				
				rowAndColumn[0]++;
				break;
			}
			
			addArea(areaToAdd,rowAndColumn[0],rowAndColumn[1]);
			
		}
		
		private void addArea(int areaToAdd) {
			
			grid = new int[][] {{areaToAdd}};
			
			ProgramLogger.logMessage("Added area to grid.",LogType.INFO);
		}
		
		private void addArea(int areaToAdd,int row,int column) {
			
			if (row < 0) {
				
				addRowAbove();
				row++;
				
			} else if (row >= grid.length) {
				
				addRowBelow();
				
			} else if (column < 0) {
				
				addColumnLeft();
				column++;
				
			} else if (column >= grid[0].length) {
				
				addColumnRight();
				
			}
			
			grid[row][column] = areaToAdd;
			
			ProgramLogger.logMessage("Added area to grid.",LogType.INFO);
		}
		
		void removeArea() {
			
			if (getNumberOfAreas() == 0) {
				
				ProgramLogger.logMessage("Area grid is empty, cannot remove from it!",LogType.WARNING);
				return;
			}
			
			if (getNumberOfAreas() == 1) {
				
				grid = new int[0][];
				ProgramLogger.logMessage("Removed area from grid.",LogType.INFO);
				return;
			}
			
			int areaToRemove = getValidIDInGrid("Please enter the ID of the area you would like to remove: ");
			
			boolean removeAll = false;
			int matchingAreasInGrid = getNumberOfInGrid(areaToRemove);
			
			if (matchingAreasInGrid > 1) {
				
				System.out.printf("%nMultiple of that area exist in the grid.%n");
				removeAll = getYesNo("Would you like to remove all instances of the area in the grid? (Y/N): ","%nInvalid value entered"
						+ ".%nWould you like to remove all instances of the area in the grid? (Please enter either 'Y' or 'N'): ");
			}
			
			int[] rowAndColumn;
			
			if (matchingAreasInGrid == 1) {
				
				rowAndColumn = findRowAndColumn(areaToRemove,0);
				
				removeArea(rowAndColumn[0],rowAndColumn[1]);
				condenseGrid();
				return;
			}
			
			if (!removeAll) {
				
				rowAndColumn = getRowAndColumn("Enter the row and column of the area you want removed in the format- row,column: ");
				rowAndColumn[0]--;rowAndColumn[1]--;
				
				removeArea(rowAndColumn[0],rowAndColumn[1]);
				condenseGrid();
				return;
			}
			
			for (int i = 0;i < matchingAreasInGrid;i++) {
				
				rowAndColumn = findRowAndColumn(areaToRemove,0);
				
				removeArea(rowAndColumn[0],rowAndColumn[1]);
			}
			
			condenseGrid();
		}
		
		private void removeArea(int row,int column) {
			
			grid[row][column] = -1;
			
			ProgramLogger.logMessage("Removed area from grid.",LogType.INFO);
		}
		
		void changeArea() {
			
			if (getNumberOfAreas() == 0) {
				
				ProgramLogger.logMessage("Area grid is empty, cannot change areas currently! Please add an area first.",LogType.WARNING);
				return;
			}
			
			int areaToChange,matchingAreasInGrid;
			
			if (getNumberOfAreas() == 1) {
				
				areaToChange = grid[0][0];
				matchingAreasInGrid = 1;
				
			} else {
				
				areaToChange = getValidIDInGrid("Please enter the ID of the area you would like to change: ");
				matchingAreasInGrid = getNumberOfInGrid(areaToChange);
			}
			
			boolean changeAll = false;
			
			if (matchingAreasInGrid > 1) {
				
				System.out.printf("%nMultiple of that area exist in the grid.%n");
				changeAll = getYesNo("Would you like to change all instances of the area in the grid? (Y/N): ","%nInvalid value entered"
						+ ".%nWould you like to change all instances of the area in the grid? (Please enter either 'Y' or 'N'): ");
			}
			
			int[] rowAndColumn;
			
			int areaToChangeTo = getValidID("Please enter the ID of the area you would like to change the selected area(s) to: ");
			
			if (matchingAreasInGrid == 1) {
				
				rowAndColumn = findRowAndColumn(areaToChange,0);
				
				changeAreas(new int[][] {{rowAndColumn[0],rowAndColumn[1]}},areaToChangeTo);
				return;
			}
			
			if (!changeAll) {
				
				rowAndColumn = getRowAndColumn("Enter the row and column of the area you want changed in the format- row,column: ");
				rowAndColumn[0]--;rowAndColumn[1]--;
				
				changeAreas(new int[][] {{rowAndColumn[0],rowAndColumn[1]}},areaToChangeTo);
				return;
			}
			
			for (int i = 0;i < matchingAreasInGrid;i++) {
				
				rowAndColumn = findRowAndColumn(areaToChange,i);
				
				changeAreas(new int[][] {{rowAndColumn[0],rowAndColumn[1]}},areaToChangeTo);
			}
		}
		
		private void changeAreas(int[][] areaCoordinates,int areaToChangeTo) {
			
			for (int[] coordinates:areaCoordinates) {
				
				grid[coordinates[0]][coordinates[1]] = areaToChangeTo;
				
				ProgramLogger.logMessage("Changed area in grid.",LogType.INFO);
			}
		}
		
		void swapAreas() {
			
			if (getNumberOfAreas() < 2) {
				
				ProgramLogger.logMessage("Less than two areas are in area grid! Cannot swap with less than two areas.",LogType.WARNING);
				return;
			}
			
			int swapFromArea = getValidIDInGrid("Please enter the ID of the area you would like to swap from: ");
			
			int matchingAreasInGrid = getNumberOfInGrid(swapFromArea);
			
			boolean swapFromAll = false;
			
			if (matchingAreasInGrid > 1) {
				
				System.out.printf("%nMultiple of that area exist in the grid.%n");
				swapFromAll = getYesNo("Would you like to swap all instances of the area in the grid? (Y/N): ","%nInvalid value entered"
						+ ".%nWould you like to swap all instances of the area in the grid? (Please enter either 'Y' or 'N'): ");
			}
			
			int[][] swapFromCoordinates;
			
			if (matchingAreasInGrid == 1) {
				
				swapFromCoordinates = new int[][] {findRowAndColumn(swapFromArea,0)};
				
			} else if (!swapFromAll) {
				
				swapFromCoordinates = new int[1][];
				swapFromCoordinates[0] = getRowAndColumn("Enter the row and column of the area you want to swap from in the format- "
						+ "row,column: ");
				swapFromCoordinates[0][0]--;swapFromCoordinates[0][1]--;
				
			} else {
				
				swapFromCoordinates = new int[matchingAreasInGrid][];
				
				for (int i = 0;i < matchingAreasInGrid;i++) {
					
					swapFromCoordinates[i] = findRowAndColumn(swapFromArea,i);
				}
			}
			
			int swapToArea = getValidIDInGrid("Please enter the ID of the area you would like to swap to: ");
			
			matchingAreasInGrid = getNumberOfInGrid(swapToArea);
			
			boolean swapToAll = false;
			
			if (matchingAreasInGrid > 1) {
				
				System.out.printf("%nMultiple of that area exist in the grid.%n");
				swapToAll = getYesNo("Would you like to swap all instances of the area in the grid? (Y/N): ","%nInvalid value entered"
						+ ".%nWould you like to swap all instances of the area in the grid? (Please enter either 'Y' or 'N'): ");
			}
			
			int[][] swapToCoordinates;
			
			if (matchingAreasInGrid == 1) {
				
				swapToCoordinates = new int[][] {findRowAndColumn(swapToArea,0)};
				
			} else if (!swapToAll) {
				
				swapToCoordinates = new int[1][];
				swapToCoordinates[0] = getRowAndColumn("Enter the row and column of the area you want to swap to in the format- "
						+ "row,column: ");
				swapToCoordinates[0][0]--;swapToCoordinates[0][1]--;
				
			} else {
				
				swapToCoordinates = new int[matchingAreasInGrid][];
				
				for (int i = 0;i < matchingAreasInGrid;i++) {
					
					swapToCoordinates[i] = findRowAndColumn(swapToArea,i);
				}
			}
			
			swapAreas(swapFromCoordinates,swapFromArea,swapToCoordinates,swapToArea);
		}
		
		private void swapAreas(int[][] swapFromCoordinates,int swapFromArea,int[][]swapToCoordinates,int swapToArea) {
			
			for (int[] coordinates:swapFromCoordinates) {
				
				grid[coordinates[0]][coordinates[1]] = swapToArea;
			}
			
			for (int[] coordinates:swapToCoordinates) {
				
				grid[coordinates[0]][coordinates[1]] = swapFromArea;
			}
			
			ProgramLogger.logMessage("Swapped areas in grid.",LogType.INFO);
		}
		
		void manualEntry() {
			
			String tempInput,unparsed = "";
			
			while (true) {
				
				System.out.print("Enter a row of areas to add to the area grid (in the format: areaID #1,areaID #2,areaID #3,etc...),"
						+ " to finish press enter without typing anything: ");
				
				tempInput = input.nextLine();
				
				if (tempInput.length() == 0) {
					
					break;
				}
				
				if (tempInput.charAt(tempInput.length()-1) == ',') {
					
					tempInput = tempInput.substring(0,tempInput.length()-1);
				}
				
				int[] tempArray = getIntArray(tempInput);
				
				if (tempArray != null) {
					
					unparsed = unparsed.concat(tempInput+"%20");
				}
			}
			
			if (unparsed.length() == 0) {
				
				ProgramLogger.logMessage("Completed manual entry.",LogType.INFO);
				return;
			}
			
			grid = makeGridFromRows(unparsed.split("%20"));
			ProgramLogger.logMessage("Completed manual entry.",LogType.INFO);
		}
		
		static void printHelp() {
			
			System.out.printf("%nHere's some basic help on how the area placing operations work!%n%nFirst off, a grid view containing all"
					+ " the areas currently placed is displayed. The grid lists areas by their in-game ID, and dashes (-) represent no"
					+ " area is currently placed in that position.%n%n1. Add Area- This lets you add an area to the area grid. You "
					+ "provide the ID of the area you want to add, the ID of the area you want it placed relative to, and which "
					+ "direction from that you want it to be placed.%n%n2. Remove Area- This lets you remove an area from the area grid."
					+ " You provide which area ID you want removed and it will be removed. If there are multiple of that area ID "
					+ "placed in the grid, you must choose which one you want removed or if you want to remove all of them.%n%n3. "
					+ "Change Area- You provide the ID for which area you want to change and the ID of the area you want it to be "
					+ "changed to. If multiple of the area you want changed are placed in the grid, you can choose to change a specific"
					+ " one or all of them.%n%n4. Swap Areas- Allows you to swap two areas placed in the grid by their IDs. If you choose"
					+ " an ID that's placed mutliple times, you can specify if you want to swap all occurences or just one of them.%n%n5. "
					+ "This lets you manually set the entire area grid with a single action. While in manual entry you can keep entering"
					+ " rows of areas indefinitely. To end manual entry simply press enter without typing any input.%nNOTE: Using manual"
					+ " entry REPLACES any current grid you have with the one made using the input from the manual entry.%n%n6. Help- "
					+ "Prints out this help text!%n%n7. Finish- Finishes the placing the placing of areas, then continues with the"
					+ " level convertion.");
		}
		
		int getValidID(String question) {
			
			String tempInput;
			int area;
			
			while (true) {
				
				System.out.print(question);
				tempInput = input.nextLine();
				
				try {
					
					Integer.parseInt(tempInput);
					
				} catch (NumberFormatException e) {
					
					ProgramLogger.logMessage("Invalid value entered. Area ID must be a number.",LogType.ERROR);
					continue;
				}
				
				area = Integer.parseInt(tempInput);
				
				if (!areaWithinRange(area,0,numberOfAreas-1)) {
					
					ProgramLogger.logMessage("ID entered is not a valid area in this level!",LogType.ERROR);
					continue;
				}
				
				return area;
			}
		}
		
		int getValidIDInGrid(String question) {
			
			int area;
			
			while (true) {
				
				area = getValidID(question);
				
				if (getNumberOfInGrid(area) < 1) {
					
					ProgramLogger.logMessage("The area ID entered is not in the grid yet.",LogType.ERROR);
					continue;
				}
				
				return area;
			}
		}
		
		String getValidDirection(String question) {
			
			String tempInput;
			
			while (true) {
				
				System.out.print(question);
				tempInput = input.nextLine().toLowerCase();
				
				switch(tempInput) {
				
				case "l","left":
					
					return "left";
				
				case "r","right":
					
					return "right";
				
				case "u","up":
					
					return "up";
				
				case "d","down":
					
					return "down";
					
				default:
					
					ProgramLogger.logMessage("Invalid direction entered!",LogType.ERROR);
					continue;
				}
			}
		}
		
		int[] getIntArray(String base) {
			
			int[] array = null;
			String[] tempArray = base.split(",");
			
			array = new int[tempArray.length];
			
			for (int i = 0;i < array.length;i++) {
				
				try {
					
					array[i] = Integer.parseInt(tempArray[i]);
					
				} catch (NumberFormatException e) {
					
					ProgramLogger.logMessage("Invalid value entered: "+tempArray[i],LogType.ERROR);
					return null;
				}
				
				if (!areaWithinRange(array[i],0,numberOfAreas-1)) {
					
					ProgramLogger.logMessage("Area provided doesn't exist in-level: "+tempArray[i],LogType.ERROR);
					return null;
				}
			}
			
			return array;
		}
		
		int[] getRowAndColumn(String question) {
			
			String tempInput;
			
			while (true) {
				
				System.out.print(question);
				
				tempInput = input.nextLine().toLowerCase();
				String[] stringOutput = tempInput.split(",");
				
				try {
					
					int[] output = {Integer.parseInt(stringOutput[0]),Integer.parseInt(stringOutput[1])};
					
					if (output[0] < 1 || output[0] > grid.length || output[1] < 1 || output[1] > grid[0].length) {
						
						ProgramLogger.logMessage("Row or column entered is not within the grid!",LogType.ERROR);
						continue;
					}
					
					return output;
					
				} catch (NumberFormatException e) {
					
					ProgramLogger.logMessage("Invalid format for row and column entered!",LogType.ERROR);
					continue;
				}
			}
		}
		
		int getNumberOfInGrid(int area) {
			
			if (grid == null || grid.length == 0 || grid[0].length == 0) {
				
				return 0;
			}
			
			int count = 0;
			
			for (int i = 0;i < grid.length;i++) {
				
				for (int j = 0;j < grid[0].length;j++) {
					
					if (grid[i][j] == area) {
						
						count++;
					}
				}
			}
			
			return count;
			
		}
		
		int getNumberOfAreas() {
			
			if (grid == null || grid.length == 0 || grid[0].length == 0) {
				
				return 0;
			}
			
			int areas = 0;
			
			for (int i = 0;i < grid.length;i++) {
				
				for (int j = 0;j < grid[0].length;j++) {
					
					if (grid[i][j] != -1) {
						
						areas++;
					}
				}
			}
			
			return areas;
		}
		
		int[] findRowAndColumn(int area,int occurencesBeforeReturn) {
			
			if (grid == null || grid.length == 0 || grid[0].length == 0) {
				
				return null;
			}
			
			for (int i = 0;i < grid.length;i++) {
				
				for (int j = 0;j < grid[0].length;j++) {
					
					if (grid[i][j] == area) {
						
						if (occurencesBeforeReturn == 0) {
							return new int[] {i,j};
						}
						
						occurencesBeforeReturn--;
					}
				}
			}
			
			return null;
		}
		
		static boolean areaWithinRange(int area,int min,int max) {
			
			if (area >= min && area <= max) {
				
				return true;
			}
			
			return false;
		}
		
		private int[][] makeGridFromRows(String[] rows) {
			
			int[][] newGrid = new int[rows.length][];
			
			int[] intRow;
			
			for (int j = 0;j < rows.length;j++) {
				
				String[] values = rows[j].split(",");
				
				intRow = new int[values.length];
				
				for (int i = 0;i < values.length;i++) {
					
					intRow[i] = Integer.parseInt(values[i]);
				}
				
				newGrid[j] = intRow;
			}
			
			return newGrid;
		}
		
		private void condenseGrid() {
			
			int[][] emptyRowsAndColumns = getEmptyRowsAndColumns();
			
			int[] emptyRows = emptyRowsAndColumns[0],emptyColumns = emptyRowsAndColumns[1];
			
			int removedRows = 0;
			
			for (int row:emptyRows) {
				
				removeRow(row-removedRows);
				removedRows++;
			}
			
			int removedColumns = 0;
			
			for (int column:emptyColumns) {
				
				removeColumn(column-removedColumns);
				removedColumns++;
			}
		}
		
		private int[][] getEmptyRowsAndColumns() {
			
			int[] emptyRows = {},emptyColumns = {};
			
			int rowLength = grid[0].length,columnLength = grid.length,rowCounter,columnCounter;
			
			for (int i = 0;i < grid.length;i++) {
				
				rowCounter = 0;
				
				int[] row = grid[i];
				
				for (int value:row) {
					
					if (value == -1) {
						
						rowCounter++;
					}
				}
				
				if (rowCounter == rowLength) {
					
					emptyRows = Utility.expandIntegerArray(emptyRows,1);
					emptyRows[emptyRows.length-1] = i;
				}
			}
			
			
			
			for (int i = 0;i < grid[0].length;i++) {
				
				columnCounter = 0;
				
				for (int j = 0;j < columnLength;j++) {
					
					if (grid[j][i] == -1) {
						
						columnCounter++;
					}
				}
				
				if (columnCounter == columnLength) {
					
					emptyColumns = Utility.expandIntegerArray(emptyColumns,1);
					emptyColumns[emptyColumns.length-1] = i;
				}
			}
			
			return new int[][] {emptyRows,emptyColumns};
		}
		
		private void removeRow(int row) {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length-1][];
			
			for (int i = 0;i < row;i++) {
				
				grid[i] = oldGrid[i];
			}
			
			for (int i = row+1;i < oldGrid.length;i++) {
				
				grid[i-1] = oldGrid[i];
			}
			
		}
		
		private void removeColumn(int column) {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length][oldGrid[0].length-1];
			
			for (int j = 0;j < oldGrid.length;j++) {
				
				for (int i = 0;i < column;i++) {
					
					grid[j][i] = oldGrid[j][i];
				}
			}
			
			for (int j = 0;j < oldGrid.length;j++) {
				
				for (int i = column+1;i < oldGrid[0].length;i++) {
					
					grid[j][i-1] = oldGrid[j][i];
				}
			}
		}
		
		private void addRowAbove() {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length+1][oldGrid[0].length];
			
			for (int i = 0;i < oldGrid.length;i++) {
				
				for (int j = 0;j < oldGrid[0].length;j++) {
					
					grid[i+1][j] = oldGrid[i][j];
				}
			}
			
			for (int i = 0;i < grid[0].length;i++) {
				
				grid[0][i] = -1;
			}
		}
		
		private void addRowBelow() {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length+1][oldGrid[0].length];
			
			for (int i = 0;i < oldGrid.length;i++) {
				
				for (int j = 0;j < oldGrid[0].length;j++) {
					
					grid[i][j] = oldGrid[i][j];
				}
			}
			
			for (int i = 0;i < grid[0].length;i++) {
				
				grid[grid.length-1][i] = -1;
			}
		}
		
		private void addColumnLeft() {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length][oldGrid[0].length+1];
			
			for (int i = 0;i < oldGrid.length;i++) {
				
				for (int j = 0;j < oldGrid[0].length;j++) {
					
					grid[i][j+1] = oldGrid[i][j];
				}
			}
			
			for (int i = 0;i < grid.length;i++) {
				
				grid[i][0] = -1;
			}
		}
		
		private void addColumnRight() {
			
			int[][] oldGrid = grid.clone();
			
			grid = new int[oldGrid.length][oldGrid[0].length+1];
			
			for (int i = 0;i < oldGrid.length;i++) {
				
				for (int j = 0;j < oldGrid[0].length;j++) {
					
					grid[i][j] = oldGrid[i][j];
				}
			}
			
			for (int i = 0;i < grid.length;i++) {
				
				grid[i][grid[0].length-1] = -1;
			}
		}
		
		public int[][] getGrid() {
			
			return grid.clone();
		}
		
		public String toString() {
			
			if (grid == null || grid.length == 0) {
				
				return "[]";
			}
			
			StringBuilder output = new StringBuilder();
			
			for (int j = 0;j < grid.length;j++) {
				
				output.append('[');
				
				for (int i = 0;i < grid[0].length;i++) {
					
					if (grid[j][i] == -1) {
						
						output.append(" - ");
						
					} else {
						output.append(" "+grid[j][i]+" ");
					}
				}
				
				output.append(']');
				
				if (j != grid.length-1) {
					
					output.append('\n');
				}
			}
			
			return output.toString();
		}
	}
}
