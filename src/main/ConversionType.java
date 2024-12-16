package main;

import util.Utility;

public class ConversionType {
	
	public boolean isValid;
	public int[] minBackerBG,minFronterBG,minMusicID,minObjectID,minTileID;
	public int[] maxBackerBG,maxFronterBG,maxMusicID,maxObjectID,maxTileID;
	public String gameVersionFrom,gameVersionTo,codeVersionFrom,codeVersionTo;
	
	int[] allMaxBackerBG = {5,5,6,7,7,12,13};
	int[] allMaxFronterBG = {5,7,14,14,14,22,22};
	int[] allMaxMusicID = {37,39,62,65,66,67,71};
	int[] allMaxObjectID = {68,69,90,96,97,125,139};
	int[] allMaxTileID = {210,250,350,360,360,410,410};
	
	String[] validGameVersions = {"0.6.0","0.6.1","0.7.0","0.7.1","0.7.2","0.8.0","0.9.0"};
	String[] validCodeVersions = {"0.4.5","0.4.5","0.4.8","0.4.9","0.4.9","0.4.9","0.5.0"};
	
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
				
				if (versionFrom.equals("0.4.3")) {
					
					isValid = true;
					
					versionFrom = "0.4.5";
					versionTo = "0.4.5";
					
				} else {
					return;
				}
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
		
		minBackerBG = new int[] {0,0};
		minFronterBG = new int[] {0,0};
		minMusicID = new int[] {0,0};
		minObjectID = new int[] {0,0};
		minTileID = new int[] {0,0};
		
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
			
			return "0.7.1 or 0.7.2 or 0.8.0";
			
		} else if (codeVersionFrom.equals("0.4.5")) {
			
			return "0.6.0 or 0.6.1";
		}
		
		return gameVersionFrom;
	}
}
