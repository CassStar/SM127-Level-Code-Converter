package main;

import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;

import util.Utility;

/**
 * <b>ProgramLogger:</b><dd>A class used to log messages of important actions take by the user
 * and the program itself.</dd>
 * <hr>
 * @version 0.5
 * </dd><hr>
 *
 */
public final class ProgramLogger {
	
	// LogType 	Representing a log type to be used with each log message.
	// INFO 	Represents normal function of the program.
	// WARNING 	Represents unexpected or unusual states that the program will try handling.
	// ERROR 	Represents a serious problem that occured, this will usually have noticable
	//			impacts on the program.
	// DEBUG	Represents more verbose information about the functions of the program.
	public enum LogType {
		INFO("INFO"),WARNING("WARNING"),ERROR("ERROR"),DEBUG("DEBUG");
		
		// String representation of the enum value.
		String repr;
		
		LogType(String repr) {
			this.repr = repr;
		}
		
		public String toString() {
			
			return repr;
		}
	}
	
	/*
	 * logFile				The file name for the program log.
	 * backupFile			The file name for the backup program log.
	 * logMessages			An array containing all the log messages produced by this program.
	 * currentMessageIndex	The index in logMessages to store the next log message.
	 * fileHandler			The file handler used to save the program log to file.
	 */
	static String logFile = "ProgramLog.txt";
	static String backupFile = "ProgramLogBackup.txt";
	static Path logDirectory,logPath;
	static String[] logMessages = new String[1];
	static int currentMessageIndex = 0;
	static FileHandler fileHandler;
	
	private static final StandardOpenOption[] OPTIONS = {StandardOpenOption.APPEND};
	
	// Private constructor so no other classes can instantiate this class.
	private ProgramLogger() {
		
	}
	
	public static void setLogDirectory(Path value) {
		
		logDirectory = value;
		
		logPath = logDirectory.resolve(logFile);
	}
	
	/**
	 * Check the file system to see if a log file exists, by calling fileHandler's checkForFile
	 * method.
	 */
	static void checkForLogFile() {
		
		if (fileHandler == null) {
			fileHandler = FileHandler.getInstance();
		}
		
		fileHandler.backupLog(logDirectory);
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Logs the given message to be of the type provided. Messages are
	 * printed out before being written to file. This method is synchronized because it assumes
	 * nothing changes during the method call.</dd><hr>
	 * 
	 * @param message	The message to log.
	 * @param type		The log type of this message. Possible values are: INFO, WARNING, and
	 * 					ERROR.
	 */
	public static void logMessage(String message,LogType type) {
		
		if (!Converter.getIsDebug() && type == LogType.DEBUG) {
			
			return;
		}
		
		// Traceback to the method that called this method.
		String trace = null;
		
		try {
			// Throw exception to get the stack trace.
			throw new GetStackTraceException();
		} catch (GetStackTraceException e) {
			trace = e.getMessage();
		}
		
		// Checking if logMessages has enough space for the next message.
		if (currentMessageIndex > logMessages.length-1) {
			
			// Expand the array if there is not enough space.
			logMessages = Utility.expandStringArray(
					logMessages,currentMessageIndex-logMessages.length+1);
		}
		
		// Create the log message and add a timestamp to it.
		logMessages[currentMessageIndex] = "["+getTime()+"]- "+type+" "+trace+": "+message;
		
		// Print out the log message. Use System.err if LogType is ERROR.
		if (type == LogType.ERROR) {
			System.err.println(logMessages[currentMessageIndex]);
		} else {
			System.out.println(logMessages[currentMessageIndex]);
		}
		
		
		saveProgramLog();
		
		// Increase the index for the next message.
		currentMessageIndex++;
		
	}
	
	/**
	 * Saves the most recent log message to file.
	 */
	static void saveProgramLog() {
		
		// Values used to make sure the below loop doesn't go on indefinitely.
		int limit = 100,count = 0;
		
		// Making sure to get the most recent non-null message.
		while (logMessages[currentMessageIndex] == null && count != limit) {
			
			// Going back through the messages.
			currentMessageIndex--;
			count++;
			
			// Looping to the end of the array.
			if (currentMessageIndex < 0) {
				currentMessageIndex = logMessages.length-1;
			}
		}
		
		// Checking if a valid message was found.
		if (count == limit && logMessages[currentMessageIndex] == null) {
			// No message was found.
			return;
		}
		
		// Write the most recent log message to file.
		fileHandler.writeToFile(logMessages[currentMessageIndex],logPath,OPTIONS);
	}
	
	/**
	 * Saves the entire program log to file.
	 */
	static void saveFullProgramLog() {
		
		// Write the entire program log to file.
		fileHandler.writeToFile(Utility.getTrimmedStringArray(logMessages),logPath,OPTIONS);
	}
	
	/**
	 * <dl>
	 * <b>Summary:</b><dd>Gets a formatted version of the current time.</dd><hr>
	 * 
	 * @return A string representation of the current time.
	 */
	static String getTime() {
		
		// Get the current time.
		String base = LocalTime.now().toString();
		// Get the index between whole seconds and fractions of a second.
		int decimalIndex = base.indexOf('.');
		// Get length of the less than a second part.
		int fractionLength = base.length()-decimalIndex;
		
		// Limit the amount of digits after the decimal place to 6 or however many digits are
		// available.
		return base.substring(0,decimalIndex+Math.min(7,fractionLength));
	}
}

/**
 * An exception class used to get the a certain part of the current stack trace.
 */
class GetStackTraceException extends Exception {

	private static final long serialVersionUID = 8501101234583377274L;

	public String getMessage() {
		
		// Get the full stack trace.
		StackTraceElement[] stack = getStackTrace();
		
		// Get the target element.
		StackTraceElement target = stack.length > 1? stack[1]:stack[0];
		
		// Return the target in a slightly formatted string.
		return target.getMethodName()+"("+target.getFileName()+":"+target.getLineNumber()+")";
	}
	
}