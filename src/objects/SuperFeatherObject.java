package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class SuperFeatherObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale;
	public double rotation,duration;
	public boolean enabled,visible,canRespawn,powerupMusic;
	
	public SuperFeatherObject(String data,ConversionType type) throws Exception {
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
		duration = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		canRespawn = (boolean) objectData[8].getValue();
		
		switch (objectData.length) {
		
		case 10:
			
			powerupMusic = (boolean) objectData[9].getValue();
		
		case 9:
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
