package sre;

import java.io.*;
import java.util.Iterator;
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
	
	
	/**
	 * Checks if 'start' and 'end' are among the dates of the CSV file
	 * @param CSVFile
	 * @return 
	 */
	public static boolean checkEvaluationPeriod(csv CSVFile)
	{
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		boolean startInDates = false;
		boolean endInDates = false;
		tuple t = new tuple();
		String date;
		while(!(startInDates && endInDates) && i.hasNext())
		{
			t = i.next();
			date = t.date;
			if(date.equals(CSVFile.start))
			{
				startInDates = true;
			}
			if(date.equals(CSVFile.end))
			{
				endInDates = true;
			}
		}
		return (startInDates && endInDates);
	}
	
	
	/**
	 * Parse the CSV file
	 * @param nameFile
	 * @param CSVFile
	 */
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
								tuple tuple = new tuple();
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
						    	System.out.println(tuple.toString());
						    	CSVFile.addTuple(tuple);						    	
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
	
		checkEvaluationPeriod(CSVFile);
		//System.out.println(checkEvaluationPeriod(CSVFile));
		
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
