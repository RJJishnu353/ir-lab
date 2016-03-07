package ir_lib;

//This class will act as a container for storing precision and average precision information

public class MetricInfoContainer {

	private Double recall;
	private Double precision;
	private Double averagePrecision;
	
	
	public MetricInfoContainer(Double recall, Double precision, Double averagePrecision){
		
		this.recall = recall;
		this.precision = precision;
		this.averagePrecision = averagePrecision;
	
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
	
	
}
