package sre;

import java.io.BufferedReader;
import java.util.ArrayList;

public class csv {

	 public BufferedReader file;
	 public String name;
	 public date start;
	 public date end;
	 public ArrayList<tuple> listTuple;
	 
	 public csv()
	 {
		 name = null;
		 listTuple = new ArrayList<tuple>() ;
		 start = new date();
		 end = new date();
	 }
	 
	 public void addTuple(tuple tuple)
	 {
		 this.listTuple.add(tuple);
	 }
	 
}
