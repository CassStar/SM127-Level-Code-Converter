package util;

import main.ProgramLogger;
import main.ProgramLogger.LogType;
import types.DataType;

public class Utility {
	
	public static boolean versionGreaterThanVersion(String version1,String version2) {
		
		int compareValue = version1.compareTo(version2);
		
		if (compareValue > 0) {
			
			return true;
		}
		
		return false;
	}
	
	public static boolean areSameGameVersions(String version1,String version2) {
		
		String[] versions = {version1,version2};
		
		boolean hasVersion1 = false,hasVersion2 = false;
		
		for (String version:versions) {
			
			if (version.equals("0.7.1")) {
				
				hasVersion1 = true;
				
			} else if (version.equals("0.7.2")) {
				
				hasVersion2 = true;
			}
		}
		
		if (hasVersion1 && hasVersion2) {
			
			return true;
		}
		
		return version1.equals(version2);
	}
	
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
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on int arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the array.
	 * @return	The expanded/shrunk array. Expanded arrays have values of 0 for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	public static int[] expandIntegerArray(int[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new int[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		int[] newArray = new int[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on double arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the array.
	 * @return	The expanded/shrunk array. Expanded arrays have values of 0 for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	public static double[] expandDoubleArray(double[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new double[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		double[] newArray = new double[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	public static DataType[] ensureMinimumLength(DataType[] array,int length) {
		
		if (array.length >= length) {
			
			return array;
		}
		
		DataType[] newArray = new DataType[length];
		
		for (int i = 0;i < newArray.length;i++) {
			
			if (i < array.length) {
				
				newArray[i] = array[i];
				
			}
		}
		
		return newArray;
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Trims off any null values from the given array and returns the result.
	 * This method works exclusively with String arrays.</dd><hr>
	 * 
	 * @param array	The array to trim of null values.
	 * @return	A String array with no null values in it.
	 */
	public static String[] getTrimmedStringArray(String[] array) {
		
		// Variable for the new array.
		String[] newArray;
		// Length of the new array, will be changed later.
		int arrayLength = 0;
		
		// Determining the length of the new array by how many non-null values were in the
		// original.
		for (String val:array) {
			if (val != null) {
				arrayLength++;
			}
		}
		
		// Creating the new array with proper length.
		newArray = new String[arrayLength];
		// Index to put values into the new array.
		int index = 0;
		
		// Looping over every value of the old array.
		for (String string:array) {
			
			// Adding the value to the new array if it isn't null.
			if (string != null) {
				newArray[index] = string;
				index++;
			}
		}
		
		// Return the new array.
		return newArray.clone();
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Gets the first index an object appears in an array.</dd><hr>
	 * 
	 * @param array	The array to search through
	 * @param value	The value to saerch for.
	 * @return	The first index the value appears in the array, -1 if the value could not be
	 * 			found.
	 */
	public static int getIndexInArray(Object[] array,Object value) {
		
		// Search through the array.
		for (int i = 0;i < array.length;i++) {
			
			// Check if the values are equal.
			if (array[i].equals(value)) {
				// Found index of value.
				return i;
			}
		}
		// Could not find value in array.
		return -1;
	}
	
	public static String getDataType(String data) {
		
		return data.substring(0,2);
	}
	
	public static boolean isValidType(String type) {
		
		if (type.length() != 2) {
			return false;
		}
		
		String[] validDataTypes = {"V2","C2","FL","BL","CL","IT","ST","SA","--"};
		boolean isValid = false;
		
		for (String value:validDataTypes) {
			
			if (type.equals(value)) {
				isValid = true;
			}
		}
		
		return isValid;
	}
	
	public static String parseString(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("ST")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing String data type. "
					+ "Expected: ST Got: "+dataType,LogType.ERROR);
			
			return null;
		}
		
		data = data.substring(2);
		
		return data;
	}
	
	public static double[] parseColour(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("CL")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Colour data type. "
					+ "Expected: CL Got: "+dataType,LogType.ERROR);
			
			return null;
		}
		
		data = data.substring(2);
		
		double rValue = 0,gValue = 0,bValue = 0,aValue = 0;
		
		boolean hasAlpha = data.split("x").length == 4;
		
		try {
			
			int firstXIndex = data.indexOf('x');
			int secondXIndex = data.indexOf('x',firstXIndex+1);
			
			rValue = Double.parseDouble(data.substring(0,firstXIndex));
			gValue = Double.parseDouble(data.substring(firstXIndex+1,secondXIndex));
			
			if (hasAlpha) {
				
				bValue = Double.parseDouble(data.substring(secondXIndex+1,data.lastIndexOf('x')));
				aValue = Double.parseDouble(data.substring(data.lastIndexOf('x')+1));
				
			} else {
				
				bValue = Double.parseDouble(data.substring(secondXIndex+1));
			}
			
		} catch (NumberFormatException e) {
			
			ProgramLogger.logMessage("Error trying to parse Colour data type. Details below:",
					LogType.ERROR);
			e.printStackTrace();
			
			return null;
		}
		
		if (hasAlpha) {
			
			return new double[] {rValue,gValue,bValue,aValue};
		}
		
		return new double[] {rValue,gValue,bValue};
	}
	
	public static double[][] parseCurve2D(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("C2")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Curve2D data type. "
					+ "Expected: C2 Got: "+dataType,LogType.ERROR);
			
			return null;
		}
		
		data = data.substring(2).toLowerCase();
		
		String[] groups = data.split(":");
		
		double[][] ouput = new double[groups.length][];
		
		for (int i = 0;i < groups.length;i++) {
			
			String groupData = groups[i];
			
			int currentIndex = 0,nextIndex = groupData.indexOf('x');
			
			double[] values = {};
			
			try {
				
				while (groupData != null && groupData.length() > 0) {
					
					values = expandDoubleArray(values,1);
					
					values[values.length-1] = Double.parseDouble(groupData.substring(currentIndex,nextIndex));
					
					groupData = groupData.substring(Math.min(groupData.length(),nextIndex+1),groupData.length());
					
					nextIndex = groupData.indexOf('x');
					
					if (nextIndex == -1) {
						
						if (groupData.length() < 1) {
							
							break;
						}
						
						nextIndex = groupData.length();
					}
					
					while (nextIndex == 0) {
						
						groupData = groupData.substring(nextIndex+1,groupData.length());
						
						if (groupData.length() < 1) {
							
							break;
						}
						
						nextIndex = groupData.indexOf('x');
					}
				}
				
				ouput[i] = values.clone();
				
			} catch (NumberFormatException e) {
				
				ProgramLogger.logMessage("Error trying to parse Vector2 data type. Details "
						+ "below:",LogType.ERROR);
				e.printStackTrace();
				
				return null;
			}
		}
		
		return ouput;
	}
	
	public static double[] parseVector2D(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("V2")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Vector2 data type. "
					+ "Expected: V2 Got: "+dataType,LogType.ERROR);
			
			return null;
		}
		
		data = data.substring(2);
		
		double xValue = 0,yValue = 0;
		
		try {
			
			xValue = Double.parseDouble(data.substring(0,data.indexOf('x')));
			yValue = Double.parseDouble(data.substring(data.indexOf('x')+1));
			
		} catch (NumberFormatException e) {
			
			ProgramLogger.logMessage("Error trying to parse Vector2 data type. Details below:",
					LogType.ERROR);
			e.printStackTrace();
			
			return null;
		}
		
		return new double[] {xValue,yValue};
	}
	
	public static long parseInteger(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("IT")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Integer data type. "
					+ "Expected: IT Got: "+dataType,LogType.ERROR);
			
			return 0;
		}
		
		data = data.substring(2);
		
		try {
			
			return Long.parseLong(data);
			
		} catch (NumberFormatException e) {
			
			ProgramLogger.logMessage("Error trying to parse Integer data type. Details below:",
					LogType.ERROR);
			e.printStackTrace();
			
			return 0;
		}
	}
	
