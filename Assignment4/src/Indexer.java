import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir_lib.DocScore;
import ir_lib.FileScanner;
import ir_lib.HindiStemmerLight;
import ir_lib.InvertedIndex;
import ir_lib.Posting;
import ir_lib.TermValue;
import ir_lib.TokenDetails;

/*
 * This is the main class which handles all the functionality of creating, managing and handling an inverted index
 * This is the main reference class for all the assignments
 * All the other classes are used here 
 * and a fruitful try has been made to incorporate all the necessary functionalities required for all the assignments
 */

public class Indexer {
	
		private Posting post;
		private int totalFiles=0;
		private BufferedReader br;
		private LinkedList<DocScore> docScore = new LinkedList<DocScore>(); //For each unique doc's score;
		private String documentLocation = "";
		private String stopWordLocation = "";
		private ArrayList<Posting> newEmptyPostingList;
		private InvertedIndex invertedIndex = new InvertedIndex();
		private HindiStemmerLight stemmer = new HindiStemmerLight();
		private ArrayList<String> stopwords = new ArrayList<String>();
		private ArrayList<String> tempTermsToBeStemmed = new ArrayList<String>();
		private ArrayList<TokenDetails> stemmedWords = new ArrayList<TokenDetails>();
		private TreeMap<Integer,File> corpusFiles;
		//-------------------------------------------------------------------------
		
		public Indexer(String documentLocation, String stopWordLocation){
			
			this.documentLocation=documentLocation;
			this.stopWordLocation=stopWordLocation;
			
		}
		
		//-------------------------------------------------------------------------
		
		/*
		 * 
		 * This method will scan all the documents inside a gicen folder and return a treemap 
		 * with docId as key and file type as value
		 */
		
		public TreeMap<Integer, File> getCorpusFilesWithID(){
			
			FileScanner corpusFolder = new FileScanner(new File(this.documentLocation));	
			
			this.corpusFiles = corpusFolder.collectScannedFiles();
			
			return this.corpusFiles;
			
		}
		
		/*
		 * Given a docID, a file type can be obtained by the below method
		 */
		
		public File getFileWithID(Integer docID){
			
			File fileName=null;
			
			for (Integer currentdocID: this.corpusFiles.keySet()){
				
				if(currentdocID == docID){
					
				fileName = this.corpusFiles.get(currentdocID);
				
				break;
				
				}
				
		      }
			
			return fileName;
			
		}
		
		//-------------------------------------------------------------------------
		
		//Taking stopwords from stop-words file in an stopwords arraylist
		
		public void scanStopWords() throws IOException{
			
			System.out.println("Scanning Stop Words");
			
			br = new BufferedReader(new FileReader(this.stopWordLocation));
			
			String currentLine="";
			
			while((currentLine=br.readLine())!=null){
				
				stopwords.add(currentLine);
				
			}
			
			br.close();
			
		}
		
		//-------------------------------------------------------------------------
		
		//This method takes a path of a file and returns the string of the file by removing HTML tags from the string
		
		public String removeHTMLTagsFrom(String fileName) throws IOException{
			
			System.out.println("Removing HTML Tags");
			
			String fileText = ""; 
		    
            /*	
             * String str = "a d, m, i.n";
               String delimiters = "\\s+|,\\s*|\\.\\s*";
               str.split(delimeter);
               output = admin; 	
            */	

		    Scanner readFile = new Scanner(new File(fileName));     
		
		    while(readFile.hasNext()){
		            
		          fileText += readFile.nextLine();
		            
		    }
		    
		    readFile.close();
		    
			String noHTMLString = fileText.replaceAll("\\<.*?>","");
				
			noHTMLString = noHTMLString.replaceAll("null", "");
				
			noHTMLString = noHTMLString.replaceAll(",", "");
				
			noHTMLString = noHTMLString.replaceAll("|", "");
				
			noHTMLString = noHTMLString.replaceAll("'", "");

			noHTMLString = noHTMLString.replaceAll("\'?'", "");
				
			return noHTMLString;
			
		}
		
