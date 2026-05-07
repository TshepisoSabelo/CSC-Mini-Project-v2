package models;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import Transformer.GraphSummariser;
import datastructures.ArrayList;

public class RandomForest implements Serializable{
	
	private ArrayList<DecisionTree> forest;
	private ArrayList<GraphSummariser> data;
	private int[] counts;
	private String[] strClass= {"Blight", "Common Rust", "Gray Leaf Spot", "Healthy"};
	
	/**
	 * This is a constructor that sets the data parameter and intializes the forest, it then trains the forest
	 * @param data - The list of graphSummarisers to train the model on
	 */
	public RandomForest(ArrayList<GraphSummariser> data)
	{
		this.data = data;
		
		forest = new ArrayList<>();
		
		//creating our tree
		for(int i =0; i<101; i++)
		{
			forest.addLast(new DecisionTree(randomDataSampling()));
		}
		trainForest();
	}

	/**
	 * Method that trains all decision trees in the forest
	 */
	private void trainForest() {
		
		for(DecisionTree tree : forest)
		{
			ArrayList<String> attributes = new ArrayList<>();
			
			//copying the array to an arrayList
			
			for(int i =0; i< tree.attributes.length; i++)
			{
				attributes.addLast(tree.attributes[i]);
			}
			
			
			tree.root = tree.buildDecisionTree(tree.graphs, attributes);
		}
		
	}
	
	/**
	 * Method to predict what classification the graph has
	 * @param graph - A graphSummariser object for prediction
	 * @return A string object representing the classification
	 */
	public String predict(GraphSummariser graph)
	{
		//resetting the counts
		counts = new int[4];
		
		//getting the predictions
		for(DecisionTree tree : forest)
		{
			int pred = tree.predict(tree.root, graph);
			counts[pred]++;
		}
		
		//checking which prediction is the highest
		int maxCount=counts[0];
		int maxPred= 0;
		for(int i =0; i< 4;i++)
		{
			if(counts[i]> maxCount)
			{
				maxCount = counts[i];
				maxPred = i;
			}
		}
		
		return strClass[maxPred];
	}
	
	/**
	 * This method randomly assigns randomly selected GraphSummarisers from the data list and assigns them to a new List
	 * @return An arrayList of Graph Summariser objects
	 */
	private ArrayList<GraphSummariser> randomDataSampling()
	{
		ArrayList<GraphSummariser> sample = new ArrayList<GraphSummariser>();
		Random rand = new Random();
		for(int i =0; i< data.size(); i++)
		{
			
			sample.addLast(data.get(rand.nextInt(0, data.size())));
		}
		
		return sample;
	}
	
	public void saveModel(String filename)
	{
	    try (ObjectOutputStream out =
	            new ObjectOutputStream(
	                    new FileOutputStream(filename)))
	    {

	        out.writeObject(this);

	        System.out.println("Model saved successfully! :D");
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}

}
