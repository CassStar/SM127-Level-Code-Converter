package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class RecoveryHeartObject extends LevelObject {
	
	public int ID,pallete = 0,minHealthPerSecond,coolDownTime;
	public double[] position,scale;
	public double rotation,healDuration;
	public boolean enabled,visible,hasCoolDown;
	
	public RecoveryHeartObject(String data,ConversionType type) throws Exception {
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
		position = (double[]) objectData[2].getValue();
		scale = (double[]) objectData[3].getValue();
		rotation = (double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (boolean) objectData[5].getValue();
		visible = (boolean) objectData[6].getValue();
		minHealthPerSecond = (int) objectData[7].getValue();
		healDuration = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		hasCoolDown = (boolean) objectData[9].getValue();
		coolDownTime = (int) objectData[10].getValue();
		
		try {
			
			pallete = (int) objectData[1].getValue();
			
		} catch (Exception e) {
		}
	}

}
