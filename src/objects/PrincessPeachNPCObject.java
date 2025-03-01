package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class PrincessPeachNPCObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long idleExpression,idleAction,speakingExpression,speakingAction,requiredShines;
	public double[] position,scale;
	public double rotation,walkSpeed;
	public boolean enabled,visible,moveType,physicsEnabled,pathReference;
	public String tagLink;
	public double[][] path,pathDuplicate;
	
	public PrincessPeachNPCObject(String data,ConversionType type) throws Exception {
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
		path = (double[][]) objectData[7].getValue();
		pathDuplicate = (double[][]) objectData[8].getValue();
		moveType = (boolean) objectData[9].getValue();
		walkSpeed = (double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		physicsEnabled = (boolean) objectData[11].getValue();
		idleExpression = (long) objectData[12].getValue();
		idleAction = (long) objectData[13].getValue();
		speakingExpression = (long) objectData[14].getValue();
		speakingAction = (long) objectData[15].getValue();
		pathReference = (boolean) objectData[16].getValue();
		tagLink = String.valueOf(objectData[17].getValue());
		requiredShines = (long) objectData[18].getValue();
	}
	
}
