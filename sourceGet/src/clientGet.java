//Christopher Tan
//Date: November 16, 2012
//
//Purpose: Reads a text file full of ZE Data Direct URLs and checks to see if they are working or if there are errors
//         Results are written into another text file.


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.util.Scanner;
import java.net.*;
import java.io.*;

public class clientGet {

	
	public static void main(String[] args) throws IOException {

		String read_filePath;
		String write_filePath;
		//String readDirectory;
		//String readingFile;
		String toPrint[] = new String[100];
		//String writeDirectory;
		//String writingFile;
		
		int frontCursor;
		int endCursor; 
		int scanLimit = 4;
		
		
		/*
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
		*/
		
		read_filePath = "U:" + "\\" + "urlOfDMDD2.txt";
		write_filePath = "U:" + "\\" + "clients.txt";
		
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
				if (lineArray[i].startsWith("http://"))
				{
					int indexOfClients = 0;
					
					try {
						//Read URL Source Code
						URL u = new URL(lineArray[i]);
						
						//Make array and count the number of lines in the URL Source
						int numberOfLines = getURLLines(u);
						String[] urlLineArray = makeURLLineArray(u, numberOfLines);
						
						for (int j = (numberOfLines - 1); j >= 0; j--)
						{
							int ulCount = 0;
							
							for (int k = 0; k < (urlLineArray[j].length() - scanLimit); k++)
							{
								frontCursor = k;
								endCursor = frontCursor + scanLimit;
								
								if (urlLineArray[j].startsWith("		<ul>"))
								{
									ulCount++;
									indexOfClients = j;
									//out.write(indexOfClients);
									
									if (ulCount == 1)
									{
										break;
									}
								}
							}
							if (ulCount == 1)
							{
								break;
							}
						}
						
						toPrint = printBetween("		<ul>", "		</ul>", u, indexOfClients);
						
						int a = 0;
						String processorName = urlLineArray[1154].trim();
						processorName = processorName.substring(63, (processorName.length()-1));
						System.out.println(processorName);
						out.write(processorName + '\n');
						
						for (a = 0; a < 100; a++)
						{
							if (toPrint[a] == null)
							{
								break;
							}
							
							System.out.println(toPrint[a]);
							out.write(toPrint[a] + '\n');
						}
						out.write('\n');
							
					}
						
					//Catch any URL reading errors
					catch (MalformedURLException ex)
					{
						System.err.println(lineArray[i] + " is not a valid URL.");
						//out.write("Error: " + lineArray[i] + " is not a valid URL." + '\n' + '\n');
					}
					
					catch(IOException ie)
					{
						System.out.println("Input/Output Error: " + ie.getMessage());
						//out.write(ie.getMessage() + '\n' + "Status: Input/Output Error" + '\n' + '\n');
					
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
	public static String[] printBetween(String top, String bottom, URL u, int index) throws Exception {
		
		int numberOfLines = getURLLines(u);
		int indexOfTop = 0;
		int counter = 0;
		
		String theLine[] = new String[100];
        
        BufferedReader in = new BufferedReader(new InputStreamReader(u.openStream()));
        
        String[] urlLineArray = makeURLLineArray(u, numberOfLines);
       
        for (int i = index+1; i < numberOfLines; i++)
        {
        	String temp = urlLineArray[i].trim();
        	//if (!urlLineArray[i].startsWith("					<") || !urlLineArray[i].startsWith("					 " )
        	//		|| !urlLineArray[i].startsWith("		<"))
        	
        	if (((temp.startsWith("A")) || (temp.startsWith("B"))
        			|| (temp.startsWith("C"))
        			|| (temp.startsWith("D"))
        			|| (temp.startsWith("E"))
        			|| (temp.startsWith("F"))
        			|| (temp.startsWith("G"))
        			|| (temp.startsWith("H"))
        			|| (temp.startsWith("I"))
        			|| (temp.startsWith("J"))
        			|| (temp.startsWith("K"))
        			|| (temp.startsWith("L"))
        			|| (temp.startsWith("M"))
        			|| (temp.startsWith("N"))
        			|| (temp.startsWith("O"))
        			|| (temp.startsWith("P"))
        			|| (temp.startsWith("Q"))
        			|| (temp.startsWith("R"))
        			|| (temp.startsWith("S"))
        			|| (temp.startsWith("T"))
        			|| (temp.startsWith("U"))
        			|| (temp.startsWith("V"))
        			|| (temp.startsWith("W"))
        			|| (temp.startsWith("X"))
        			|| (temp.startsWith("Y"))
        			|| (temp.startsWith("Z"))
        			|| (temp.startsWith("a"))
        			|| (temp.startsWith("b"))
        			|| (temp.startsWith("c"))
        			|| (temp.startsWith("d"))
        			|| (temp.startsWith("e"))
        			|| (temp.startsWith("f"))
        			|| (temp.startsWith("g"))
        			|| (temp.startsWith("h"))
        			|| (temp.startsWith("i"))
        			|| (temp.startsWith("j"))
        			|| (temp.startsWith("k"))
        			|| (temp.startsWith("l"))
        			|| (temp.startsWith("m"))
        			|| (temp.startsWith("n"))
        			|| (temp.startsWith("o"))
        			|| (temp.startsWith("p"))
        			|| (temp.startsWith("q"))
        			|| (temp.startsWith("r"))
        			|| (temp.startsWith("s"))
        			|| (temp.startsWith("t"))
        			|| (temp.startsWith("v"))
        			|| (temp.startsWith("w"))
        			|| (temp.startsWith("x"))
        			|| (temp.startsWith("y"))
        			|| (temp.startsWith("z"))
        			))
        	{
        		//System.out.print(urlLineArray[i].trim());
        		theLine[counter] = urlLineArray[i].trim();
        		counter++;
        	}
        	
        	
        	if (urlLineArray[i].startsWith(bottom))
        		break;
        }
        
        //indexOfTop++;
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
