# SM127-Level-Code-Converter

What it Does:

	This program converts SM127 level codes between game versions 0.6.0, 0.6.1, 0.7.0, 0.7.1, 0.7.2,
		and 0.8.0
		You can choose to "convert" level codes to the same versions they were made in, this won't
		actually convert anything, but will let you change the level to consist of only one area

	You can try to use it to convert levels from other version not listed above, but the program will
		probably give you an error complaining about an invalid code version, and I haven't tested
		any of the other versions, so expect bugs if you try that
		(Note: you will have to edit the level code in order to get around the error)

Important:

	This program requires a not-ancient version of Java installed in order to run. If you do not
		have Java installed or have a Java version less than 14.0.0, please install Java/
		update your installation before running the program. You can find which version of Java
		you have installed by running 'java -version' from the command line. Java can be
		downloaded from https://www.java.com/download

	Do NOT delete 'Converter.jar' or the 'src' folder! These are the source files for the program,
		so deleting them will delete the program.

	You CAN safely delete the 'info' folder, it's not used by the program,
		it's just for documentation purposes. All other folder aside from 'src' can also be
		deleted, and will be remade by the program as needed

	The logs folder holds log files generated by the program. Use these for troubleshooting if
		needed

Usage:

	1. Put all the files you want to convert into the 'input' folder

	2. Run 'Converter.bat', you may or may not be able to see the file extension based on your
		system's settings
	
	2a. If 'Converter.bat' gives you an error when you try running it, try instead to run
		'Converter2.bat'

	3. Follow the directions the program tells you to do

	4. Converted files will be put in the 'output' folder, check it once the program finishes

	5. Remember to clear out the 'input' folder between uses of the program,
		it will try to convert every text file it finds in that folder
