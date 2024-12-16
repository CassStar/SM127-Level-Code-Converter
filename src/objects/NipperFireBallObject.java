package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class NipperFireBallObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,velocity;
	public double rotation;
	public boolean enabled,visible,bouncy;
	
	public NipperFireBallObject(String data,ConversionType type) throws Exception {
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
		velocity = (double[]) objectData[7].getValue();
		bouncy = (boolean) objectData[8].getValue();
	}
	
}
