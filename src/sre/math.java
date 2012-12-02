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
	public double duration ;

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
		duration = days(start, end) / 365.2422 ;
	}

	// pour les fonctions suivantes voir la def correspondante dans le module

	public int days (date begin, date end) {
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

	// retourne -1 en cas d'erreur
	public double twr (date s, date e) {
		int i_s = di(s) ;
		int i_e = di(e) ;
		double wealth = 1 ;
		
		if (i_s == -1) {
			System.out.println(s + " is not a date of the file") ;
			return -1 ;
		}
		if (i_e == -1) {
			System.out.println(e + " is not a date of the file") ;
			return -1 ;
		}
		if (e.before(s)) {
			System.out.println(s + " is not before " + e) ;
			return -1 ;
		}
		
		for (int i = i_s + 1 ; i < i_e ; i++) {
			if (tr.get(i-1).mv + tr.get(i-1).cf + tr.get(i-1).af == 0) {
				System.out.println("impossible to compute twr between " + s + " and " + e) ;
				return -1 ;
			}	
			
			wealth *= tr.get(i).mv /(tr.get(i-1).mv + tr.get(i-1).cf + tr.get(i-1).af) ;
		}
		
		return wealth - 1 ;
	}

	public double annual_compounded_TWR (date s, date e) {
		if (duration >= 1) {
			return (Math.pow(1 + twr(s, e),1/duration) - 1) * 100 ;
		}
		else {
			return twr(s, e) * 100 ;
		}
	}

	public double roi (date s, date e) {
		int i_s = di(s) ;
		int i_e = di(e) ;
		
		if (i_s == -1) {
			System.out.println(s + " is not a date of the file") ;
			return -1 ;
		}
		if (i_e == -1) {
			System.out.println(e + " is not a date of the file") ;
			return -1 ;
		}
		if (e.before(s)) {
			System.out.println(s + " is not before " + e) ;
			return -1 ;
		}

		return 0 ;
	}

	public static date max (date f, date s) {
		return null ;
	}

	public static date min (date f, date s) {
		return null ;
	}


}













