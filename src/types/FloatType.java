package types;

import util.Utility;

public class FloatType implements DataType {
	
	double value;
	String type;
	
	public FloatType(String data) {
		
		value = Utility.parseFloat(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (double) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "FL"+Utility.doubleToString(value);
		
	}
}
