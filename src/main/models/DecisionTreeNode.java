package models;

import java.io.Serializable;

public class DecisionTreeNode implements Serializable {
	
	String attribute;
	int classification;
	double threshold;
	DecisionTreeNode child1=null;
	DecisionTreeNode child2=null;
	
	//Constructor
	DecisionTreeNode(String attribute,double threshold)
	{
		this.attribute = attribute;
		this.threshold = threshold;
	}
	
	DecisionTreeNode(int classification)
	{
		this.classification = classification;
	}
	
	//method to check if the node is a leaf node
	boolean isLeaf()
	{
		return (attribute==null);
	}


}
