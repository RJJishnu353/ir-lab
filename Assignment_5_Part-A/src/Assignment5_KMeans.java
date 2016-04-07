
import ir_lib.Cluster;
import ir_lib.DocDetails;
import ir_lib.DocDistance;
import ir_lib.DocScore;
import ir_lib.KMeans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.print.Doc;


public class Assignment5_KMeans {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {	
		
		System.out.println("Please enter the correct location else null pointer exception will occur.");
		
		System.out.print("Enter corpus folder location : ");
		
		Scanner sc = new Scanner(System.in);
		
		String documentLocation = sc.nextLine();
		
		System.out.print("Enter the document vector file path (directory) : ");
		
		sc = new Scanner(System.in);
		
		String docVectorPath = sc.next(); //Getting docVector directory path
		
		System.out.print("Enter the number of clusters : ");
		
		int numberOfCluster = sc.nextInt(); //Getting the number of clusters
		
		KMeans kmeansOBJ = new KMeans(documentLocation,docVectorPath,numberOfCluster);
		
		kmeansOBJ.getCorpusFilesWithID();
		
		kmeansOBJ.createEmptyClusters();  //Creating empty clusters
		
		HashSet<Integer> seeds = kmeansOBJ.generateInitialSeeds(); // Now giving each empty cluster a COM by randomly choosing a COM from the doc vector
		
		kmeansOBJ.loadCOM_FromFileToMemory(seeds); // Loading the COM doc vectors from secondary memory to main memory
		
		ArrayList<DocDetails> distanceSet = kmeansOBJ.createClusters(); // Now given all the doc vectors, creating a cluster out of them
		
		/*for (int clusterID = 0; clusterID < kmeansOBJ.getClusterSet().length; clusterID++) {
			
			kmeansOBJ.printCluster(clusterID); // Printing each cluster along with its members documents
			
		}*/
		
		/*
		 * Printing top 5 docs from each cluster
		 * If the cluster is not having a minimum 5 then total documents inside will be printed based on closeness
		 * If the cluster is having more than 5 elements then top 5 will be printed based on closeness of distance
		 * First document appearing in each cluster is close to it than the consecutive docs
		 */
		
		kmeansOBJ.printTopDocsFromClusters(distanceSet, 5); 
		
	}

}

