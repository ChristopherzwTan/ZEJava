//Date: October 23rd, 2012
//Programmer: Christopher and his Amazing Friends!!
//Purpose: To print out in the console the log of the last run of the processor.
//

import java.io.IOException;
import java.util.Scanner;

public class Log_Reader {

	public static void main(String[] args) {
		
		Scanner user_input = new Scanner (System.in);
		
		//Put the file directory here
		
		System.out.print("Enter the directory: ");
		String directory = user_input.nextLine();
		
		System.out.print("Enter the name of the log file: ");
		String logFile = user_input.nextLine();
		
		String fileName =  directory + "/" + logFile + ".log";
		
		int whereBegin = 0;
		
		//Your bottom and top. Whatever is in between will be printed
		String top = "Begin Processing";
		String bottom = "Finish Processing";
			
		try
		{
			readFile file = new readFile(fileName);
			String[] lineArray = file.OpenFile();
			
			System.out.println("Checking log of: ");
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
	