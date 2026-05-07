package models;

import java.io.Serializable;

public class Split implements Serializable {
	
	String attribute;
	double threshold;
	
	public Split(String attribute, double threshold)
	{
		this.attribute= attribute;
		this.threshold = threshold;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

}
