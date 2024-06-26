package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import main.ProgramLogger.LogType;

public class UpdateChecker {
	
	private static final String VERSION = "V0.3.2",LATEST_URL = "https://github.com/CassStar/SM127-Level-Code-Converter/releases/latest";
	private Scanner input;
	private String workingDirectory;
	
	UpdateChecker(Scanner input,String workingDir) {
		
		Path wgetPath = Path.of(workingDir+"\\wget");
		
		if (!Files.exists(wgetPath)) {
			
			ProgramLogger.logMessage("wget folder doesn't exist, skipping update check.",LogType.WARNING);
			return;
		}
		
		this.input = input;
		workingDirectory = workingDir;
		
		ProgramLogger.logMessage("Checking for updates... Current program version: "+VERSION,LogType.INFO);
		
		String latestVersion = null;
		
		try {
			
			latestVersion = getLatestVersionNumber();
			
		} catch (IOException e) {
			
			latestVersion = null;
		}
		
		
		if (latestVersion == null) {
			
			ProgramLogger.logMessage("Couldn't get latest program version from url: "+LATEST_URL+
					"\nUpdate checking will be skipped.",LogType.WARNING);
			
			ProgramLogger.logMessage("Update check finished.",LogType.INFO);
			return;
		}
		
		int difference = VERSION.compareTo(latestVersion);
		boolean updated = false;
		
		if (difference < 0) {
			
			ProgramLogger.logMessage("A newer version of the program is available!",LogType.INFO);
			
			boolean updateVersion = getYesNo("Would you like to update the program from: "+VERSION+" to: "+latestVersion+"? (Y/N): ",
					"%nInvalid value entered.%nWould you like to update the program from: "+VERSION+" to: "+latestVersion+"? (Y/N): ");
			
			if (updateVersion) {
				
				updated = updateProgram(latestVersion.toLowerCase());
				
				if (updated) {
					
					ProgramLogger.logMessage("Updated program to latest version!",LogType.INFO);
					
					String downloadOutput = "NEW DOWNLOAD Code Converter "+latestVersion;
					
					ProgramLogger.logMessage("You can find the new program in the folder: "+downloadOutput,LogType.INFO);
					
				} else {
					
					ProgramLogger.logMessage("Could not update program to latest version!",LogType.ERROR);
				}
			}
			
		} else if (difference == 0) {
			
			ProgramLogger.logMessage("You have the latest version of the program.",LogType.INFO);
			
		} else {
			
			ProgramLogger.logMessage("Your version of the program is newer than the latest version found online! This may be a bug.",
					LogType.WARNING);
		}
		
		ProgramLogger.logMessage("Update check finished.",LogType.INFO);
		
		if (updated) {
			
			ProgramLogger.logMessage("Please close this program. Extract and use the new program version for added functionality/stability.",
					LogType.WARNING);
			input.nextLine();
		}
	}
	
	private String getLatestVersionNumber() throws IOException {
		
		URL latestURL = new URL(LATEST_URL);
		URLConnection connection = latestURL.openConnection();
		BufferedReader dataReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		StringBuilder data = new StringBuilder(dataReader.readLine());
		
		while (true) {
			
			String line = dataReader.readLine();
			
			if (line == null) {
				break;
			}
			
			data.append(line);
		}
		
		int tagStart = data.indexOf("<h1 data-view-component=\"true\" class=\"d-inline mr-3\">");
		int versionStart = data.indexOf("V",tagStart);
		int versionEnd = data.indexOf("</h1>",versionStart);
		
		return data.substring(versionStart,versionEnd);
	}
	
	private boolean updateProgram(String versionTo) {
		
		Path downloadDirectory = Path.of(workingDirectory+"\\NEW DOWNLOAD Code Converter "+versionTo);
		
		FileHandler.getInstance().setupDirectories(new Path[] {downloadDirectory});
		
		String downloadLink = "https://github.com/CassStar/SM127-Level-Code-Converter/releases/download/"+versionTo+"/Code.Converter.zip";
		String downloadOutput = workingDirectory+"\\NEW DOWNLOAD Code Converter "+versionTo+"\\Code Converter.zip";
		String command = "\""+workingDirectory+"\\wget\\wget.exe\" --output-document=\""+downloadOutput+"\" "+downloadLink;
		
		try {
			
			Runtime.getRuntime().exec(command);
			
		} catch (IOException e) {
			
			ProgramLogger.logMessage("Couldn't get latest version of Code Converter! Detailed error below:",LogType.ERROR);
			ProgramLogger.logMessage(e.getMessage(),LogType.ERROR);
			return false;
		}
		
		return true;
	}
	
	private boolean getYesNo(String question,String invalid) {
		
		System.out.printf(question);
		
		String tempInput = input.nextLine().toLowerCase();

		while (true) {
			
			switch (tempInput) {
			
			case "y","ye","yes":
				
				return true;
				
			case "n","no","nay":
				
				return false;
			
			default:
				
				System.out.printf(invalid);
				tempInput = input.nextLine().toLowerCase();
			}
		}
	}
}
