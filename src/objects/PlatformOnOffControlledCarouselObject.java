package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class PlatformOnOffControlledCarouselObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long parts = 2,platformCount = 4;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0,speed = 2,radius = 2,startAngle = 0;
	public boolean enabled = true,visible = true,unkownBooleanValue = true,inverted = false;
	
	public PlatformOnOffControlledCarouselObject(String data,
			ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,parts,speed,radius,platformCount,startAngle,
				unkownBooleanValue,inverted};
		
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
		speed = (objectData[8] == null) ? speed:(double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		radius = (objectData[9] == null) ? radius:(double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		platformCount = (objectData[10] == null) ? platformCount:(long) objectData[10].getValue();
		startAngle = (objectData[11] == null) ? startAngle:(double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		unkownBooleanValue = (objectData[12] == null) ? unkownBooleanValue:(boolean) objectData[12].getValue();
		inverted = (objectData[13] == null) ? inverted:(boolean) objectData[13].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new IntegerType(Utility.integerToString(parts));}
		if (objectData[8] == null) { objectData[8] = new FloatType(Utility.floatToString(speed));}
		if (objectData[9] == null) { objectData[9] = new FloatType(Utility.floatToString(radius));}
		if (objectData[10] == null) { objectData[10] = new IntegerType(Utility.integerToString(platformCount));}
		if (objectData[11] == null) { objectData[11] = new FloatType(Utility.floatToString(startAngle));}
		if (objectData[12] == null) { objectData[12] = new BooleanType(Utility.booleanToString(unkownBooleanValue));}
		if (objectData[13] == null) { objectData[13] = new BooleanType(Utility.booleanToString(inverted));}
	}

}
