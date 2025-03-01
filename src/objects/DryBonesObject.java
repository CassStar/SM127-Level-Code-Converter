package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class DryBonesObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long respawnMode = 0,maxEnemies = 1;
	public double[] position = {0,0},scale = {1,1},initialVelocity = {0,0};
	public double rotation = 0,respawnTime = 15,spawnOffset = 0,tossWait = 5,regenerateTime = 10;
	public boolean enabled = true,visible = true,isStationary = false;
	
	public DryBonesObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,respawnTime,respawnMode,maxEnemies,initialVelocity,
				spawnOffset,isStationary,tossWait,regenerateTime};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,15);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		respawnTime = (objectData[7] == null) ? respawnTime:(double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		respawnMode = (objectData[8] == null) ? respawnMode:(long) objectData[8].getValue();
		maxEnemies = (objectData[9] == null) ? maxEnemies:(long) objectData[9].getValue();
		initialVelocity = (objectData[10] == null) ? initialVelocity:(double[]) objectData[10].getValue();
		spawnOffset = (objectData[11] == null) ? spawnOffset:(double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		isStationary = (objectData[12] == null) ? isStationary:(boolean) objectData[12].getValue();
		tossWait = (objectData[13] == null) ? tossWait:(double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		regenerateTime = (objectData[14] == null) ? regenerateTime:(double) Double.valueOf(String.valueOf(objectData[14].getValue()));
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new FloatType(Utility.floatToString(respawnTime));}
		if (objectData[8] == null) { objectData[8] = new IntegerType(Utility.integerToString(respawnMode));}
		if (objectData[9] == null) { objectData[9] = new IntegerType(Utility.integerToString(maxEnemies));}
		if (objectData[10] == null) { objectData[10] = new Vector2Type(Utility.vector2ToString(initialVelocity,false));}
		if (objectData[11] == null) { objectData[11] = new FloatType(Utility.floatToString(spawnOffset));}
		if (objectData[12] == null) { objectData[12] = new BooleanType(Utility.booleanToString(isStationary));}
		if (objectData[13] == null) { objectData[13] = new FloatType(Utility.floatToString(tossWait));}
		if (objectData[14] == null) { objectData[14] = new FloatType(Utility.floatToString(regenerateTime));}
	}
}