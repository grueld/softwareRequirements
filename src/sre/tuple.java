package sre;

public class tuple {
	public date date;
	public float mv;
	public float cf;
	public float af;
	public float bm;
	
	public tuple()
	{
		date = new date();
	}
	
	public tuple (date d) {
		date = d ;
		mv = 0 ;
		cf = 0 ; 
		af = 0 ;
		bm = 0 ; 
	}
	
	public String toString()
	{
		String s = null;
		s = "tuple : date = " + date.toString() + "\n";
		s += "        market value = " + mv + "\n";
		s += "        cash flow = " + cf + "\n";
		s += "        agent fees = " + af + "\n";
		s += "        benchmark = " + bm + "\n";
		return s;
	}
}
