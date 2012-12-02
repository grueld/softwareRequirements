package sre;

import java.util.GregorianCalendar;

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
	
	
	public boolean validDate()
	{
		if((1583 <= this.year && this.year <= 9999) &&
				(1 <= this.month && this.month <= 12) &&
				(1 <= this.day && this.day <= 31))
		{
			if(this.month == 1 || this.month == 3 || this.month == 5 || this.month == 7 || this.month == 8 || this.month == 10 || this.month == 12)
			{
				return true;
			}
			else if(this.month == 4 || this.month == 6 || this.month == 9 || this.month == 11)
			{
				if (this.day <= 30)
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
				if (leapYear(this.year))
				{
					if(this.day <= 29)
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
					if(this.day <= 28)
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
	
	
	public boolean before(date date)
	{
		if (this.year < date.year)
		{
			return true;
		}
		if (this.year > date.year)
		{
			return false;
		}
		else //same year
		{
			if (this.month < date.month)
			{
				return true;
			}
			if (this.month > date.month)
			{
				return false;
			}
			else //same month
			{
				if(this.day < date.day)
				{
					return true;
				}
				if (this.day > date.day)
				{
					return false;
				}
				else //same day
				{
					return false;
				}
			}
		}
		
	}
	
	//appel : dateBegin.differenceDate(dateEnd)
	public int differenceDate(date date)
	{
		long MILISECOND_PER_DAY = 24 * 60 * 60 * 1000;	
		GregorianCalendar dateBegin= new java.util.GregorianCalendar(this.year, this.month, this.day);
		GregorianCalendar dateEnd= new java.util.GregorianCalendar(date.year, date.month, date.day);
		return Math.round(Math.abs((dateEnd.getTimeInMillis()- dateBegin.getTimeInMillis())/MILISECOND_PER_DAY));
	}
	
}
