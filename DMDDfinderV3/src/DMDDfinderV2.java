
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DMDDfinderV2 {

	public static void DMDDfindV2(String[] args) throws IOException {
		
		while (true) {
		
		Scanner input = new Scanner(System.in);
		String readFile = null;
		String writeFile = null;
		
		System.out.print("Please type in the full path of the text file you want to read: ");
		readFile = input.nextLine();
		
		if (readFile.equalsIgnoreCase("exit")) {
			break;
		}
		
		System.out.print("Please type in the full path of the csv file you want to write to as output: ");
		writeFile = input.nextLine();
		
		//Safe measure, makes sure the output is a csv file
		writeFile = writeFile + ".csv";
		
		//File buffers and stuff
		FileReader input_read = new FileReader (readFile);
		BufferedReader in_file = new BufferedReader(input_read);
		
		FileWriter output_write = new FileWriter (writeFile);
		BufferedWriter out_file = new BufferedWriter(output_write);
		
		try {
			int fileLines = countLines(readFile);
			String[] lineArray = makeLineArray(fileLines, readFile);
			
			in_file.close();
			
			//This part writes to the output file
			for (int i = 0; i < fileLines; i++) {
				int k = 0;
				String[] tempString = DMDDfind(lineArray[i]);
				while(!tempString[k].equals("end")) {
					out_file.write(tempString[k] + "\n");
					k++;
				}
			}
			
		}
		catch(IOException ierr) {
			System.out.println("Input/Output Error: " + ierr.getMessage());
		}
		out_file.close();
		
		System.out.println("\nDone!\n\n\n");
	}
	}

	
	public static String[] DMDDfind(String processorName) throws IOException {
		
		//Variables
		boolean found = false;
		String processorEditURL_string = null;
		
		String[] arrayOfInfo = new String[200];
		
		processorName.trim();
		String realProcessorName = processorName;
		
		processorName = "<li>" + processorName;
				
		try {
			//DMDD URL
			URL DMDDurl = new URL("http://zema-util-01:8080/DMDataDictionary/allObjects.do?type=Processor&orderBy=name");

			BufferedReader in = new BufferedReader(new InputStreamReader(DMDDurl.openStream()));

			//Make the url source code into an array of Strings
			int numberOfLinesURL = getURLLines(DMDDurl);
			String[] DMDDurlLineArray = makeURLLineArray(DMDDurl, numberOfLinesURL);
			String temp;
					
			for (int i = 0; i < numberOfLinesURL; i++) {
				//Goes through the array trying to find the url to the edit part of the desired processor
				temp = DMDDurlLineArray[i].trim();
				if (temp.startsWith(processorName)) {
					//Get the URL
					processorEditURL_string = DMDDurlLineArray[i+1];
					found = true;
					break;
				}	
			}
					
			//Couldn't find processor message
			if (found == false) {
				String[] failed = new String [2];
				failed[0] = ("Sorry. Could not find the processor you were looking for. It may be depracated or does not exist.");
				failed[1] = "end";
				return failed;
			}
					
			//If found
			else {
				//Use this character to see if it is a 3 digit ID or 4 digit ID
				char charPointer1 = processorEditURL_string.charAt(97);
				char charPointer2 = processorEditURL_string.charAt(98);
				char charPointer3 = processorEditURL_string.charAt(99);
				int charASCII1 = (int) charPointer1;
				int charASCII2 = (int) charPointer2;
				int charASCII3 = (int) charPointer3;
				boolean ASCII1_ok = false;
				boolean ASCII2_ok = false;
				processorEditURL_string = processorEditURL_string.substring(78,100);
				String temp2 = processorEditURL_string;
				temp2.trim();
				//processorEditURL_string.trim();
			
				//Checks if last character is a number using ASCII references, if it isn't then throw it away
				if (charASCII1 < 48 || charASCII1 > 57) {
					processorEditURL_string = temp2.substring(0,19);
				}
				else
					ASCII1_ok = true;
				
				if ((charASCII2 < 48 || charASCII2> 57) &&  ASCII1_ok) {
					processorEditURL_string = temp2.substring(0,20);
				}
				else
					ASCII2_ok = true;
				
				if ((charASCII3 < 48 || charASCII3 > 57) && ASCII2_ok && ASCII1_ok) {
					processorEditURL_string = temp2.substring(0,21);
				}
						
				//Now that we have the next URL, we look into the source HTML code of it 
				try {
					URL processorEditURL = new URL ("http://zema-util-01:8080/DMDataDictionary/editProcessor.do?" + processorEditURL_string);
							
					//Use this new URL as another array of strings again
					BufferedReader in2 = new BufferedReader(new InputStreamReader(processorEditURL.openStream()));
							
					int numberOfLinesURL_edit = getURLLines(DMDDurl);
					String[] DMDDurlLineArray_edit = makeURLLineArray(processorEditURL, numberOfLinesURL_edit);
					int tableIndex = 0;
							
					for (int i = 0; i < numberOfLinesURL_edit; i++) {
						temp2 = DMDDurlLineArray_edit[i].trim();
						if (temp2.startsWith("Tables")) {
							tableIndex = i;
							break;
						}
					}
					
					int j = 0;
							
					//If no tables
					if (tableIndex == 0)
						System.out.println("Error, no data table found at: " + processorEditURL);
					else {
						//out.write(realProcessorName);
				
						//Calibration purposes, see source code to understand how this cuts down looping time 4799 processors + some useless lines
						tableIndex = tableIndex + 4669;	
						
						//Prints the tables
						while (!DMDDurlLineArray_edit[tableIndex].trim().startsWith("</ul>")) {
							if ((!DMDDurlLineArray_edit[tableIndex].trim().startsWith("<")) && (!DMDDurlLineArray_edit[tableIndex].endsWith("	"))) {
								//out.write(" ," + DMDDurlLineArray_edit[tableIndex].trim() + ", " + '\n');
								arrayOfInfo[j] = realProcessorName + ", " + DMDDurlLineArray_edit[tableIndex].trim() + ", "+  processorEditURL;
								j++;
							}
							tableIndex++;
						}
						
						arrayOfInfo[j] = "end";
						//out.write(processorEditURL_string);
					}
					
					in2.close();
				}
						
				catch(IOException ierr) {
					System.out.println("Input/Output Error: " + ierr.getMessage());
				}
						 
			}

			in.close();
		}
					
		catch(IOException ierr) {
			System.out.println("Input/Output Error: " + ierr.getMessage());
		}
		
		return arrayOfInfo;
	}

	
	//Counts how many lines of text are there in a text file
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
	
	//Makes the text file into an array
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
	
}
