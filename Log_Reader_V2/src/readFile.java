import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;		
		
	
	
public class readFile 
{
	private String path;
	
	public readFile (String file_path)
	{
		path = file_path;
	}
	
	public String[] OpenFile () throws IOException 
	{
		FileReader fileSearcher = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fileSearcher);
	
		int numberOfLines = countLines();
		String[] textData = new String[numberOfLines];
	
		for (int i = 0; i < numberOfLines; i++) {
			textData[i] = textReader.readLine();
		}
		
		textReader.close();
		return textData;
	}
	
	int countLines() throws IOException
	{
		FileReader file_to_read = new FileReader(path);
		BufferedReader bf = new BufferedReader(file_to_read);
		
		int numberOfLines = 0;
		
		while ((bf.readLine()) != null)
		{
			numberOfLines++;
		}
		bf.close();
		
		return numberOfLines;
	}
}
	
	