package ir_lib;

public class DocDistance implements Comparable<DocDistance>{
	
	private int docID;
	
	private Double distance;
	
	public DocDistance(int docID, double distance){
		
		this.docID = docID;
		this.distance = distance;
		
	}

	public int getDocID() {
		return docID;
	}

	public double getDistance() {
		return distance;
	}

	@Override
	public int compareTo(DocDistance d) {

		return this.distance.compareTo(d.getDistance());
	}
	
}
