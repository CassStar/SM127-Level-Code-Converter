package types;

public class IDType implements DataType {
	
	int value;
	String type;
	
	public IDType(String data) {
		
		value = Integer.parseInt(data);
		type = "ID";
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
		
		return String.valueOf(value);
	}
}
