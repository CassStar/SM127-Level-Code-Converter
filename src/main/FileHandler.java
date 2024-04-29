package main;

import static java.nio.file.StandardOpenOption.*;

import java.io.*;
import java.nio.file.*;

import main.ProgramLogger.LogType;

/**
 * <b>FileHandler:</b><dd>A class used to handle all interactions with files and the file system.
 * </dd>
 * <hr>
 * </dd><hr>
 *
 */
public class FileHandler {
	
	private static final FileHandler FILE_HANDLER = new FileHandler();
	
	// Private constructor to make sure no other classes can make instances of this class.
	private FileHandler() {
		
	}
	// Method used to get the only running instance of this class.
	public static FileHandler getInstance() {
		
		return FILE_HANDLER;
	}
	
	public boolean setupDirectories(Path[] directories) {
		
		boolean createdDir = false;
		
		for (Path directory:directories) {
			
			if (!Files.exists(directory)) {
				
				ProgramLogger.logMessage("Directory: "+directory.getFileName()+" doesn't"
						+ " exist, creating...",LogType.INFO);
				
				try {
					
					Files.createDirectory(directory);
					createdDir = true;
					
					ProgramLogger.logMessage("Successfully created directory.",LogType.INFO);
					
				} catch (IOException e) {
					
					ProgramLogger.logMessage("Couldn't create directory! Detailed error below:",
							LogType.ERROR);
					e.printStackTrace();
				}
			}
		}
		
		return createdDir;
	}
	
	boolean backupLog(Path logDirectory) {
		
		// Checking if the log directory exists.
		if (!Files.exists(logDirectory)) {
			try {
				// Trying to create the log directory.
				Files.createDirectory(logDirectory);
			} catch (IOException e) {
				ProgramLogger.logMessage("Couldn't create log directory!",LogType.ERROR);
				assert(false) : "Check file permissions for: "+logDirectory;
				// We can't do anything else if the log directory doesn't exist.
				return false;
			}
		}
		
		boolean fileExists;
		Path filePath = logDirectory.resolve(ProgramLogger.logFile);
		
		// Checking if the log file exists.
		fileExists = Files.exists(filePath);
		
		if (fileExists) {
			
			Path backupLog = logDirectory.resolve(ProgramLogger.backupFile);
			
			try {
				Files.move(filePath,backupLog,StandardCopyOption.REPLACE_EXISTING);
				
				ProgramLogger.logMessage("Backing up old log file...",LogType.INFO);
				
				fileExists = false;
			} catch (IOException e) {
				ProgramLogger.logMessage("Could not backup log file!",LogType.ERROR);
			}
		} else {
			
			ProgramLogger.logMessage("No log file currently exists, one will be made with "
					+ "the name '"+ProgramLogger.logFile+"'.",LogType.INFO);
		}
		
		fileExists = createNewFile(filePath);
		
		if (fileExists) {
			// Saving the full program log instead of just the most recent message.
			ProgramLogger.saveFullProgramLog();
			
			ProgramLogger.logMessage("Log file successfuly created!",LogType.INFO);
		} else {
			ProgramLogger.logMessage("Could not create new log file!",LogType.ERROR);
		}
		return Boolean.valueOf(fileExists);
		
	}
	
	Path getDataFile(String file) {
		
		return Path.of(file);
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Creates a file from the given Path.</dd><hr>
	 * 
	 * @param file	The file to create.
	 * @return	true if the file was successfully created, false otherwise.
	 */
	boolean createNewFile(Path file) {
		
		try {
			Files.newOutputStream(file);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	boolean deleteFile(Path file) {
		
		try {
			return Files.deleteIfExists(file);
		} catch (IOException e) {
			ProgramLogger.logMessage(e.getMessage(),LogType.ERROR);
			return false;
		}
	}
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Writes string data to a file. The data is appended to the end of the
	 * file.</dd>
	 * <hr>
	 * 
	 * @param data	The data to write to file.
	 * @param file	The file to write data to.
	 */
	void writeToFile(String data,Path file,OpenOption[] options) {
		
		boolean createNew = false;
		
		for (OpenOption option:options) {
			
			if (option == CREATE) {
				
				createNew = true;
			}
		}
		
		if (!Files.exists(file) && !createNew) {
			return;
		}
		
		// Beginning output to file.
		try (OutputStream out = new BufferedOutputStream(
			      Files.newOutputStream(file,options))) {
			
			// Writing the data to file.
			out.write(data.getBytes());
			
			if (!createNew) {
				out.write("\n".getBytes());
			}
			
			
			// Making sure everything gets written to file.
			out.flush();
			
		} catch (IOException e) {
			ProgramLogger.logMessage("Couldn't write to "+file+"!",LogType.ERROR);
		}
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Writes string data to a file. The data is appended to the end of the
	 * file.</dd>
	 * <hr>
	 * 
	 * @param data	An array of data to write to file. Each index represents one line of data.
	 * @param file	The file to write data to.
	 */
	void writeToFile(String[] data,Path file,OpenOption[] options) {
		
		boolean createNew = false;
		
		for (OpenOption option:options) {
			
			if (option == CREATE) {
				
				createNew = true;
			}
		}
		
		if (!Files.exists(file) && !createNew) {
			return;
		}
		
		// Beginning output to file.
		try (OutputStream out = new BufferedOutputStream(
				Files.newOutputStream(file,options))) {
			
			// StringBuilder object to hold all of the data.
			StringBuilder lines = new StringBuilder();
			
			// Adding each line to the StringBuilder object.
			for (String line:data) {
				lines.append(line);
				lines.append("\n");
			}
			
			// Writing all the lines to file.
			out.write(lines.toString().getBytes());
			
			// Making sure everything gets written to file.
			out.flush();
			
		} catch (IOException e) {
			ProgramLogger.logMessage("Couldn't write to "+file+" !",LogType.ERROR);
		}
	}
	
	String readFromFile(Path file) {
		
		try (InputStream in = new BufferedInputStream(Files.newInputStream(file,READ))) {
			
			return new String(in.readAllBytes());
			
		} catch (IOException e) {
			ProgramLogger.logMessage("Couldn't read from "+file.getFileName()+" file!",
					LogType.ERROR);
			return null;
		}
	}
}
