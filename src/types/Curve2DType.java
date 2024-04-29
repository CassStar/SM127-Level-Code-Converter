package types;

import util.Utility;

public class Curve2DType implements DataType {
	
	double[][] value = {};
	String type;
	
	public Curve2DType(String data) {
		
		value = Utility.parseCurve2D(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (double[][]) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		StringBuilder output = new StringBuilder("C2");
		
		for (int i = 0;i < value.length;i++) {
			
			output = output.append(Utility.doubleToString(value[i][0]));
			output = output.append("x");
			output = output.append(Utility.doubleToString(value[i][1]));
			output = output.append("XX");
			
			if (value[i].length > 2) {
				
				output = output.append(Utility.doubleToString(value[i][2]));
				output = output.append("x");
				output = output.append(Utility.doubleToString(value[i][3]));
			}
			
			output = output.append(":");
		}
		
		output = output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
}
