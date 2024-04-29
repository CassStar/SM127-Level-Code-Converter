package main;

import util.Utility;

public class ConversionType {
	
	public boolean isValid;
	public int[] minBackerBG,minFronterBG,minMusicID,minObjectID,minTileID;
	public int[] maxBackerBG,maxFronterBG,maxMusicID,maxObjectID,maxTileID;
	public String gameVersionFrom,gameVersionTo,codeVersionFrom,codeVersionTo;
	
	int[] allMinBackerBG = {0,0,0,0,0};
	int[] allMinFronterBG = {0,0,0,0,0};
	int[] allMinMusicID = {0,0,0,0,0};
	int[] allMinObjectID = {0,0,0,0,0};
	int[] allMinTileID = {0,0,0,0,0};
	
	int[] allMaxBackerBG = {5,5,6,7,7};
	int[] allMaxFronterBG = {5,7,14,14,14};
	int[] allMaxMusicID = {37,39,62,65,66};
	int[] allMaxObjectID = {68,69,90,96,97};
	int[] allMaxTileID = {210,250,350,360,360};
	
	String[] validGameVersions = {"0.6.0","0.6.1","0.7.0","0.7.1","0.7.2"};
	String[] validCodeVersions = {"0.4.3","0.4.5","0.4.8","0.4.9","0.4.9"};
	
	public ConversionType(boolean fromGameVersion,String versionFrom,String versionTo) {
		
		if (fromGameVersion) {
			
			isValid = getAreValidGameVersions(new String[] {versionFrom,versionTo});
			
			if (!isValid) {
				return;
			}
			
			gameVersionFrom = versionFrom;
			gameVersionTo = versionTo;
			
			int[] indexes = getIndexes(true,gameVersionFrom,gameVersionTo);
			
			codeVersionFrom = validCodeVersions[indexes[0]];
			codeVersionTo = validCodeVersions[indexes[1]];
			
			setupValues(indexes);
			
		} else {
			
			isValid = getAreValidCodeVersions(new String[] {versionFrom,versionTo});
			
			if (!isValid) {
				return;
			}
			
			codeVersionFrom = versionFrom;
			codeVersionTo = versionTo;
			
			int[] indexes = getIndexes(false,codeVersionFrom,codeVersionTo);
			
			gameVersionFrom = validGameVersions[indexes[0]];
			gameVersionTo = validGameVersions[indexes[1]];
			
			setupValues(indexes);
		}
	}
	
	public boolean getAreValidGameVersions(String[] versions) {
		
		for (String version:versions) {
			
			boolean isValid = false;
			
			for (String validVersion:validGameVersions) {
				
				if (version.equals(validVersion)) {
					
					isValid = true;
				}
			}
			
			if (!isValid) {
				
				return false;
			}
		}
		
		return true;
	}
	
	public boolean getAreValidCodeVersions(String[] versions) {
		
		for (String version:versions) {
			
			boolean isValid = false;
			
			for (String validVersion:validCodeVersions) {
				
				if (version.equals(validVersion)) {
					
					isValid = true;
				}
			}
			
			if (!isValid) {
				
				return false;
			}
		}
		
		return true;
	}
	
	int[] getIndexes(boolean fromGameVersion,String versionFrom,String versionTo) {
		
		if (fromGameVersion) {
			return new int[] {
					Utility.getIndexInArray(validGameVersions,versionFrom),
					Utility.getIndexInArray(validGameVersions,versionTo)};
		}
		
		return new int[] {
				Utility.getIndexInArray(validCodeVersions,versionFrom),
				Utility.getIndexInArray(validCodeVersions,versionTo)};
	}
	
	void setupValues(int[] indexes) {
		
		minBackerBG = new int[] {allMinBackerBG[indexes[0]],allMinBackerBG[indexes[1]]};
		minFronterBG = new int[] {allMinFronterBG[indexes[0]],allMinFronterBG[indexes[1]]};
		minMusicID = new int[] {allMinMusicID[indexes[0]],allMinMusicID[indexes[1]]};
		minObjectID = new int[] {allMinObjectID[indexes[0]],allMinObjectID[indexes[1]]};
		minTileID = new int[] {allMinTileID[indexes[0]],allMinTileID[indexes[1]]};
		
		maxBackerBG = new int[] {allMaxBackerBG[indexes[0]],allMaxBackerBG[indexes[1]]};
		maxFronterBG = new int[] {allMaxFronterBG[indexes[0]],allMaxFronterBG[indexes[1]]};
		maxMusicID = new int[] {allMaxMusicID[indexes[0]],allMaxMusicID[indexes[1]]};
		maxObjectID = new int[] {allMaxObjectID[indexes[0]],allMaxObjectID[indexes[1]]};
		maxTileID = new int[] {allMaxTileID[indexes[0]],allMaxTileID[indexes[1]]};
	}
	
	String listValidGameVersions() {
		
		String output = "[";
		
		for (int i = 0;i < validGameVersions.length-1;i++) {
			
			output += validGameVersions[i]+", ";
		}
		
		output += validGameVersions[validGameVersions.length-1]+"]";
		
		return output;
	}
	
	String getGameVersionFrom() {
		
		if (codeVersionFrom.equals("0.4.9")) {
			
			return "0.7.1 or 0.7.2";
		}
		
		return gameVersionFrom;
	}
	
	boolean areSameGameVersions(String version1,String version2) {
		
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
}
