// Date: November 2nd, 2012 
// Scheduler_Reader_V2
// Programmer: Christopher Tan
// ZE PowerGroup
// Purpose: Automatically reads a processor log file, takes a directory and file name as arguments
//

import java.io.IOException;

public class Log_Reader_V2 {

	public static void main(String[] args) {
		
		int dirIndex;
		
		if (args[0].equalsIgnoreCase("help"))
		{
			
			System.out.print('\n' + "java Log_Reader_V2 [directory] [filename]");
			System.out.println("     Reads the latest log of a ");
			System.out.println("                                              " + "processor log file. Make sure to ");
			System.out.println("                                              " + "put square brackets at the ");
			System.out.println("                                              " + "beginning and end of the");
			System.out.println("                                              " + "directory name only" + '\n'); 
			System.out.println("Example: java Log_Reader_V2 [C:/Users] processor.log");
		}
		
		else {
			
			String directory = "";
			String logFile = "";
			int logIndex = 0;
			
			//PROCEDURE TO GET THE CORRECT DIRECTORY AND FILENAME ARGUMENTS
			if (args[0].startsWith("["))
			{
				directory = args[0];
			}
			
			if (args[0].endsWith("]"))
			{
				directory = args[0].substring(1,(args[0].length()-1));
				dirIndex = 0;
			}
			
			else
			{
				dirIndex = 1;
			
				while (!(args[dirIndex].endsWith("]")))
				{
					directory = directory + " " + args[dirIndex];
					dirIndex++;
				}
			
				directory = directory + " " + args[dirIndex];
			
				directory = directory.substring(1, (directory.length()-1));
			}
			
			logIndex = dirIndex + 1;
			
			System.out.print('\n');
			
			logFile = args[logIndex];
		
			for (int i = logIndex + 1; i < args.length; i++)
			{
				logFile = logFile + " " + args[i];
			}
		
		
			String fileName =  directory + "/" + logFile;
			
			int whereBegin = 0;
		
			//Your bottom and top. Whatever is in between will be printed
			String top = "Begin Processing";
			String bottom = "Finish Processing";
			
			try
			{
				readFile file = new readFile(fileName);
				String[] lineArray = file.OpenFile();
			
				System.out.println("Checking log of: " + '\n');
				System.out.println(fileName + '\n');
			
				//find the last begin
				for (int i = (lineArray.length - 1); i > 0; i--)
				{
					if (lineArray[i].endsWith(top))
					{
						whereBegin = i;
						break;
					}
				}
				printLog(whereBegin, fileName, bottom);
			}
			catch (Exception e)
			{
				System.err.println("Error: " + e.getMessage());
			}
			
		}
	}
	static void printLog(int index, String file_Name, String toStop) throws IOException
	{
		readFile file = new readFile(file_Name);
		String[] lineArray = file.OpenFile();
		
		//Prints Begin
		System.out.println(lineArray[index]);
		index++;
		
		//Prints everything inside
		while (!lineArray[index].endsWith(toStop))
		{
			System.out.println(lineArray[index]);
			index++;
		}
		
		//Prints Finish
		System.out.println(lineArray[index]);
	}
	
}
