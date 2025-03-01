package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class DialogueControllerObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long autoStart,displayMode;
	public double[] position,scale;
	public double rotation,targetZoom;
	public boolean enabled,visible,interactable;
	public String characterName,bubbleText,tag,delegateTag;
	public String[] dialogue;
	
	public DialogueControllerObject(String data,ConversionType type) throws Exception {
		super(data,type);
		setupValues();
	}
	
	protected double[] getPosition() {
		
		return position.clone();
	}
	
	protected void setPosition(double[] position) {
		
		this.position = position.clone();
		objectData[2] = new Vector2Type(Utility.vector2DToString(this.position,false));
	}
	
	void setupValues() {
		
		ID = objectID;
		pallete = (int) objectData[1].getValue();
		position = (double[]) objectData[2].getValue();
		scale = (double[]) objectData[3].getValue();
		rotation = (double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (boolean) objectData[5].getValue();
		visible = (boolean) objectData[6].getValue();
		dialogue = (String[]) objectData[7].getValue();
		characterName = String.valueOf(objectData[8].getValue());
		autoStart = (long) objectData[9].getValue();
		interactable = (boolean) objectData[10].getValue();
		bubbleText = String.valueOf(objectData[11].getValue());
		displayMode = (long) objectData[12].getValue();
		tag = String.valueOf(objectData[13].getValue());
		delegateTag = String.valueOf(objectData[14].getValue());
		targetZoom = (double) Double.valueOf(String.valueOf(objectData[15].getValue()));
	}
	
}
