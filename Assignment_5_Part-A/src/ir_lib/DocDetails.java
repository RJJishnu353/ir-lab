package ir_lib;

import java.util.ArrayList;

public class DocDetails {
	
	private int docID;
	private double distance;
	private int clusterID;
	
	public DocDetails(int docID, int clusterID, double distance) {
		super();
		this.docID = docID;
		this.distance = distance;
		this.clusterID = clusterID;
	}

	public int getDocID() {
		return docID;
	}

	public double getDistance() {
		return distance;
	}
	
	public int getClusterID() {
		return clusterID;
	}
	
}
