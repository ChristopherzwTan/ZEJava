// Date: November 2nd, 2012 
// Scheduler_Reader_V2
// Programmer: Christopher Tan
// ZE PowerGroup
// Purpose: Automatically reads a scheduler log file, takes a args[0], filename and a number as the interval
//

import java.io.IOException;
//import java.util.Scanner;



public class Scheduler_Reader_V2 
{

	public static void main (String[] args) 
	{
		if (args[0].equalsIgnoreCase("help"))
		{
			System.out.println('\n' + "java Scheduler_Reader_V2 [directory] [filename] [interval in minutes]");
			System.out.println("Example: java Scheduler_Reader_V2 [C:/Users] processor.log 3");
			System.out.println( '\n' + "                                              Reads the latest logs of a ");
			System.out.println("                                              " + "scheduler log file. Make sure to ");
			System.out.println("                                              " + "put square brackets at the ");
			System.out.println("                                              " + "beginning and end of the");
			System.out.println("                                              " + "directory only" + '\n'); 
		}
		
		else {
			String theTime;
			String tempHour;
			String tempMinute;
			String tempSecond;
			String schedulerFile = "";
			String directory = "";
			int schedulerIndex = 0;
			
			//ARGUMENT READING PROCEDURE
			if (args[0].startsWith("["))
			{
				directory = args[0];
			}
			
			int dirIndex = 1;
			
			while (!(args[dirIndex].endsWith("]")))
			{
				directory = directory + " " + args[dirIndex];
				dirIndex++;
			}
			
			directory = directory + " " + args[dirIndex];
			
			directory = directory.substring(1, (directory.length()-1));
			
			schedulerIndex = dirIndex + 1;
			
			System.out.print('\n');
			
			schedulerFile = args[schedulerIndex];
		
			for (int i = schedulerIndex + 1; i < args.length - 2; i++)
			{
				schedulerFile = schedulerFile + " " + args[i];
			}
			
			String fileName =  directory + "/" + schedulerFile;
	
		
			int interval = Integer.parseInt(args[args.length - 1]);
			System.out.print('\n');
			//ARGUMENT READING PROCEDURE FINISH
	
		
			try {
				readFile file = new readFile(fileName);
				String[] lineArray = file.OpenFile();
			
				//Getting the date of the scheduler log file
				String dateCompare = lineArray[0].substring(0,11);
			
				int index = lineArray.length - 1;
			
				//Procedure to find the latest time in the log file
				while (!(lineArray[index].startsWith(dateCompare)))
				{
					index--;
				}
			
				theTime = lineArray[index].substring(0, 20);
			
				tempHour = theTime.substring(12,14);
				tempMinute = theTime.substring(15,17);
				tempSecond = theTime.substring(18,20);
			
				int timeHour = Integer.parseInt(tempHour);
				int minuteInput = Integer.parseInt(tempMinute);
			
				int limit = minuteInput - interval;
			
				System.out.println("Printing log of " + args[1] + " from " + theTime + '\n');
			
				for (int timeMinute = minuteInput; timeMinute >= limit; timeMinute--)	
				{	
					//Checks time is correct
					if (timeMinute < 0)
					{
						timeHour--;
						timeMinute = timeMinute + 59;
						limit = limit + 59;
					}
				
					for (int timeSecond = 59; timeSecond >= 0; timeSecond--)
					{
					
						boolean timeStamped = false;
					
						// Make time strings from integers
						if (timeHour < 10)
							tempHour = "0" + timeHour;
						else 
							tempHour = "" + timeHour;
					
						if (timeMinute < 10)
							tempMinute = "0" + timeMinute;
						else 
							tempMinute = "" + timeMinute;
					
						if (timeSecond < 10)
							tempSecond = "0" + timeSecond;
						else 
							tempSecond = "" + timeSecond;
			
						//Combines all the components of the time
						String timeCompare = tempHour + ":" + tempMinute + ":" + tempSecond;
					
						String target = dateCompare + " " + timeCompare;
					
						for (int i = (lineArray.length-1); i >= 0 ; i--)
						{
							if (lineArray[i].startsWith(target))
							{
								timeStamped = find_Queued_And_Running (i, fileName, timeStamped);
							}
						}	
					}	
				}
			}
		
			catch (IOException error) {
				System.out.println(error.getMessage());
			}
		}
	}		
			
		

	
	public static boolean find_Queued_And_Running (int index, String fileName, boolean timeStamped) throws IOException
	{
		timeStamped = printQueued (index, fileName, timeStamped);
		timeStamped = printRunning (index, fileName, timeStamped);
		return timeStamped;
	}
	
	
	//This method prints Queued processors for the respective date and time, assuming there is one
	public static boolean printQueued (int i, String fileName, boolean timeStamped) throws IOException
	{
		readFile file = new readFile(fileName);
		String [] lineArray = file.OpenFile();
		
		int index = i;
		
		while ((!lineArray[i].startsWith("Q")) && (!lineArray[i].startsWith("R")))
		{
			i++;
		}
		
		if (lineArray[i].startsWith("Queued"))
		{
			System.out.println(lineArray[index]);
			timeStamped = true;
			System.out.println(lineArray[i]);
			i++;
		}
		
		while (lineArray[i].startsWith("	"))
		{
			System.out.println(lineArray[i]);
			i++;
		}	
		return timeStamped;
	}
	
	
	//This method prints Running processors for the respective date and time
	public static boolean printRunning (int i, String fileName, boolean timeStamped) throws IOException
	{
		
		readFile file = new readFile(fileName);
		String [] lineArray = file.OpenFile();
		
		int index = i;
		
		while (!(lineArray[i].startsWith("R")))
		{
			i++;
		}
		
		if (lineArray[i].startsWith("Running"))
		{
			if (!timeStamped)
			{
				System.out.println(lineArray[index]);
				timeStamped = true;
			}
			System.out.println(lineArray[i]);
			i++;
		}
		
		while (lineArray[i].startsWith("	") == true)
		{
			System.out.println(lineArray[i]);
			i++;
		}
		System.out.print('\n');
		return timeStamped;
	}
	
}
