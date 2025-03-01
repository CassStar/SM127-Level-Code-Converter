package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class DoorConditionLockedObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long areaID = 0,requiredAmount = 1;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0;
	public boolean enabled = true,visible = true,teleportMode = false,doorType = false;
	public String destinationTag = "default_teleporter",collectible = "shine",
			insufficientText = "Sorry%21%20You%20need%20%7bnum%7d%20%7bcol%7d%20to%20open%20this%20door%21";
	
	public DoorConditionLockedObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,areaID,destinationTag,teleportMode,collectible,requiredAmount,insufficientText,doorType};
		
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
		areaID = (objectData[7] == null) ? areaID:(long) objectData[7].getValue();
		destinationTag = (objectData[8] == null) ? destinationTag:String.valueOf(objectData[8].getValue());
		teleportMode = (objectData[9] == null) ? teleportMode:(boolean) objectData[9].getValue();
		collectible = (objectData[10] == null) ? collectible:String.valueOf(objectData[10].getValue());
		requiredAmount = (objectData[11] == null) ? requiredAmount:(long) objectData[11].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new IntegerType(Utility.integerToString(areaID));}
		if (objectData[8] == null) { objectData[8] = new StringType(Utility.stringToString(destinationTag));}
		if (objectData[9] == null) { objectData[9] = new BooleanType(Utility.booleanToString(teleportMode));}
		if (objectData[10] == null) { objectData[10] = new StringType(Utility.stringToString(collectible));}
		if (objectData[11] == null) { objectData[11] = new IntegerType(Utility.integerToString(requiredAmount));}
		
		if (objectData.length > 13) {
			
			insufficientText = (objectData[12] == null) ? insufficientText:String.valueOf(objectData[12].getValue());
			doorType = (objectData[13] == null) ? doorType:(boolean) objectData[13].getValue();
		}
	}
}
