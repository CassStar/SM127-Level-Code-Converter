package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class FloatingDuckPlatformObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long strongBouncePower;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,physicsEnabled;
	
	public FloatingDuckPlatformObject(String data,ConversionType type) throws Exception {
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
		strongBouncePower = (long) objectData[7].getValue();
		physicsEnabled = (boolean) objectData[8].getValue();
	}
}
