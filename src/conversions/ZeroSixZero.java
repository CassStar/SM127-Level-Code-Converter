package conversions;

import java.util.ArrayList;

import level.*;
import main.ConversionType;
import objects.SignObject;
import util.ConversionUtility;

public class ZeroSixZero implements ConversionBase {
	
	int numberOfWarpPipes;
	String thisGameVersion = "0.6.0";
	ArrayList<LevelObject> postConversionAdditions;
	ConversionType conversionType;
	
	public ZeroSixZero(int numberOfWarpPipes,ArrayList<LevelObject> postConversionAdditions,ConversionType conversionType) {
		
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
		
		return toArea;
	}
	
	@Override
	public LevelTile[] convertTiles(AreaCode toArea,LevelTile[] tileArray) {
		
		return tileArray;
	}
	
	@SuppressWarnings("unchecked")
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
				
			case "0.7.1","0.7.2":
				
				if (!conversionsDone[3]) {
					
					object = ConversionUtility.convertUpToZeroSevenTwo(object,conversionType);
					
					conversionsDone[3] = true;
				}
			
			case "0.7.0":
				
				if (!conversionsDone[2]) {
					
					Object[] data = ConversionUtility.convertUpToZeroSevenZero(
							object,conversionType,toArea,postConversionAdditions,numberOfWarpPipes);
					
					object = (LevelObject) data[0];
					postConversionAdditions = (ArrayList<LevelObject>) data[1];
					numberOfWarpPipes = (int) data[2];
					
					conversionsDone[2] = true;
				}
				
			case "0.6.1":
				
				if (!conversionsDone[1]) {
					
					object = ConversionUtility.convertUpToZeroSixOne(object,conversionType);
					
					conversionsDone[1] = true;
				}
				
			}
			
			objectArray[i] = object;
		}
		
		return new Object[]  {objectArray,conversionsDone};
	}
}
