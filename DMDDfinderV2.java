
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

	public static void main(String[] args) throws IOException {
		
		Scanner input = new Scanner(System.in);
		String readFile = null;
		String writeFile = null;
		
		FileWriter fstream = new FileWriter (writeFile);
		BufferedWriter out = new BufferedWriter(fstream);
		
		System.out.print("Please type in the full path of the text file you want to read: ");
		readFile = input.nextLine();
		
		System.out.print("Please type in the full path of the text file you want to write to as output: ");
		writeFile = input.nextLine();
		
		try {
			DMDDfind("PlattsInsert", readFile, writeFile);
			//DMDDfind("PlatssRef",readFile, writeFile);
			out.close();
		}
		catch(IOException ierr) {
			System.out.println("Input/Output Error: " + ierr.getMessage());
		}
	}

	
	public static void DMDDfind(String processorName, String readFile, String writeFile) throws IOException {
		
		//Variables
		boolean found = false;
		String processorEditURL_string = null;
		
		FileWriter fstream = new FileWriter (writeFile);
		BufferedWriter out = new BufferedWriter(fstream);
		
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
				System.out.println("\nSorry. Could not find the processor you were looking for. It may be depracated or does not exist. \n \n \n");
			}
					
			//If found
			else {
				//Use this character to see if it is a 3 digit ID or 4 digit ID
				char charPointer = processorEditURL_string.charAt(99);
				int charASCII = (int) charPointer;
				processorEditURL_string = processorEditURL_string.substring(78,100);
				processorEditURL_string.trim();
						
				//Checks if last character is a number using ASCII references, if it isn't then throw it away
				if (charASCII < 48 || charASCII > 57) {
					processorEditURL_string = processorEditURL_string.substring(0,21);
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
						String temp2 = DMDDurlLineArray_edit[i].trim();
						if (temp2.startsWith("Tables")) {
							tableIndex = i;
							break;
						}
					}
							
					//If no tables
					if (tableIndex == 0)
						System.out.println("Error, no data table found at: " + processorEditURL);
					else {
							
						//System.out.print(realProcessorName);
						out.write(realProcessorName);
						
						//Calibration purposes, see source code to understand how this cuts down looping time 4799 processors + some useless lines
						tableIndex = tableIndex + 4823;
							
						//Prints the tables
						while (!DMDDurlLineArray_edit[tableIndex].trim().startsWith("</ul>")) {
							if ((!DMDDurlLineArray_edit[tableIndex].trim().startsWith("<")) && (!DMDDurlLineArray_edit[tableIndex].endsWith("	"))) {
								//System.out.println(" ," + DMDDurlLineArray_edit[tableIndex].trim() + ", ");
								out.write(" ," + DMDDurlLineArray_edit[tableIndex].trim() + ", " + '\n');
							}
							tableIndex++;
						}
					
						//System.out.print(processorEditURL_string);
						out.write(processorEditURL_string);
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
