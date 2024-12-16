package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class WarpZoneObject extends LevelObject {
	
	public int ID,pallete = 0,areaDestination,parts;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,teleportMode,vertical,stopsCamera,forceFadeOut;
	public String destinationTag;
	
	public WarpZoneObject(String data,ConversionType type) throws Exception {
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
		areaDestination = (int) objectData[7].getValue();
		destinationTag = String.valueOf(objectData[8].getValue());
		teleportMode = (boolean) objectData[9].getValue();
		vertical = (boolean) objectData[10].getValue();
		parts = (int) objectData[11].getValue();
		stopsCamera = (boolean) objectData[12].getValue();
		
		if (objectData.length > 13) {
			
			forceFadeOut = (boolean) objectData[13].getValue();
		}
	}
}
