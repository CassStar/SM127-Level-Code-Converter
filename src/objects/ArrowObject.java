package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class ArrowObject extends LevelObject {
	
	public int ID,pallete = 0;
	public double[] position,scale,colour;
	public double rotation;
	public boolean enabled,visible,showBehindPlayer,moves;
	
	public ArrowObject(String data,ConversionType type) throws Exception {
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
		showBehindPlayer = (boolean) objectData[7].getValue();
		
		try {
			
			colour = (double[]) objectData[8].getValue();
			
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		
		try {
			
			pallete = (int) objectData[1].getValue();
			moves = (boolean) objectData[8].getValue();
			
		} catch (Exception e) {
			
			// Making sure moves has a value when converting levels made in 0.8.0
			if (conversionType.gameVersionFrom.equals("0.8.0")) {
				
				moves = true;
			}
		}
	}

}
