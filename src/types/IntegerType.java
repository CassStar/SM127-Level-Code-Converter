package types;

import util.Utility;

public class IntegerType implements DataType {
	
	long value;
	String type;
	
	public IntegerType(String data) {
		
		value = Utility.parseInteger(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (long) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "IT"+String.valueOf(value);
	}
}
