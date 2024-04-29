package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class DoorObject extends LevelObject {
	
	public int ID,pallete = 0,areaID;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,teleportMode;
	public String tag,destinationTag;
	
	public DoorObject(String data,ConversionType type) throws Exception {
		super(data,type);
		setupValues();
	}
	
	void setupValues() {
		
		ID = objectID;
		position = (double[]) objectData[2].getValue();
		scale = (double[]) objectData[3].getValue();
		rotation = (double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (boolean) objectData[5].getValue();
		visible = (boolean) objectData[6].getValue();
		
		if (!Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			tag = String.valueOf(objectData[7].getValue());
			destinationTag = String.valueOf(objectData[8].getValue());
			
		} else {
			
			pallete = (int) objectData[1].getValue();
			areaID = (int) objectData[7].getValue();
			tag = String.valueOf(objectData[8].getValue());
			
			if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.7.0")) {
				
				teleportMode = (boolean) objectData[9].getValue();
			}
		}
	}

}
