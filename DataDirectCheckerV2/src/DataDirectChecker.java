//Christopher Tan
//Date: November 16, 2012
//
//Purpose: Reads a text file full of ZE Data Direct URLs and checks to see if they are working or if there are errors
//         Results are written into another text file.


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DataDirectChecker {

	
	public static void main(String[] args) throws IOException {

		String read_filePath;
		String write_filePath;
		String readDirectory;
		String readingFile;
		String urlStatus;
		String writeDirectory;
		String writingFile;
		
		int frontCursor;
		int endCursor; 
		int scanLimit = 7;
		
		Scanner user_input = new Scanner (System.in);
		
		//READING FILE PROMPTS
		System.out.print("Please enter a directory: ");
		readDirectory = user_input.nextLine();
		
		System.out.print("Enter the file you want to read in the above directory: ");
		readingFile = user_input.nextLine();
		
		//WRITING FILE PROMPTS
		System.out.print("Please enter a directory to write to: ");
		writeDirectory = user_input.nextLine();
		
		System.out.println("WARNING: If you happen to have a text file of the same name in the same directory, it will be overwritten.");
		System.out.print("Enter the file you want to write to or create in the above directory: ");
		writingFile = user_input.nextLine();
		
		read_filePath = readDirectory + "/" + readingFile + ".txt";
		write_filePath = writeDirectory + "/" + writingFile + ".txt";
		
		System.out.print('\n');
		
		FileWriter fstream = new FileWriter (write_filePath);
		BufferedWriter out = new BufferedWriter(fstream);
		
		
		try
		{
			//Read text file containing URLs
			int fileLines = countLines(read_filePath);
			String[] lineArray = makeLineArray(fileLines, read_filePath);
			
			
			for (int i = 0; i < lineArray.length; i++)
			{
				//FOR USER PROFILE
				if (lineArray[i].startsWith("https://"))
				{
					int indexOfError = 0;
					
					try {
						//Read URL Source Code
						URL u = new URL(lineArray[i]);
						
						
						//Looks for errors in the URL source code
						int numberOfLines = getURLLines(u);
						String[] urlLineArray = makeURLLineArray(u, numberOfLines);
						
						for (int j = 0; j < numberOfLines; j++)
						{
							for (int k = 0; k < (urlLineArray[j].length() - scanLimit); k++)
							{
								frontCursor = k;
								endCursor = frontCursor + scanLimit;
								if (urlLineArray[j].substring(frontCursor, endCursor).equalsIgnoreCase("\"error\""))
								{
									indexOfError = j;
									break;
								}
							}
							if (indexOfError != 0)
								break;
						}
						
					
						
						//IF NO ERROR
						if (indexOfError == 0)
						{
							urlStatus = lineArray[i] + '\n' + "Status: Good";
							System.out.println(urlStatus + '\n');
						
							try {
								out.write(urlStatus + '\n');
								out.write('\n');
							}
						
							catch(Exception e) {
								System.err.println("Error: " + e.getMessage());
							}
						
						}	
						
						//IF THERE IS AN ERROR
						else
						{
							urlStatus = lineArray[i] + '\n' + "Status: Error found";
							System.out.println(urlStatus);
							System.out.println("Printing error: ");
							String errorLog = printBetween(urlLineArray[indexOfError], "</div>", u);
							System.out.println('\n');
						
							try {
								out.write(urlStatus + '\n');
								out.write("Printing error: " + '\n');
								out.write(errorLog);
							}
						
							catch(Exception e) {
								System.err.println("Error: " + e.getMessage());
							}
						}
							
					}
						
					//Catch any URL reading errors
					catch (MalformedURLException ex)
					{
						System.err.println(lineArray[i] + " is not a valid URL.");
						out.write("Error: " + lineArray[i] + " is not a valid URL." + '\n' + '\n');
					}
					
					catch(IOException ie)
					{
						System.out.println("Input/Output Error: " + ie.getMessage());
						out.write(ie.getMessage() + '\n' + "Status: Input/Output Error" + '\n' + '\n');
					
					}
					
				}
				
			}
			out.close();
		}
		
	
		//Catch file reading errors
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	//This method returns whatever is between top and bottom parameters in the URL u parameter source code
	public static String printBetween(String top, String bottom, URL u) throws Exception {
		
		int numberOfLines = getURLLines(u);
		int indexOfTop = 0;
		
		String theLine = "";
        
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
        
        String[] urlLineArray = makeURLLineArray(u, numberOfLines);
       
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
        	theLine += urlLineArray[indexOfTop] + '\n';
        	indexOfTop++;
        }	
        
        System.out.print('\n');
        
		in.close();
		
		return theLine;
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
	
	
	//Makes a lineArray variable for a given URL source code, usually used in combination with the getURLLines helper method
	public static String[] makeURLLineArray (URL u, int numberOfLines) throws IOException
	{
		
		BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
		
		String[] lineArray = new String[numberOfLines];
		
		for (int i = 0; i < numberOfLines; i++)
        {
        	lineArray[i] = in.readLine();
        }
		return lineArray;
	}
	
	
	public static int countLines (String read_filePath) throws IOException {
		
		FileReader readIt = new FileReader (read_filePath);
		BufferedReader in = new BufferedReader (readIt);
		
		int fileLines = 0;
		
		while (in.readLine() != null)
		{
			fileLines++;
		}
		in.close();
		
		return fileLines;
	}
	
	
	public static String[] makeLineArray (int fileLines, String read_filePath) throws IOException {
		
		FileReader readIt = new FileReader (read_filePath);
		BufferedReader in = new BufferedReader (readIt);
		
		String[] lineArray = new String[fileLines];
		
		for (int i = 0; i < fileLines; i++)
		{
			lineArray[i] = in.readLine();
		}
		in.close();
		
		return lineArray;
	}
}
