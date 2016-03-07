package ir_lib;

/*
 * 
 * This class contains stores docId and score for ranked list calculation
 */

import java.util.Comparator;

public class DocScore {
	
	private int docID;
	
	private double score;
	
	//Takes docID and score as input values
	
	public DocScore(int docID, double score){
		
		this.docID = docID;
		
		this.score = score;
		
	}

	//This method returns docID
	
	public int getDocID() {
		return docID;
	}

	//This method sets docID
	
	public void setDocID(int docID) {
		this.docID = docID;
	}

	//This method returns score
	
	public double getScore() {
		return score;
	}

	//This method sets score
	
	public void setScore(double score) {
		this.score = score;
	}
	
	//This method will sort a list of DocScore type 
	//according to score in asc and desc order (uncomment the appropriate return statement to use your preference)
	
	public static Comparator<DocScore> DSComparator = new Comparator<DocScore>() {

		public int compare(DocScore s1, DocScore s2) {
		
			Double token1 = s1.getScore();
			
			Double token2 = s2.getScore();
			
			//ascending order
			
			//return token1.compareTo(token2);
		
			//descending order
			
			return token2.compareTo(token1);
			
		}

	};


	//This method will sort a list of DocScore type 
	//according to DocID in asc and desc order (uncomment the appropriate return statement to use your preference)
	
	public static Comparator<DocScore> DSIDComparator = new Comparator<DocScore>() {

		public int compare(DocScore s1, DocScore s2) {
		
			Integer token1 = s1.getDocID();
			
			Integer token2 = s2.getDocID();
			
			//ascending order
			
			return token1.compareTo(token2);
		
			//descending order
			
			//return token2.compareTo(token1);
			
		}

	};
	

}
