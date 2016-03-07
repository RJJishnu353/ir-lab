package ir_lib;

/*
 * 
 * This class will be used to store the inverted index
 * and retrieve the same
 */

import java.util.*;

public class InvertedIndex {
	
	private HashMap<String,ArrayList<Posting>> invertedIndex =  new HashMap<String,ArrayList<Posting>>();

	public InvertedIndex() {
		
	}
		
	//This method returns a hashmap representing an inverted index.
	
	public HashMap<String,ArrayList<Posting>> getInvertedIndex(){
		
		return this.invertedIndex;
		
	}
	
}


