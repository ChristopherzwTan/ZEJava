//Programmer: Christopher Tan 
//Date: November 5, 2012
//
//Purpose: ????????
//


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DataDirectReader {

	public static void main(String[] args) {
		
		String fileName;
		String directory;
		String readingFile;
		boolean userProfile = false;
		boolean userList = false;
		boolean companyProfile = false;
		int frontCursor;
		int endCursor; 
		int scanLimit = 8;

		Scanner user_input = new Scanner (System.in);
		
		System.out.print("Please enter a directory: ");
		directory = user_input.nextLine();
		
		System.out.print("Enter the file you want to readin the above directory: ");
		readingFile = user_input.nextLine();
		
		fileName = directory + "/" + readingFile + ".txt";
		
		System.out.print('\n');
		
		try
		{
			//Read text file containing URLs
			readFile file = new readFile(fileName);
			String[] lineArray = file.OpenFile();
				
			for (int i = 0; i < lineArray.length; i++)
			{
				//FOR USER PROFILE
				if (lineArray[i].startsWith("https://"))
				{
					//CHECKS WHICH DATA DIRECT DATA IS IT TO RETRIEVE AND RAISES THE RESPECTIVE FLAG
					for (int j = 0; j < lineArray[i].length()/2; j++)
					{
						frontCursor = j;
						endCursor = frontCursor + scanLimit;
						
						String comparer = lineArray[i].substring(frontCursor, endCursor);
						
						if (comparer.startsWith("UserP"))
						{
							userProfile = true;
						}
						else if (comparer.startsWith("User&"))
							userList = true;
						else if (comparer.startsWith("Compa"))
							companyProfile = true;
					}
					
					try {
						//Read URL Source Code
						URL u = new URL(lineArray[i]);
						//BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));

						if (userProfile)						
						{
							printBetween("<title>", "</title>", u);
							printBetweenLine("<td class", "</tr>", u);
							//printBetween("", "", u);
						}
						
						else if (userList)
						{
							printBetween("<title>", "</title>", u);
						}
						
						else if (companyProfile)
						{
							printBetween("<title>", "</title>", u);
						}
					}
					
					catch (MalformedURLException ex)
					{
						System.err.println(lineArray[i] + " is not a valid URL.");
					}
					
					catch(IOException ie)
					{
						System.out.println("Input/Output Error: " + ie.getMessage());
					}
					
				}
				
				//RESET FLAGS
				userProfile = false;
				userList = false;
				companyProfile = false;
			}
		}
	
		
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	
	public static void printBetween(String top, String bottom, URL u) throws Exception
	{
		int numberOfLines = 0;
		int indexOfTop = 0;
		int cursor = 0;
		
		numberOfLines = getURLLines(u);
		
		String[] urlLineArray = makeLineArray(u, numberOfLines);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
		
		for (int i = 0; i < numberOfLines; i++)
        {
        	if (urlLineArray[i].startsWith(top))
        	{
        		indexOfTop = i;
        		break;
        	}
        }
		
		while ((urlLineArray[indexOfTop].indexOf(cursor) != '>'))
		{
			cursor++;
		}
		
		while ((urlLineArray[indexOfTop].indexOf(cursor) != '<'))
		{
			System.out.print(urlLineArray[indexOfTop].charAt(cursor));
			cursor++;
		}
		
		in.close();
	}
	
	
	public static void printBetweenLine(String top, String bottom, URL u) throws Exception {
		
		int numberOfLines = 0;
		int indexOfTop = 0;
		
		numberOfLines = getURLLines(u);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
        
        String[] urlLineArray = makeLineArray(u, numberOfLines);
       
        for (int i = 0; i < numberOfLines; i++)
        {
        	if (urlLineArray[i].startsWith(top))
        	{
        		indexOfTop = i;
        		break;
        	}
        }
        
        //indexOfTop++;
        
        while (!(urlLineArray[indexOfTop].endsWith(bottom)))
        {
        	System.out.println(urlLineArray[indexOfTop]);
        	indexOfTop++;
        }	
        
        System.out.print('\n');
        
		in.close();
	}
	
	//Counts how many lines of URL source code 
	public static int getURLLines(URL u) throws IOException
	{
		int counter = 0;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
		
        while ((in.readLine()) != null)
        {
            counter++;
        }
        return counter;
	}
	
	
	//Makes a lineArray variable for a given URL source code
	public static String[] makeLineArray (URL u, int numberOfLines) throws IOException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
		
		String[] lineArray = new String[numberOfLines];
		
		for (int i = 0; i < numberOfLines; i++)
        {
        	lineArray[i] = in.readLine();
        }
		return lineArray;
	}
}

	