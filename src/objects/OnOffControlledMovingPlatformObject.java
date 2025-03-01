package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class OnOffControlledMovingPlatformObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long parts,moveType;
	public double[] position,scale;
	public double rotation,maxSpeed,startOffset,unkownFloatValue;
	public boolean enabled,visible,touchStart,unkownBooleanValue,inverted;
	public double[][] path,pathDuplicate;
	
	public OnOffControlledMovingPlatformObject(String data,
			ConversionType type) throws Exception {
		super(data,type);
		setupValues();
	}
	
	protected double[] getPosition() {
		
		return position.clone();
	}
	
	protected void setPosition(double[] position) {
		
		this.position = position.clone();
		objectData[2] = new Vector2Type(Utility.vector2DToString(this.position,false));
	}
	
	void setupValues() {
		
		ID = objectID;
		pallete = (int) objectData[1].getValue();
		position = (double[]) objectData[2].getValue();
		scale = (double[]) objectData[3].getValue();
		rotation = (double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (boolean) objectData[5].getValue();
		visible = (boolean) objectData[6].getValue();
		parts = (long) objectData[7].getValue();
		maxSpeed = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		path = (double[][]) objectData[9].getValue();
		moveType = (long) objectData[10].getValue();
		touchStart = (boolean) objectData[11].getValue();
		startOffset = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		unkownBooleanValue = (boolean) objectData[13].getValue();
		inverted = (boolean) objectData[14].getValue();
		
		switch(objectData.length) {
		
		case 17:
			
			unkownFloatValue = (double) Double.valueOf(String.valueOf(objectData[16].getValue()));
		
		case 16:
			
			pathDuplicate = (double[][]) objectData[15].getValue();
		}
	}

}
