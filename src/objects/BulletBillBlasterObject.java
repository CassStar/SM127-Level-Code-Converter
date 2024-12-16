package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class BulletBillBlasterObject extends LevelObject {
	
	public int ID,pallete = 0,chaseDirection;
	public double[] position,scale,colour;
	public double rotation,speed,waitTime,offset;
	public boolean enabled,visible,chase,invincible;
	
	public BulletBillBlasterObject(String data,ConversionType type) throws Exception {
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
		chase = (boolean) objectData[7].getValue();
		speed = (double) Double.valueOf(String.valueOf(objectData[8].getValue()));
		colour = (double[]) objectData[9].getValue();
		waitTime = (double) Double.valueOf(String.valueOf(objectData[10].getValue()));
		invincible = (boolean) objectData[11].getValue();
		chaseDirection = (int) objectData[12].getValue();
		
		switch(objectData.length) {
		
		case 14:
			
			offset = (double) Double.valueOf(String.valueOf(objectData[13].getValue()));
			
		case 13:
			
			pallete = (int) objectData[1].getValue();
		}
	}

}
