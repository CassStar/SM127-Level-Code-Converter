package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class PillarFireObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position = {0,0},scale = {0,0},colour = {1,0,0,1};
	public double rotation = 0,retractedTime = 2.5,burningTime = 2.5,offset = 0;
	public boolean enabled = true,visible = true,reversed = false;
	
	public PillarFireObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,retractedTime,burningTime,colour,reversed,offset};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,12);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		retractedTime = (objectData[7] == null) ? retractedTime:(double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		burningTime = (objectData[8] == null) ? burningTime:(double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (objectData[9] == null) ? colour:(double[]) objectData[9].getValue();
		reversed = (objectData[10] == null) ? reversed:(boolean) objectData[10].getValue();
		offset = (objectData[11] == null) ? offset:(double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new FloatType(Utility.floatToString(retractedTime));}
		if (objectData[8] == null) { objectData[8] = new FloatType(Utility.floatToString(burningTime));}
		if (objectData[9] == null) { objectData[9] = new ColourType(Utility.colourToString(colour));}
		if (objectData[10] == null) { objectData[10] = new BooleanType(Utility.booleanToString(reversed));}
		if (objectData[11] == null) { objectData[11] = new FloatType(Utility.floatToString(offset));}
	}

}
