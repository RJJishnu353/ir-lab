import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.Messaging.SyncScopeHelper;

import ir_lib.DocScore;
import ir_lib.InvertedIndex;
import ir_lib.Posting;
import ir_lib.TokenDetails;

public class TF_IDF_Ranked_Retrieval {

	public static void main(String[] args) throws IOException {
		
		//String documentLocation = args[0]; //--From console take doc location @ run time
		//String stopWordLocation= args[1];  //--From console take stopwords file location @ run time
			
		//Temporary corpus file and stopwords file for testing purpose
		//NOTE : Once done comment both of these and take the files from console
				
		//-------------------------------------------------------------------------
		String documentLocation="/home/sysadmin/Downloads/hindi";
		String stopWordLocation= "/home/sysadmin/Downloads/stopwords_hi.txt";
		//-------------------------------------------------------------------------
		
		/*System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("\nEnter corpus stopwords file location : ");
		
		sc = new Scanner(System.in);
		
		String stopWordLocation = sc.nextLine();
		
		*/
		
		String currentFile;
			
		String currentString; 
		
		String inputToken;
		
		Indexer indexer = new Indexer(documentLocation, stopWordLocation);
		
		ArrayList<Posting> posting;
		
		ArrayList<ArrayList<Posting>> postingArray = new ArrayList<ArrayList<Posting>>();
		
		TreeMap<Integer,File> corpusFiles = indexer.getCorpusFilesWithID(); //Here, files are given docID
		
		indexer.scanStopWords(); //Here, stop words are scanned once and stored for future use
		
		for(Integer docID : corpusFiles.keySet()){ //Traversing through each file
			
			System.out.println("File #:" + docID);
			
			currentFile = corpusFiles.get(docID).toString();   // getting filename from respective docID
			
			currentString = indexer.removeHTMLTagsFrom(currentFile); //removing HTML tags from the string of each file
			
			indexer.getTempWordsForStemming(currentString); //Get tokens from current file
			
			indexer.removeStopWordsFromTokens(); //removing stop words from current file tokens
			
			indexer.getStemmedWordsFrom(docID);  //Getting individual words from the current file
			
			indexer.clearTempWordsToBeStemmed(); //Clearing the tempWordsToBeStemmed object to save memory
			
			indexer.sortStemmedWords(); //sorting stemmed words to achieve O(m+n)
			
			indexer.buildInvertedIndex(docID); //building inverted index
			
			indexer.clearStemmedWords(); //Clearing the tempWordsToBeStemmed object to save memory
	
		}
		
		indexer.printInvertedIndex(); // prints inverted index
	
		//File incidenceMatrixFile = indexer.getTermIncidenceMatrixFile("/home/sysadmin/Desktop/incidenceMatrixFile.txt");		
		
		System.out.print("Enter a query : ");
		
		Scanner sc = new Scanner(System.in);
		 
		inputToken = sc.nextLine();
		
		sc = new Scanner(System.in);
		
		System.out.print("Enter the number documents do you want to retrieve : ");
		
		int documentsToBeRetrieved = sc.nextInt();
		
		indexer.getRankedListOf(inputToken, documentsToBeRetrieved); //prints the top ranked documents according to the given query
			
		}
	
}
