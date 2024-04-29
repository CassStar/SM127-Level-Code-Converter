package types;

import util.Utility;

public class IntegerType implements DataType {
	
	int value;
	String type;
	
	public IntegerType(String data) {
		
		value = Utility.parseInteger(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (int) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "IT"+String.valueOf(value);
	}
}
