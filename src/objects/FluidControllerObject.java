package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class FluidControllerObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale;
	public double rotation,moveSpeed,offset,cycleTimer,cycleOffset;
	public boolean enabled,visible,autoActivate,horizontal;
	public String tag;
	
	public FluidControllerObject(String data,ConversionType type) throws Exception {
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
		tag = String.valueOf(objectData[7].getValue());
		autoActivate = (boolean) objectData[8].getValue();
		moveSpeed = (double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		offset = (double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		horizontal = (boolean) objectData[11].getValue();
		
		if (objectData.length > 12) {
			
			cycleTimer = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
			cycleOffset = (double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		}
	}

}
