import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import ir_lib.InvertedIndex;
import ir_lib.Posting;

public class TermPipelineDemo {

	public static void main(String[] args) throws IOException {
		
		//String documentLocation = args[0]; //--From console take doc location @ run time
		//String stopWordLocation= args[1];  //--From console take stopwords file location @ run time
			
		//Temporary corpus file and stopwords file for testing purpose
		//NOTE : Once done comment both of these and take the files from console
				

		//-------------------------------------------------------------------------
		/*String documentLocation="E:/Study/6thSem/IR/Lab/Lab_1/hindi";
		String stopWordLocation= "E:/Study/6thSem/IR/Lab/Lab_1/stop-words/stop-words_hindi_1_hi.txt";*/
		//-------------------------------------------------------------------------
		
		System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("\nEnter corpus stopwords file location : ");
		
		sc = new Scanner(System.in);
		
		String stopWordLocation = sc.nextLine();

		System.out.println();
		
	
		String currentFile;
			
		String currentString; 
		
		String inputToken;
				
		Indexer indexer = new Indexer(documentLocation, stopWordLocation);
		
		ArrayList<Posting> posting;
		
		TreeMap<Integer, File> corpusFiles = indexer.getCorpusFilesWithID(); //Here, files are given docID
		
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
		
		System.out.println("\nOutput Statistics of index built");
		
		System.out.println("# of documents : " + indexer.getTotalNumberOfFiles(corpusFiles));
		
		System.out.println("Size of the term list : " + indexer.getInvertedIndex().getInvertedIndex().size());
		
		System.out.println();
		
		System.out.print("Enter a term : ");
		
		sc = new Scanner(System.in);
		
		inputToken = sc.nextLine();
		
		posting = indexer.searchToken(inputToken);
		
		if(posting == null){
			
			System.out.println("The word may not be existing or has linked structure with other characters");
			
		}else{
			
			int collectionFrequency = 0;
			
			for (int j = 0; j < posting.size(); j++) {
				
				collectionFrequency += posting.get(j).getTermFrequency();
		     	
			}
			
			System.out.println("Document Frequency : " + posting.size());
			
			System.out.println("Collection Frequency : " + collectionFrequency);
			
			System.out.println("Inverse Document Frequency : " + Math.log10(indexer.getTotalFiles()/posting.size()));
			
		}
		
	}

}
