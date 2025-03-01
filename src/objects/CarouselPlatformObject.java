package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class CarouselPlatformObject extends LevelObject {
	
	public int ID,pallete = 0;
	public long parts,platformCount;
	public double[] position,scale,colour;
	public double rotation,speed,radius,startAngle;
	public boolean enabled,visible;
	
	public CarouselPlatformObject(String data,ConversionType type) throws Exception {
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
		parts = (long) objectData[7].getValue();
		speed = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		radius = (double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		platformCount = (long) objectData[10].getValue();
		colour = (double[]) objectData[11].getValue();
		startAngle = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		
		if (objectData.length > 12) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
