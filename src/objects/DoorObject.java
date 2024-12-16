package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class DoorObject extends LevelObject {
	
	public int ID,pallete = 0,areaID;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,teleportMode,forceFadeOut;
	public String tag,destinationTag;
	
	public DoorObject(String data,ConversionType type) throws Exception {
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
		
		switch (objectData.length) {
		
		case 8:
			
			tag = String.valueOf(objectData[7].getValue());
			destinationTag = String.valueOf(objectData[8].getValue());
			break;
			
		case 9:
			
			pallete = (int) objectData[1].getValue();
			tag = String.valueOf(objectData[7].getValue());
			destinationTag = String.valueOf(objectData[8].getValue());
			break;
		
		case 10:
			
			pallete = (int) objectData[1].getValue();
			
			try {
				
				areaID = (int) objectData[7].getValue();
				teleportMode = (boolean) objectData[9].getValue();
				
			} catch (Exception e) {
				
				tag = String.valueOf(objectData[7].getValue());
			}
			
			destinationTag = String.valueOf(objectData[8].getValue());
		}
	}

}
