package ir_lib;

/*
 * 
 * This class is used for storing the parameters of relevance feedback and PRF 
 */

public class Parameters {
	
	
	private double alpha;
	private double beta;
	private double gamma;
	
	public Parameters(double d, double e, double f){
		
		this.alpha = d;
		this.beta = e;
		this.gamma = f;
		
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}
	
	

}
