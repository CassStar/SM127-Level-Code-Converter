package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class KoopaTroopaObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation;
	public boolean enabled,visible,rainbow,troopaType,shelled;
	
	public KoopaTroopaObject(String data,ConversionType type) throws Exception {
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
		colour = (double[]) objectData[7].getValue();
		rainbow = (boolean) objectData[8].getValue();
		troopaType = (boolean) objectData[9].getValue();
		
		switch(objectData.length) {
		
		case 11:
			
			shelled = (boolean) objectData[10].getValue();
		
		case 10:
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
