package types;

import util.Utility;

public class StringType implements DataType {
	
	String value;
	String type;
	
	public StringType(String data) {
		
		value = Utility.parseString(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (String) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "ST"+value;
	}
}
