import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

import ir_lib.DocScore;
import ir_lib.MetricInfoContainer;


public class IR_Evaluation extends Indexer{

	private String queryFile;
	private String queryRelevanceFile;
	private TreeMap<Integer, File> corpusFiles = super.getCorpusFilesWithID();
	public static int index;
	
	public IR_Evaluation(String documentLocation, String stopWordLocation, String queryFile, String queryRelevanceFile) {
		
		super(documentLocation, stopWordLocation);
		this.queryFile = queryFile;
		this.queryRelevanceFile = queryRelevanceFile;
	
	}

	public String getQueryFile() {
		return queryFile;
	}

	public void setQueryFile(String queryFile) {
		this.queryFile = queryFile;
	}

	public String getQueryRelevanceFile() {
		return queryRelevanceFile;
	}

	public void setQueryRelevanceFile(String queryRelevanceFile) {
		this.queryRelevanceFile = queryRelevanceFile;
	}
	
	/*This method reads the query file 
	 * Reads each query and gives it a proper id
	 * This id will be used for further processing
	 */
	
	public TreeMap<Integer,String> getQueryMap(String queryFile) throws IOException{
		
		System.out.println("Scanning query file and assigning id to each query");
		
		TreeMap<Integer,String> queryMap = new TreeMap<Integer,String>();
		
		BufferedReader br = new BufferedReader(new FileReader(queryFile));
		
		String currentLine="";
		
		int count = 0;
		
		while((currentLine=br.readLine())!=null){
		
			System.out.println("getQueryMap");
			
			queryMap.put(++count, currentLine);
			
		}
		
		br.close();
		
		return queryMap;
		
	}
	
	/*
	 * This method returns the file id by taking filename as input
	 */
	
	public int getFileIDWith(String fileName){
		
		int fileID = -1;
		
		for(int key: this.corpusFiles.keySet()) {
			
			//System.out.println(this.corpusFiles.get(key).getName() + " " + fileName + " this.corpusFiles.get(key).equals(fileName)" + this.corpusFiles.get(key).equals(fileName));
			
			if(this.corpusFiles.get(key).getName().equals(fileName)) {
		
				fileID = key;
				
				break;
				
		    }
		
		}
		
		return fileID;
		
	}
	
	/*
	 * 
	 * This method returns a TreeMap of evaluation Table for evaluation purposes
	 * With query Id as key and linked list of relevant Doc ID corresponding to it
	 */
	
	public TreeMap<Integer,LinkedList<Integer>> getevaluationTable(String fileName) throws FileNotFoundException{
		
		TreeMap<Integer,LinkedList<Integer>> evalTable = new TreeMap<Integer,LinkedList<Integer>>(); //query->LinkedList<relevantDocList>
		
		LinkedList<Integer> relDoc;
		
		Scanner sc = new Scanner(new FileReader(fileName));
		
		String notNeeded,docID,FILENAME;
		
		int relDocID;
		
		int id = -1;
		
		System.out.println("Evaluating for relevance of a file");
		
		while(sc.hasNext()){
			
			//System.out.println("getEvaluationTable");
			
			docID = sc.next();
			
			FILENAME = sc.next();
			
			notNeeded = sc.next();
			
			if(id == Integer.parseInt(docID)){
				
				//System.out.println("if");
				
				//System.out.println("getFileWithID:"+docID);
				
				relDocID = this.getFileIDWith(FILENAME);
				
				if(relDocID != (-1)){
				
				evalTable.get(id).add(relDocID);
				
				}
				
				id = Integer.parseInt(docID);
				
			}else{
				
				//System.out.println("else");
				
				relDoc = new LinkedList<Integer>();
				
				//System.out.println("getFileWithID:"+docID);
				
				relDocID = this.getFileIDWith(FILENAME);
				
				if(relDocID != (-1)){
				
				relDoc.add(relDocID);
				
				evalTable.put(Integer.parseInt(docID), relDoc);
				
				}
				
				id = Integer.parseInt(docID);
				
			}
			
		}
		
		return evalTable;
		
		
	}
	
	//This method will take rankedList and relevant docList and return the recall for a query
	
	public MetricInfoContainer getMetricOf(LinkedList<DocScore> rankedList, LinkedList<Integer> docList, int size){
	
		Double recall =  0.0;

		Double precision  = 0.0;
		
		Double averagePrecision  = 0.0;
		
		Double count = 0.0;
		
		//System.err.println("function:getMetricOf(rankedList, docList)" + "\n" + docList.toString());
		
		if(docList != null){
		
		int totalRelevantDoc = docList.size();
		
		for (this.index = 0; this.index < rankedList.size(); this.index++) {
			
			for (int j = 0; j < totalRelevantDoc; j++) {
				
				if (rankedList.get(index).getDocID() == docList.get(j)) { //checks for relevant doc
					
					//calculate recall,precision,AP
					
					recall = (++count) / totalRelevantDoc;
					
					//System.out.println((count) + " " +  totalRelevantDoc + " " + size + " ");
					
					precision = count / size; //out of retrieved howmany are relevant to the user
					
					averagePrecision = precision / count;
					
					//System.out.println(recall + " " + precision + " " + averagePrecision);
					
					break;
					
				}
				
			}
			
		}
		
		}
		
		return new MetricInfoContainer(recall, precision, averagePrecision);	
	
	}
	
}
