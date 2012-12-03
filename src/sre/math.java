package sre;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;

// classe du module TWR_ROI_CALCULATION
public class math {

	// voir def constantes 
	public ArrayList<tuple> tr ;
	public int count ;		
	public ArrayList<date> dates ;
	public date start, end ;

	//voir def constantes
	public math(ArrayList<tuple> t) {
		tr = t ;
		count = tr.size() ;
		dates = new ArrayList<date>() ;
		Iterator<tuple> it = tr.iterator() ;
		while (it.hasNext()) {
			dates.add(it.next().date) ;
		}
		start = dates.get(0) ;
		end = dates.get(count-1) ;
	}

	// pour les fonctions suivantes voir la def correspondante dans le module

	public int days (date begin, date end) {
		if (begin.equals(end)) return 0 ;
		if (end.before(begin)) {
			System.err.println("error: in days end is before begin") ;
			return -1 ;
		}
		return begin.differenceDate(end) ;
	}

	// retourne -1 si la date n'est pas def
	public int di (date d) {
		int i = 0 ;
		Iterator<date> it = dates.iterator() ;
		while (it.hasNext()) {
			if (it.next().equals(d)) {
				return i ;
			}
			i++ ;
		}
		System.err.println("error: " + d + " is not a date of the file") ;
		return -1 ;
	}


	/*************************** TWR ***********************************/
	
	public boolean twr_calculable(date s, date e) {
		for ( int i = di(s) + 1 ; i <= di(e) ; i++ ) {
			if (tr.get(i).mv + tr.get(i-1).cf + tr.get(i-1).af == 0) return false ;
		}
		return true ;
	}
	
	// retourne -1 en cas d'erreur
	public double twr (date s, date e) {
		int m = di(s) ;
		int n = di(e) ;
		double wealth = 1 ;

		if (m == -1) {
			System.out.println(s + " is not a date of the file") ;
			return -1 ;
		}
		if (n == -1) {
			System.out.println(e + " is not a date of the file") ;
			return -1 ;
		}
		if (e.before(s)) {
			System.out.println(s + " is not before " + e) ;
			return -1 ;
		}

		for (int i = m + 1 ; i <= n ; i++) {
			if (tr.get(i-1).mv + tr.get(i-1).cf + tr.get(i-1).af == 0) {
				System.out.println("impossible to compute twr between " + s + " and " + e) ;
				return -1 ;
			}	
			double PV = tr.get(i-1).mv + tr.get(i-1).cf + tr.get(i-1).af ;
			double FV = tr.get(i).mv ;
			wealth *= FV / PV ;
		}

		return wealth - 1 ;
	}

	public double annual_compounded_TWR (date s, date e) {
		double duration = days(s, e) / 365.2422 ;
		if (duration >= 1) {
			return (Math.pow(1 + twr(s, e),1/duration) - 1) * 100 ;
		}
		else {
			return twr(s, e) * 100 ;
		}
	}


	/*************************** ROI ***********************************/
	public double roi (date s, date e) {
		int m = di(s) ;
		int n = di(e) ;

		if (m == -1) {
			System.out.println(s + " is not a date of the file") ;
			return -1 ;
		}
		if (n == -1) {
			System.out.println(e + " is not a date of the file") ;
			return -1 ;
		}
		if (e.before(s)) {
			System.out.println(s + " is not before " + e) ;
			return -1 ;
		}

		double x0 = 0 ;
		double x1 = x0 - f(s,e,x0)/ fPrime(s, e, x0) ;
		double x2 = x1 - f(s,e,x1)/ fPrime(s, e, x1) ;
		double x3 = x2 - f(s,e,x2)/ fPrime(s, e, x2) ;
		double x4 = x3 - f(s,e,x3)/ fPrime(s, e, x3) ;


		return x4 ;
	}

	public double f (date s, date e, double x) {
		int m = di(s) ;
		int n = di(e) ;
		double dur = days(s, e) / 365.2422 ;

		double result = (tr.get(m).mv + tr.get(m).cf) * Math.pow(1+x/100, dur) ;

		for (int i = m + 1 ; i < n ; i++) {
			dur = days (tr.get(i).date, e)/365.2422 ;
			result += (tr.get(i).cf + tr.get(i).af) * Math.pow(1+x/100, dur) ;
		}
		result -= tr.get(n).mv ;

		return result ;
	}

	public double fPrime (date s, date e, double x) {
		int m = di(s) ;
		int n = di(e) ;
		double dur = days(s, e) / 365.2422 ;

		double result = (tr.get(m).mv + tr.get(m).cf) * dur /100 * Math.pow( 1 + x/100, dur - 1) ;

		for (int i = m + 1 ; i < n ; i++) {
			dur = days (tr.get(i).date, e)/365.2422 ;
			result += (tr.get(i).cf + tr.get(i).af) * dur/100 * Math.pow(1+x/100, dur - 1) ;
		}

		return result ;
	}


