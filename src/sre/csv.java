package sre;

import java.io.BufferedReader;
import java.util.ArrayList;

public class csv {

	 public BufferedReader file;
	 public String name;
	 public date start;
	 public date end;
	 public ArrayList<tuple> listTuple;
	 public boolean warningNoName;
	 public boolean warningInvalidEvaluationPeriod;
	 public boolean errorInvalidFile;
	 
	 
	 public csv()
	 {
		 name = null;
		 listTuple = new ArrayList<tuple>() ;
		 start = new date();
		 end = new date();
		 warningInvalidEvaluationPeriod = false;
		 errorInvalidFile = false;
		 warningNoName = false;
	 }
	 
	 
	 public void addTuple(tuple tuple)
	 {
		 this.listTuple.add(tuple);
	 }
	 
	 
	 public void printWarningsErrors()
	 {
		 if(warningNoName)
		 {
			 System.out.println("Warning: Incomplete file: absence of name.");
		 }
		 if(warningInvalidEvaluationPeriod)
		 {
			 System.out.println("Warning: Invalid evaluation period.");
		 }
		 if(errorInvalidFile)
		 {
			 System.out.println("Error: Invalid file.");
		 }
	 }
	 
}
