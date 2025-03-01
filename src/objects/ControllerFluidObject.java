package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class ControllerFluidObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0,moveSpeed = 1,offset = 0,cycleTimer = 0,cycleOffset = 0;
	public boolean enabled = true,visible = true,autoActivate = false,horizontal = false;
	public String tag = "default";
	
	public ControllerFluidObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,tag,autoActivate,moveSpeed,offset,horizontal,
				cycleTimer,cycleOffset};
		
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
		tag = (objectData[7] == null) ? tag:String.valueOf(objectData[7].getValue());
		autoActivate = (objectData[8] == null) ? autoActivate:(boolean) objectData[8].getValue();
		moveSpeed = (objectData[9] == null) ? moveSpeed:(double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		offset = (objectData[10] == null) ? offset:(double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		horizontal = (objectData[11] == null) ? horizontal:(boolean) objectData[11].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new StringType(Utility.stringToString(tag));}
		if (objectData[8] == null) { objectData[8] = new BooleanType(Utility.booleanToString(autoActivate));}
		if (objectData[9] == null) { objectData[9] = new FloatType(Utility.floatToString(moveSpeed));}
		if (objectData[10] == null) { objectData[10] = new FloatType(Utility.floatToString(offset));}
		if (objectData[11] == null) { objectData[11] = new BooleanType(Utility.booleanToString(horizontal));}
		
		if (objectData.length > 12) {
			
			cycleTimer = (objectData[12] == null) ? cycleTimer:(double) Double.valueOf(String.valueOf(objectData[12].getValue()));
			cycleOffset = (objectData[13] == null) ? cycleOffset:(double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		}
	}

}
