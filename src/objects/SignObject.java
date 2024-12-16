package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class SignObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,openMenu,onWall;
	public String text;
	
	public SignObject(String data,ConversionType type) throws Exception {
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
		text = String.valueOf(objectData[7].getValue());
		
		switch(objectData.length) {
		
		case 10:
			
			openMenu = (boolean) objectData[8].getValue();
			onWall = (boolean) objectData[9].getValue();
		
		case 8:
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
