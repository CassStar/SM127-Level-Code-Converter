package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class MovingPlatformObject extends LevelObject {
	
	public int ID,pallete = 0,parts,moveType;
	public double[] position,scale,colour;
	public double rotation,maxSpeed,startOffset,unkownFloatValue;
	public boolean enabled,visible,touchStart;
	public double[][] path,pathDuplicate;
	
	public MovingPlatformObject(String data,ConversionType type) throws Exception {
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
		colour = (double[]) objectData[12].getValue();
		startOffset = (double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		
		switch (objectData.length) {
		
		case 16:
			
			unkownFloatValue = (double) Double.valueOf(String.valueOf(objectData[15].getValue()));
		
		case 15:
			
			pathDuplicate = (double[][]) objectData[14].getValue();
		
		case 14:
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
