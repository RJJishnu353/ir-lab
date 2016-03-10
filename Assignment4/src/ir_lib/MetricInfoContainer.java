package ir_lib;

//This class will act as a container for storing precision and average precision information

public class MetricInfoContainer {

	private Double recall;
	private Double precision;
	private Double averagePrecision;
	private Double MRR;
	
	
	public MetricInfoContainer(Double recall, Double precision, Double averagePrecision, Double MRR){
		
		this.recall = recall;
		this.precision = precision;
		this.averagePrecision = averagePrecision;
		this.MRR = MRR;
		
	}
	
	public Double getRecall() {
	
		return recall;
	
	}

	public void setRecall(Double recall) {
	
		this.recall = recall;
	
	}

	public Double getPrecision() {
	
		return precision;
	
	}

	public void setPrecision(Double precision) {
	
		this.precision = precision;
	
	}

	public Double getAveragePrecision() {
	
		return averagePrecision;
	
	}

	public void setAveragePrecision(Double averagePrecision) {
	
		this.averagePrecision = averagePrecision;
	
	}

	public Double getMRR() {
		return MRR;
	}

	public void setMRR(Double MRR) {
		this.MRR = MRR;
	}
	
	
	
}
