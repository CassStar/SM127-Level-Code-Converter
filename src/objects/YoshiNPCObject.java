package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class YoshiNPCObject extends LevelObject {
	
	public int ID,pallete = 0,idleExpression,idleAction,speakingExpression,speakingAction,requiredShines;
	public double[] position,scale,skinColour,shoeColour;
	public double rotation,walkSpeed;
	public boolean enabled,visible,moveType,physicsEnabled,pathReference,rainbow;
	public String tagLink;
	public double[][] path,pathDuplicate;
	
	public YoshiNPCObject(String data,ConversionType type) throws Exception {
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
		idleExpression = (int) objectData[12].getValue();
		idleAction = (int) objectData[13].getValue();
		speakingExpression = (int) objectData[14].getValue();
		speakingAction = (int) objectData[15].getValue();
		pathReference = (boolean) objectData[16].getValue();
		tagLink = String.valueOf(objectData[17].getValue());
		requiredShines = (int) objectData[18].getValue();
		skinColour = (double[]) objectData[19].getValue();
		shoeColour = (double[]) objectData[20].getValue();
		rainbow = (boolean) objectData[21].getValue();
	}
	
}
