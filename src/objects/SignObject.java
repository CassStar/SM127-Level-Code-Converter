package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class SignObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0;
	public boolean enabled = true,visible = true,openMenu = false,onWall = false;
	public String text = "This%20is%20a%20sign.%20Click%20on%20it%20in%20the%20editor%20to%20edit%20this%20text%21";
	
	public SignObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,text,openMenu,onWall};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,8);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		text = (objectData[7] == null) ? text:String.valueOf(objectData[7].getValue());
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new StringType(Utility.stringToString(text));}
		
		if (objectData.length > 8) {
		
			openMenu = (objectData[8] == null) ? openMenu:(boolean) objectData[8].getValue();
			onWall = (objectData[9] == null) ? onWall:(boolean) objectData[9].getValue();
		}
	}

}
