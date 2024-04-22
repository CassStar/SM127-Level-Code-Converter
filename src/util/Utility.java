package util;

public class Utility {
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on String arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	public static String[] expandStringArray(String[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new String[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		String[] newArray = new String[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String getDataType(String data) {
		
		return data.substring(0,2);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isValidType(String type) {
		
		if (type.length() != 2) {
			return false;
		}
		
		String[] validDataTypes = {"V2","C2","FL","BL","CL","IT","ST"};
		boolean isValid = false;
		
		for (String value:validDataTypes) {
			
			if (type.equals(value)) {
				isValid = true;
			}
		}
		
		return isValid;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static double[] parseVector2D(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("V2")) {
			
			System.err.printf("Found invalid prefix while parsing Vector2D data type.%n"
					+ "Expected: V2 Got: %s%n",prefix);
			
			return null;
		}
		
		data = data.replace(prefix,"");
		
		double xValue = 0,yValue = 0;
		
		try {
			
			xValue = Double.parseDouble(data.substring(0,data.indexOf('x')));
			yValue = Double.parseDouble(data.substring(data.indexOf('x')+1));
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Vector2D data type. Details below:");
			e.printStackTrace();
			
			return null;
		}
		
		return new double[] {xValue,yValue};
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static int parseInteger(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("IT")) {
			
			System.err.printf("Found invalid prefix while parsing Integer data type.%n"
					+ "Expected: IT Got: %s%n",prefix);
			
			return 0;
		}
		
		data = data.replace(prefix,"");
		
		try {
			
			return Integer.parseInt(data);
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Integer data type. Details below:");
			e.printStackTrace();
			
			return 0;
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static double parseFloat(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("FL")) {
			
			System.err.printf("Found invalid prefix while parsing Float data type.%n"
					+ "Expected: FL Got: %s%n",prefix);
			
			return 0;
		}
		
		data = data.replace(prefix,"");
		
		try {
			
			return Double.parseDouble(data);
			
		} catch (NumberFormatException e) {
			
			System.err.println("Error trying to parse Float data type. Details below:");
			e.printStackTrace();
			
			return 0;
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static boolean parseBoolean(String data) {
		
		String prefix = data.substring(0,2);
		
		if (!prefix.equals("BL")) {
			
			System.err.printf("Found invalid prefix while parsing Boolean data type.%n"
					+ "Expected: BL Got: %s%n",prefix);
			
			return false;
		}
		
		data = data.replace(prefix,"");
		
		if (data.equals("1")) {
			
			return true;
			
		} if (data.equals("0")) {
			
			return false;
		}
		
		System.err.printf("Invalid value for Boolean data type.%nExpected: 0 or 1 Got: %s%n"
				+ "Will set value to 0.%n",data);
		
		return false;
	}
	
	/**
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 */
	public static boolean areSameVectors(double[] vector1,double[] vector2) {
		
		if (vector1 == null && vector2 == null) {
			
			return true;
			
		} else if (vector1 == null || vector2 == null) {
			
			return false;
		}
		
		if (vector1.length != vector2.length) {
			
			return false;
		}
		
		for (int i = 0;i < vector1.length;i++) {
			
			if (vector1[i] != vector2[i]) {
				
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param vector
	 * @param flattenValues
	 * @return
	 */
	public static String vector2DToString(double[] vector,boolean flattenValues) {
		
		Object[] parsedVector = new Object[vector.length];
		
		for (int i = 0;i < vector.length;i++) {
			
			if ((int) vector[i] == vector[i]) {
				
				parsedVector[i] = (int) vector[i];
			} else {
				parsedVector[i] = vector[i];
			}
		}
		
		if (flattenValues) {
			
			return new String("V2"+(int) vector[0]+"x"+(int) vector[1]);
		}
		
		return new String("V2"+parsedVector[0]+"x"+parsedVector[1]);
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String booleanToString(boolean value) {
		
		return value? "BL1":"BL0";
	}
}
