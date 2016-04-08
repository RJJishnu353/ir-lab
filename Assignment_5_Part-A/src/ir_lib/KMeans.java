package ir_lib;
import ir_lib.Cluster;
import ir_lib.DocDetails;
import ir_lib.DocDistance;
import ir_lib.DocScore;
import ir_lib.FileScanner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.Doc;


public class KMeans {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
		
		private ArrayList<ArrayList<Integer>> COM_SET = new ArrayList<ArrayList<Integer>>(); 
		
		private ArrayList<Integer> tempVEC = new ArrayList<Integer>();
		
		private ArrayList<Integer> tempCOM = new ArrayList<Integer>();
		
		private double distance;
		
		String documentLocation=""; //
		
		String docVectorPath=""; //Getting docVector directory path
		
		int  numberOfCluster=0;
		
		private TreeMap<Integer, File> corpusFiles;
		
		private Scanner sc;
		
		public KMeans(String documentLocation, String docVectorPath, int numberOfCluster){
			
			this.setDocumentLocation(documentLocation);
			this.setDocVectorPath(docVectorPath);
			this.setDocVector(new File(this.getDocVectorPath()));
			this.setNumberOfCluster(numberOfCluster);
			
			/*System.out.println(this.getDocumentLocation());
			System.out.println(this.getDocVectorPath());
			System.out.println(this.getNumberOfCluster());
			System.out.println(this.getDocVector().getPath());
			*/
			
		}
		
		
		private File docVector;	
		
		private Cluster[] clusterSet;
		
		double[] euclideanDistance;
		
		
		
		public ArrayList<ArrayList<Integer>> getCOM_SET() {
			return COM_SET;
		}

		public void setCOM_SET(ArrayList<ArrayList<Integer>> cOM_SET) {
			COM_SET = cOM_SET;
		}

		public ArrayList<Integer> getTempVEC() {
			return tempVEC;
		}

		public void setTempVEC(ArrayList<Integer> tempVEC) {
			this.tempVEC = tempVEC;
		}

		public ArrayList<Integer> getTempCOM() {
			return tempCOM;
		}

