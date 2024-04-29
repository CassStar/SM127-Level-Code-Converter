package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class WarpPipeTopObject extends LevelObject {
	
	public int ID,pallete = 0,areaID;
	public double[] position,scale,colour,destination;
	public double rotation;
	public boolean enabled,visible,teleportMode;
	public String tag;
	
	public WarpPipeTopObject(String data,ConversionType type) throws Exception {
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
		areaID = (int) objectData[7].getValue();
		
		if (!Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			destination = (double[]) objectData[8].getValue();
			teleportMode = (boolean) objectData[9].getValue();
			
		} else {
			
			pallete = (int) objectData[1].getValue();
			tag = (String) objectData[8].getValue();
			colour = (double[]) objectData[9].getValue();
			teleportMode = (boolean) objectData[10].getValue();
		}
	}

}
