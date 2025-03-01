package level;

import main.ConversionType;
import main.Converter;
import types.*;
import util.Utility;

public abstract class LevelObject {
	
	public int objectID;
	public String stringData;
	public DataType[] objectData;
	protected ConversionType conversionType;
	protected Object[] defaultValues;
	
	public LevelObject(String data,ConversionType type) throws Exception {
		
		stringData = data;
		conversionType = type;
		
		String[] splitData;
		
		if (data.endsWith(",")) {
			
			data = data.concat("--");
		}
		
		splitData = data.split(",");
		String tempType;
		
		try {
			
			tempType = Utility.getDataType(splitData[1]);
			
		} catch (Exception e) {
			
			tempType = "";
		}
		
		
		if (Utility.isValidType(tempType)) {
			
			// Add pallete of 0.
			if (data.indexOf(',') != -1) {
				
				data = data.substring(0,data.indexOf(',')+1)+"0,"+
						data.substring(data.indexOf(',')+1);
				
			} else {
				
				data += ",0";
			}
			
		} else if (data.split(",").length == 1) {
			
			data += ",0";
		}
		
		splitData = data.split(",");
		
		objectData = new DataType[splitData.length];
		
		int startIndex = 2;
		
		objectData[0] = new IDType(splitData[0]);
		objectID = (int) objectData[0].getValue();
		
		if (splitData[1].length() == 0) {
			
			splitData[1] = "0";
		}
		
		objectData[1] = new PalleteType(splitData[1]);
		
		for (int i = startIndex;i < splitData.length;i++) {
			
			String dataType;
			
			if (splitData[i].length() == 0) {
				
				dataType = "--";
				
			} else {
				
				dataType = Utility.getDataType(splitData[i]);
			}
			
			boolean isValid = Utility.isValidType(dataType);
			
			// Special case for On/Off Switch Controlled Moving Platform.
			if (objectID == 94 && dataType.equals("Nu")) {
				
				isValid = true;
			}
			
			if (!isValid) {
				
				throw new Exception("Invalid data type found while parsing an object!\n"
						+ "Value of: "+dataType+" is not a recognized data type.");
			}
			
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
				
			case "SA":
				
				objectData[i] = new DialogueType(splitData[i]);
				break;
				
			case "Nu":
				
				objectData[i] = new BooleanType("BL0");
				break;
				
			case "--":
				
				objectData[i] = null;
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
			
			boolean optimizableValue = (Converter.getOptimizeObjectCode() &&
					(i != 0 && Utility.areSameValue(objectData[i],defaultValues[i])));
			
			if (optimizableValue) {
				
				// Don't add anything to optimize the level code.
				
			} else {
				
				output.append(objectData[i].toString());
			}
			
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
