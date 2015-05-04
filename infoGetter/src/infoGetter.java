import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class infoGetter {

	public static void main(String[] args) throws IOException {
		
		//The read and write files
		String fileName = "U:" + "\\" + "arrangeMe.txt";
		String writeFile = "U:" + "\\" + "FinalSheetofCredentials.txt";
			
		
		//The Client, Processor, user name and Password
		//String client = null;
		String processor = "EMPTY";
		String userName = "EMPTY";
		String password = "EMPTY";
		
		boolean correctCredentials = false;
		boolean noCredential = false;
		
		FileWriter fstream = new FileWriter (writeFile);
		BufferedWriter out = new BufferedWriter(fstream);
		
		try
		{
			int fileLines = countLines(fileName);
			String[] lineArray = makeLineArray(fileLines, fileName);
			
			for (int i = 0; i < fileLines; i++)
			{	
				
				if (lineArray[i].startsWith(" A") || lineArray[i].startsWith(" B")
						|| lineArray[i].startsWith(" C")
						|| lineArray[i].startsWith(" D")
						|| lineArray[i].startsWith(" E")
						|| lineArray[i].startsWith(" F")
						|| lineArray[i].startsWith(" G")
						|| lineArray[i].startsWith(" H")
						|| lineArray[i].startsWith(" I")
						|| lineArray[i].startsWith(" J")
						|| lineArray[i].startsWith(" K")
						|| lineArray[i].startsWith(" L")
						|| lineArray[i].startsWith(" M")
						|| lineArray[i].startsWith(" N")
						|| lineArray[i].startsWith(" O")
						|| lineArray[i].startsWith(" P")
						|| lineArray[i].startsWith(" Q")
						|| lineArray[i].startsWith(" R")
						|| lineArray[i].startsWith(" S")
						|| lineArray[i].startsWith(" T")
						|| lineArray[i].startsWith(" U")
						|| lineArray[i].startsWith(" V")
						|| lineArray[i].startsWith(" W")
						|| lineArray[i].startsWith(" X")
						|| lineArray[i].startsWith(" Y")
						|| lineArray[i].startsWith(" Z")
						)
				{
					processor = lineArray[i];
					//System.out.println(lineArray[i]);
					//System.out.println(i);
				}
				
				else if (lineArray[i].endsWith("FTPLoginSourceConfig"))
				{
					correctCredentials = true;
					noCredential = false;
				}
				
				else if (lineArray[i].endsWith("DBDestinationConfig"))
				{
					noCredential = true;
				}
				
				else if (lineArray[i].endsWith("HTTPSourceConfig"))
				{
					password = "None because HTTPSourceConfig";
					userName = "None because HTTPSourceConfig";	
					noCredential = false;
				}
				
				else if (lineArray[i].endsWith("EmailSubjectConfig"))
				{
					
				}
				
				else if (lineArray[i].startsWith("		password") && correctCredentials == true)
				{
					password = lineArray[i];
					password = printGoodInfo (password);
				}
				
				else if (lineArray[i].startsWith("		username") && correctCredentials == true)
				{
					userName = lineArray[i];
					userName = printGoodInfo (userName);
				} 
				
				//client = "ENE";
				
				else if (lineArray[i].endsWith("Processor	"))
				{
					if (!processor.equals("EMPTY") && !password.equals("EMPTY") && !userName.equals("EMPTY") && noCredential == false)
					{
						out.write(processor + "," + userName + "," + password + '\n');
						//System.out.print(processor + "       ," + userName + "       ," + password + '\n');
						correctCredentials = false;
						processor = password = userName = "EMPTY";
					}
				
					else if (!processor.equals("EMPTY") && password.equals("EMPTY") && userName.equals("EMPTY") && noCredential == true)
					{
						out.write(processor + "," + "No credential" + "," + "No credential" + '\n');
						correctCredentials = false;
						noCredential = false;
						processor = password = userName = "EMPTY";
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
	
	public static String printGoodInfo (String target)
	{
		String goodInfo = "";
		int printIndex = "		password => ".length();
		char stringPoints;
		
		for (int i = printIndex; i < target.length(); i++)
		{
			stringPoints = target.charAt(i);
			goodInfo += stringPoints;
		}
		return goodInfo;
	}
	
	//Counts rows of lines in a text file
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
	
	//Makes the text file into an array of strings
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
