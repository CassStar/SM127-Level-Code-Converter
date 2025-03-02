package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import objects.*;
import util.ConversionUtility;
import util.Utility;

public class ZeroNineZero implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.9.0";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroNineZero(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
		this.conversionType = conversionType;
		this.numberOfWarpPipes = numberOfWarpPipes;
		this.postConversionAdditions = postConversionAdditions;
	}
	
	@Override
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			int backerBG = (int) fromArea.getBackerBG();
			
			switch(backerBG) {
			
			// Brown and Black Gradient
			case 12:
				
				// Set Backer BG to Blue and Black Gradient
				toArea.setBackerBG(9);
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
			
			// File Select (63)
			case 67:
				
				// Set MusicID to Main Menu
				toArea.setMusicID(31);
				break;
				
			// Level Designer Portal
			case 68:
				
				// Set MusicID to Cosmic Cove Galaxy
				toArea.setMusicID(42);
				break;
				
			// Excess Express at Day
			case 69:
				
				// Set MusicID to Excess Express at Dusk
				toArea.setMusicID(30);
				break;
				
			// Excess Express at Night
			case 70:
				
				// Set MusicID to Excess Express at Dusk
				toArea.setMusicID(30);
				break;
			}
		}
		
		return toArea;
	}
	
	@Override
	public LevelTile[] convertTiles(AreaCode toArea,LevelTile[] tileArray) {
		
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
			
			case "0.8.0":
				
				if (!conversionsDone[4]) {
					
					object = ConversionUtility.convertDownToZeroEightZero(object,conversionType);
					
					conversionsDone[4] = true;
				}
				
				break;
				
			case "0.9.1":
				
				if (!conversionsDone[6]) {
					
					object = ConversionUtility.convertUpToZeroNineOne(object,conversionType);
					
					conversionsDone[6] = true;
				}
			}
			
			objectArray[i] = object;
		}
		
		return new Object[]  {objectArray,conversionsDone};
	}
}
