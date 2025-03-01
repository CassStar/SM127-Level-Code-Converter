package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class PowerUpSuperFeatherObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0,duration = 30;
	public boolean enabled = true,visible = true,canRespawn = true,powerupMusic = true;
	
	public PowerUpSuperFeatherObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,duration,canRespawn,powerupMusic};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,9);
		
		ID = objectID;
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		duration = (objectData[7] == null) ? duration:(double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		canRespawn = (objectData[8] == null) ? canRespawn:(boolean) objectData[8].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new FloatType(Utility.floatToString(duration));}
		if (objectData[8] == null) { objectData[8] = new BooleanType(Utility.booleanToString(canRespawn));}
		
		if (objectData.length > 9) {
		
			powerupMusic = (objectData[9] == null) ? powerupMusic:(boolean) objectData[9].getValue();
		}
	}

}
