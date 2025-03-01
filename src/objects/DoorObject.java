package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class DoorObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long areaID = 0;
	public double[] position = {0,0},scale = {0,0};
	public double rotation = 0;
	public boolean enabled = true,visible = true,teleportMode = false,forceFadeOut = false;
	public String tag = "default_teleporter",destinationTag = "default_teleporter";
	
	public DoorObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,areaID,destinationTag,teleportMode,forceFadeOut};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,7);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		
		switch (objectData.length) {
		
		case 8:
			
			tag = (objectData[7] == null) ? tag:String.valueOf(objectData[7].getValue());
			destinationTag = (objectData[8] == null) ? destinationTag:String.valueOf(objectData[8].getValue());
			break;
			
		case 9:
			
			tag = (objectData[7] == null) ? tag:String.valueOf(objectData[7].getValue());
			destinationTag = (objectData[8] == null) ? destinationTag:String.valueOf(objectData[8].getValue());
			break;
		
		case 10:
			
			try {
				
				areaID = (objectData[7] == null) ? areaID:(long) objectData[7].getValue();
				teleportMode = (objectData[9] == null) ? teleportMode:(boolean) objectData[9].getValue();
				
			} catch (Exception e) {
				
				tag = (objectData[7] == null) ? tag:String.valueOf(objectData[7].getValue());
			}
			
			destinationTag = (objectData[8] == null) ? destinationTag:String.valueOf(objectData[8].getValue());
		}
	}

}
