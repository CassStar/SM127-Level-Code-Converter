package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class ShineSpriteObject extends LevelObject {
	
	public int ID,pallete = 0,shinePlacedIndex,sortIndex;
	public double[] position,scale,colour;
	public double rotation;
	public boolean enabled,visible,showInMenu,activated,redCoinsActivate,shineShardsActivate,doKickOut;
	public String title,description;
	
	public ShineSpriteObject(String data,ConversionType type) throws Exception {
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
		title = String.valueOf(objectData[7].getValue());
		description = String.valueOf(objectData[8].getValue());
		showInMenu = (boolean) objectData[9].getValue();
		activated = (boolean) objectData[10].getValue();
		redCoinsActivate = (boolean) objectData[11].getValue();
		shineShardsActivate = (boolean) objectData[12].getValue();
		colour = (double[]) objectData[13].getValue();
		shinePlacedIndex = (int) objectData[14].getValue();
		doKickOut = (boolean) objectData[15].getValue();
		sortIndex = (int) objectData[16].getValue();
		
		try {
			
			pallete = (int) objectData[1].getValue();
			
		} catch (Exception e) {
		}
	}

}
