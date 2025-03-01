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
			
			for (int j = 0;j < value[i].length;j += 2) {
				
				output = output.append(Utility.doubleToString(value[i][j]));
				output = output.append("x");
				output = output.append(Utility.doubleToString(value[i][j+1]));
				output = output.append("X");
			}
			
			if (value[i].length < 3) {
				
				output = output.append("X");
				
			} else if (value[i].length == 6) {
				
				output = output.deleteCharAt(output.length()-1);
			}
			
			output = output.append(":");
		}
		
		output = output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
}
