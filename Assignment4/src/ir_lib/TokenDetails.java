package ir_lib;

/*
 * 
 * 
 *This class will associate a string term with its corresponding document id
 *That is, this term exists in which document can easily be identified by using this class 
 * 
*/

import java.util.Comparator;

public class TokenDetails{
	
	private String token="";
	private int docID;
	
	public TokenDetails(String token, int docID) {
		
		this.token = token;
		this.docID = docID;
		
	}
	
	public String getToken(){
		
		return this.token;
		
	}
	
	public int getDocID(){
		
		return this.docID;
		
	} 
	
	//This method sorts TokenDetails list in ascending order
	
	public static Comparator<TokenDetails> TDComparator = new Comparator<TokenDetails>() {

		public int compare(TokenDetails s1, TokenDetails s2) {
		
			String token1 = s1.getToken().toUpperCase();
			
			String token2 = s2.getToken().toUpperCase();
			
			//ascending order
			
			return token1.compareTo(token2);
		
			//descending order
			
			//return token2.compareTo(token1);
			
		}

	};
	
}