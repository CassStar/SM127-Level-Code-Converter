package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class WaterBalloonObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long addedStamina = 100,addedWater = 50;
	public double[] position = {0,0},scale = {1,1},colour = {0,0.7,1,1};
	public double rotation = 0,respawnTimer = 10;
	public boolean enabled = true,visible = true;
	
	public WaterBalloonObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,addedStamina,addedWater,respawnTimer,colour};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,11);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		addedStamina = (objectData[7] == null) ? addedStamina:(long) objectData[7].getValue();
		addedWater = (objectData[8] == null) ? addedWater:(long) objectData[8].getValue();
		respawnTimer = (objectData[9] == null) ? respawnTimer:(double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		colour = (objectData[10] == null) ? colour:(double[]) objectData[10].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new IntegerType(Utility.integerToString(addedStamina));}
		if (objectData[8] == null) { objectData[8] = new IntegerType(Utility.integerToString(addedWater));}
		if (objectData[9] == null) { objectData[9] = new FloatType(Utility.floatToString(respawnTimer));}
		if (objectData[10] == null) { objectData[10] = new ColourType(Utility.colourToString(colour));}
	}
}
