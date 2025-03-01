package main;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.zip.*;

import main.ProgramLogger.LogType;

public class UpdateChecker {
	
	private static final String VERSION = "V0.4.3",LATEST_URL = "https://github.com/CassStar/SM127-Level-Code-Converter/releases/latest";
	private final long MAX_FILE_SIZE = 10000000;
	private Scanner input;
	private String workingDirectory,parentDirectory;
	
	UpdateChecker(Scanner input,String workingDir) {
		
		this.input = input;
		workingDirectory = workingDir;
		getParentDirectory();
		
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
		
		String downloadOutput = parentDirectory+"\\Code Converter "+latestVersion;
		
		int difference = VERSION.compareTo(latestVersion);
		boolean updated = false;
		
		if (difference < 0) {
			
			ProgramLogger.logMessage("A newer version of the program is available! View changelog from here: "+LATEST_URL,LogType.INFO);
			
			boolean updateVersion = getYesNo("Would you like to update the program from: "+VERSION+" to: "+latestVersion+"? (Y/N): ",
					"%nInvalid value entered.%nWould you like to update the program from: "+VERSION+" to: "+latestVersion+"? (Y/N): ");
			
			if (updateVersion) {
				
				updated = updateProgram(latestVersion.toLowerCase());
				
				System.out.println("________________________________________________________________________________________________"+
						"________________________________________________________________________________________________\n");
				
				if (updated) {
					
					ProgramLogger.logMessage("Updated program to latest version!",LogType.INFO);
					
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
			
			ProgramLogger.logMessage("Please close this program. Use the new program version for added or fixed functionality.",
					LogType.WARNING);
			input.nextLine();
		}
	}
	
	private void getParentDirectory() {
		
		parentDirectory = workingDirectory.substring(0,workingDirectory.lastIndexOf('\\'));
	}
	
	private String getLatestVersionNumber() throws IOException {
		
		URL latestURL;
		URI uri = null;
		
		try {
			
			uri = new URI(LATEST_URL);
			
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
			
			return "0";
		}
		
		latestURL = uri.toURL();
		
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
		
		Path downloadDirectory = Path.of(parentDirectory+"\\Code Converter "+versionTo);
		
		FileHandler.getInstance().setupDirectories(new Path[] {downloadDirectory});
		
		String downloadLink = "https://github.com/CassStar/SM127-Level-Code-Converter/releases/download/"+versionTo+"/Code.Converter.zip";
		String downloadOutput = downloadDirectory.toString()+"\\Code Converter.zip";
		URL downloadURL;
		
		ProgramLogger.logMessage("Downloading...",LogType.INFO);
		
		try {
			
			URI uri = new URI(downloadLink);
			downloadURL = uri.toURL();
			ReadableByteChannel bytesChannel = Channels.newChannel(downloadURL.openStream());
			
			try (FileOutputStream outputStream = new FileOutputStream(downloadOutput)) {
				outputStream.getChannel().transferFrom(bytesChannel,0,MAX_FILE_SIZE);
			}
			
			ProgramLogger.logMessage("Finished downloading.",LogType.INFO);
			
		} catch (IOException | URISyntaxException e) {
			
			ProgramLogger.logMessage("Couldn't download file! Detailed error below:",LogType.ERROR);
			e.printStackTrace();
			return false;
		}
		
		FileInputStream inputStream;
		
        //buffer for read and write data to file.
        byte[] buffer = new byte[1024];
        
        try {
        	
        	inputStream = new FileInputStream(downloadOutput);
            ZipInputStream zipInput = new ZipInputStream(inputStream);
            ZipEntry zipEntry = zipInput.getNextEntry();
            
            while(zipEntry != null){
            	
                String fileName = zipEntry.getName();
                File newFile = new File(downloadDirectory.toString()+File.separator+fileName);
                
                ProgramLogger.logMessage("Extracting to "+newFile.getAbsolutePath(),LogType.INFO);
                
                //create directories for sub directories in zip.
                if (fileName.endsWith("/")) {
                	
                	FileHandler.getInstance().setupDirectories(new Path[] {Path.of(newFile.getAbsolutePath())});
                	
                } else {
                	
                	FileOutputStream outputStream = new FileOutputStream(newFile);
                    int length;
                    
                    while ((length = zipInput.read(buffer)) > 0) {
                    	
                    	outputStream.write(buffer,0,length);
                    	
                    	}
                    
                    outputStream.close();
                }
                
                //close this ZipEntry.
                zipInput.closeEntry();
                zipEntry = zipInput.getNextEntry();
            }
            //close last ZipEntry.
            zipInput.closeEntry();
            zipInput.close();
            inputStream.close();
            
            // Delete the zip file.
            Files.deleteIfExists(Path.of(downloadOutput));
            
            // Move any input files over to the new input folder.
            Files.list(Path.of(workingDirectory+"\\input")).forEach(path -> {
            	
            	String stringPath = path.toString();
            	Path newInput = Path.of(downloadDirectory+"\\input\\"+stringPath.substring(stringPath.lastIndexOf('\\')));
            	
            	try {
            		
            		ProgramLogger.logMessage("Copying file: "+path+" to new input folder",LogType.INFO);
					Files.copy(path,newInput,StandardCopyOption.REPLACE_EXISTING);
					
				} catch (IOException e) {
					
					ProgramLogger.logMessage("Couldn't copy file! See below for detailed error:",LogType.ERROR);
					ProgramLogger.logMessage(e.getMessage(),LogType.ERROR);
				}
            });
            
        } catch (IOException e) {
        	
        	ProgramLogger.logMessage("Couldn't extract files! Detailed error below:",LogType.ERROR);
            e.printStackTrace();
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
