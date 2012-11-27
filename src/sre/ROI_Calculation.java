package sre;

import java.io.*;
import java.util.StringTokenizer;

public class ROI_Calculation {
	
	public static float deletePercentage(String benchmark)
	{
		if(benchmark.equals(""))
		{
			return 0;
		}
		else
		{
			return Float.valueOf(benchmark.substring(0,benchmark.length()-1));
		}
	}
	
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
				tuple tuple = new tuple();
				while ((line = CSVFile.file.readLine()) != null) {
					StringTokenizer stComma = new StringTokenizer(line, ",");
					if (stComma.hasMoreTokens()) 
					{
						word_with_space = stComma.nextToken();
					    StringTokenizer stSpace = new StringTokenizer(word_with_space);
					    word_without_space = stSpace.nextToken();
					    
					    
					    if(!word_without_space.equals("Name:") &&
					    		!word_without_space.equals("Description:") &&
					    		!word_without_space.equals("Account#:") &&
					    		!word_without_space.equals("Email:") &&
					    		!word_without_space.equals("Address:") &&
					    		!word_without_space.equals("Phone:") &&
					    		!word_without_space.equals("Transaction"))
					    {

					    	//----- EVALUATION PERIOD -----//
						    if(word_without_space.equals("Evaluation"))
						    {
						    	word_without_space = stSpace.nextToken();
						    	if (word_without_space.equals("Period:"))
						    	{
						    		word_without_space = stSpace.nextToken();
						    		CSVFile.start = word_without_space;
						    		word_without_space = stSpace.nextToken();
						    		word_without_space = stSpace.nextToken();
						    		CSVFile.end = word_without_space;
						    	}
						    }
						    
						    //----- TUPLES -----//
						    else 
						    {
						    	String[] arrayLine = line.split(",",-1); 
						    	tuple.date = arrayLine[0];
						    	tuple.marketValue = Float.valueOf(arrayLine[1]);
						    	
						    	if(!arrayLine[2].equals(""))
						    	{
						    		tuple.cashFlow = Float.valueOf(arrayLine[2]);
						    	}
						    	else
						    	{
						    		tuple.cashFlow = 0;
						    	}
						    	
						    	if(!arrayLine[3].equals(""))
						    	{
						    		tuple.agentFees = Float.valueOf(arrayLine[3]);
						    	}
						    	else
						    	{
						    		tuple.agentFees = 0;
						    	}

						    	tuple.benchmark = deletePercentage(arrayLine[4]);
						    	
						    	CSVFile.addTuple(tuple);
						    	System.out.println(tuple.toString());
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
