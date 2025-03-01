package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class ConditionLockedDoorObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long areaID,requiredAmount;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,teleportMode,doorType;
	public String destinationTag,collectible,insufficientText;
	
	public ConditionLockedDoorObject(String data,ConversionType type) throws Exception {
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
		areaID = (long) objectData[7].getValue();
		destinationTag = String.valueOf(objectData[8].getValue());
		teleportMode = (boolean) objectData[9].getValue();
		collectible = String.valueOf(objectData[10].getValue());
		requiredAmount = (long) objectData[11].getValue();
		
		if (objectData.length > 13) {
			
			insufficientText = String.valueOf(objectData[12].getValue());
			doorType = (boolean) objectData[13].getValue();
		}
	}
}
