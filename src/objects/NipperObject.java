package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class NipperObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long fireType = 0,jumpHeight = 280;
	public double[] position = {0,0},scale = {1,1},colour = {0,1,0,1};
	public double rotation = 0;
	public boolean enabled = true,visible = true,wander = false,rainbow = false;
	
	public NipperObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,wander,fireType,colour,rainbow,jumpHeight};
		
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
		wander = (objectData[7] == null) ? wander:(boolean) objectData[7].getValue();
		fireType = (objectData[8] == null) ? fireType:(long) objectData[8].getValue();
		colour = (objectData[9] == null) ? colour:(double[]) objectData[9].getValue();
		rainbow = (objectData[10] == null) ? rainbow:(boolean) objectData[10].getValue();
		jumpHeight = (objectData[11] == null) ? jumpHeight:(long) objectData[11].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new BooleanType(Utility.booleanToString(wander));}
		if (objectData[8] == null) { objectData[8] = new IntegerType(Utility.integerToString(fireType));}
		if (objectData[9] == null) { objectData[9] = new ColourType(Utility.colourToString(colour));}
		if (objectData[10] == null) { objectData[10] = new BooleanType(Utility.booleanToString(rainbow));}
		if (objectData[11] == null) { objectData[11] = new IntegerType(Utility.integerToString(jumpHeight));}
	}
	
}
