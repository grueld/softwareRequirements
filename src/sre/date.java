package sre;

public class date {
	int year, month, day;
	
	public boolean leapYear(int year)
	{
		if(year >= 1583 &&
				((year%4)==0) &&
				(year%400) != 100 &&
				(year%400) != 200 &&
				(year%400) != 300)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean validDate(date d)
	{
		if((1583 <= d.year && d.year <= 9999) &&
				(1 <= d.month && d.month <= 12) &&
				(1 <= d.day && d.day <= 31))
		{
			if(d.month == 1 || d.month == 3 || d.month == 5 || d.month == 7 || d.month == 8 || d.month == 10 || d.month == 12)
			{
				return true;
			}
			else if(d.month == 4 || d.month == 6 || d.month == 9 || d.month == 11)
			{
				if (d.day <= 30)
				{
					return true;
				}
				else //d>30
				{
					return false;
				}
			}
			else //m=2
			{
				if (leapYear(d.year))
				{
					if(d.day <= 29)
					{
						return true;
					}
					else //d>29
					{
						return false;
					}
				}
				else //not leap year
				{
					if(d.day <= 28)
					{
						return true;
					}
					else //d>28
					{
						return false;
					}
				}
			}
		}
		else
		{
			return false;
		}
	}
	
	
	public String toString()
	{
		return "year = " + year + ", month = " + month + ", day = " + day;
	}
	
	
	public boolean equals(date date)
	{
		if (this.year == date.year &&
				this.month == date.month &&
				this.day == date.day)
		{
			return true;
		}
		else
		{
			return false;
		}		
	}
	
}
