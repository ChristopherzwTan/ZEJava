//Author: Christopher Tan
//Date: August 20, 2013
//
//
//

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DBPropertiesChanger {

	public static void main(String[] args) throws IOException {
		
		Scanner input = new Scanner(System.in);
		String readFile = null;
		String writeFile = null;
		String hostName = null;
		String SID_serviceName = null;
		String schemaPassword = null;
		int port = 1521;
		int choice = 0;
		String DB_url = null;
		
		//Input database.properties file
		System.out.print("\nPlease input the database.properties file you want to modify: ");
		readFile = input.nextLine();
		System.out.print("\n");
		
		//Output file
		System.out.print("Please type in the full path of the database.properties you want to write to as output: ");
		writeFile = input.nextLine();
		writeFile = writeFile + ".properties";
		System.out.print("\n");
		
		//File buffers and stuff
		FileReader input_read = new FileReader (readFile);
		BufferedReader in_file = new BufferedReader(input_read);
				
		FileWriter output_write = new FileWriter (writeFile);
		BufferedWriter out_file = new BufferedWriter(output_write);	
		
		System.out.print("Enter 1 for Oracle, 2 for MSSQL DB: ");
		choice = input.nextInt();
		input.nextLine();
		
		//oracle
		if (choice == 1) {
			
			port = 1521;
		
			//Prompts
			System.out.print("Please enter the hostname: ");
			hostName = input.nextLine();
			System.out.print("\n");
			
			System.out.print("Please enter the SID/Service Name: ");
			SID_serviceName = input.nextLine();
			System.out.print("\n");
			
			System.out.print("Please enter the port number: ");
			port = input.nextInt();
			input.nextLine();
			System.out.print("\n");
			
			DB_url = "jdbc:oracle:thin:@" + hostName + ":" +  port + ":" + SID_serviceName;
		}
		
		//MSSQL
		else if (choice == 2) {
			
			port = 1433;
			String property = "false";
			
			System.out.print("Please enter the hostname: ");
			hostName = input.nextLine();
			System.out.print("\n");
			
			System.out.print("Please enter the SID/Service Name: ");
			SID_serviceName = input.nextLine();
			System.out.print("\n");
			
			System.out.print("Please enter the port number: ");
			port = input.nextInt();
			input.nextLine();
			System.out.print("\n");
			
			System.out.print("Please enter security property: ");
			property = input.nextLine();
			System.out.print("\n");
			
			if (!(property.equalsIgnoreCase("false")) || !(property.equalsIgnoreCase("true"))) {
				property = "false";
			}
			
			DB_url = "jdbc:sqlserver://" + hostName + ":" + port + ";" + "DatabaseName=" + SID_serviceName + ";integratedSecurity=" + property;
		}
		
		try {
			int finished = 0;
			int fileLines = countLines(readFile);
			String[] lineArray = makeLineArray(fileLines, readFile);
			
			String user = null;
			
			//Look through the file
			for (int i = 0; i < fileLines; i++) {
				
				//Check if it's still changing properties
				if (lineArray[i].startsWith("db_vendor")) {
					finished = i;
					break;
				}
				
				//Check for empty lines
				else if (!(lineArray[i].isEmpty())) {
					if (lineArray[i].trim().startsWith("ze_")) {
						int dotIndex = lineArray[i].indexOf(".");
				
						user = lineArray[i].substring(0, dotIndex);
				
						//Auto-change URL
						if (lineArray[i].trim().startsWith("writeService.jdbc.url=")) {
							out_file.write("ze_data" + ".jdbc.url=" + DB_url + "\n");
						}
						else if (lineArray[i].trim().startsWith(user + ".jdbc.url=")) {
							out_file.write(user + ".jdbc.url=" + DB_url + "\n");
						}
					
						//put auto-change passwords here
						else if (lineArray[i].trim().startsWith(user + ".jdbc.password=")) {
							System.out.println("Please enter schema password for schema " + user + ": ");
							schemaPassword = input.nextLine();
							out_file.write(user + ".jdbc.password=" + schemaPassword + "\n");
						}
						else if (lineArray[i].trim().startsWith("writeService.jdbc.password=")) {
							System.out.println("Please enter schema password for ZE_DATA: ");
							schemaPassword = input.nextLine();
							out_file.write("writeService.jdbc.password=" + schemaPassword + "\n");
						}
					
						//For empty lines
						else
							out_file.write(lineArray[i] + "\n");
					}
					//For other not empty lines
					else
						out_file.write(lineArray[i] + "\n");
				}
				else
					out_file.write(lineArray[i] + "\n");
			}		
			
			if (choice == 1) {
				out_file.write("db_vendor=oracle\n");
			}
			else if (choice == 2) {
				out_file.write("db_vendor=mssql\n");
			}
			finished++;
			
			//For other than ZE schemas
			for (int i = finished; i < fileLines; i++) {
				out_file.write(lineArray[i] + "\n");
			}
		}
		
		catch(IOException ierr) {
			System.out.println("Input/Output Error: " + ierr.getMessage());
		}
		
		//Finishing message
		System.out.println("\n" + "DONE!");
		
		out_file.close();
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
	
}
