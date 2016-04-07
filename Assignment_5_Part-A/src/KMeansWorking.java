
import ir_lib.Cluster;
import ir_lib.DocDetails;
import ir_lib.DocDistance;
import ir_lib.DocScore;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.print.Doc;


public class KMeansWorking {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		
		ArrayList<ArrayList<Integer>> COM_SET = new ArrayList<ArrayList<Integer>>(); 
		
		ArrayList<Integer> tempVEC = new ArrayList<Integer>();
		
		ArrayList<Integer> tempCOM = new ArrayList<Integer>();
		
		Cluster[] clusterSet;
		
		double distance;
		
		double[] euclideanDistance; 
		
		System.out.println("K-Means Algorithm");
		
		Scanner sc;
		
		System.out.print("Enter the document vector file path (directory) : ");
		
		sc = new Scanner(System.in);
		
		String docVectorPath = sc.next(); //Getting docVector directory path
		
		File docVector = new File(docVectorPath);
		
		System.out.print("Enter the number of clusters : ");
		
		int numberOfCluster = sc.nextInt(); //Getting the number of clusters
		
		clusterSet = new Cluster[numberOfCluster];
		
		euclideanDistance = new double[numberOfCluster];
		
		//Giving each cluster an id for tracking and storing docs into appropriate ones
		
		for (int i = 0; i < clusterSet.length; i++) {
			
			clusterSet[i] = new Cluster(i);
			
		}
		
		//System.out.println("Generating 10 random integers in range 0.." + (docVector.list().length - 1));
		
		Random randomGenerator = new Random();
		
		HashSet<Integer> seeds = new HashSet<Integer>();
		
		int randomSeed = 0;
		
		while(seeds.size() <= numberOfCluster ){
		
			randomSeed = randomGenerator.nextInt(docVector.list().length - 1);
			
			if (seeds.add(randomSeed)) {
				
				//System.out.println("Random seed generated : " + randomSeed);
				
			}else{
				
				//System.out.println("Random seed not fed to seed set");
				
			}
			
		    
		}
		
		System.out.println("Loading COM from file to memory");
		
		for (int file = 0; file < seeds.size(); file++) {
			
			System.out.println("File : " + file);
			
			tempCOM = getDocVector(docVectorPath + "/" + file + ".txt");
					
			COM_SET.add(tempCOM); //adding this COM to COM set
			
			//System.out.println("tempComp.size():" + tempCOM.size());
			
		}   
		
		int nearestCluster;
		
		double smallestDistance;
		
		double eleDistance;
		
		LinkedList<Integer> clusterDocList;
		
		ArrayList<DocDetails> distanceSet = new ArrayList<DocDetails>();
		
		//for (int docID = 0; docID < (docVector.list().length); docID++) {
			
		 for (int docID = 0; docID < 100; docID++) {
		
			tempVEC = getDocVector(docVectorPath + "/" + docID + ".txt");
			
			nearestCluster = -1;
			
			for (int COM = 0; COM < numberOfCluster; COM++) {
				
				tempCOM = COM_SET.get(COM);
				
				distance = 0.0;
				
				//System.out.println("tempCOM.size():" + tempCOM.size());
				
				for (int i = 0; i < tempVEC.size(); i++) {
					
					//System.out.println("(tempCOM.get(i) - tempVEC.get(i)=" + (tempCOM.get(i) - tempVEC.get(i)));
					
					try{
					
					eleDistance = Math.sqrt((tempCOM.get(i) - tempVEC.get(i)) * (tempCOM.get(i) - tempVEC.get(i)));
					
					distance += eleDistance;
					
					}catch(IndexOutOfBoundsException e){
						
						System.out.println("Error : " + e.getMessage());
						
					}
					
						
				}
				
				euclideanDistance[COM] = distance;
				
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
			
			System.out.println("File#:" + docID + "added to cluster#:"+ nearestCluster);
			
		}
		
		for (int i = 0; i < clusterSet.length; i++) {
			
			System.out.println("Cluster:" + i + " " + clusterSet[i].getClusterElements().toString());
			
		}
		
		int topK;
		
		for (int clusterID = 0; clusterID < clusterSet.length; clusterID++) {
			
			ArrayList<DocDistance> docDis = getDocsOf(distanceSet,clusterID);
			
			Collections.sort(docDis);
			
			topK = (docDis.size() > 5 ) ? 5 : docDis.size(); //Ternary operator
			
			for (int i = 0; i < topK; i++) {
				
				
				System.out.println("Cluster:" + clusterID + " | ");
				
			}
			
		}

	}
	
	private static ArrayList<Integer> getDocVector(String docPath) throws NumberFormatException, IOException{
		
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
	
	public static ArrayList<DocDistance> getDocsOf(ArrayList<DocDetails> d, int clusterID){
		
		ArrayList<DocDistance> temp = new ArrayList<DocDistance>();
		
		for (int i = 0; i < d.size(); i++) {
			
			if(d.get(i).getClusterID() == clusterID){
				
				temp.add(new DocDistance(d.get(i).getDocID(), d.get(i).getDistance()));
				
			}
			
		}
		
		return temp;
		
	}


}

