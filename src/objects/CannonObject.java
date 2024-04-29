package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class CannonObject extends LevelObject {
	
	public int ID,pallete = 0,launchPower,minRotation,maxRotation;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible,facesRight;
	
	public CannonObject(String data,ConversionType type) throws Exception {
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
		launchPower = (int) objectData[7].getValue();
		minRotation = (int) objectData[8].getValue();
		maxRotation = (int) objectData[9].getValue();
		facesRight = (boolean) objectData[10].getValue();
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
