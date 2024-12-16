package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class WaterObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation,width,height,toxicity;
	public boolean enabled,visible,renderInFront,tapMode;
	public String tag;
	
	public WaterObject(String data,ConversionType type) throws Exception {
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
		width = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		height = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (double[]) objectData[9].getValue();
		renderInFront = (boolean) objectData[10].getValue();
		tag = String.valueOf(objectData[11].getValue());
		toxicity = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		tapMode = (boolean) objectData[13].getValue();
	}

}
