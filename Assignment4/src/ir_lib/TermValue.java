package ir_lib;

/*
 * 
 * This class is created with an intension of 
 * sorting in descending order for ranking according to tfidf score
 */

import java.util.Comparator;

public class TermValue {
	
	private double value;
	
	private String term;
	
	public TermValue(double value, String term){
		
		this.value = value;
		
		this.term = term;
		
	}

	
	
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}



	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}



	/**
	 * @return the term
	 */
	public String getTerm() {
		return term;
	}



	/**
	 * @param term the term to set
	 */
	public void setTerm(String term) {
		this.term = term;
	}

	//This method sorts TermValue list in descending order
	
	public static Comparator<TermValue> TVComparator = new Comparator<TermValue>() {

		public int compare(TermValue t1, TermValue t2) {
		
			Double token1 = t1.getValue();
			
			Double token2 = t2.getValue();
			
			//ascending order
			
			//return token1.compareTo(token2);
		
			//descending order
			
			return token2.compareTo(token1);
			
		}

	};

	

}
