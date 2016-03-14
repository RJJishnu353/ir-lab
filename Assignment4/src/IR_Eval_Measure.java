import ir_lib.DocScore;
import ir_lib.MetricInfoContainer;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

/*
 * 
 * @author Jishnu
 */
public class IR_Eval_Measure {

	public static void main(String[] args) throws IOException {
	
		String queryFile = "query_HINDI.txt";
		String queryRelevanceFile = "clinss13-en-hi.qrel";	
		
		String currentString = "";
		String currentFile;
		
		System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.println("\n***The qrel file and query hindi file are already loaded. So don't worry.***\n");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("\nEnter corpus stopwords file location : ");
		
		sc = new Scanner(System.in);
		
		String stopWordLocation = sc.nextLine();
		
		IR_Evaluation irEval = new IR_Evaluation(documentLocation, stopWordLocation, queryFile, queryRelevanceFile);
		
		TreeMap<Integer, File> corpusFiles = irEval.getCorpusFilesWithID(); //Here, files are given docID
		
		irEval.scanStopWords(); //Here, stop words are scanned once and stored for future use
		
		for(Integer docID : corpusFiles.keySet()){ //Traversing through each file
			
			System.err.println("File #:" + docID);
			
			currentFile = corpusFiles.get(docID).toString();   // getting filename from respective docID
			
			currentString = irEval.removeHTMLTagsFrom(currentFile); //removing HTML tags from the string of each file
			
			irEval.getTempWordsForStemming(currentString); //Get tokens from current file
			
			irEval.removeStopWordsFromTokens(); //removing stop words from current file tokens
			
			irEval.getStemmedWordsFrom(docID);  //Getting individual words from the current file
			
			irEval.clearTempWordsToBeStemmed(); //Clearing the tempWordsToBeStemmed object to save memory
			
			irEval.sortStemmedWords(); //sorting stemmed words to achieve O(m+n)
			
			irEval.buildInvertedIndex(docID); //building inverted index
			
			irEval.clearStemmedWords(); //Clearing the tempWordsToBeStemmed object to save memory
	
		}
		
		//irEval.printInvertedIndex(); // prints inverted index

		//System.out.println("1");
		
		TreeMap<Integer,String> queryMap = irEval.getQueryMap(queryFile);
		
		//System.out.println("2");
		
		TreeMap<Integer,LinkedList<Integer>> evaluationTable = irEval.getevaluationTable(queryRelevanceFile);
		
		//System.out.println("3");
		
		String inputToken;
		
		LinkedList<DocScore> rankedList;
		
		LinkedList<Integer> docList;
		
		MetricInfoContainer metricContainer;
		
		Double MRR = 0.0;
		
		//System.out.println("4");
		
		int documentsToBeRetrieved=20;
		
		System.out.println("Evaluation process is starting...");
		
		System.out.println("Query ID | Recall | Precision | AP");
		
		for(Integer queryID : queryMap.keySet()){
			
			//System.out.println("queryID:"+queryID);
			
			inputToken = queryMap.get(queryID);
			
			//System.out.println("inputToken : " + inputToken);
			
			rankedList = irEval.getRankedListWithoutPrintingOf(inputToken, documentsToBeRetrieved); //gets the top ranked documents according to the given query
			
			//System.out.println("Getting evaluation table");
			
			docList = evaluationTable.get(queryID);
			
			//System.out.println("Calculating metrics");
			
			//System.out.println("DocList : " + docList.toString());
			
			metricContainer = irEval.getMetricOf(rankedList, docList,documentsToBeRetrieved);
			
			//Print format : queryID recall precision AP
			
			System.out.println(queryID + " " + metricContainer.getRecall() + " " + metricContainer.getPrecision() + " " + metricContainer.getAveragePrecision());
			
			MRR += metricContainer.getMRR();
			
		}
		
		System.out.println("MRR : " + ( MRR / queryMap.size() ) );
		
	}

}
