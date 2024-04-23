package level;

import util.Utility;
import tools.superMario127.Converter.ConversionType;

public class LevelObject {
	
	public int objectID;
	public String stringData;
	public Object[] objectData;
	public String[] dataTypes;
	ConversionType conversionType;
	
	public LevelObject(String data) throws Exception {
		
		stringData = data;
		
		if (conversionType == ConversionType.OLD_TO_NEW) {
			
			// Add pallete of 0.
			data = data.substring(0,data.indexOf(',')+1)+"0,"+
					data.substring(data.indexOf(',')+1);
			
		}
		
		String[] splitData = data.split(",");
		
		objectData = new Object[splitData.length];
		dataTypes = new String[splitData.length];
		
		int startIndex = 2;
		
		objectID = Integer.parseInt(splitData[0]);
		objectData[0] = objectID;
		dataTypes[0] = "ID";
		
		objectData[1] = splitData[1];
		dataTypes[1] = "PL";
		
		for (int i = startIndex;i < splitData.length;i++) {
			
			String dataType = Utility.getDataType(splitData[i]);
			
			boolean isValid = Utility.isValidType(dataType);
			
			// Special case for On/Off Switch Controlled Moving Platform.
			if (objectID == 94 && dataType.equals("Nu")) {
				
				isValid = true;
			}
			
			if (!isValid) {
				
				throw new Exception("Invalid data type found while parsing an object!\n"
						+ "Value of: "+dataType+" is not a recognized data type.");
			}
			
			dataTypes[i] = dataType;
			
			switch (dataType) {
			case "V2":
				
				objectData[i] = Utility.parseVector2D(splitData[i]);
				break;
				
			case "C2","CL","ST":
				
				objectData[i] = splitData[i];
				break;
				
			case "FL":
				
				objectData[i] = Utility.parseFloat(splitData[i]);
				break;
				
			case "BL":
				
				objectData[i] = Utility.parseBoolean(splitData[i]);
				break;
				
			case "IT":
				
				objectData[i] = Utility.parseInteger(splitData[i]);
				break;
				
			case "Nu":
				
				dataTypes[i] = "BL";
				objectData[i] = false;
			}
		}
	}
	
	public static int getObjectID(String data) {
		
		String[] splitData = data.split(",");
		
		return Integer.parseInt(splitData[0]);
	}
	
	public void setConversionType(ConversionType type) {
		
		conversionType = type;
	}
	
	void removeLastValue() {
		
		objectData = expandObjectArray(objectData,-1);
		dataTypes = Utility.expandStringArray(dataTypes,-1);
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Expands or shrinks the given array using the given amount. This
	 * method works exclusively on Object arrays.</dd><hr>
	 * 
	 * @param array		The array to expand/shrink.
	 * @param amount	The amount to expand the array by. Negative values will shrink the
	 * 					array.
	 * @return	The expanded/shrunk array. Expanded arrays have null values for new indexes,
	 * 			shrunk arrays may lose data at removed indexes.
	 */
	Object[] expandObjectArray(Object[] array,int amount) {
		
		// Null check.
		if (array == null) {
			array = new Object[0];
		}
		
		// Making sure the amount to shrink by doesn't exceed the current array length.
		if (array.length+amount < 0) {
			amount = array.length*-1;
		}
		
		// Create a new array with increased/decreased capacity.
		Object[] newArray = new Object[array.length+amount];
		
		// Copy values from the old array to the new array.
		for (int i = 0;i < Math.min(newArray.length,array.length);i++) {
			newArray[i] = array[i];
		}
		
		// Return the new expanded/shrunk array.
		return newArray.clone();
	}
	
	public boolean equals(Object o) {
		
		if (o == null) {
			return false;
		}
		if (!(o instanceof LevelObject)) {
			return false;
		}
		
		LevelObject l = (LevelObject) o;
		
		if (!stringData.equals(l.stringData)) {
			return false;
		}
		
		return true;
	}
	
	public String toString() {
		
		String output = "";
		
		for (int i = 0;i < objectData.length;i++) {
			
			if (dataTypes[i].equals("V2")) {
				
				output += Utility.vector2DToString(
						(double[]) objectData[i],false)+",";
				
			} else if (dataTypes[i].equals("PL")) {
				
				if (conversionType == ConversionType.OLD_TO_NEW) {
					
					output += objectData[i]+",";
				}
				
			} else if (dataTypes[i].equals("ID") || dataTypes[i].equals("ST") ||
					dataTypes[i].equals("CL") || dataTypes[i].equals("C2")) {
				
				output += objectData[i]+",";
				
			} else if (dataTypes[i].equals("BL")) {
				
				output += dataTypes[i]+((boolean) objectData[i]? "1,":"0,");
				
			} else {
				
				output += dataTypes[i]+objectData[i]+",";
			}
		}
		
		output = output.substring(0,output.length()-1);
		
		return output;
	}
}
