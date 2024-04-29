package types;

import util.Utility;

public class BooleanType implements DataType {
	
	boolean value;
	String type;
	
	public BooleanType(String data) {
		
		value = Utility.parseBoolean(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (boolean) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		return "BL"+(value? "1":"0");
	}
}
