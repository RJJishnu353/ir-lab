package ir_lib;

/*
 * 
 * This class represents a post of a posting list in an inverted index
 * 
 * stores the attributes of a post in a posting list of inverted index.
 */

public class Posting{
	
	private int docID;    //DocID of post
	private int count;    //TermFrequency of post
	private double tfidf; //tfidf weight of post
	
	public Posting(int docID, int count){

		this.docID = docID;
		this.count = count;
		this.tfidf = 0;
		
	}
	
	public int getDocID(){
		
		return docID;
		
	}
	
	public int getTermFrequency(){
		
		return count;
		
	}
	
	public void setTermFrequency(int tf){
		
		this.count = tf;
		
	}
	
	public double getTFIDF(){
		
		return this.tfidf;
		
	}
	
	public void setTFIDF(double tfidf){
		
		this.tfidf = tfidf;
		
	}
	
	
	
	
}