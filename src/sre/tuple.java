package sre;

public class tuple {
	public String date;
	public float marketValue;
	public float cashFlow;
	public float agentFees;
	public float benchmark;
	
	public String toString()
	{
		String s = null;
		s += "tuple : date = " + date + "\n";
		s += "        market value = " + marketValue + "\n";
		s += "        cash flow = " + cashFlow + "\n";
		s += "        agent fees = " + agentFees + "\n";
		s += "        benchmark = " + benchmark + "\n";
		return s;
	}
}
