import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

import ir_lib.DocScore;
import ir_lib.Parameters;
import ir_lib.Posting;

/**
 * @author Jishnu
 *
 */

public class PseudoRelevanceFeedback {

public static void main(String[] args) throws IOException {
		
		//String documentLocation = args[0]; //--From console take doc location @ run time
		//String stopWordLocation= args[1];  //--From console take stopwords file location @ run time
			
		//Temporary corpus file and stopwords file for testing purpose
		//NOTE : Once done comment both of these and take the files from console
				
		//-------------------------------------------------------------------------
		//String documentLocation="E:/Study/6thSem/IR/Lab/Lab_1/hindi_original";
		//String stopWordLocation= "E:/Study/6thSem/IR/Lab/Lab_1/stop-words/stop-words_hindi_1_hi.txt";
		//-------------------------------------------------------------------------
		
		System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("\nEnter corpus stopwords file location : ");
		
		sc = new Scanner(System.in);
		
		String stopWordLocation = sc.nextLine();
		
		int documentsToBeretrieved = 10;
		
		String currentFile;
			
		String currentString; 
		
		String inputToken;
		
		Indexer indexer = new Indexer(documentLocation, stopWordLocation);
		
		ArrayList<Posting> posting;
		
		ArrayList<Integer> relevantDocIDs = new ArrayList<Integer>();
		
		ArrayList<Integer> nonRelevantDocIDs = new ArrayList<Integer>();
		
		ArrayList<ArrayList<Posting>> postingArray = new ArrayList<ArrayList<Posting>>();
		
		TreeMap<Integer, File> corpusFiles = indexer.getCorpusFilesWithID(); //Here, files are given docID
		
		indexer.scanStopWords(); //Here, stop words are scanned once and stored for future use
		
		for(Integer docID : corpusFiles.keySet()){ //Traversing through each file
			
			System.err.println("File #:" + docID);
			
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
		
		
		indexer.printInvertedIndex();
		
		System.out.print("Enter your query : ");
		
		sc = new Scanner(System.in);
		 
		inputToken = sc.nextLine();
		
		System.out.print("Enter the number documents do you want to retrieve : ");
		
		int documentsToBeRetrieved = sc.nextInt();
		
		sc = new Scanner(System.in);
		
		LinkedList<DocScore>docScore = indexer.getRankedListOf(inputToken, documentsToBeRetrieved); //Getting first ranked list
		
		//LinkedList<Integer> sampleVector;
		
		int vectorSize = indexer.getInvertedIndex().getInvertedIndex().size(); 
		
		int[] queryVector = new int[vectorSize];   //Will contain query vector 
		
		System.out.println("Initializing queryVector");
		
		
			
		int[] relevantSumVector = new int[vectorSize] ;  //Will contain relevant document vector 
		
		int[] nonRelevantSumVector = new int[vectorSize] ; //Will contain non-relevant document vector
		
		for (int i = 0; i < nonRelevantSumVector.length; i++) {
			
			relevantSumVector[i]=0;
			
			nonRelevantSumVector[i]=0;
			
		}
		
		Parameters parameter = new Parameters(1.0, 0.75, 0.0);	//setting the parameters of the formula for finding modified query
		
		for(String token : inputToken.split(" ")){
			
			queryVector = indexer.addIntVectors(queryVector, indexer.getVector(token,vectorSize)); //calculating query vector
			
			System.out.println("Query Vector calculation");
			
		}
		
		//PRF mechanism starts
		
		System.out.println("Enter the number of top relevant documents (between 1 and " + documentsToBeretrieved + ")");
		
		sc = new Scanner(System.in);

		int topK = sc.nextInt();
		
		docScore = indexer.getDocScore();
		
		int num = docScore.size();
		
		int till = topK;
		
		if(num > topK){
			
			till = topK;
			
		}else if(num < topK){
			
			till = num;
			
		}
		
		for(int index=0; index<till; index++){
			
			relevantDocIDs.add(docScore.get(index).getDocID());
			
		}

		
		LinkedList<DocScore> docScoreAll = indexer.getDocScore();
		
		if(docScoreAll.size() > documentsToBeretrieved){
			
			till = documentsToBeretrieved;
			
		}else{
			
			till = docScoreAll.size();
			
		}
		
		for (int i = 0; i < till; i++) {
			
			docScore.add(docScoreAll.get(i));
			
		}
		
		boolean flag;
		
		for (int i = 0; i < docScore.size(); i++) {
			
			flag = true;
			
			for (int j = 0; j < relevantDocIDs.size(); j++) {
					
					if(docScore.get(i).getDocID() == relevantDocIDs.get(j)){
						
						flag = false;
						
						break;
						
					}else{
						
						flag = true;
						
					}
					
				}
			
			if(flag == true){
				
				nonRelevantDocIDs.add(docScore.get(i).getDocID());
				
			}
			
			}
		
			
		int totalRelevantDocs = relevantDocIDs.size();
		
		int totalNonRelevantDocs = documentsToBeretrieved - relevantDocIDs.size();
		
		int currentDoc;
		
		System.out.println("|R| = " + totalRelevantDocs + " and |NR| = " + totalNonRelevantDocs);
		
		//LinkedList<Integer> relevantSumVector = new LinkedList<Integer>();
		
		//LinkedList<Integer> nonRelevantSumVector = new LinkedList<Integer>();
		
		for (int i = 0; i < totalRelevantDocs; i++) {
			
			relevantSumVector = indexer.addIntVectors(relevantSumVector, indexer.getDocumentVectorOf(relevantDocIDs.get(i),vectorSize));
			
			System.out.println("relevantSumVector calculation : " + i);
			
		}
		
		for (int i = 0; i < totalNonRelevantDocs; i++) {
			
			currentDoc = nonRelevantDocIDs.get(i);
			
			nonRelevantSumVector = indexer.addIntVectors(nonRelevantSumVector, indexer.getDocumentVectorOf(currentDoc,vectorSize));
			
			System.out.println("nonRelevantSumVector calculation : " + currentDoc);
	
		}
		
		for (int index = 0; index < queryVector.length; index++) {
			
			queryVector[index] = (int) ((parameter.getAlpha() * queryVector[index]) + ((parameter.getBeta() / totalRelevantDocs) * relevantSumVector[index]) - ((parameter.getGamma() / totalNonRelevantDocs) * nonRelevantSumVector[index]));
			
		}
		
		/*LinkedList<Double> modifiedQuery = indexer.addDoubleVectors(indexer.multiplyVectorWith(queryVector, parameter.getAlpha()),indexer.subtractVectors(indexer.multiplyVectorWith(relevantSumVector, (parameter.getBeta() / totalRelevantDocs)), 
					indexer.multiplyVectorWith(nonRelevantSumVector, (parameter.getGamma() / totalNonRelevantDocs))));
			*/
			String newQuery = indexer.generateQueryFrom(queryVector, indexer, 20); // new query contain top 20 terms
			
			System.out.println("\nNew Query : " + newQuery + "\n");
			
			indexer.getRankedListOf(newQuery, 50);
			
			}						
			

		
		}
