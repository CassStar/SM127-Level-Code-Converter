package level;

import main.ConversionType;
import main.Converter.AreaGrid;
import util.Utility;

public class LevelCode {
	
	String levelData;
	String codeVersion,levelName,levelAuthor,levelDescription,levelThumbnail,levelHotBar;
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
		setLevelAuthor(base.getLevelAuthor());
		setLevelDescription(base.getLevelDescription());
		setLevelThumbnail(base.getLevelThumbnail());
		setLevelHotBar(base.getLevelHotBar());
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
	
	String getLevelAuthor() {
		
		return levelAuthor;
	}
	
	public void setLevelAuthor(String value) {
		
		levelAuthor = new String(value);
	}
	
	String getLevelDescription() {
		
		return levelDescription;
	}
	
	public void setLevelDescription(String value) {
		
		levelDescription = new String(value);
	}
	
	String getLevelThumbnail() {
		
		return levelThumbnail;
	}
	
	public void setLevelThumbnail(String value) {
		
		levelThumbnail = new String(value);
	}
	
	String getLevelHotBar() {
		
		return levelHotBar;
	}
	
	public void setLevelHotBar(String value) {
		
		levelHotBar = new String(value);
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
		
		// More data needs to be added to the level code depending on the version
		if (Utility.versionGreaterThanVersion(codeVersion,"0.4.9")) {
			
			code.append(levelAuthor);
			code.append(',');
			code.append(levelDescription);
			code.append(',');
			code.append(levelThumbnail);
			code.append(',');
		}
		code.append(levelHotBar);
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
