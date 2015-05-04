
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DMDDfinder {
	
	public static void main(String[] args) throws IOException{

		
		while (true) {	
	
			
			//Variables
			Scanner input = new Scanner(System.in);
			String processorName = null;
			boolean found = false;
			String processorEditURL_string = null;
		
			//Gets input which is processor name
			System.out.print("\nPlease input the processor: ");
			processorName = input.nextLine();
			processorName.trim();
		
		
			if (processorName.equalsIgnoreCase("exit")) {
				break;
			}
		
			else if (processorName.equalsIgnoreCase("help")) {
				System.out.println ("\nWrite down the processor name and this program will give you the processor's tables and edit link. \n \n \n");
			}
			else {
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
						if (temp.startsWith(processorName.trim())) {
							//Get the URL
							processorEditURL_string = DMDDurlLineArray[i+1];
							found = true;
							break;
						}	
					}
				
					if (processorName.equals("<li>")) {
						found = false;
					}
				
					//Couldn't find processor message
					if (found == false) {
						System.out.println("\nSorry. Could not find the processor you were looking for. It may be depracated or does not exist. \n \n \n");
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
					
							//If no tables
							if (tableIndex == 0)
								System.out.println("Error, no data table found at: " + processorEditURL);
							else {
					
								//Calibration purposes, see source code to understand how this cuts down looping time 4000+ processor list to skip.
								tableIndex = tableIndex + 4669;
								System.out.println("\nTables: ");
					
								//Prints the tables
								while (!DMDDurlLineArray_edit[tableIndex].trim().startsWith("</ul>")) {
									if ((!DMDDurlLineArray_edit[tableIndex].trim().startsWith("<")) && (!DMDDurlLineArray_edit[tableIndex].endsWith("	"))) {
										System.out.println(" - " + DMDDurlLineArray_edit[tableIndex].trim());
									}
									tableIndex++;
								}
					
								System.out.print("\nLink of Processor: \n \n" +  processorEditURL + "\n \n \n");
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
