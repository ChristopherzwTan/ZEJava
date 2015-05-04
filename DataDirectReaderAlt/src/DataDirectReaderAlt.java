import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Scanner;


public class DataDirectReaderAlt {

	public static void main (String[] args)
	{
		String fileName;
		String directory;
		String readingFile;

		Scanner user_input = new Scanner (System.in);
		
		System.out.print("Please enter a directory: ");
		directory = user_input.nextLine();
		
		System.out.print("Enter the file you want to readin the above directory: ");
		readingFile = user_input.nextLine();
		
		fileName = directory + "/" + readingFile + ".txt";
		
		//Reads file
		try
		{
			FileInputStream fstream = new FileInputStream(fileName);

			DataInputStream in = new DataInputStream(fstream);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			PrintStream out = new PrintStream(new FileOutputStream(fileName));
			
			while ((strLine = br.readLine()) != null)
			{
				//get lineArray
			}

			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}

}
