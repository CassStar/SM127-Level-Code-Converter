package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class PlatformMovingObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long parts = 4,moveType = 0;
	public double[] position = {0,0},scale = {1,1},colour = {0,1,0,1};
	public double rotation = 0,maxSpeed = 1,startOffset = 0,unkownFloatValue = 0;
	public boolean enabled = true,visible = true,touchStart = false;
	public double[][] path = {{0,0},{0,-64}},pathDuplicate = {{0,0},{0,-64}};
	
	public PlatformMovingObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,parts,maxSpeed,path,moveType,touchStart,colour,
				startOffset,pathDuplicate,unkownFloatValue};
		
		setupValues();
	}
	
	protected double[] getPosition() {
		
		return position.clone();
	}
	
	protected void setPosition(double[] position) {
		
		this.position = position.clone();
		objectData[2] = new Vector2Type(Utility.vector2ToString(this.position,false));
	}
	
	void setupValues() {
		
		objectData = Utility.ensureMinimumLength(objectData,14);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		parts = (objectData[7] == null) ? parts:(long) objectData[7].getValue();
		maxSpeed = (objectData[8] == null) ? maxSpeed:(double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		path = (objectData[9] == null) ? path:(double[][]) objectData[9].getValue();
		moveType = (objectData[10] == null) ? moveType:(long) objectData[10].getValue();
		touchStart = (objectData[11] == null) ? touchStart:(boolean) objectData[11].getValue();
		colour = (objectData[12] == null) ? colour:(double[]) objectData[12].getValue();
		startOffset = (objectData[13] == null) ? startOffset:(double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new IntegerType(Utility.integerToString(parts));}
		if (objectData[8] == null) { objectData[8] = new FloatType(Utility.floatToString(maxSpeed));}
		if (objectData[9] == null) { objectData[9] = new Curve2DType(Utility.curve2DToString(path));}
		if (objectData[10] == null) { objectData[10] = new IntegerType(Utility.integerToString(moveType));}
		if (objectData[11] == null) { objectData[11] = new BooleanType(Utility.booleanToString(touchStart));}
		if (objectData[12] == null) { objectData[12] = new ColourType(Utility.colourToString(colour));}
		if (objectData[13] == null) { objectData[13] = new FloatType(Utility.floatToString(startOffset));}
		
		switch (objectData.length) {
		
		case 16:
			
			unkownFloatValue = (objectData[15] == null) ? unkownFloatValue:(double) Double.valueOf(String.valueOf(objectData[15].getValue()));
		
		case 15:
			
			pathDuplicate = (objectData[14] == null) ? pathDuplicate:(double[][]) objectData[14].getValue();
		}
	}

}