	public static double parseFloat(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("FL")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Float data type. "
					+ "Expected: FL Got: "+dataType,LogType.ERROR);
			
			return 0;
		}
		
		data = data.substring(2);
		
		try {
			
			return Double.parseDouble(data);
			
		} catch (NumberFormatException e) {
			
			ProgramLogger.logMessage("Error trying to parse Float data type. Details below:",
					LogType.ERROR);
			e.printStackTrace();
			
			return 0;
		}
	}
	
	public static boolean parseBoolean(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("BL")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Boolean data type. "
					+ "Expected: BL Got: "+dataType,LogType.ERROR);
			
			return false;
		}
		
		data = data.substring(2);
		
		if (data.equals("1")) {
			
			return true;
			
		} if (data.equals("0")) {
			
			return false;
		}
		
		ProgramLogger.logMessage("Invalid value for Boolean data type. Expected: 0 or 1 "
				+ "Got: "+data+" Will set value to 0.",LogType.ERROR);
		
		return false;
	}
	
	public static String[] parseDialogue(String data) {
		
		String dataType = getDataType(data);
		
		if (!dataType.equals("SA")) {
			
			ProgramLogger.logMessage("Found invalid prefix while parsing Dialogue data type. "
					+ "Expected: SA Got: "+dataType,LogType.ERROR);
			
			// Format: UnkownID,NPCExpressionID,NPCActionID,PlayerExpressionID,FollowUpTag,%3b,Text,
			//: (if another dialogue box follows), pattern repeats...
			return new String[] {"0","1","0","0","","%3b","This%20is%20a%20dialogue%20object.",":","0","1","0","0","",
					"3b","Try%20putting%20this%20on%20top%20of%20an%20NPC%20and%20see%20what%20happens%21"};
		}
		
		data = data.substring(2);
		
		int numberOfColons = 0;
		
		for (char character:data.toCharArray()) {
			
			if (character == ':') {
				
				numberOfColons++;
			}
		}
		
		String[] arrayData = new String[7+numberOfColons*8];
		
		// Expression and Action IDs.
		arrayData[0] = data.substring(0,1);
		arrayData[1] = data.substring(1,2);
		arrayData[2] = data.substring(2,3);
		arrayData[3] = data.substring(3,4);
		
		int textStartIndex = data.indexOf("%3b");
		
		// Follow up Tag.
		arrayData[4] = data.substring(4,textStartIndex);
		
		// Text start indicator.
		arrayData[5] = data.substring(textStartIndex,textStartIndex+3);
		
		// Dialogue Text.
		try {
			arrayData[6] = data.substring(textStartIndex+3,data.indexOf(':'));
			arrayData[7] = ":";
			
			data = data.substring(data.indexOf(':')+1);
			
		} catch (IndexOutOfBoundsException e) {
			
			arrayData[6] = data.substring(textStartIndex+3);
		}
		
		// Loop over the remaining dialogue boxes.
		for (int i = 8;i < arrayData.length;i += 8) {
			
			// Expression and Action IDs.
			arrayData[i] = data.substring(0,1);
			arrayData[i+1] = data.substring(1,2);
			arrayData[i+2] = data.substring(2,3);
			arrayData[i+3] = data.substring(3,4);
			
			textStartIndex = data.indexOf("%3b");
			
			// Follow up Tag.
			arrayData[i+4] = data.substring(4,textStartIndex);
			
			// Text start indicator.
			arrayData[i+5] = data.substring(textStartIndex,textStartIndex+3);
			
			// Dialogue Text.
			try {
				arrayData[i+6] = data.substring(textStartIndex+3,data.indexOf(':'));
				arrayData[i+7] = ":";
				
				data = data.substring(data.indexOf(':')+1);
				
			} catch (IndexOutOfBoundsException e) {
				
				arrayData[i+6] = data.substring(textStartIndex+3);
			}
		}
		
		return arrayData;
	}
	
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
	
	public static boolean areSameValue(DataType data1,Object data2) {
		
		switch (data1.getType()) {
		
		case "BL":
			
			boolean b1 = (boolean) data1.getValue(), b2 = (boolean) data2;
			
			return b1 == b2;
			
		case "CL":
			
			return areSameVectors((double[]) data1.getValue(),(double[]) data2);
			
		case "C2":
			
			double[][] cv1 = (double[][]) data1.getValue(),cv2 = (double[][]) data2;
			
			if (cv1 == null && cv2 == null) {
				
				return true;
				
			} else if (cv1 == null || cv2 == null) {
				
				return false;
				
			} else if (cv1.length != cv2.length) {
				
				return false;
			}
			
			for (int i = 0;i < cv1.length;i++) {
				
				if (!areSameVectors(cv1[i],cv2[i])) {
					
					return false;
				}
			}
			
			return true;
			
		case "SA":
			
			String[] d1 = (String[]) data1.getValue(),d2 =(String[]) data2;
			
			if (d1 == null && d2 == null) {
				
				return true;
				
			} else if (d1 == null || d2 == null) {
				
				return false;
				
			} else if (d1.length != d2.length) {
				
				return false;
				
			}
			
			for (int i = 0;i < d1.length;i++) {
				
				if (!d1[i].equals(d2[i])) {
					
					return false;
				}
			}
			
			return true;
			
		case "FL":
			
			return (double) data1.getValue() == (double) data2;
			
		case "IT":
			
			double d = Double.valueOf(String.valueOf(data2));
			d = Math.round(d);
			
			String s = String.valueOf(d);
			
			s = s.substring(0,s.indexOf('.'));
			
			return (long) data1.getValue() == Long.valueOf(s);
			
		case "PL":
			
			return (int) data1.getValue() == (int) data2;
			
		case "ST":
			
			return ((String) data1.getValue()).equals((String) data2);
			
		case "V2":
			
			return areSameVectors((double[]) data1.getValue(),(double[]) data2);
		}
		
		return true;
	}
	
	public static String doubleToString(double value) {
		
		if (value%1 == 0) {
			
			return String.valueOf((int) value);
		}
		
		return String.valueOf(value);
	}
	
	public static String booleanToString(boolean value) {
		
		return value? "BL1":"BL0";
	}
	
	public static String colourToString(double[] colour) {
		
		StringBuilder output = new StringBuilder("CL");
		
		for (double value:colour) {
			
			output = output.append(value);
			output = output.append('x');
		}
		
		output = output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
	
	public static String curve2DToString(double[][] curve) {
		
		StringBuilder output = new StringBuilder("C2");
		
		for (double[] segment:curve) {
			
			for (int i = 0;i < segment.length;i+=2) {
				
				output = output.append(segment[i]);
				output = output.append('x');
				output = output.append(segment[i+1]);
				output = output.append('X');
			}
			
			if (segment.length > 4) {
				
				output = output.deleteCharAt(output.length()-1);
			}
			
			output = output.append(':');
		}
		
		output = output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
	
	public static String dialogueToString(String[] dialogue) {
		
		StringBuilder output = new StringBuilder("SA");
		
		for (String section:dialogue) {
			
			output = output.append(section);
		}
		
		return output.toString();
	}
	
	public static String floatToString(double value) {
		
		return "FL"+value;
	}
	
	public static String integerToString(long integer) {
		
		return "IT"+integer;
	}
	
	public static String stringToString(String string) {
		
		return "ST"+string;
	}
	
	public static String vector2ToString(double[] vector,boolean flattenValues) {
		
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
}
