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
	
	
	public static boolean checkEvaluationPeriod(csv CSVFile)
	{
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		boolean startInDates = false;
		boolean endInDates = false;
		date date;
		while(!(startInDates && endInDates) && i.hasNext())
		{
			date = i.next().date;
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
	
	
	public static boolean checkTupleHasDate(csv CSVFile)
	{
		boolean result = true;
		date date;
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		while(result && i.hasNext())
		{
			date = i.next().date;
			if(date.year == 0 || date.month == 0 || date.day == 0)
			{
				result = false;
			}
		}
		return result;
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
				while ((line = CSVFile.file.readLine()) != null) {
					StringTokenizer stComma = new StringTokenizer(line, ",");
					if (stComma.hasMoreTokens()) 
					{
						word_with_space = stComma.nextToken();
					    StringTokenizer stSpace = new StringTokenizer(word_with_space);
					    word_without_space = stSpace.nextToken();
					    
					    //TODO : name obligatoire
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
						    		StringTokenizer stDashStart = new StringTokenizer(word_without_space, "-");
						    		if (stDashStart.hasMoreTokens())
						    		{
						    			CSVFile.start.year = Integer.valueOf(stDashStart.nextToken());
						    			if (stDashStart.hasMoreTokens())
						    			{
						    				CSVFile.start.month = Integer.valueOf(stDashStart.nextToken());
						    				if (stDashStart.hasMoreTokens())
							    			{
						    					CSVFile.start.day = Integer.valueOf(stDashStart.nextToken());
							    			}
						    			}
						    		}
						    		//CSVFile.start = word_without_space;
						    		word_without_space = stSpace.nextToken();
						    		word_without_space = stSpace.nextToken();
						    		StringTokenizer stDashEnd = new StringTokenizer(word_without_space, "-");
						    		if (stDashEnd.hasMoreTokens())
						    		{
						    			CSVFile.end.year = Integer.valueOf(stDashEnd.nextToken());
						    			if (stDashEnd.hasMoreTokens())
						    			{
						    				CSVFile.end.month = Integer.valueOf(stDashEnd.nextToken());
						    				if (stDashEnd.hasMoreTokens())
							    			{
						    					CSVFile.end.day = Integer.valueOf(stDashEnd.nextToken());
							    			}
						    			}
						    		}
						    		//CSVFile.end = word_without_space;
						    	}
						    	System.out.println("start : " + CSVFile.start.toString());
						    	System.out.println("end   : " + CSVFile.end.toString());
						    }
						    
						    //----- TUPLES -----//
						    else 
						    {
						    	String[] arrayLine = line.split(",",-1); 
								tuple tuple = new tuple();

								StringTokenizer stDashDate = new StringTokenizer(arrayLine[0], "-");
					    		if (stDashDate.hasMoreTokens())
					    		{
					    			tuple.date.year = Integer.valueOf(stDashDate.nextToken());
					    			if (stDashDate.hasMoreTokens())
					    			{
					    				tuple.date.month = Integer.valueOf(stDashDate.nextToken());
					    				if (stDashDate.hasMoreTokens())
						    			{
					    					tuple.date.day = Integer.valueOf(stDashDate.nextToken());
						    			}
					    			}
					    		}
								//tuple.date = arrayLine[0];
					    		
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
				System.out.println("Each tuple has a date : " + checkTupleHasDate(CSVFile));
				System.out.println("Evaluation period dates are in dates : " + checkEvaluationPeriod(CSVFile));
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
	
	
	public static void main(String[] args) {
		//String nameFile = args[1];
		//System.out.println(nameFile);
		csv CSVFile = new csv();
		parseFile("sample.csv", CSVFile);
		//parseFile("test_eval_not_in_dates.csv", CSVFile);
		//parseFile("test_tuple_without_date.csv", CSVFile);
	}

}
