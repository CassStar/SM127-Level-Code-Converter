package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class WaterBalloonObject extends LevelObject {
	
	public int ID,pallete = 0,addedStamina,addedWater;
	public double[] position,scale,colour;
	public double rotation,respawnTimer;
	public boolean enabled,visible;
	
	public WaterBalloonObject(String data,ConversionType type) throws Exception {
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
		addedStamina = (int) objectData[7].getValue();
		addedWater = (int) objectData[8].getValue();
		respawnTimer = (double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		colour = (double[]) objectData[10].getValue();
	}
}