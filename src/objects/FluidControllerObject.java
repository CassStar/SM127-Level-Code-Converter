package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class FluidControllerObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale;
	public double rotation,moveSpeed,offset;
	public boolean enabled,visible,autoActivate,horizontal;
	public String tag;
	
	public FluidControllerObject(String data,ConversionType type) throws Exception {
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
		tag = (String) objectData[7].getValue();
		autoActivate = (boolean) objectData[8].getValue();
		moveSpeed = (double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		offset = (double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		horizontal = (boolean) objectData[11].getValue();
		
		
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