		//This method takes a string as input and returns an arraylist comprising of individual words in the string input
		
		public ArrayList<String> getTempWordsForStemming(String noHTMLString){
			
			for (String retrievedWord: noHTMLString.split(" ")){
				
				if(!retrievedWord.equals(" ")){
				
				this.tempTermsToBeStemmed.add(retrievedWord);
				
				}
				
		      }
			
			return this.tempTermsToBeStemmed;
			
		}
		
		//This method will remove stopwords from file words list
		
		public ArrayList<String> removeStopWordsFromTokens(){
			
			int index1,index2;
			
			for(index1=0; index1 < this.tempTermsToBeStemmed.size(); index1++){
				
				for (index2 = 0; index2 < stopwords.size(); index2++) {
					
					if(this.tempTermsToBeStemmed.get(index1).equals(stopwords.get(index2)) || this.tempTermsToBeStemmed.get(index1).equals("")){
						
						this.tempTermsToBeStemmed.remove(index1);
						
						break;
						
					}
					
				}
				
			}
			
			return this.tempTermsToBeStemmed;
			
		}
		
		
		//Returns stemmed words from a given docID
		
		public ArrayList<TokenDetails> getStemmedWordsFrom(int docID){
			
			int index1;
			
			String returnedString="";
			
			for(index1=0; index1 < this.tempTermsToBeStemmed.size(); index1++){
				
				returnedString=this.stemmer.stem(this.tempTermsToBeStemmed.get(index1));
				
				if (returnedString!=null) {
				
					this.stemmedWords.add(new TokenDetails(returnedString, docID));
					
				}
				
		      }
		
			return this.stemmedWords;
			
		}
		
		//Returns total number of documents in the corpus
		
		public int getTotalNumberOfFiles(TreeMap<Integer,File> corpusFiles){
			
			this.totalFiles = corpusFiles.size();
			
			return this.totalFiles;
			
		}
		
		//For clearing storage
		
		public void clearTempWordsToBeStemmed(){
			
			tempTermsToBeStemmed.clear();
			
		}
		
		//For clearing storage
		
		public void clearStemmedWords(){
			
			stemmedWords.clear();
			
		}
		
		//Sorting stemmed words 
		
		public void sortStemmedWords(){
			
			Collections.sort(this.stemmedWords, TokenDetails.TDComparator);

		}
		
		//This method takes docId as input and builds an inverted index
		
		public InvertedIndex buildInvertedIndex(int docID){
			
			int index1,index2,termFrequency;
			
			String token1,token2;
			
			for (index1 = 0; index1 < this.stemmedWords.size(); index1+=termFrequency) {
				
				token1 = this.stemmedWords.get(index1).getToken().toString();
				
				 termFrequency=1;
				
				for (index2 = index1+1; index2 < this.stemmedWords.size(); index2++) {
					
					token2 = this.stemmedWords.get(index2).getToken().toString();
				
					if(token1.equals(token2)){
								
						termFrequency++;
						
					}else{
						
						break;
						
					}
					
				}
				
				this.post = new Posting(docID, termFrequency);
				
				if(this.invertedIndex.getInvertedIndex().containsKey(token1)){
					
					this.invertedIndex.getInvertedIndex().get(token1).add(post);
					
				}else{
					
					this.newEmptyPostingList = new ArrayList<Posting>();
					
					this.newEmptyPostingList.add(this.post);
					
					this.invertedIndex.getInvertedIndex().put(token1, this.newEmptyPostingList);
					
					
				}
				
			}
			
			return invertedIndex;
			
		}
		
		//For printing inverted index
		
		public void printInvertedIndex(){
			
			ArrayList<Posting> posting;
			
			for(String key : this.invertedIndex.getInvertedIndex().keySet()){
			
			String token = key.toString();
			
			posting = invertedIndex.getInvertedIndex().get(key);
		
			System.out.print(key + " --> ");
	     
			for (int j = 0; j < posting.size(); j++) {
	     	
	     	System.out.print("(" + posting.get(j).getDocID() + "," + posting.get(j).getTermFrequency() + ")" + "-");
				
			}
	     
			System.out.println("");
					
			}
		
		}

