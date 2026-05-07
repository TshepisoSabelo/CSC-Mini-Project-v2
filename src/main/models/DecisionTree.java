package models;

import java.io.Serializable;

import Transformer.GraphSummariser;
import datastructures.ArrayList;

public class DecisionTree implements Serializable {

	ArrayList<GraphSummariser> graphs;
	String[] attributes = { "numNodes", "numEdges", "avgDegree", "minDegree", "maxDegree", "stdR", "stdG", "stdB",
			"avgR", "avgG", "avgB", "avgTexture", "avgEdgeWeight", "minEdgeWeight", "maxEdgeWeight", "stdtexture" };
	DecisionTreeNode root;

	//main method just to test the tree
	public static void main(String[] args) {
		ArrayList<GraphSummariser> data = GraphSummariser.readFromCSV("maize_graph_dataset_random_targets.csv");

		System.out.println("Loaded: " + data.size());

		// split train/test
		ArrayList<GraphSummariser> train = new ArrayList<>();

		ArrayList<GraphSummariser> test = new ArrayList<>();

		for (int i = 0; i < data.size(); i++) {
			if (i < data.size() * 0.8) {
				train.addLast(data.get(i));
			} else {
				test.addLast(data.get(i));
			}
		}

		// create tree
		DecisionTree tree = new DecisionTree(train);

		// attributes
		ArrayList<String> attrs = new ArrayList<>();

		for (String a : tree.attributes) {
			attrs.addLast(a);
		}

		// build
		DecisionTreeNode root = tree.buildDecisionTree(train, attrs);

		// print tree
		System.out.println(tree.toString(root, 0));

		// test accuracy
		int correct = 0;

		for (GraphSummariser g : test) {
			int prediction = tree.predict(root, g);

			int actual = g.getTarget();

			System.out.println("Predicted: " + prediction + " Actual: " + actual);

			if (prediction == actual) {
				correct++;
			}
		}

		double accuracy = ((double) correct / test.size()) * 100;

		System.out.println("Accuracy: " + accuracy + "%");
	}

	/**
	 * This constructor sets the ArrayList of GraphSummarisers
	 * @param graphs
	 */
	DecisionTree(ArrayList<GraphSummariser> graphs) {
		this.graphs = graphs;
	}

	/**
	 * This method calculates the entropy using the array of counts
	 * @param counts - an integer array representing the counts per class
	 * @return a double representing the entropy
	 */
	private double entropy(int[] counts) {

		double total = 0;

		for (int c : counts) {
			total += c;
		}

		double entropy = 0;

		for (int c : counts) {
			if (c == 0)
				continue;

			double ratio = c / total;

			entropy -= ratio * Math.log(ratio) / Math.log(2);
		}
		return entropy;
	}

	/**
	 * This method calculates the information gain for a specified attribute
	 * @param data - An arrayList of graphSumarisers to calculate the Information Gain for
	 * @param attribute - a String representing the attribute to calculate Information Gain for
	 * @param threshold - a double representing the specified threshold for separating data
	 * @return a double representing information gain
	 */
	private double calculateInformationGain(ArrayList<GraphSummariser> data, String attribute, double threshold) {
		int[] counts = getCounts(data);

		int sum = data.size();

		if (sum == 0)
			return 0;

		// getting the main entropy
		double initEntropy = entropy(counts);

		ArrayList<GraphSummariser> left = new ArrayList<>();
		ArrayList<GraphSummariser> right = new ArrayList<>();

		for (GraphSummariser graph : data) {
			if (graph.getAttributeValue(attribute) <= threshold) {
				left.addLast(graph);
			} else
				right.addLast(graph);
		}

		double remainder = 0;

		// calculating the remainder
		if (!left.isEmpty()) {
			remainder += ((double) left.size() / data.size()) * entropy(getCounts(left));
		}

		// calculating the remainder
		if (!right.isEmpty()) {
			remainder += ((double) right.size() / data.size()) * entropy(getCounts(right));
		}

		// return the gain
		return initEntropy - remainder;
	}

