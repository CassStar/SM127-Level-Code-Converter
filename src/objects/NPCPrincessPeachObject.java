package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class NPCPrincessPeachObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long idleExpression = 0,idleAction = 0,speakingExpression = 1,speakingAction = 0,requiredShines = 0;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0,walkSpeed = 0;
	public boolean enabled = true,visible = true,moveType = true,physicsEnabled = true,pathReference = false;
	public String tagLink = "";
	public double[][] path = {{-50,-50},{50,-50}},pathDuplicate = {{-50,-50},{50,-50}};
	
	public NPCPrincessPeachObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,path,pathDuplicate,moveType,walkSpeed,physicsEnabled,
				idleExpression,idleAction,speakingExpression,speakingAction,pathReference,tagLink,requiredShines};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,19);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		path = (objectData[7] == null) ? path:(double[][]) objectData[7].getValue();
		pathDuplicate = (objectData[8] == null) ? pathDuplicate:(double[][]) objectData[8].getValue();
		moveType = (objectData[9] == null) ? moveType:(boolean) objectData[9].getValue();
		walkSpeed = (objectData[10] == null) ? walkSpeed:(double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		physicsEnabled = (objectData[11] == null) ? physicsEnabled:(boolean) objectData[11].getValue();
		idleExpression = (objectData[12] == null) ? idleExpression:(long) objectData[12].getValue();
		idleAction = (objectData[13] == null) ? idleAction:(long) objectData[13].getValue();
		speakingExpression = (objectData[14] == null) ? speakingExpression:(long) objectData[14].getValue();
		speakingAction = (objectData[15] == null) ? speakingAction:(long) objectData[15].getValue();
		pathReference = (objectData[16] == null) ? pathReference:(boolean) objectData[16].getValue();
		tagLink = (objectData[17] == null) ? tagLink:String.valueOf(objectData[17].getValue());
		requiredShines = (objectData[18] == null) ? requiredShines:(long) objectData[18].getValue();
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new Curve2DType(Utility.curve2DToString(path));}
		if (objectData[8] == null) { objectData[8] = new Curve2DType(Utility.curve2DToString(pathDuplicate));}
		if (objectData[9] == null) { objectData[9] = new BooleanType(Utility.booleanToString(moveType));}
		if (objectData[10] == null) { objectData[10] = new FloatType(Utility.floatToString(walkSpeed));}
		if (objectData[11] == null) { objectData[11] = new BooleanType(Utility.booleanToString(physicsEnabled));}
		if (objectData[12] == null) { objectData[12] = new IntegerType(Utility.integerToString(idleExpression));}
		if (objectData[13] == null) { objectData[13] = new IntegerType(Utility.integerToString(idleAction));}
		if (objectData[14] == null) { objectData[14] = new IntegerType(Utility.integerToString(speakingExpression));}
		if (objectData[15] == null) { objectData[15] = new IntegerType(Utility.integerToString(speakingAction));}
		if (objectData[16] == null) { objectData[16] = new BooleanType(Utility.booleanToString(pathReference));}
		if (objectData[17] == null) { objectData[17] = new StringType(Utility.stringToString(tagLink));}
		if (objectData[18] == null) { objectData[18] = new IntegerType(Utility.integerToString(requiredShines));}
	}
	
}
