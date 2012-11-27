package sre;

import java.io.BufferedReader;
import java.util.ArrayList;

public class csv {

	 public BufferedReader file;
	 public String start;
	 public String end;
	 public ArrayList<tuple> listTuple;
	 
	 public csv()
	 {
		 listTuple = new ArrayList<tuple>() ;
	 }
	 
	 public void addTuple(tuple tuple)
	 {
		 this.listTuple.add(tuple);
	 }
	 
}
