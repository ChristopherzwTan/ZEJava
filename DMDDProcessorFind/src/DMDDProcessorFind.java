import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;


public class DMDDProcessorFind {

	public static void main(String[] args) throws IOException {
		
		while (true) {
		
		//Variables
		Scanner input = new Scanner(System.in);
		String tableName = null;
		boolean found = false;
		String tableEditURL_string = null;
		String processorName = null;
	
		//Gets input which is table name
		System.out.print("\nPlease input the table: ");
		tableName = input.nextLine();
		tableName.trim();
		
		if (tableName.equalsIgnoreCase("exit")) {
			break;
		}
	
		else if (tableName.equalsIgnoreCase("help")) {
			System.out.println ("\nWrite down the table name and this program will give you the table's processors and edit link. \n \n \n");
		}
		else {
			tableName = "<li>" + tableName;
			
			try {
				//DMDD URL for tables
				URL DMDDurl = new URL("http://zema-util-01:8080/DMDataDictionary/allObjects.do?type=Table&orderBy=name");
				
				BufferedReader in = new BufferedReader(new InputStreamReader(DMDDurl.openStream()));

				//Make the url source code into an array of Strings
				int numberOfLinesURL = getURLLines(DMDDurl);
				String[] DMDDurlLineArray = makeURLLineArray(DMDDurl, numberOfLinesURL);
				String temp;
		
				for (int i = 0; i < numberOfLinesURL; i++) {
					//Goes through the array trying to find the url to the edit part of the desired processor
					temp = DMDDurlLineArray[i].trim();
					if (temp.startsWith(tableName.trim())) {
						//Get the URL
						tableEditURL_string = DMDDurlLineArray[i+1];
						found = true;
						break;
					}	
				}
			
				if (tableName.equals("<li>")) {
					found = false;
				}
			
				//Couldn't find processor message
				if (found == false) {
					System.out.println("\nSorry. Could not find the table you were looking for. It may be depracated or does not exist. \n \n \n");
				}
		
				//If found
				else {
					
					tableEditURL_string = tableEditURL_string.trim();
					//System.out.println(tableEditURL_string);
					
					char charPointer1 = tableEditURL_string.charAt(81);
					char charPointer2 = tableEditURL_string.charAt(82);
					char charPointer3 = tableEditURL_string.charAt(83);
					//System.out.println(charPointer1 + "    " + charPointer2 + "     " + charPointer3);
					int charASCII1 = (int) charPointer1;
					int charASCII2 = (int) charPointer2;
					int charASCII3 = (int) charPointer3;
					boolean ASCII1_ok = false;
					boolean ASCII2_ok = false;
					tableEditURL_string = tableEditURL_string.substring(66,84);
					String temp2 = tableEditURL_string;
					temp2.trim();
					
					//Checks if last character is a number using ASCII references, if it isn't then throw it away
					if (charASCII1 < 48 || charASCII1 > 57) {
						tableEditURL_string = temp2.substring(0,15);
					}
					else
						ASCII1_ok = true;
				
					if ((charASCII2 < 48 || charASCII2> 57) &&  ASCII1_ok) {
						tableEditURL_string = temp2.substring(0,16);
					}
					else
						ASCII2_ok = true;
				
					if ((charASCII3 < 48 || charASCII3 > 57) && ASCII2_ok && ASCII1_ok) {
						tableEditURL_string = temp2.substring(0,17);
					}
					
					//Now that we have the next URL, we look into the source HTML code of it 
					try {
						URL tableEditURL = new URL ("http://zema-util-01:8080/DMDataDictionary/editTable.do?" + tableEditURL_string);
				
						//Use this new URL as another array of strings again
						BufferedReader in2 = new BufferedReader(new InputStreamReader(tableEditURL.openStream()));
				
						int numberOfLinesURL_edit = getURLLines(DMDDurl);
						String[] DMDDurlLineArray_edit = makeURLLineArray(tableEditURL, numberOfLinesURL_edit);
						int processorIndex = 0;
				
						for (int i = 0; i < numberOfLinesURL_edit; i++) {
							temp2 = DMDDurlLineArray_edit[i].trim();
							if (temp2.startsWith("Processors")) {
								processorIndex = i;
								break;
							}
						}
						
						//If no tables
						if (processorIndex == 0)
							System.out.println("Error, no data table found at: " + processorIndex);
						else {
				
							//Calibration purposes, see source code to understand how this cuts down looping time 4000+ table list to skip.
							processorIndex = processorIndex + 3090;
							System.out.println("\nProcessors: ");
				
							//Prints the tables
							while (!DMDDurlLineArray_edit[processorIndex].trim().startsWith("</ul>")) {
								if ((!DMDDurlLineArray_edit[processorIndex].trim().startsWith("<")) && (!DMDDurlLineArray_edit[processorIndex].endsWith("	"))) {
									System.out.println(" - " + DMDDurlLineArray_edit[processorIndex].trim());
									processorName = DMDDurlLineArray_edit[processorIndex].trim();
								}
								processorIndex++;
							}
				
							System.out.print("\nLink of table: \n \n" +  tableEditURL + "\n \n \n");
						}
			
						in2.close();
					}
					catch(IOException ierr) {
						System.out.println("Input/Output Error: " + ierr.getMessage());
					}
					
				}
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

