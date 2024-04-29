package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class CarouselPlatformObject extends LevelObject {
	
	public int ID,pallete = 0,parts,platformCount;
	public double[] position,scale,colour;
	public double rotation,speed,radius,startAngle;
	public boolean enabled,visible;
	
	public CarouselPlatformObject(String data,ConversionType type) throws Exception {
		super(data,type);
		setupValues();
	}
	
	void setupValues() {
		
		ID = objectID;
		position = (double[]) objectData[2].getValue();
		scale = (double[]) objectData[3].getValue();
		rotation = (double) Double.valueOf(String.valueOf(objectData[4].getValue()));
		enabled = (boolean) objectData[5].getValue();
		visible = (boolean) objectData[6].getValue();
		parts = (int) objectData[7].getValue();
		speed = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		radius = (double) Double.valueOf(String.valueOf(objectData[9].getValue()));
		platformCount = (int) objectData[10].getValue();
		colour = (double[]) objectData[11].getValue();
		startAngle = (double) Double.valueOf(String.valueOf(objectData[12].getValue()));
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
