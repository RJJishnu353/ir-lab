
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

import ir_lib.InvertedIndex;
import ir_lib.Posting;

public class docVectorFile {

	public static void main(String[] args) throws IOException {
		
		//String documentLocation = args[0]; //--From console take doc location @ run time
		//String stopWordLocation= args[1];  //--From console take stopwords file location @ run time
			
		//Temporary corpus file and stopwords file for testing purpose
		//NOTE : Once done comment both of these and take the files from console
				

		//-------------------------------------------------------------------------
		//String documentLocation="/home/sysadmin/Downloads/hindi";
		//String stopWordLocation= "/home/sysadmin/Downloads/stopwords_hi.txt";
		//-------------------------------------------------------------------------
		
		System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("\nEnter corpus stopwords file location : ");
		
		sc = new Scanner(System.in);
		
		String stopWordLocation = sc.nextLine();

		System.out.println();
		
		System.out.print("\nEnter location where doc vectors will be stored (folder): ");
		
		sc = new Scanner(System.in);
		
		String dir = sc.nextLine();

		System.out.println();
		
		//---------------------------------------------------------------------------------------
		
		/*
		 * Building inverted index
		 * 
		 */
		
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
		
		/*
		 * Inverted index created
		 * 
		 */
		
		indexer.printInvertedIndex(); // prints inverted index
		
		//---------------------------------------------------------------------------------------
		
		/*
		 * Now for each document build the vector and store it in a file with name = document id
		 * 
		 */
	
		/* Creating a directory named docVectorFiles which will contain files, that will store document vectors 
		 * for fast computation 
		 * This method is also helpful beacuse RAM is less used.
		 * It is better to store the doc vectors in file rather than RAM because RAM is limited in size.		   
		 */    
		
		File file = new File(dir);
        
		if (!file.exists()) {
        
			if (file.mkdir()) {
            
				System.out.println("Directory is created!");
           
			} else {
            
				System.out.println("Failed to create directory!");
            
			}
        
		}else {
			
			/*
			 * Directory already exists so delete it for new info to be gathered and stored.
			 */
			
			System.out.println("ss");
			
			//list all the directory contents
     	    
			String files[] = file.list();
  
     	    for (String temp : files) {
     	   
     		   //construct the file structure
     	       
     		   File fileDelete = new File(file, temp);
     		      	     
     		   fileDelete.delete();
     	   
     	    }
     		
     	    //check the directory again, if empty then delete it
     	   
     	    if(file.list().length==0){
        	
     		   file.delete();
     	     
     		   System.out.println("Directory is deleted : " 
                                               + file.getAbsolutePath());
     	    }
			
		    } 
	
		/*
		 * Directory creation / updation completed
		 */
		
		/*
		 * Now starting to store vector in file
		 */
			
		ArrayList<Posting> postingList;

		String content;

		PrintWriter out;
		
		int dimension = 0;
		
		for (int docID = 0; docID < corpusFiles.size() - 1; docID++) {
			
			System.out.println("DocID : " +docID);
			
			dimension = 0;
			
			
			
			for (String keyToken : indexer.getInvertedIndex().getInvertedIndex().keySet()) {
			
				if(++dimension <= 10000){
				
				postingList = indexer.getInvertedIndex().getInvertedIndex().get(keyToken);
				
				content = "";
				
				for (int iterator = 0; iterator < postingList.size(); iterator++) {
					
					if(postingList.get(iterator).getDocID() < docID){
						
						continue;
						
					}else if(postingList.get(iterator).getDocID() == docID){
						
						content = content + postingList.get(iterator).getTermFrequency();
						
						file = new File(dir +"/" + docID + ".txt");

						// if file doesnt exists, then create it
						
						/*
						 * if (!file.exists()) {
						
							file.createNewFile();
						
						}
						*/

						file.createNewFile();
						
						out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true)));
						
						out.println(content);
						
						out.close();
						
						break;
						
					}else if(postingList.get(iterator).getDocID() > docID){
						
						content = "0";
						
						file = new File(dir +"/" + docID + ".txt");

						// if file doesnt exists, then create it
						
						/*
						 * if (!file.exists()) {
						
							file.createNewFile();
						
						}
						*/

						file.createNewFile();
						
						out = new PrintWriter(new BufferedWriter(new FileWriter(file.getAbsoluteFile(),true)));
						
						out.println(content);
						
						out.close();
						
						break;

					}
					
				}
		
			}
			
			
			}

			
		}
		
		
	}
	
}
