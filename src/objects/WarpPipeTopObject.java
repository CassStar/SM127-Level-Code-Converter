package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class WarpPipeTopObject extends LevelObject {
	
	public int ID,pallete = 0,areaID;
	public double[] position,scale,colour,destination;
	public double rotation;
	public boolean enabled,visible,teleportMode;
	public String tag;
	
	public WarpPipeTopObject(String data,ConversionType type) throws Exception {
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
		areaID = (int) objectData[7].getValue();
		
		try {
			
			pallete = (int) objectData[1].getValue();
			tag = String.valueOf(objectData[7].getValue());
			colour = (double[]) objectData[9].getValue();
			
		} catch (Exception e) {
			
			destination = (double[]) objectData[8].getValue();
			teleportMode = (boolean) objectData[9].getValue();
			
			try {
				
				teleportMode = (boolean) objectData[10].getValue();
				
			} catch (ArrayIndexOutOfBoundsException e2) {
			}
			
		}
	}

}
