package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class ShineSpriteObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long shinePlacedIndex = 0,sortIndex = 0,requiredPurples = 0;
	public double[] position = {0,0},scale = {1,1},colour = {1,1,0,1};
	public double rotation = 0;
	public boolean enabled = true,visible = true,showInMenu = true,activated = true,redCoinsActivate = false,shineShardsActivate = false,
			doKickOut = true;
	public String title = "Unnamed%20Shine",description = "";
	
	public ShineSpriteObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,title,description,showInMenu,activated,
				redCoinsActivate,shineShardsActivate,colour,shinePlacedIndex,doKickOut,sortIndex,requiredPurples};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,17);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		title = (objectData[7] == null) ? title:String.valueOf(objectData[7].getValue());
		description = (objectData[8] == null) ? description:String.valueOf(objectData[8].getValue());
		showInMenu = (objectData[9] == null) ? showInMenu:(boolean) objectData[9].getValue();
		activated = (objectData[10] == null) ? activated:(boolean) objectData[10].getValue();
		redCoinsActivate = (objectData[11] == null) ? redCoinsActivate:(boolean) objectData[11].getValue();
		shineShardsActivate = (objectData[12] == null) ? shineShardsActivate:(boolean) objectData[12].getValue();
		colour = (objectData[13] == null) ? colour:(double[]) objectData[13].getValue();
		shinePlacedIndex = (objectData[14] == null) ? shinePlacedIndex:(long) objectData[14].getValue();
		doKickOut = (objectData[15] == null) ? doKickOut:(boolean) objectData[15].getValue();
		sortIndex = (objectData[16] == null) ? sortIndex:(long) objectData[16].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new StringType(Utility.stringToString(title));}
		if (objectData[8] == null) { objectData[8] = new StringType(Utility.stringToString(description));}
		if (objectData[9] == null) { objectData[9] = new BooleanType(Utility.booleanToString(showInMenu));}
		if (objectData[10] == null) { objectData[10] = new BooleanType(Utility.booleanToString(activated));}
		if (objectData[11] == null) { objectData[11] = new BooleanType(Utility.booleanToString(redCoinsActivate));}
		if (objectData[12] == null) { objectData[12] = new BooleanType(Utility.booleanToString(shineShardsActivate));}
		if (objectData[13] == null) { objectData[13] = new ColourType(Utility.colourToString(colour));}
		if (objectData[14] == null) { objectData[14] = new IntegerType(Utility.integerToString(shinePlacedIndex));}
		if (objectData[15] == null) { objectData[15] = new BooleanType(Utility.booleanToString(doKickOut));}
		if (objectData[16] == null) { objectData[16] = new IntegerType(Utility.integerToString(sortIndex));}
		
		if (objectData.length > 17) {
		
			requiredPurples = (objectData[17] == null) ? requiredPurples:(long) objectData[17].getValue();
		}
	}

}
