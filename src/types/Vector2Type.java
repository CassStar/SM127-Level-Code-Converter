package types;

import util.Utility;

public class Vector2Type implements DataType {
	
	double[] value;
	String type;
	
	public Vector2Type(String data) {
		
		value = Utility.parseVector2D(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (double[]) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "V2"+Utility.doubleToString(value[0])+"x"+Utility.doubleToString(value[1]);
	}
}
