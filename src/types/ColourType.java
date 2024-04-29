package types;

import util.Utility;

public class ColourType implements DataType {
	
	double[] value;
	String type;
	
	public ColourType(String data) {
		
		value = Utility.parseColour(data);
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
		
		String output = "CL";
		
		for (int i = 0;i < value.length;i++) {
			
			output += Utility.doubleToString(value[i]);
			
			if (i != value.length-1) {
				
				output += "x";
			}
		}
		
		
		return output;
	}
}
