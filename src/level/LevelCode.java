package level;

import main.ConversionType;
import main.Converter.AreaGrid;

public class LevelCode {
	
	String levelData;
	String codeVersion,levelName,emptyArray;
	int numberOfAreas;
	AreaCode[] areaCodes;
	
	ConversionType conversionType;
	
	public LevelCode(String code,AreaGrid areaGrid,ConversionType type) {
		
		setConversionType(type);
		
		if (areaGrid != null) {
			
			LevelRearranger rearranger = new LevelRearranger(this,code,areaGrid);
			
			code = rearranger.getCode();
		}
		
		setLevelData(code);
	}
	
	public LevelCode(String code,int numberOfAreas,ConversionType type) {
		
		setLevelData(code);
		setConversionType(type);
		setNumberOfAreas(numberOfAreas);
	}
	
	public LevelCode(LevelCode base) {
		
		setLevelData(base.getLevelData());
		setCodeVersion(base.getCodeVersion());
		setLevelName(base.getLevelName());
		setEmptyArray(base.getEmptyArray());
		setNumberOfAreas(base.getNumberOfAreas());
		setAreaCodes(base.getAreaCodes());
	}
	
	public String getLevelData() {
		
		return levelData;
	}
	
	void setLevelData(String value) {
		
		levelData = new String(value);
	}
	
	AreaCode[] getAreaCodes() {
		
		return areaCodes.clone();
	}
	
	void setAreaCodes(AreaCode[] value) {
		
		areaCodes = value.clone();
	}
	
	public AreaCode getAreaCode(int area) {
		
		return areaCodes[area];
	}
	
	String getCodeVersion() {
		
		return codeVersion;
	}
	
	public void setCodeVersion(String value) {
		
		codeVersion = new String(value);
	}
	
	String getLevelName() {
		
		return levelName;
	}
	
	public void setLevelName(String value) {
		
		levelName = new String(value);
	}
	
	String getEmptyArray() {
		
		return emptyArray;
	}
	
	public void setEmptyArray(String value) {
		
		emptyArray = new String(value);
	}
	
	public int getNumberOfAreas() {
		
		return Integer.valueOf(numberOfAreas);
	}
	
	public void setNumberOfAreas(int value) {
		
		numberOfAreas = Integer.valueOf(value);
		
		areaCodes = new AreaCode[numberOfAreas];
		
		for (int i = 0;i < areaCodes.length;i++) {
			
			areaCodes[i] = new AreaCode(this);
		}
	}
	
	public void setConversionType(ConversionType type) {
		
		conversionType = type;
	}
	
	/*
	 * ^ Getters and Setters ^
	 * 
	 * v Functional Methods v
	 */
	
	public void generateCode() {
		
		StringBuilder code = new StringBuilder();
		
		code.append(codeVersion);
		code.append(',');
		code.append(levelName);
		code.append(',');
		code.append(emptyArray);
		code.append(',');
		
		for (int i = 0;i < areaCodes.length;i++) {
			
			areaCodes[i].generateAreaData();
			
			code.append(areaCodes[i].getAreaData());
			code.append(',');
		}
		
		code.deleteCharAt(code.length()-1);
		
		setLevelData(code.toString());
	}
}