		public Posting getPost() {
		
			return post;
		
		}

		public int getTotalFiles() {
		
			return totalFiles;
		
		}

		public String getDocumentLocation() {
		
			return documentLocation;
		
		}

		public void setDocumentLocation(String documentLocation) {
		
			this.documentLocation = documentLocation;
		
		}

		public String getStopWordLocation() {
		
			return stopWordLocation;
		
		}

		public void setStopWordLocation(String stopWordLocation) {
		
			this.stopWordLocation = stopWordLocation;
		
		}

		public ArrayList<Posting> getNewEmptyPostingList() {
		
			return newEmptyPostingList;
		
		}

		public InvertedIndex getInvertedIndex() {
		
			return invertedIndex;
		
		}

		public HindiStemmerLight getStemmer() {

			return stemmer;
		
		}

		public void setStemmer(HindiStemmerLight stemmer) {
		
			this.stemmer = stemmer;
		
		}

		public ArrayList<String> getStopwords() {
		
			return stopwords;
		
		}

		public ArrayList<String> getTempTermsToBeStemmed() {
		
			return tempTermsToBeStemmed;
		
		}
		
		
		//For getting the postingList of a token (term)
		
		public ArrayList<Posting> searchToken(String userToken){
			
				ArrayList<Posting> posting = null;
				
				for(String searchToken : this.invertedIndex.getInvertedIndex().keySet()){
					
					String obtainedToken = searchToken.toString();
					
					if(obtainedToken.equals(userToken)){
						
						posting = this.invertedIndex.getInvertedIndex().get(obtainedToken);
							
						break;
						
					}
					
				}
				return posting;
				
			}
		
		//This method takes a docID and a HTML tag name, returns the substring in the HTML tag of given docID 
		
		public String getFileTitle(int docID, String HTMLTag) throws IOException{
			
			String fileText = ""; 
			
			for(Integer id : this.corpusFiles.keySet()){
				
				if(id == docID){
					
					@SuppressWarnings("resource")
					
					Scanner readFile = new Scanner(new File(this.corpusFiles.get(id).getAbsolutePath().toString()));     
					
				    while(readFile.hasNext()){
				            
				          fileText += readFile.nextLine();
				            
				    }
				    
				    readFile.close();

				    break;
				    
				}
				
			}
			
			String tagREGEX = "<"+HTMLTag+">(.+?)</"+HTMLTag+">";
			
			final Pattern pattern = Pattern.compile(tagREGEX);
			
			final Matcher matcher = pattern.matcher(fileText);
			
			matcher.find();
			
			return matcher.group(0);	
			
		}
		
		//This method constructs a term incidence matrix with term frequency as value of each cell
		
		public int[][] getTermIncidenceMatrix(int totalDoc, int totalTerms){
			
			int[][] incidenceMatrix = new int[totalTerms][totalDoc];
			
			ArrayList<Posting> posting;
			
			int currentRow = 0;
			
			for(String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()){
				
				posting = this.getInvertedIndex().getInvertedIndex().get(keyToken);
				
				for (int docID = 0; docID < totalDoc; docID++) {
			
					for (int i = 0; i < posting.size(); i++) {
						
						if(posting.get(i).getDocID() > docID ){
							
							incidenceMatrix[currentRow][docID] = 0;
							
							break;
							
						}else{
							
							if(posting.get(i).getDocID() == docID){
								
								incidenceMatrix[currentRow][docID] = posting.get(i).getTermFrequency();
							
								break;
								
							}
							
						}
						
					}
						
					}
				
				currentRow++;
					
				}	
			
			return incidenceMatrix;
			
		}
		
		//For getting LinkedList vector of given token
		
