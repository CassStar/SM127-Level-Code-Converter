package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class WaterObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation,width,height,toxicity;
	public boolean enabled,visible,renderInFront,tapMode;
	public String tag;
	
	public WaterObject(String data,ConversionType type) throws Exception {
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
		width = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		height = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (double[]) objectData[9].getValue();
		renderInFront = (boolean) objectData[10].getValue();
		tag = (String) objectData[11].getValue();
		toxicity = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		tapMode = (boolean) objectData[13].getValue();
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
