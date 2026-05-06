package Transformer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import datastructures.ArrayList;
import datastructures.Dictionary;
import datastructures.DoublyLinkedList;
import datastructures.IEntry;
import image_segmentation.Edge;
import image_segmentation.Graph;
import image_segmentation.SuperPixel;

public class GraphSummariser {

	Graph graph;
	private DoublyLinkedList<SuperPixel> segments;
	private ArrayList<Edge> edges;
	int numNodes;
	int numEdges;
	double avgDegree;
	int minDegree;
	int maxDegree;
	double[] stdRGB;
	double[] avgRGB;
	Dictionary<Integer, Integer> degrees;
	double avgTexture;
	double avgEdgeWeight;
	double minEdgeWeight;
	double maxEdgeWeight;
	double stdtexture;
	int target;

	/**
	 * Constructor that takes in a graph object
	 * 
	 * @param graph - The graph object to summarise
	 */
	public GraphSummariser(Graph graph) {
		this.graph = graph;
		this.segments = graph.getSegments();
		this.edges = graph.getEdges();
	}

	// just for testing
	public GraphSummariser(int numNodes, int numEdges, double avgDegree, int minDegree, int maxDegree, double stdR,
			double stdG, double stdB, double avgR, double avgG, double avgB, double avgTexture, double avgEdgeWeight,
			double minEdgeWeight, double maxEdgeWeight, double stdtexture, int target) {
		this.numNodes = numNodes;
		this.numEdges = numEdges;
		this.avgDegree = avgDegree;
		this.minDegree = minDegree;
		this.maxDegree = maxDegree;

		this.stdRGB = new double[3];
		this.stdRGB[0] = stdR;
		this.stdRGB[1] = stdG;
		this.stdRGB[2] = stdB;

		this.avgRGB = new double[3];
		this.avgRGB[0] = avgR;
		this.avgRGB[1] = avgG;
		this.avgRGB[2] = avgB;

		this.avgTexture = avgTexture;
		this.avgEdgeWeight = avgEdgeWeight;
		this.minEdgeWeight = minEdgeWeight;
		this.maxEdgeWeight = maxEdgeWeight;
		this.stdtexture = stdtexture;

		this.target = target;
	}

	// just for testing
	public static ArrayList<GraphSummariser> readFromCSV(String filename) {
		ArrayList<GraphSummariser> data = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			String line;

			// skip header
			br.readLine();

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");

				GraphSummariser gs = new GraphSummariser(

						Integer.parseInt(values[0]), Integer.parseInt(values[1]), Double.parseDouble(values[2]),
						Integer.parseInt(values[3]), Integer.parseInt(values[4]),

						Double.parseDouble(values[5]), Double.parseDouble(values[6]), Double.parseDouble(values[7]),

						Double.parseDouble(values[8]), Double.parseDouble(values[9]), Double.parseDouble(values[10]),

						Double.parseDouble(values[11]), Double.parseDouble(values[12]), Double.parseDouble(values[13]),
						Double.parseDouble(values[14]), Double.parseDouble(values[15]),

						Integer.parseInt(values[16]));

				data.addLast(gs);
			}

