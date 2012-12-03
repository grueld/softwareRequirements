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
		if (!(startInDates && endInDates))
		{
			CSVFile.warningInvalidEvaluationPeriod = true;
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
		if (!result)
		{
			CSVFile.errorInvalidFile = true;
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
			CSVFile.warningInvalidEvaluationPeriod = true;
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
			marketValue = i.next().mv;
			if(marketValue < 0)
			{
				result = false;
			}
		}
		if (!result)
		{
			CSVFile.errorInvalidFile = true;
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
		if (!result)
		{
			CSVFile.errorInvalidFile = true;
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
			addition = tuple.mv + tuple.cf;
			if (addition < 0)
			{
				result = false;
			}
		}
		if (!result)
		{
			CSVFile.errorInvalidFile = true;
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
		if (!result)
		{
			CSVFile.warningInvalidEvaluationPeriod = true;
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
		if (!result)
		{
			CSVFile.errorInvalidFile = true;
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
				if (CSVFile.listTuple.get(i).mv == 0 &&
						CSVFile.listTuple.get(i).cf == 0 && 
						CSVFile.listTuple.get(i+1).mv != 0)
				{
					result = false;
				}
				i++;
			}
			if (!result)
			{
				CSVFile.errorInvalidFile = true;
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
			CSVFile.warningNoName = true;
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
			CSVFile.file = new BufferedReader(new FileReader(nameFile)); 

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
						if(stSpace.hasMoreTokens())
						{
							word_without_space = stSpace.nextToken();

							if(!word_without_space.equals("Description:") &&
									!word_without_space.equals("Account#:") &&
									!word_without_space.equals("Email:") &&
									!word_without_space.equals("Address:") &&
									!word_without_space.equals("Phone:") &&
									!word_without_space.equals("Transaction") &&
									!word_without_space.equals("Description") &&
									!word_without_space.equals("Account#") &&
									!word_without_space.equals("Email") &&
									!word_without_space.equals("Address") &&
									!word_without_space.equals("Phone"))
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
								else if(word_without_space.equals("Name"))
								{
									if(stSpace.hasMoreTokens())
									{
										word_without_space = stSpace.nextToken();
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
								}

								//----- EVALUATION PERIOD -----//
								else if(word_without_space.equals("Evaluation"))
								{
									try
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
										else if (word_without_space.equals("Period"))
										{
											if(stSpace.hasMoreTokens())
											{
												word_without_space = stSpace.nextToken();
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
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Evaluation period is not right.");
									}
								}

								//----- TUPLES -----//
								else 
								{
									String[] arrayLine = line.split(",",-1); 
									tuple tuple = new tuple();
									try
									{
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
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Date is not right.");
									}

									try
									{
										tuple.mv = Float.valueOf(arrayLine[1]);
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Market Value is not right.");
									}

									try
									{
										if(!arrayLine[2].equals(""))
										{
											tuple.cf = Float.valueOf(arrayLine[2]);
										}
										else
										{
											tuple.cf = 0;
										}
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Cash Flow is not right.");
									}

									try
									{
										if(!arrayLine[3].equals(""))
										{
											tuple.af = Float.valueOf(arrayLine[3]);
										}
										else
										{
											tuple.af = 0;
										}
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Agent Fees is not right.");
									}

									try
									{
										tuple.bm = deletePercentage(arrayLine[4]);
									}
									catch (NumberFormatException e)
									{
										System.out.println("The format of Benchmark is not right.");
									}

									//System.out.println(tuple.toString());
									CSVFile.addTuple(tuple);						    	
								}
							}
						}
					}
				}
				//				System.out.println("The name is not null :                               " + checkNameNotNull(CSVFile));
				//				System.out.println("Tuple dates are valid :                              " + checkTuplesDates(CSVFile));
				//				System.out.println("Evaluation period dates are valid :                  " + checkEvaluationPeriodDates(CSVFile));
				//				System.out.println("Start before end :                                   " + checkStartBeforeEnd(CSVFile));
				//				System.out.println("Evaluation period dates are in dates :               " + checkEvaluationPeriod(CSVFile));
				//				System.out.println("Each tuple has a date :                              " + checkTupleHasDate(CSVFile));
				//				System.out.println("Each tuple has a non-negative market value :         " + checkNonNegativeMarketValue(CSVFile));
				//				System.out.println("Dates are unique and ordered :                       " + checkDateUniqueOrdered(CSVFile));
				//				System.out.println("Withdraws are less important than the market value : " + checkWithdraw(CSVFile));
				//				System.out.println("Account does not grow  :                             " + checkAccountGrow(CSVFile));
				checkNameNotNull(CSVFile);
				checkTuplesDates(CSVFile);
				checkEvaluationPeriodDates(CSVFile);
				checkStartBeforeEnd(CSVFile);
				checkEvaluationPeriod(CSVFile);
				checkTupleHasDate(CSVFile);
				checkNonNegativeMarketValue(CSVFile);
				checkDateUniqueOrdered(CSVFile);
				checkWithdraw(CSVFile);
				checkAccountGrow(CSVFile);
				//				System.out.println("") ;
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


	public static void printOutput(csv CSVFile,
			double TWR_WI, double ROI_WI, double bench_WI,
			double TWR_EP, double ROI_EP, double bench_EP)
	{
		if(CSVFile.warningNoName)
		{
			System.out.println("Name: undefined");
		}
		else
		{
			System.out.println("Name: " + CSVFile.name);
		}

		System.out.println("--------------------------------------------");
		date first = CSVFile.listTuple.get(0).date;
		date last = CSVFile.listTuple.get(CSVFile.listTuple.size()-1).date;
		System.out.println("Whole input: " + first.toString() + " to " + last.toString());

		if(TWR_WI == Double.MAX_VALUE)
		{
			System.out.println("TWR: undefined");
		}
		else
		{
			double newTWR_WI = (double) Math.round(TWR_WI * 100) / 100;
			System.out.println("TWR: " + newTWR_WI + " %");
		}

		if(ROI_WI == Double.MAX_VALUE)
		{
			System.out.println("ROI: undefined");
		}
		else
		{
			double newROI_WI = (double) Math.round(ROI_WI * 100) / 100;
			System.out.println("ROI: " + newROI_WI + " %");
		}

		if(bench_WI == Double.MAX_VALUE)
		{
			System.out.println("Benchmark: undefined");
		}
		else
		{
			double newBench_WI = (double) Math.round(bench_WI * 100) / 100;
			System.out.println("Benchmark: " + newBench_WI + " %");
		}

		System.out.println("--------------------------------------------");

		if(CSVFile.warningInvalidEvaluationPeriod)
		{
			System.out.println("Evaluation period: undefined");
		}
		else
		{
			System.out.println("Evaluation period: " + CSVFile.start.toString() + " to " + CSVFile.end.toString());
		}

		if(TWR_EP == Double.MAX_VALUE)
		{
			System.out.println("TWR: undefined");
		}
		else
		{
			double newTWR_EP = (double) Math.round(TWR_EP * 100) / 100;
			System.out.println("TWR: " + newTWR_EP + " %");
		}

		if(ROI_EP == Double.MAX_VALUE)
		{
			System.out.println("ROI: undefined");
		}
		else
		{
			double newROI_EP = (double) Math.round(ROI_EP * 100) / 100;
			System.out.println("ROI: " + newROI_EP + " %");
		}

		if(bench_EP == Double.MAX_VALUE)
		{
			System.out.println("Benchmark: undefined");
		}
		else
		{
			double newBench_EP = (double) Math.round(bench_EP * 100) / 100;
			System.out.println("Benchmark: " + newBench_EP + " %");
		}

		System.out.println("--------------------------------------------");
	}


	public static void main(String[] args) {
		csv CSVFile = new csv();
		//parseFile("sample.csv", CSVFile);
		//parseFile("sampleAgentFees.csv", CSVFile);		
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
		//parseFile("sample_warning_invalid_eval_period.csv", CSVFile);
		//parseFile("sample_warning_name.csv", CSVFile);
		//parseFile("sample_warning_no_eval_period.csv", CSVFile);
		System.out.println(args[0]);
		parseFile(args[0], CSVFile);
		

		CSVFile.printWarningsErrors();

		math m = new math(CSVFile.listTuple) ;

		// whole input
		date start = CSVFile.listTuple.get(0).date ;
		date end = CSVFile.listTuple.get (CSVFile.listTuple.size()- 1).date ;

		// evaluation periode
		date a_start = CSVFile.start ;
		date a_end = CSVFile.end ;

		double TWR_WI   = Double.MAX_VALUE ;
		double ROI_WI   = Double.MAX_VALUE ;
		double bench_WI = Double.MAX_VALUE ;
		double TWR_EP   = Double.MAX_VALUE ;
		double ROI_EP   = Double.MAX_VALUE ;
		double bench_EP = Double.MAX_VALUE ;

		if (m.twr_calculable(start, end)) {
			TWR_WI   = m.annual_compounded_TWR(start, end) ; 
		}
		ROI_WI   = m.roi(start, end) ;
		if (m.bm_calculable(start, end)) {
			bench_WI = m.benchmark(start, end)	;
		}

		if (!CSVFile.warningInvalidEvaluationPeriod) {
			if (m.twr_calculable(start, end)) {
				TWR_EP   = m.annual_compounded_TWR(a_start, a_end) ; 
			}
			ROI_EP   = m.roi(a_start, a_end) ; 
			if (m.bm_calculable(a_start, a_end)) {
				bench_EP = m.benchmark(a_start, a_end)	;
			}
		}	

		printOutput(
				CSVFile,
				TWR_WI ,  
				ROI_WI ,	
				bench_WI , 
				TWR_EP ,
				ROI_EP ,
				bench_EP
				) ;
	}

}
