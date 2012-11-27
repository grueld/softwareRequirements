package sre;

import java.io.*;
import java.util.StringTokenizer;

public class ROI_Calculation {
	
	public static void parseFile(String nameFile, csv CSVFile)
	{
		try
		{
			CSVFile.file = new BufferedReader(new FileReader("csv/" + nameFile)); 

			try 
			{
				String line;
				String word_with_space;
				String word_without_space;
				while ((line = CSVFile.file.readLine()) != null) {
					//System.out.println(line);
					StringTokenizer stComma = new StringTokenizer(line, ",");
					while (stComma.hasMoreTokens()) {
						word_with_space = stComma.nextToken();
					    //System.out.println(word_with_space);
					    StringTokenizer stSpace = new StringTokenizer(word_with_space);
					    word_without_space = stSpace.nextToken();
					    //System.out.println(word_without_space);
					    if(word_without_space.equals("Evaluation"))
					    {
					    	word_without_space = stSpace.nextToken();
					    	//System.out.println(">>> " + word_without_space);
					    	if (word_without_space.equals("Period:"))
					    	{
					    		word_without_space = stSpace.nextToken();
					    		//System.out.println(word_without_space);
					    		CSVFile.start = word_without_space;
					    		System.out.println("start : " + CSVFile.start);
					    		word_without_space = stSpace.nextToken();
					    		word_without_space = stSpace.nextToken();
					    		CSVFile.end = word_without_space;
					    		System.out.println("end : " + CSVFile.end);
					    	}
					    }
					}
				}
			}
			finally 
			{
				CSVFile.file.close();
			}
		}
		catch (IOException ioe)
		{
			System.out.println("Error --" + ioe.toString());
		}
		
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String nameFile = args[1];
		//System.out.println(nameFile);
		csv CSVFile = new csv();
		parseFile("test.csv", CSVFile);
	}

}
