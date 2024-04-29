package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class OnOffControlledMovingPlatformObject extends LevelObject {
	
	public int ID,pallete = 0,parts,moveType;
	public double[] position,scale;
	public double rotation,maxSpeed,startOffset;
	public boolean enabled,visible,touchStart,unkownBooleanValue,inverted;
	public double[][] path;
	
	public OnOffControlledMovingPlatformObject(String data,
			ConversionType type) throws Exception {
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
		parts = (int) objectData[7].getValue();
		maxSpeed = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		path = (double[][]) objectData[9].getValue();
		moveType = (int) objectData[10].getValue();
		touchStart = (boolean) objectData[11].getValue();
		startOffset = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		unkownBooleanValue = (boolean) objectData[13].getValue();
		inverted = (boolean) objectData[14].getValue();
		
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
