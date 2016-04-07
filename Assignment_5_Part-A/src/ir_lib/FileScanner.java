
package ir_lib;

/*
 * 
 * This class will be used to scan a particular corpus folder 
 * and return a treemap with docId as the key
 *            and a file type as a value.   
 */

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

public class FileScanner {
	
	private File folder;
	
	public FileScanner(File folder){
		
		try {
			
		if(folder.isDirectory()){
		
			this.folder = folder;
		
		}
		
		}catch(Exception ex){
			
			System.out.println("Hey!!! This is not a directory");
			
		}
		
	}
	
	//This funtion does the activity  of scanning files in a given folder and returns a treemap 
	//with docID as the key and file type as its value
	public TreeMap<Integer,File> collectScannedFiles(){
		
		TreeMap<Integer,File> fileList = new TreeMap<Integer,File>();
		
		int docId = 0;
		
		for(File eachFile : folder.listFiles()){
			
			fileList.put(docId, eachFile);
			
			//System.out.println("Loading DOC : " + docId);
			
			docId++;
			
		}
		
		return fileList;

	}
			
			
	
}
