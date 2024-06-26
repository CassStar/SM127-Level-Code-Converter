package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class FirePillarObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation,retractedTime,burningTime,offset;
	public boolean enabled,visible,reversed;
	
	public FirePillarObject(String data,ConversionType type) throws Exception {
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
		retractedTime = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		burningTime = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (double[]) objectData[9].getValue();
		reversed = (boolean) objectData[10].getValue();
		offset = (double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		
		try {
			
			pallete = (int) objectData[1].getValue();
			
		} catch (Exception e) {
		}
	}

}