		public LinkedList<Integer> getVector(String token){
		
			LinkedList<Integer> vector = new LinkedList<Integer>();
	
			for (String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()) {
		
				if(token.equals(keyToken)){
		
					vector.add(1);
			
				}else{
			
					vector.add(0);
			
				}
		
			}

			return vector;
	
		}

		//For getting array vector of given token
		
		public int[] getVector(String token, int size){
			
			int[] vector = new int[size];
	
			int i = 0;
			
			for (String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()) {
		
				if(token.equals(keyToken)){
		
					vector[i]=1;
			
				}else{
			
					vector[i]=0;
			
				}
				
				i++;
		
			}

			return vector;
	
		}

		//For initializing a vector with a value 
		
		public LinkedList<Integer> inititalizeVector(LinkedList<Integer> vector,int with, int size){
			
			for (int i = 0; i < size; i++) {
				
				vector.add(with);
				
			}
			
			return vector;
			
		}
		
		//For initializing a vector with a value 
		
		public LinkedList<Double> inititalizeVector(LinkedList<Double> vector,Double with, int size){
			
			for (int i = 0; i < size; i++) {
				
				vector.add(with);
				
			}
			
			return vector;
			
		}
		
		//This method adds two vectors of type integer and returns vector as linked list
		
		public LinkedList<Integer> addIntVectors(LinkedList<Integer> sourceVector, LinkedList<Integer> vectorToBeAdded){
			
			LinkedList<Integer> resultVector = new LinkedList<Integer>();
			
			for (int i = 0; i < sourceVector.size(); i++) {
				
				resultVector.add(sourceVector.get(i) + vectorToBeAdded.get(i));
				
			}
			
			return resultVector;
			
		}
		
		//This method adds two vectors of type integer and returns vector as an array
		
		public int[] addIntVectors(int[] sourceVector, int[] vectorToBeAdded){
			
			int[] resultVector = new int[sourceVector.length];
			
			for (int i = 0; i < sourceVector.length; i++) {
				
				resultVector[i]=(sourceVector[i] + vectorToBeAdded[i]);
				
			}
			
			return resultVector;
			
		}
		
		//This method adds two vectors of type Double and returns vector as linked list
		
		public LinkedList<Double> addDoubleVectors(LinkedList<Double> linkedList1, LinkedList<Double> linkedList2){
			
			LinkedList<Double> resultVector = new LinkedList<Double>();
			
			for (int i = 0; i < linkedList1.size(); i++) {
				
				resultVector.add((linkedList1.get(i) - linkedList2.get(i)));
				
			}
			
			return resultVector;
			
		}
		
		//This method subtracts two vectors of type Double and returns vector as linked list
		
		public LinkedList<Double> subtractVectors(LinkedList<Double> linkedList1, LinkedList<Double> linkedList2){
			
			LinkedList<Double> resultVector = new LinkedList<Double>();
			
			for (int i = 0; i < linkedList1.size(); i++) {
				
				resultVector.add((linkedList1.get(i) - linkedList2.get(i)));
				
			}
			
			return resultVector;
			
		}
		
		//This method return a vector of given document (docID) as a linked list
		
		public LinkedList<Integer> getDocumentVectorOf(int docID){
			
			LinkedList<Integer> docVector = new LinkedList<>();
			
			docVector = this.inititalizeVector(docVector, 0, this.getInvertedIndex().getInvertedIndex().size());
			
			for (String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()) {
				
				for (int i = 0; i < this.getInvertedIndex().getInvertedIndex().get(keyToken).size(); i++) {
					
					if(this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getDocID() > docID){
						
						docVector.add(0);
						
						break;
						
					}else{
						
						if(this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getDocID() == docID){
							
							docVector.add(this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getTermFrequency());
						
							break;
							
						}
						
					}
					
				}
				
			}
			
			return docVector;
			
		}
		
		//This method return a vector of given document (docID) as an array
		