		public void setTempCOM(ArrayList<Integer> tempCOM) {
			this.tempCOM = tempCOM;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		public String getDocumentLocation() {
			return documentLocation;
		}

		public void setDocumentLocation(String documentLocation) {
			this.documentLocation = documentLocation;
		}

		public String getDocVectorPath() {
			return docVectorPath;
		}

		public void setDocVectorPath(String docVectorPath) {
			this.docVectorPath = docVectorPath;
		}

		public int getNumberOfCluster() {
			return numberOfCluster;
		}

		public void setNumberOfCluster(int numberOfCluster) {
			this.numberOfCluster = numberOfCluster;
		}

		public TreeMap<Integer, File> getCorpusFiles() {
			return corpusFiles;
		}

		public void setCorpusFiles(TreeMap<Integer, File> corpusFiles) {
			this.corpusFiles = corpusFiles;
		}

		public Scanner getSc() {
			return sc;
		}

		public void setSc(Scanner sc) {
			this.sc = sc;
		}

		public File getDocVector() {
			return docVector;
		}

		public void setDocVector(File docVector) {
			this.docVector = docVector;
		}

		public Cluster[] getClusterSet() {
			return clusterSet;
		}

		public void setClusterSet(Cluster[] clusterSet) {
			this.clusterSet = clusterSet;
		}

		public double[] getEuclideanDistance() {
			return euclideanDistance;
		}

		public void setEuclideanDistance(double[] euclideanDistance) {
			this.euclideanDistance = euclideanDistance;
		}

		public void createEmptyClusters(){
			
			//Giving each cluster an id for tracking and storing docs into appropriate ones
			
			this.setClusterSet(new Cluster[numberOfCluster]);
			
			for (int i = 0; i < this.getClusterSet().length; i++) {
				
				this.getClusterSet()[i] = new Cluster(i);
				
			}
			
		}
		
		public HashSet<Integer> generateInitialSeeds(){
			
			//System.out.println("Generating 10 random integers in range 0.." + (docVector.list().length - 1));
			
			Random randomGenerator = new Random();
			
			HashSet<Integer> seeds = new HashSet<Integer>();
			
			int randomSeed = 0;
			
			while(seeds.size() <= this.getNumberOfCluster() ){
			
				randomSeed = randomGenerator.nextInt(this.getDocVector().list().length - 1);
				
				if (seeds.add(randomSeed)) {
					
					//System.out.println("Random seed generated : " + randomSeed);
					
				}else{
					
					//System.out.println("Random seed not fed to seed set");
					
				}
				
			    
			}
			
			return seeds;
			
		}
		
		public void loadCOM_FromFileToMemory(HashSet<Integer> seeds) throws NumberFormatException, IOException{
			
			System.out.println("\nLoading COM from file to memory\n");
			
			for (int file = 0; file < seeds.size(); file++) {
				
				//System.out.println("File : " + file);
				
				tempCOM = getDocVector(this.getDocVectorPath() + "/" + file + ".txt");
						
				COM_SET.add(tempCOM); //adding this COM to COM set
				
				//System.out.println("tempComp.size():" + tempCOM.size());
				
			}
			
		}   
		
		public ArrayList<DocDetails> createClusters() throws NumberFormatException, IOException{
			
			System.out.println("Creating clusters from doc vectors");
			
			this.setEuclideanDistance(new double[numberOfCluster]);
			
			int nearestCluster;
			
			double smallestDistance;
			
			double eleDistance;
			
			LinkedList<Integer> clusterDocList;
			
			ArrayList<DocDetails> distanceSet = new ArrayList<DocDetails>();
			
			for (int docID = 0; docID < (docVector.list().length); docID++) {
				
			 //for (int docID = 0; docID < 100; docID++) {
			
				tempVEC = getDocVector(docVectorPath + "/" + docID + ".txt");
				
				System.out.println("Doc : " + docID);
				
				nearestCluster = -1;
				
				for (int COM = 0; COM < numberOfCluster; COM++) {
					
					tempCOM = COM_SET.get(COM);
					
					distance = 0.0;
					
					//System.out.println("tempCOM.size():" + tempCOM.size());
					
					for (int i = 0; i < tempVEC.size(); i++) {
						
						//System.out.println("(tempCOM.get(i) - tempVEC.get(i)=" + (tempCOM.get(i) - tempVEC.get(i)));
						
						try{
						
						eleDistance = (tempCOM.get(i) - tempVEC.get(i)) * (tempCOM.get(i) - tempVEC.get(i));
						
						distance += eleDistance;
						
						}catch(IndexOutOfBoundsException e){
							
							System.out.println("Error : " + e.getMessage());
							
						}
						
							
					}
					
					try{
					euclideanDistance[COM] = Math.sqrt(distance);
					}catch(Exception e){
						
						System.out.println(e.getLocalizedMessage());
						
					}
					//System.out.println("COM:" + COM + " | distance:" + distance);
					
				}
				
				smallestDistance = 99999.9;
				
				//System.out.println("sd:"+smallestDistance);
				
				for (int i = 0; i < euclideanDistance.length; i++){
				    
					if (euclideanDistance[i] < smallestDistance)
				    {
				        smallestDistance = euclideanDistance[i];
				        
				        nearestCluster = i;
				    
				    }
				    
				}
				
				clusterSet[nearestCluster].getClusterElements().add(docID);
			
				distanceSet.add(new DocDetails(docID, nearestCluster, smallestDistance));
				
				//System.out.println("File#:" + docID + "added to cluster#:"+ nearestCluster);
				
			}
			 System.out.println();
			
			return distanceSet;
			
		}
		
		public void printCluster(int clusterID){
			
			System.out.println("Cluster:" + clusterID + " " + clusterSet[clusterID].getClusterElements().toString());
			
		}
		
		public TreeMap<Integer, File> getCorpusFilesWithID(){
			
			FileScanner corpusFolder = new FileScanner(new File(this.documentLocation));	
			
			this.corpusFiles = corpusFolder.collectScannedFiles();
			
			return this.corpusFiles;
			
		}
		
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
			
			public void printTopDocsFromClusters(ArrayList<DocDetails> distanceSet, int size) throws IOException{
				
				int topK;
				
				for (int clusterID = 0; clusterID < clusterSet.length; clusterID++) {
					
					ArrayList<DocDistance> docDis = getDocsOf(distanceSet,clusterID);
					
					Collections.sort(docDis);
					
					topK = (docDis.size() > size ) ? 5 : docDis.size(); //Ternary operator
					
					System.out.println("ClusterID : " + clusterID);
					
					for (int i = 0; i < topK; i++) {
						
						System.out.println(this.getFileTitle(docDis.get(i).getDocID(), "title"));
						
					}
					
					System.out.println();
					
				}		
				
			}
		
	
	private ArrayList<Integer> getDocVector(String docPath) throws NumberFormatException, IOException{
		
		ArrayList<Integer> tempVEC = new ArrayList<Integer>();
		
		Scanner sc = new Scanner(new File(docPath));
		
		
		String readLine;
		
		while(sc.hasNext()){
			
			readLine = sc.nextLine();
			
			if (!readLine.equals("")) {
				
				tempVEC.add(Integer.parseInt(readLine));
				
			}
			
		}
		
		sc.close();
		
		return tempVEC;
		
	}
	
	public ArrayList<DocDistance> getDocsOf(ArrayList<DocDetails> d, int clusterID){
		
		ArrayList<DocDistance> temp = new ArrayList<DocDistance>();
		
		for (int i = 0; i < d.size(); i++) {
			
			if(d.get(i).getClusterID() == clusterID){
				
				temp.add(new DocDistance(d.get(i).getDocID(), d.get(i).getDistance()));
				
			}
			
		}
		
		return temp;
		
	}


}