	/**
	 * This is a private method that calculates the counts per class
	 * @param data - an arraylist of GraphSummarisers to do the class counting on
	 * @return - an array of integers representing counts per class
	 */
	private int[] getCounts(ArrayList<GraphSummariser> data) {
		int[] counts = new int[4];

		for (GraphSummariser graph : data) {
			if (graph.getTarget() == 0)
				counts[0]++;
			else if (graph.getTarget() == 1)
				counts[1]++;
			else if (graph.getTarget() == 2)
				counts[2]++;
			else
				counts[3]++;
		}

		return counts;
	}


	/**
	 * This is a method that gets the best attribute and threshold to split on
	 * @param data - this is the arraylist of GraphSummarisers being split
	 * @param attribute - The attribute being split
	 * @return a Split object containing the best Attribute and the best threshold
	 */
	private Split getBestAttribute(ArrayList<GraphSummariser> data, ArrayList<String> attribute) {
		String bestAttribute = null;
		double bestGain = -1;
		double bestThreshold = 0;

		// going through every attributes
		for (String a : attribute) {

			ArrayList<Double> thresholds = getThresholds(data, a);

			// going through different thresholds
			for (Double threshold : thresholds) {

				double gain = calculateInformationGain(data, a, threshold);
				if (gain > bestGain) {
					bestGain = gain;
					bestAttribute = a;
					bestThreshold = threshold;
				}

			}

		}
		return new Split(bestAttribute, bestThreshold);

	}

	/**
	 * Method to get different possible thresholds for the tree
	 * 
	 * @param data      - The arrayList of graphSummarisers to get thresholds for
	 * @param attribute - The specific attribute we are getting the threshold for
	 * @return an ArrayList of Doubles representing possible thresholds
	 */
	private ArrayList<Double> getThresholds(ArrayList<GraphSummariser> data, String attribute) {
		ArrayList<Double> t = new ArrayList<>();
		for (GraphSummariser gs : data) {
			t.addLast(gs.getAttributeValue(attribute));
		}
		return t;
	}

	
	/**
	 * This method removes am attribute from the arraylist
	 * @param attributes - The list of attributes
	 * @param attribute - the singular attribute being removed
	 * @return An arrayList of Strings representing a deep copy of the attributes array without the one attribute
	 */
	private ArrayList<String> removeAttribute(ArrayList<String> attributes, String attribute) {
		ArrayList<String> newArr = new ArrayList<>();

		for (String a : attributes) {
			if (!a.equals(attribute)) {
				newArr.addLast(a);
			}
		}

		return newArr;
	}

	/**
	 * This method seperates a given array list of graphSummarisers according to a specific threshold based on a certain attribute
	 * @param data - the List of graphSummarisers being separated
	 * @param attribute - a String representing the attribute to consider
	 * @param threshold - a double representing the threshold to split on
	 * @return An arrayList of GraphSummarisers with attributes smaller than or equal to the specified threshold
	 */
	private ArrayList<GraphSummariser> getLeftSubSet(ArrayList<GraphSummariser> data, String attribute,
			double threshold) {
		ArrayList<GraphSummariser> leftSubset = new ArrayList<>();

		for (GraphSummariser graph : data) {
			if (graph.getAttributeValue(attribute) <= threshold)
				leftSubset.addLast(graph);
		}

		return leftSubset;
	}

	/**
	 * This method seperates a given array list of graphSummarisers according to a specific threshold based on a certain attribute
	 * @param data - the List of graphSummarisers being separated
	 * @param attribute - a String representing the attribute to consider
	 * @param threshold - a double representing the threshold to split on
	 * @return An arrayList of GraphSummarisers with attributes greater than the specified threshold
	 */
	private ArrayList<GraphSummariser> getRightSubSet(ArrayList<GraphSummariser> data, String attribute,
			double threshold) {
		ArrayList<GraphSummariser> rightSubset = new ArrayList<>();

		for (GraphSummariser graph : data) {
			if (graph.getAttributeValue(attribute) > threshold)
				rightSubset.addLast(graph);
		}

		return rightSubset;
	}

