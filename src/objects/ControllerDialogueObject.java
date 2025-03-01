package objects;

import level.LevelObject;
import main.ConversionType;
import types.*;
import util.Utility;

public class ControllerDialogueObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long autoStart = 0,displayMode = 0;
	public double[] position = {0,0},scale = {1,1};
	public double rotation = 0,targetZoom = 0.65;
	public boolean enabled = true,visible = true,interactable = true;
	public String characterName = "",bubbleText = "This%20text%20appears%20as%20a%20speech%20bubble%20above%20your%20NPC%21",
			tag = "",delegateTag = "";
	public String[] dialogue = {"0","1","0","0","","%3b","This%20is%20a%20dialogue%20object.",":","0","1","0","0","",
			"%3b","Try%20putting%20this%20on%20top%20of%20an%20NPC%20and%20see%20what%20happens%21"};
	
	public ControllerDialogueObject(String data,ConversionType type) throws Exception {
		super(data,type);
		
		defaultValues = new Object[] {ID,pallete,position,scale,rotation,enabled,visible,dialogue,characterName,autoStart,interactable,
				bubbleText,displayMode,tag,delegateTag,targetZoom};
		
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
		
		objectData = Utility.ensureMinimumLength(objectData,16);
		
		ID = objectID;
		pallete = (objectData[1] == null) ? pallete:(int) objectData[1].getValue();
		position = (objectData[2] == null) ? position:(double[]) objectData[2].getValue();
		scale = (objectData[3] == null) ? scale:(double[]) objectData[3].getValue();
		rotation = (objectData[4] == null) ? rotation:(double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (objectData[5] == null) ? enabled:(boolean) objectData[5].getValue();
		visible = (objectData[6] == null) ? visible:(boolean) objectData[6].getValue();
		dialogue = (objectData[7] == null) ? dialogue:(String[]) objectData[7].getValue();
		characterName = (objectData[8] == null) ? characterName:String.valueOf(objectData[8].getValue());
		autoStart = (objectData[9] == null) ? autoStart:(long) objectData[9].getValue();
		interactable = (objectData[10] == null) ? interactable:(boolean) objectData[10].getValue();
		bubbleText = (objectData[11] == null) ? bubbleText:String.valueOf(objectData[11].getValue());
		displayMode = (objectData[12] == null) ? displayMode:(long) objectData[12].getValue();
		tag = (objectData[13] == null) ? tag:String.valueOf(objectData[13].getValue());
		delegateTag = (objectData[14] == null) ? delegateTag:String.valueOf(objectData[14].getValue());
		targetZoom = (objectData[15] == null) ? targetZoom:(double) Double.valueOf(String.valueOf(objectData[15].getValue()));
		
		if (objectData[2] == null) { objectData[2] = new Vector2Type(Utility.vector2ToString(position,false));}
		if (objectData[3] == null) { objectData[3] = new Vector2Type(Utility.vector2ToString(scale,false));}
		if (objectData[4] == null) { objectData[4] = new FloatType(Utility.floatToString(rotation));}
		if (objectData[5] == null) { objectData[5] = new BooleanType(Utility.booleanToString(enabled));}
		if (objectData[6] == null) { objectData[6] = new BooleanType(Utility.booleanToString(visible));}
		if (objectData[7] == null) { objectData[7] = new DialogueType(Utility.dialogueToString(dialogue));}
		if (objectData[8] == null) { objectData[8] = new StringType(Utility.stringToString(characterName));}
		if (objectData[9] == null) { objectData[9] = new IntegerType(Utility.integerToString(autoStart));}
		if (objectData[10] == null) { objectData[10] = new BooleanType(Utility.booleanToString(interactable));}
		if (objectData[11] == null) { objectData[11] = new StringType(Utility.stringToString(bubbleText));}
		if (objectData[12] == null) { objectData[12] = new IntegerType(Utility.integerToString(displayMode));}
		if (objectData[13] == null) { objectData[13] = new StringType(Utility.stringToString(tag));}
		if (objectData[14] == null) { objectData[14] = new StringType(Utility.stringToString(delegateTag));}
		if (objectData[15] == null) { objectData[15] = new FloatType(Utility.floatToString(targetZoom));}
	}
	
}
