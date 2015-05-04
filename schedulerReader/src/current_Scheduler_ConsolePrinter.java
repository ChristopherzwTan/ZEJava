//
//Date: October 18, 2012
//Programmed by: Reinhard and Christopher
//ZE PowerGroup
//Purpose: Looks through the current_scheduler-(default).log file in the U drive and prints out the
//		   Queued Processes and Running Processes of the input date and time. (Assuming if there is a queued)
//		   
//

import java.io.IOException;
import java.util.Scanner;


public class current_Scheduler_ConsolePrinter 
{

	public static void main (String[] args) throws IOException
	{
		Scanner user_input = new Scanner (System.in);
		
		String theTime;
		String tempHour;
		String tempMinute;
		String tempSecond;
		
		//Inputs
		System.out.print("Enter the directory: ");
		String directory = user_input.nextLine();
		
		System.out.print("Enter the name of the scheduler file: ");
		String schedulerFile = user_input.nextLine();
		
		String fileName =  directory + "/" + schedulerFile + ".log";
		
		System.out.print("Enter interval in minutes: ");
		int interval = user_input.nextInt();
		
		System.out.print('\n');
		
		try {
			readFile file = new readFile(fileName);
			String[] lineArray = file.OpenFile();
			
			String dateCompare = lineArray[0].substring(0,11);
			
			int index = lineArray.length - 1;
			
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
			
			System.out.println("Printing log of " + schedulerFile + "from " + theTime + '\n');
			
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