		public int[] getDocumentVectorOf(int docID, int size){
			
			int[] docVector = new int[size];
			
			for (int i = 0; i < docVector.length; i++) {
				
				docVector[i] = 0;
				
			}
			
			for (String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()) {
				
				for (int i = 0; i < this.getInvertedIndex().getInvertedIndex().get(keyToken).size(); i++) {
					
					if(this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getDocID() > docID){
						
						docVector[i]=0;
						
						break;
						
					}else{
						
						if(this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getDocID() == docID){
						
							docVector[i]=this.getInvertedIndex().getInvertedIndex().get(keyToken).get(i).getTermFrequency();
							
							break;
							
						}
						
					}
					
				}
				
			}
			
			return docVector;
			
		}
		
		
		//For scalar multiplication of a vector, returns a linkedlist vector   
		
		public LinkedList<Double> multiplyVectorWith(LinkedList<Integer> vector, Double scalar){
			
			LinkedList<Double> v = new LinkedList<Double>();
			
			this.inititalizeVector(v, 0.0, vector.size());
			
			for (int i = 0; i < vector.size(); i++) {
				
				v.add(scalar * vector.get(i));
				
			}
			
			return v;
			
		}
		
		//This method takes a linked list vector and generates new query according to it
		
		public String generateQueryFrom(LinkedList<Double> queryVector, Indexer indexer, int forNumberOfTerms){
			
			String query = "";
			
			String temp;
			
			int value;
			
			int counter = 0;
			
			LinkedList<TermValue> termValue = new LinkedList<TermValue>();
			
			for (String keyToken : indexer.invertedIndex.getInvertedIndex().keySet()) {
				
				termValue.add(new TermValue(queryVector.get(counter),keyToken));
				
				//System.out.println(queryVector.get(counter) + " " + keyToken);
				
				counter++;
				
			}
			
			Collections.sort(termValue, TermValue.TVComparator); //Sorting in descending order for ranking according to tfidf score
			
			for (int i = 0; i < forNumberOfTerms; i++) {
				
				query = query + termValue.get(i).getTerm() + " ";
				
			}
			
			return query;
			
		}

		//This method takes an array vector and generates new query according to it
		
		public String generateQueryFrom(int[] queryVector, Indexer indexer, int forNumberOfTerms){
			
			String query = "";
			
			String temp;
			
			int value;
			
			int counter = 0;
			
			LinkedList<TermValue> termValue = new LinkedList<TermValue>();
			
			for (String keyToken : indexer.invertedIndex.getInvertedIndex().keySet()) {
				
				termValue.add(new TermValue(queryVector[counter],keyToken));
				
				//System.out.println(queryVector.get(counter) + " " + keyToken);
				
				counter++;
				
			}
			
			Collections.sort(termValue, TermValue.TVComparator); //Sorting in descending order for ranking according to tfidf score
			
			for (int i = 0; i < forNumberOfTerms; i++) {
				
				query = query + termValue.get(i).getTerm() + " ";
				
			}
			
			return query;
			
		}

		
		//This method creates a file of incidence matrix
		
		public File getTermIncidenceMatrixFile(String filename) throws IOException{
			
			//int[][] incidenceMatrix = new int[totalTerms][totalDoc];
			
			File incidenceMatrixFile = new File(filename);
			
			incidenceMatrixFile.createNewFile();
			
			FileWriter fw = null;
			
			String string="";
			
			ArrayList<Posting> posting;
			
			int currentRow = 0;
			
			for(String keyToken : this.getInvertedIndex().getInvertedIndex().keySet()){
				
				posting = this.getInvertedIndex().getInvertedIndex().get(keyToken);
				
				string = string + keyToken + " ";
				
				System.out.println("(" + currentRow +  ")" + keyToken);
				
				for (int docID = 0; docID < this.getTotalFiles(); docID++) {
			
					for (int i = 0; i < posting.size(); i++) {
						
						if(posting.get(i).getDocID() > docID ){
							
							string = string + "0 ";
							
							break;
							
						}else{
							
							if(posting.get(i).getDocID() == docID){
								
								string = string + posting.get(i).getTermFrequency() + " ";
							
								break;
								
							}
							
						}
						
					}
						
					}
				
				string += "\n";
				
				try {

					fw = new FileWriter(incidenceMatrixFile,true);
					fw.write(string);	
					fw.close();

				} catch (IOException e) {
					e.printStackTrace();
				} 

				
				currentRow++;
					
				}				
			
			
			return incidenceMatrixFile;
			
		}
		
