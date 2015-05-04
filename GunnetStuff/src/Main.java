
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Main
{
	public static void main (String[] args)
	{
		try
		{
			FileInputStream fstream = new FileInputStream("test.txt");

			DataInputStream in = new DataInputStream(fstream);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			PrintStream out = new PrintStream(new FileOutputStream("temp.txt"));
			
			while ((strLine = br.readLine()) != null)
			{
				if (!(strLine.equals("Started Processes:") || strLine.equals("Finished Processes:") || strLine.equals("Vetoed Processes:")))
				{
					out.println(strLine);
				}
				else
				{
					String temp;
					
					while((temp = br.readLine()) != null)
					{
						if ((temp.contains("Queued Processes")) || (temp.contains("Running Processes")))
						{
							out.println(temp);
							break;
						}
					}
				}
			}

			in.close();
		}
		catch (Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
}