	/**
	 * This method gets the majority class from an array list of graphsummarisers
	 * @param data - an array list of graphsummarisers to get the highest count from
	 * @return an int representing the majority class
	 */
	private int getMajorityClass(ArrayList<GraphSummariser> data) {

		int[] counts = getCounts(data);

		// assume the first class is the majority class
		int maxCount = 0;
		int majorityClass = 0;

		for (int i = 0; i < 4; i++) {
			int currentCount = counts[i];

			if (currentCount > maxCount) {
				maxCount = currentCount;
				majorityClass = i;
			}
		}

		return majorityClass;
	}

	/**
	 * method to check if a graphSummariser array have the same final classification
	 * @param data
	 * @return an int representing the class if all objects in the arraylist are the same otherwise -1 is returned
	 */
	private int checkClassification(ArrayList<GraphSummariser> data) {
		if (data.isEmpty())
			return -1;
		else {
			double firstClass = data.get(0).getAttributeValue("target");

			for (GraphSummariser graph : data) {
				if (graph.getAttributeValue("target") != firstClass)
					return -1;
			}

			return (int) firstClass;
		}
	}

	// building the decision tree via recursion
	public DecisionTreeNode buildDecisionTree(ArrayList<GraphSummariser> data, ArrayList<String> attributes) {
		if (data.isEmpty())
			return null;

		// check if the mushrooms have the same edible classification
		int sameClass = checkClassification(data);
		if (sameClass != -1)
			return new DecisionTreeNode(sameClass);

		// return the majority classification if no attributes are left
		if (attributes.isEmpty())
			return new DecisionTreeNode(getMajorityClass(data));

		// get the best attribute to split
		Split best = getBestAttribute(data, attributes);
		String best_attribute = best.getAttribute();
		double best_threshold = best.getThreshold();
		DecisionTreeNode node = new DecisionTreeNode(best_attribute, best_threshold);

		// remove the attribute from the array
		attributes = removeAttribute(attributes, best_attribute);

		// making a subtree for the left side of the threshold
		ArrayList<GraphSummariser> sub1 = getLeftSubSet(data, best_attribute, best_threshold);

		if (!sub1.isEmpty()) {
			node.child1 = buildDecisionTree(sub1, attributes);
		} else {
			node.child1 = new DecisionTreeNode(getMajorityClass(data));
		}

		// making a subtree for the right side of the threshold
		ArrayList<GraphSummariser> sub2 = getRightSubSet(data, best_attribute, best_threshold);

		if (!sub2.isEmpty()) {
			node.child2 = buildDecisionTree(sub2, attributes);
		} else {
			node.child2 = new DecisionTreeNode(getMajorityClass(data));
		}

		return node;

	}

	// method that the classification of the graph
	public int predict(DecisionTreeNode node, GraphSummariser graph) {
		if (node.isLeaf()) {
			return node.classification;
		}

		if (graph.getAttributeValue(node.attribute) <= node.threshold) {
			return predict(node.child1, graph);
		} else {
			return predict(node.child2, graph);
		}
	}

	// converting the tree to String
	public String toString(DecisionTreeNode node, int indent) {
		String str = " ".repeat(indent);

		String classification = null;
		if (node.classification == 0) {
			classification = "Blight";
		} else if (node.classification == 1) {
			classification = "Common Rust";

		} else if (node.classification == 2) {
			classification = "Gray Leaf Spot";
		} else {
			classification = "Healthy";
		}
		if (node.isLeaf())
			return str + "{\n" + str + "  \"class\": \"" + (classification) + "\"\n" + str + "}";

		StringBuilder sb = new StringBuilder();

		sb.append(str).append("{\n");
		sb.append(str).append("  \"attribute\": \"").append(node.attribute).append("\",\n");
		sb.append(str).append("  \"children\": {\n");
		sb.append(str).append("    \"Under Threshold\": ").append(toString(node.child1, indent + 6)).append(",\n");
		sb.append(str).append("    \"Above Threshold\": ").append(toString(node.child2, indent + 6)).append("\n");
		sb.append(str).append("  }\n");
		sb.append(str).append("}");
		return sb.toString();
	}

}
