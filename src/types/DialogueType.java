package types;

import util.Utility;

public class DialogueType implements DataType {
	
	String[] value;
	String type;
	
	public DialogueType(String data) {
		
		value = Utility.parseDialogue(data);
		type = Utility.getDataType(data);
	}
	
	public Object getValue() {
		
		return value;
	}
	
	public void setValue(Object data) {
		
		value = (String[]) data;
	}
	
	public String getType() {
		
		return type;
	}
	
	public String toString() {
		
		StringBuilder output = new StringBuilder("SA");
		
		for (String val:value) {
			
			output.append(val);
		}
		
		return output.toString();
	}
}
