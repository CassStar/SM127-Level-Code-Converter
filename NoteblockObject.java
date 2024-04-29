package objects;

import level.LevelObject;
import main.ConversionType;
import util.Utility;

public class NoteblockObject extends LevelObject {
	
	public int ID,pallete = 0,parts,strongBouncePower;
	public double[] position,scale;
	public double rotation;
	public boolean enabled,visible;
	
	public NoteblockObject(String data,ConversionType type) throws Exception {
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
		strongBouncePower = (int) objectData[8].getValue();
		
		if (Utility.versionGreaterThanVersion(conversionType.gameVersionFrom,"0.6.9")) {
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
