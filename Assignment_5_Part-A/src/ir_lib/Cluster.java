package ir_lib;
import java.util.LinkedList;


public class Cluster {
	
	private int clusterID;
	
	private LinkedList<Integer> clusterElements;
	
	public Cluster(int clusterID){
		
		this.clusterID = clusterID;
	
		this.clusterElements = new LinkedList<Integer>();
		
	}

	public int getClusterID() {
		return clusterID;
	}

	public LinkedList<Integer> getClusterElements() {
		return clusterElements;
	}

}
