
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DataDirectChecker {

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String read_filePath;
		String write_filePath;
		String readDirectory;
		String readingFile;
		String urlStatus;
		String writeDirectory;
		String writingFile;
		String IOerror;
		
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
		
		System.out.print("Enter the file you want to write or create in the above directory: ");
		writingFile = user_input.nextLine();
		
		read_filePath = readDirectory + "/" + readingFile + ".txt";
		write_filePath = writeDirectory + "/" + writingFile + ".txt";
		
		System.out.print('\n');
		
		FileWriter fstream = new FileWriter (write_filePath);
		BufferedWriter out = new BufferedWriter(fstream);
		
		try
		{
			//Read text file containing URLs
			readFile file = new readFile(read_filePath);
			String[] lineArray = file.OpenFile();
				
			for (int i = 0; i < lineArray.length; i++)
			{
				//FOR USER PROFILE
				if (lineArray[i].startsWith("https://"))
				{
					int indexOfError = 0;
					try {
						//Read URL Source Code
						URL u = new URL(lineArray[i]);
						//BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
						
						
						
						//Finds for errors in the URL source code
						int numberOfLines = getURLLines(u);
						String[] urlLineArray = makeLineArray(u, numberOfLines);
						
						for (int j = 0; j < numberOfLines; j++)
						{
							for (int k = 0; k < (urlLineArray[j].length() - scanLimit); k++)
							{
								frontCursor = k;
								endCursor = frontCursor + scanLimit;
								if (urlLineArray[j].substring(frontCursor, endCursor).equalsIgnoreCase("\"error\""))
										//|| urlLineArray[j].substring(frontCursor, endCursor).equalsIgnoreCase("Apache "))
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
						
				catch (MalformedURLException ex)
				{
					System.err.println(lineArray[i] + " is not a valid URL.");
					out.write(lineArray[i] + " is not a valid URL.");
				}
					
				catch(IOException ie)
				{
					System.out.println("Input/Output Error: " + ie.getMessage());
					out.write("Input/Output Error: " + ie.getMessage() + '\n' + '\n');
					
				}
					
			}
				
		}
		out.close();
	}
	
		
	catch (Exception e)
	{
		System.err.println("Error: " + e.getMessage());
	}
}
	
	
	public static String printBetween(String top, String bottom, URL u) throws Exception {
		
		int numberOfLines = 0;
		int indexOfTop = 0;
		
		String theLine = "";
		
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
        	theLine += urlLineArray[indexOfTop] + '\n';
        	//System.out.println(urlLineArray[indexOfTop]);
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
