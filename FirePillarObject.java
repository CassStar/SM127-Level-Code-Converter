package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class FirePillarObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation,retractedTime,burningTime,offset;
	public boolean enabled,visible,reversed;
	
	public FirePillarObject(String data,ConversionType type) throws Exception {
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
		retractedTime = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		burningTime = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (double[]) objectData[9].getValue();
		reversed = (boolean) objectData[10].getValue();
		offset = (double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