			br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return data;
	}

	/**
	 * This method write the graph summary to csv file
	 */
	public void generateCSV() {
		System.out.println("Calculating values");
		calcValues();
		System.out.println("Calculated values");

		File file = new File("SVM_data.csv");

		try (PrintWriter txtout = new PrintWriter(new FileOutputStream(file, true))) {
			if (!file.exists() || file.length() == 0) {
				txtout.println(
						"numNodes,numEdges,avgDegree,minDegree,maxDegree,stdR, stdG,stdB,avgR,avgG,avgB,avgTexture,avgEdgeWeight,minEdgeWeight,maxEdgeWeight,stdtexture");
			}

			txtout.println(numNodes + "," + numEdges + "," + avgDegree + "," + minDegree + "," + maxDegree + ","
					+ stdRGB[0] + "," + stdRGB[1] + "," + stdRGB[2] + "," + avgRGB[0] + "," + avgRGB[1] + ","
					+ avgRGB[2] + "," + avgTexture + "," + avgEdgeWeight + "," + minEdgeWeight + "," + maxEdgeWeight
					+ "," + stdtexture);

		} catch (FileNotFoundException fnfex) {
			fnfex.printStackTrace();
		}
	}

	/**
	 * This method calls the methods to calculate all required values for the graph
	 * summary
	 */
	private void calcValues() {
		calcNumNodes();
		calcNumEdges();
		System.out.println("Generating degrees");
		generateDegrees();
		System.out.println("Generating degrees");
		calcAvgDegree();
		calcMaxMinDegree();
		calcAvgRGB();
		calcStdRGB();
		calcAvgTexture();
		calcAvgMinMaxWeight();
		calcStdTexture();
	}

	/**
	 * This method gets the number of nodes for the graph
	 */
	private void calcNumNodes() {
		numNodes = segments.getSize();
	}

	/**
	 * This method gets the number of edges for the graph
	 */
	private void calcNumEdges() {
		numEdges = edges.size();
	}

	/**
	 * This method calculates the average number of degrees for the graph
	 */
	private void calcAvgDegree() {
		//avgDegree = (2.0 * numEdges) / numNodes;
		
		int sum =0;
		for(SuperPixel pixel: segments)
		{
			sum += degrees.find(pixel.getRoot()).getValue();
		}
		avgDegree = (double)sum/numNodes;
	}

	/**
	 * This method populates the degree dictionary with number of degrees for each
	 * superPixel in the graph
	 */
	private void generateDegrees() {

		this.degrees = new Dictionary<>(numEdges);
		// make all the degrees equal to 0
		for (SuperPixel pixel : segments) {
			degrees.insert(pixel.getRoot(), 0);
		}

		// count the degrees
		for (Edge edge : edges) {
			int[] vertices = edge.getVertices();

			int id1 = vertices[0];
			int id2 = vertices[1];

			IEntry<Integer, Integer> num1 = degrees.find(id1);
			IEntry<Integer, Integer> num2 = degrees.find(id2);
			
			if (num1 != null)
				degrees.replace(id1, num1.getValue() + 1);
			if (num2 != null)
				degrees.replace(id2, num2.getValue() + 1);

		}

	}

	/**
	 * This method calculates the min and max degree for a node for the entire graph
	 */
	private void calcMaxMinDegree() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (SuperPixel pixel : segments) {
			int current = degrees.find(pixel.getRoot()).getValue();

			System.out.println(pixel.getRoot() + " degree = " + current);

			min = Math.min(min, current);
			max = Math.max(max, current);
		}

		minDegree = min;
		maxDegree = max;

	}

	/**
	 * This method calculates the avg RGB of the graph
	 */
	private void calcAvgRGB() {
		double[] avg = new double[3];

		// calculating avg RGB
		double r = 0;
		double g = 0;
		double b = 0;
		for (SuperPixel pixel : segments) {
			r += pixel.getMeanRGB()[0];
			g += pixel.getMeanRGB()[1];
			b += pixel.getMeanRGB()[2];
		}

		avg[0] = r / numNodes;
		avg[1] = g / numNodes;
		avg[2] = b / numNodes;

		avgRGB = avg;

	}

	/**
	 * This method calculates the std of the RGB for the graph
	 */
	private void calcStdRGB() {
		double[] std = new double[3];
		double[] var = new double[3];

		// Calculating variance

		for (SuperPixel pixel : segments) {
			double rgb[] = pixel.getMeanRGB();

			var[0] += Math.pow(rgb[0] - avgRGB[0], 2);
			var[1] += Math.pow(rgb[1] - avgRGB[1], 2);
			var[2] += Math.pow(rgb[2] - avgRGB[2], 2);
		}

		// calculating std
		std[0] = Math.sqrt(var[0] / numNodes);
		std[1] = Math.sqrt(var[1] / numNodes);
		std[2] = Math.sqrt(var[2] / numNodes);

		stdRGB = std;
	}

	/**
	 * This method calcuates the avg texture per node of the graph
	 */
	private void calcAvgTexture() {
		int sum = 0;

		for (SuperPixel pixel : segments) {
			sum += pixel.getAvgGrayScale();
		}

		double avg = (double) sum / numNodes;

		avgTexture = avg;
	}

	/**
	 * This method calculates the average, minimum and maximum weight of all the
	 * edges in the graph
	 */
	private void calcAvgMinMaxWeight() {
		double min = 0;
		double max = 0;
		double sum = 0;
		for (Edge edge : edges) {
			double current = edge.getWeight();
			sum += current;
			min = Math.min(current, min);
			max = Math.max(current, max);
		}

		avgEdgeWeight = sum / numEdges;
		minEdgeWeight = min;
		maxEdgeWeight = max;
	}

	/**
	 * This method calculates the std in regards to texture of the nodes in the
	 * graph
	 */
	private void calcStdTexture() {
		// calculating variance
		double var = 0;

		for (SuperPixel pixel : segments) {
			var += Math.pow(pixel.getAvgGrayScale() - avgTexture, 2);
		}

		// calculating std

		double std = Math.sqrt(var / numNodes);

		stdtexture = std;

	}

	public int getTarget() {
		return target;
	}

	// method to get the value of a certain attribute
	public double getAttributeValue(String attribute) {
		switch (attribute) {
		case "numNodes":
			return numNodes;
		case "numEdges":
			return numEdges;
		case "avgDegree":
			return avgDegree;
		case "minDegree":
			return minDegree;
		case "maxDegree":
			return maxDegree;
		case "stdR":
			return stdRGB[0];
		case "stdG":
			return stdRGB[1];
		case "stdB":
			return stdRGB[2];
		case "avgR":
			return avgRGB[0];
		case "avgG":
			return avgRGB[1];
		case "avgB":
			return avgRGB[2];
		case "avgTexture":
			return avgTexture;
		case "avgEdgeWeight":
			return avgEdgeWeight;
		case "minEdgeWeight":
			return minEdgeWeight;
		case "maxEdgeWeight":
			return maxEdgeWeight;
		case "stdtexture":
			return stdtexture;
		case "target":
			return target;
		default:
			return -1;
		}

	}

}