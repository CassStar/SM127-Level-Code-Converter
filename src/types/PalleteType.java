package types;

public class PalleteType implements DataType {

	int value;
	String type;
	
	public PalleteType(String data) {
		
		value = Integer.parseInt(data);
		type = "PL";
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