		//Returns a linkedlist of ranked documents
		
		public LinkedList<DocScore> getRankedListOf(String inputToken,int documentsToBeRetrieved) throws IOException{
			
			LinkedHashSet<Integer> documentsPerQuery = new LinkedHashSet(); //Unique doc
			
			LinkedList<ArrayList<Posting>> postingListCollection = new LinkedList<ArrayList<Posting>>(); 
			
		    int currentDocID,docID;
			
			int indexCounter=0;
			
			Double score;
			
			TreeSet documentsPerQuerySet = new TreeSet();
			
			Iterator<Integer> iterator;
			
			for(String token : inputToken.split(" ")){
				
				//System.out.println(token);
				
				if (this.getInvertedIndex().getInvertedIndex().get(token) != null) {
				
					postingListCollection.add(this.getInvertedIndex().getInvertedIndex().get(token));
					
				}
				
			}
			
			for (int i = 0; i < postingListCollection.size(); i++) {
				
				for (int j = 0; j < postingListCollection.get(i).size(); j++) {
					
					documentsPerQuery.add(postingListCollection.get(i).get(j).getDocID());
					
				}
				
			}
			
			documentsPerQuerySet.addAll(documentsPerQuery);
			
			documentsPerQuery.clear();
			
			int num = documentsPerQuerySet.size();
			
			iterator = documentsPerQuerySet.iterator();
			
			docID=0;
			
			while (iterator.hasNext()) {
			    
				currentDocID = iterator.next();
				
				score = 0.0;
				
				for (int i = 0; i < postingListCollection.size(); i++) {
					
					for (int j = 0; j < postingListCollection.get(i).size(); j++) {
						
						docID = postingListCollection.get(i).get(j).getDocID();
						
						if(docID > currentDocID){
							
							break;
							
						}else{
							
							if(docID == currentDocID){
								
								score += postingListCollection.get(i).get(j).getTermFrequency() * Math.log10((this.getTotalNumberOfFiles(corpusFiles) * 1.0)/postingListCollection.get(i).size());
								
								break;
								
							}
					
				}
			
			}
					
			
				}
				
				docScore.add(new DocScore(currentDocID,score));
				
			}
			
			
			
			Collections.sort(docScore, DocScore.DSComparator); //Sorting in descending order for ranking according to tfidf score
			
			
				int till = documentsToBeRetrieved;
				
				if(num > documentsToBeRetrieved){
					
					till = documentsToBeRetrieved;
					
				}else if(num < documentsToBeRetrieved){
					
					till = num;
					
				}
				
				DecimalFormat df = new DecimalFormat("#.####");
				
				/*for (int i = 0; i < docScore.size(); i++) {
					
					System.out.println("Rank=" + i + " | Score=" + df.format(docScore.get(i).getScore()) + " | DocID=" + docScore.get(i).getDocID() + " | " + indexer.getFileTitle(docScore.get(i).getDocID(), "title") + " ");
					
				}*/
				
				
				LinkedHashSet dScore = new LinkedHashSet (docScore);
				
				docScore = new LinkedList(dScore);
				
				for (int i = 0; i < till; i++) {
					
					System.out.println("Rank=" + i + " | Score=" + df.format(docScore.get(i).getScore()) + " | DocID=" + docScore.get(i).getDocID() + " | " + this.getFileTitle(docScore.get(i).getDocID(), "title") + " ");
					
				}
				
				return this.docScore;

			
		}
		
		public LinkedList<DocScore> getDocScore(){
			
			return this.docScore;
			
		}
		
}
