package sre;

import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.swing.text.html.CSS;

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
	
	
	public static boolean checkStartBeforeEnd(csv CSVFile)
	{
		if (CSVFile.start.before(CSVFile.end))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	
	public static boolean checkNonNegativeMarketValue(csv CSVFile)
	{
		boolean result = true;
		float marketValue;
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		while(result && i.hasNext())
		{
			marketValue = i.next().marketValue;
			if(marketValue < 0)
			{
				result = false;
			}
		}
		return result;
	}

	
	public static boolean checkDateUniqueOrdered(csv CSVFile)
	{
		boolean result = true;
		int i = 0;
		while(result && i<CSVFile.listTuple.size()-1)
		{
			if (!CSVFile.listTuple.get(i).date.before(CSVFile.listTuple.get(i+1).date))
			{
				result = false;
			}
			i++;
		}
		return result;
	}
		
	
	public static boolean checkWithdraw(csv CSVFile)
	{
		boolean result = true;
		float addition;
		tuple tuple = new tuple();
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		while(result && i.hasNext())
		{
			tuple = i.next();
			addition = tuple.marketValue + tuple.cashFlow;
			if (addition < 0)
			{
				result = false;
			}
		}
		return result;
	}
	
	
	public static boolean checkEvaluationPeriodDates(csv CSVFile)
	{
		boolean result = true;
		if(!CSVFile.start.validDate())
		{
			result = false;
		}
		
		if(result && !CSVFile.end.validDate())
		{
			result = false;
		}
		return result;
	}
	
	
	public static boolean checkTuplesDates(csv CSVFile)
	{
		boolean result = true;	
		date date;
		Iterator<tuple> i = CSVFile.listTuple.iterator();
		while(result && i.hasNext())
		{
			date = i.next().date;
			if(!date.validDate())
			{
				result = false;
			}
		}
		return result;
	}
	
	
	public static boolean checkAccountGrow(csv CSVFile)
	{
		if(CSVFile.listTuple.size()>1)
		{
			boolean result = true;
			int i = 0;
			while(result && i<CSVFile.listTuple.size()-1)
			{
				if (CSVFile.listTuple.get(i).marketValue == 0 &&
						CSVFile.listTuple.get(i).cashFlow == 0 && 
						CSVFile.listTuple.get(i+1).marketValue != 0)
				{
					result = false;
				}
				i++;
			}
			return result;
		}
		else
		{
			return true;
		}
	}
	
	
	public static boolean checkNameNotNull(csv CSVFile)
	{
		if(CSVFile.name == null)
		{
			return false;
		}
		else
		{
			return true;
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
				while ((line = CSVFile.file.readLine()) != null) {
					StringTokenizer stComma = new StringTokenizer(line, ",");
					if (stComma.hasMoreTokens()) 
					{
						word_with_space = stComma.nextToken();
					    StringTokenizer stSpace = new StringTokenizer(word_with_space);
					    word_without_space = stSpace.nextToken();
					    
					    if(!word_without_space.equals("Description:") &&
					    		!word_without_space.equals("Account#:") &&
					    		!word_without_space.equals("Email:") &&
					    		!word_without_space.equals("Address:") &&
					    		!word_without_space.equals("Phone:") &&
					    		!word_without_space.equals("Transaction"))
					    {
					    	//----- NAME -----//
						    if(word_without_space.equals("Name:"))
						    {
						    	if(stSpace.hasMoreTokens())
					    		{
							    	word_without_space = stSpace.nextToken();
							    	CSVFile.name = word_without_space;
							    	while (stSpace.hasMoreTokens())
							    	{
							    		CSVFile.name += " " + stSpace.nextToken();
							    	}	
							    	
					    		}
						    }
					    	
					    	//----- EVALUATION PERIOD -----//
						    else if(word_without_space.equals("Evaluation"))
						    {
						    	word_without_space = stSpace.nextToken();
						    	if (word_without_space.equals("Period:"))
						    	{
						    		if(stSpace.hasMoreTokens())
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
							    		if(stSpace.hasMoreTokens())
							    		{
								    		word_without_space = stSpace.nextToken();
								    		if(stSpace.hasMoreTokens())
								    		{
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
								    		}
							    		}
							    	}
						    	}
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
						    	//System.out.println(tuple.toString());
						    	CSVFile.addTuple(tuple);						    	
						    }
					    }
					}
				}
				System.out.println("The name is not null :                               " + checkNameNotNull(CSVFile));
				System.out.println("Tuple dates are valid :                              " + checkTuplesDates(CSVFile));
				System.out.println("Evaluation period dates are valid :                  " + checkEvaluationPeriodDates(CSVFile));
				System.out.println("Start before end :                                   " + checkStartBeforeEnd(CSVFile));
				System.out.println("Evaluation period dates are in dates :               " + checkEvaluationPeriod(CSVFile));
				System.out.println("Each tuple has a date :                              " + checkTupleHasDate(CSVFile));
				System.out.println("Each tuple has a non-negative market value :         " + checkNonNegativeMarketValue(CSVFile));
				System.out.println("Dates are unique and ordered :                       " + checkDateUniqueOrdered(CSVFile));
				System.out.println("Withdraws are less important than the market value : " + checkWithdraw(CSVFile));
				System.out.println("Account does not grow  :                             " + checkAccountGrow(CSVFile));
			}
			finally 
			{
				CSVFile.file.close();
			}
		}
		catch (IOException ioe)
		{
			System.out.println("Error -- " + ioe.toString());
		}
		
	}
	
	
	public static void main(String[] args) {
		//String nameFile = args[1];
		//System.out.println(nameFile);
		csv CSVFile = new csv();
		parseFile("sample.csv", CSVFile);
		//parseFile("test_without_name.csv", CSVFile);
		//parseFile("test_date_invalid.csv", CSVFile);
		//parseFile("test_date_invalid_February.csv", CSVFile);
		//parseFile("test_without_evaluation_period.csv", CSVFile);
		//parseFile("test_start_invalid.csv", CSVFile);
		//parseFile("test_end_invalid.csv", CSVFile);
		//parseFile("test_start_after_end.csv", CSVFile);
		//parseFile("test_eval_not_in_dates.csv", CSVFile);
		//parseFile("test_tuple_without_date.csv", CSVFile);
		//parseFile("test_negative_market_value.csv", CSVFile);
		//parseFile("test_dates_non_unique.csv", CSVFile);
		//parseFile("test_dates_non_ordered.csv", CSVFile);
		//parseFile("test_withdraw.csv", CSVFile);
		//parseFile("test_grow.csv", CSVFile);
	}

}
