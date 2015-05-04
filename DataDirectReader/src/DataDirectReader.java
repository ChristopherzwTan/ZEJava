//Programmer: Christopher Tan 
//Date: November 5, 2012
//
//Purpose: ????????
//



import java.io.IOException;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class DataDirectReader {

	public static void main(String[] args) {
		
		String fileName;
		String directory;
		String readingFile;

		Scanner user_input = new Scanner (System.in);
		
		System.out.print("Please enter a directory: ");
		directory = user_input.nextLine();
		
		System.out.print("Enter the file you want to readin the above directory: ");
		readingFile = user_input.nextLine();
		
		fileName = directory + "/" + readingFile + ".txt";
		
		try
		{
			readFile file = new readFile(fileName);
			String[] lineArray = file.OpenFile();
				
			for (int i = 0; i < lineArray.length; i++)
			{
				if (lineArray[i].startsWith("https://"))
				{
					try {
						URL u = new URL(lineArray[i]);
						HttpURLConnection uc = (HttpURLConnection) u.openConnection();
						int code = uc.getResponseCode();
						String response = uc.getResponseMessage();
						System.out.println("HTTP/1.x " + code + " " + response);
						for(int j = 1; ; j++)
						{
							String header = uc.getHeaderField(j);
							String key = uc.getHeaderFieldKey(j);
							if(header == null || key == null)
								break;
							System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
						}
						InputStream in = new BufferedInputStream(uc.getInputStream());
						Reader r = new InputStreamReader(in);
						int c;
						while((c = r.read()) != -1)
						{
						  System.out.print((char)c);
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
			}
		}
	
		
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

}
