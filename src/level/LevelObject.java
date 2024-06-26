package level;

import main.ConversionType;
import types.*;
import util.Utility;

public abstract class LevelObject {
	
	public int objectID;
	public String stringData;
	public DataType[] objectData;
	protected ConversionType conversionType;
	
	public LevelObject(String data,ConversionType type) throws Exception {
		
		stringData = data;
		conversionType = type;
		
		String[] splitData = data.split(",");
		String tempType;
		
		try {
			
			tempType = Utility.getDataType(splitData[1]);
			
		} catch (Exception e) {
			
			tempType = "";
		}
		
		
		if (Utility.isValidType(tempType)) {
			
			// Add pallete of 0.
			data = data.substring(0,data.indexOf(',')+1)+"0,"+
			data.substring(data.indexOf(',')+1);
		}
		
		splitData = data.split(",");
		
		objectData = new DataType[splitData.length];
//		dataTypes = new String[splitData.length];
		
		int startIndex = 2;
		
		objectData[0] = new IDType(splitData[0]);
		objectID = (int) objectData[0].getValue();
//		dataTypes[0] = "ID";
		
		objectData[1] = new PalleteType(splitData[1]);
//		dataTypes[1] = "PL";
		
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
			
//			dataTypes[i] = dataType;
			
			switch (dataType) {
			case "V2":
				
				objectData[i] = new Vector2Type(splitData[i]);
				break;
				
			case "C2":
				
				objectData[i] = new Curve2DType(splitData[i]);
				break;
				
			case "CL":
				
				objectData[i] = new ColourType(splitData[i]);
				break;
				
			case "ST":
				
				objectData[i] = new StringType(splitData[i]);
				break;
				
			case "FL":
				
				objectData[i] = new FloatType(splitData[i]);
				break;
				
			case "BL":
				
				objectData[i] = new BooleanType(splitData[i]);
				break;
				
			case "IT":
				
				objectData[i] = new IntegerType(splitData[i]);
				break;
				
			case "Nu":
				
//				dataTypes[i] = "BL";
				objectData[i] = new BooleanType("BL0");
			}
		}
	}
	
	public static int getObjectID(String data) {
		
		String[] splitData = data.split(",");
		
		return Integer.parseInt(splitData[0]);
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
	
	protected abstract double[] getPosition();
	protected abstract void setPosition(double[] position);
	
	public String toString() {
				
		StringBuilder output = new StringBuilder();
		
		for (int i = 0;i < objectData.length;i++) {
			
			if (i == 1 && !Utility.versionGreaterThanVersion(conversionType.gameVersionTo,"0.6.9")) {
				continue;
			}
			output.append(objectData[i].toString());
			output.append(",");
		}
		
		output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
	
	public String toString(boolean forceShowPallete) {
		
		if (!forceShowPallete) {
			
			return this.toString();
		}
		
		StringBuilder output = new StringBuilder();
		
		for (int i = 0;i < objectData.length;i++) {
			
			output.append(objectData[i].toString());
			output.append(",");
		}
		
		output.deleteCharAt(output.length()-1);
		
		return output.toString();
	}
}
