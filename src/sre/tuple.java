package sre;

public class tuple {
	public date date;
	public float marketValue;
	public float cashFlow;
	public float agentFees;
	public float benchmark;
	
	public tuple()
	{
		date = new date();
	}
	
	public String toString()
	{
		String s = null;
		s = "tuple : date = " + date.toString() + "\n";
		s += "        market value = " + marketValue + "\n";
		s += "        cash flow = " + cashFlow + "\n";
		s += "        agent fees = " + agentFees + "\n";
		s += "        benchmark = " + benchmark + "\n";
		return s;
	}
}