	/*************************** Benchmark ***********************************/
	public static date max (date f, date s) {
		if (f.before(s)) {
			return s ;
		}
		else {
			return f ;
		}
	}

	public static date min (date f, date s) {
		if (f.before(s)) {
			return f ;
		}
		else {
			return s ;
		}
	}

	public tuple at (date d) {
		int i = di(d) ;
		if (i != -1) {
			tuple t = tr.get(i) ;
			if (!t.date.equals(d)) {
				System.err.println("the function at doesn't return the right tuple") ;
				return null ;
			}
			return t ;
		}
		else {
			return new tuple(d) ;
		}
	}

	public boolean bm_calculable (date s, date e) {
		date a = new date (s.year + 1 , 1, 1) ;
		date b = new date (e.year + 1 , 1, 1) ;
		boolean c = (e.month == 1 && e.day == 1) ; 
		boolean p = true ;
		boolean res = false ;

		if (di(s) == -1 && di(e) == -1) return false ;
		if (e.before(s) || e.equals(s)) return false ;

		for ( int y = s.year + 1 ; y <= e.year ; y++ ) {
			date d = new date (y, 1, 1) ;
			if (at(d).bm == 0) p = false ;
		}

		res = (at(min(end, a)).bm != 0) && p &&
				((c && at(e).bm != 0) || (!c && at(min(end, b)).bm != 0)) ;

		return res ;
	}

	public double po(date s, date e, date d1, date d2) {
		if (d1.equals(d2)) {
			System.err.println("error: denominator equals to zero in function po") ;
			return -1 ;
		}
		return days (d1, min (d2,e)) / (double)days(new date(d1.year,1,1), d2) ;
	}

	public ArrayList<tuple> bm_seq (date s, date e) {
		int m = di(s) ;
		int n = di(e) ;
		date b = new date(e.year + 1 , 1, 1) ;
		tuple t ;
		ArrayList<tuple> res = new ArrayList<tuple>() ;

		for ( int i = m + 1 ; i < n ; i++ ) {
			if (tr.get(i).bm != 0) res.add(tr.get(i)) ;
		}

		if (e.month == 1 && e.day == 1) t = at(e) ;
		else t = at(min(end, b)) ;

		res.add(res.size(), t) ;

		return res ;
	}

	public double bm_final_value (date s, date e) {
		int m = di(s) ;
		int n = di(e) ;
		tuple a, t ;
		ArrayList<tuple> seq = new ArrayList<tuple>() ;
		double res, p ;

		res = tr.get(m).mv ;
		a = new tuple(s) ;
		seq = bm_seq(s, e) ;
		seq.add(0, a) ;

		for ( int i = 1 ; i < seq.size() ; i++ ) {
			t = seq.get(i) ;
			res *= Math.pow(1 + t.bm/100 , po(s, e, seq.get(i-1).date, t.date)) ;
		}

		for ( int k = m ; k < n ; k++ ) {
			tuple b = new tuple(tr.get(k).date) ;
			seq = bm_seq(tr.get(k).date, e) ;
			seq.add(0, b) ;
			p = tr.get(k).cf - tr.get(k).af ;

			for ( int i = 1 ; i < seq.size() ; i++ ) {
				t = seq.get(i) ;
				p *= Math.pow(1 + t.bm/100, po(s, e, seq.get(i-1).date, t.date)) ;
			}
			res += p ;
		}
		
		return res ;

	}

	public double benchmark (date s, date e) {

		if (!bm_calculable(s, e)) {
			System.err.println("benchmark not calculable") ;
			return -1 ;
		}
		
		double x0 = 0 ;
		double x1 = x0 - g(s,e,x0)/ gPrime(s, e, x0) ;
		double x2 = x1 - g(s,e,x1)/ gPrime(s, e, x1) ;
		double x3 = x2 - g(s,e,x2)/ gPrime(s, e, x2) ;
		double x4 = x3 - g(s,e,x3)/ gPrime(s, e, x3) ;

		return x4 ;
	}

	public double g (date s, date e, double x) {
		int m = di(s) ;
		int n = di(e) ;
		double dur = days(s, e) / 365.2422 ;

		double result = tr.get(m).mv * Math.pow(1+x/100, dur) ;

		for (int k = m ; k < n ; k++) {
			dur = days (tr.get(k).date, e) / 365.2422 ;
			result += tr.get(k).cf * Math.pow(1 + x/100, dur) ;
		}
		result -= bm_final_value(s, e) ;

		return result ;
	}

	public double gPrime (date s, date e, double x) {
		int m = di(s) ;
		int n = di(e) ;
		double dur = days(s, e) / 365.2422 ;

		double result = (tr.get(m).mv + tr.get(m).cf) * dur /100 * Math.pow( 1 + x/100, dur - 1) ;

		for (int k = m + 1 ; k < n ; k++) {
			dur = days (tr.get(k).date, e) / 365.2422 ;
			result += tr.get(k).cf * dur/100 * Math.pow(1+x/100, dur - 1) ;
		}

		return result ;
	}

}







