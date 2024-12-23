package objects;

import level.LevelObject;
import main.ConversionType;
import types.Vector2Type;
import util.Utility;

public class DryBonesObject extends LevelObject {
	
	public int ID,pallete = 0,respawnMode,maxEnemies;
	public double[] position,scale,initialVelocity;
	public double rotation,respawnTime,spawnOffset,tossWait,regenerateTime;
	public boolean enabled,visible,isStationary;
	
	public DryBonesObject(String data,ConversionType type) throws Exception {
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
		respawnTime = (double) Double.valueOf(String.valueOf(objectData[7].getValue()));
		respawnMode = (int) objectData[8].getValue();
		maxEnemies = (int) objectData[9].getValue();
		initialVelocity = (double[]) objectData[10].getValue();
		spawnOffset = (double) Double.valueOf(String.valueOf(objectData[11].getValue()));
		isStationary = (boolean) objectData[12].getValue();
		tossWait = (double) Double.valueOf(String.valueOf(objectData[13].getValue()));
		regenerateTime = (double) Double.valueOf(String.valueOf(objectData[14].getValue()));
	}
}