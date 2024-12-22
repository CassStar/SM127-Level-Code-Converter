package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import objects.*;
import util.ConversionUtility;
import util.Utility;

public class ZeroSevenTwo implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.7.2";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroSevenTwo(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
		this.conversionType = conversionType;
		this.numberOfWarpPipes = numberOfWarpPipes;
		this.postConversionAdditions = postConversionAdditions;
	}
	
	@Override
	public AreaCode convertBackerBG(AreaCode fromArea,AreaCode toArea) {
		
		return toArea;
	}
	
	@Override
	public AreaCode convertFronterBG(AreaCode fromArea,AreaCode toArea) {
		
		return toArea;
	}
	
	@Override
	public AreaCode convertMusicIDs(AreaCode fromArea,AreaCode toArea) {
		
		if (Utility.versionGreaterThanVersion(thisGameVersion,conversionType.gameVersionTo)) {
			
			switch(fromArea.getMusicID()) {
			
			// Princess Peach's Castle
			case 65:
				
				// Set MusicID to Princess Peach's Castle, which is ID 1 in all other versions.
				toArea.setMusicID(1);
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
				
			case "0.8.0":
				
				if (!conversionsDone[4]) {
					
					object = ConversionUtility.convertUpToZeroEightZero(object,conversionType);
					
					conversionsDone[4] = true;
				}
			}
			
			objectArray[i] = object;
		}
		
		return new Object[]  {objectArray,conversionsDone};
	}
}
