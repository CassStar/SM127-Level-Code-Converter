package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class ZoomTriggerObject extends LevelObject {
	
	public int ID,pallete = 0,parts;
	public double[] position,scale;
	public double rotation,targetZoom,zoomTime;
	public boolean enabled,visible;
	
	public ZoomTriggerObject(String data,ConversionType type) throws Exception {
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
		targetZoom = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		zoomTime = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		parts = (int) objectData[9].getValue();
		
	}
	
}
