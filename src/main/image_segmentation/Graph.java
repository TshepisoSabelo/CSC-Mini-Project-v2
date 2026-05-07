package image_segmentation;
import datastructures.UnionFind;
import datastructures.Dictionary;
import datastructures.IEntry;
import java.util.Iterator;
import datastructures.ArrayList;
import datastructures.DoublyLinkedList;

/**
 * Represents an image as a graph structure for segmentation.
 *
 * <p>Each pixel is treated as a node and adjacency edges connect
 * neighboring pixels. This graph is used to merge similar pixels into
 * larger segments based on edge weights.</p>
 */
public class Graph {
	private Pixel[][] image;
	private DoublyLinkedList<SuperPixel> segments;
	private ArrayList<Edge> edges;
	int width;
	int height;
	double K = 100; // tunable constant for threshold function
	
	/**
	 * Constructs a new Graph with the given image grid.
	 * Initializes the graph with the provided pixel grid and dimensions,
	 * @param gridImage the 2D array of pixels representing the image
	 * @param width the width of the image
	 * @param height the height of the image
	 */
	public Graph(Pixel[][] gridImage,int width, int height){
		this.width = width;
		this.height = height;
		image = gridImage;
		segments = new DoublyLinkedList<>();
		edges = new ArrayList<>();
	}
	
 	public DoublyLinkedList<SuperPixel> getSegments() {
 		return segments;
 	}
 	
 	public ArrayList<Edge> getEdges(){
 		return edges;
 	}
	
	/**
	 * Creates initial segments by assigning each pixel to its own region.
	 * This method initializes the segmentation process where each pixel
	 * starts as its own individual segment.
	 */
	public void createSegments() {
		for(int i = 0; i< height; i++) {
			for(int j = 0; j<width; j++) {
				SuperPixel S = new SuperPixel(image[i][j]);
				segments.addLast(S);
			}
		}
	}
	
	/**
	 * Creates edges between adjacent pixels (right and down neighbors).
	 * For each pixel, edges are created to the pixel to its right and below it,
	 * with weights calculated based on the difference between the pixels.
	 *
	 * <p>After all edges are created, they are sorted and reversed so that
	 * higher-weight edges appear first in the list.</p>
	 */
public void createEdges() {
		for(int y = 0; y< height; y++) {
			for(int x = 0; x<width; x++) {
				int id1 = image[y][x].getID();
				
				//Add right node
		        if (x < width - 1) {
					int id2 = image[y][x + 1].getID();
					double w = diff(image[y][x], image[y][x + 1]);
		            Edge edge = new Edge(id1, id2);
		            edge.setWeight(w);
		            edges.addLast(edge);
		        }
		        
		        //Add down node
		        if(y< height - 1) {
		        	int id2 = image[x][y+1].getID();
		            double w = diff(image[x][y], image[x][y+1]);
		            Edge edge = new Edge(id1, id2);
		            edge.setWeight(w);
		            edges.addLast(edge);
		        }
			}
		}
		
		edges.sort();
	}

	
/**
	 * Performs image segmentation by merging adjacent regions.
	 *
	 * <p>Processes edges in weight order, merging adjacent segments if their
	 * boundary weight is below the merge threshold. Continues until either
	 * all possible merges are completed or n segments remain.</p>
	 *
	 * @param n the number of segmentation iterations to perform
	 */
	public void segmentation() {
		UnionFind uf = new UnionFind(width * height);

		int totalPixels = width * height;
		int[] size = new int[totalPixels];
		double[] internalDiff = new double[totalPixels];

		for (int i = 0; i < totalPixels; i++) {
			size[i] = 1;
			internalDiff[i] = 0.0;
		}

		for (Edge e : edges) {
			int[] vertices = e.getVertices();

			int root1 = uf.find(vertices[0]);
			int root2 = uf.find(vertices[1]);

			if (root1 == root2) {
				continue;
			}

			double edgeWeight = e.getWeight();

			double threshold1 = internalDiff[root1] + threshold(size[root1]);
			double threshold2 = internalDiff[root2] + threshold(size[root2]);

			if (edgeWeight <= Math.min(threshold1, threshold2)) {
				uf.union(root1, root2);

				int newRoot = uf.find(root1);
				int oldRoot = newRoot == root1 ? root2 : root1;

				size[newRoot] += size[oldRoot];
				internalDiff[newRoot] = edgeWeight;
			}
		}

		 segments = buildSuperPixels(uf);
		 edges = buildSuperPixelEdges(uf);
	}

	private DoublyLinkedList<SuperPixel> buildSuperPixels(UnionFind uf)
	{
		Dictionary<Integer, SuperPixel> map = new Dictionary<>(0);

		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				Pixel p = image[y][x];
				int root = uf.find(p.getID());

				IEntry<Integer, SuperPixel> entry = map.find(root);

				if(entry == null)
				{
					SuperPixel sp = new SuperPixel(p);
					map.insert(root, sp);
				}
				else
				{
					entry.getValue().addPixel(p);
				}
			}
		}

		DoublyLinkedList<SuperPixel> result = new DoublyLinkedList<>();

		Iterator<IEntry<Integer, SuperPixel>> iterator = map.entries();

		while(iterator.hasNext())
		{
			IEntry<Integer, SuperPixel> entry = iterator.next();
			result.addLast(entry.getValue());
		}

		return result;
	}

	///////////////////////////helper methods///////////////////////////
	/**
	 * Computes the merge threshold for a segment.
	 *
	 * @param S the segment
	 * @return threshold adjustment based on the segment size
	 */
	private double threshold(int size) {
	    return K / size;
	}
	

	private double segmentDiff(SuperPixel S1, SuperPixel S2) {
		double minWeight = Double.POSITIVE_INFINITY;
		for (Edge edge : edges) {
			int[] vertices = edge.getVertices();
			boolean endpointsAcrossSegments =
				(S1.find(vertices[0]) && S2.find(vertices[1])) ||
				(S1.find(vertices[1]) && S2.find(vertices[0]));
			if (endpointsAcrossSegments) {
				minWeight = Math.min(minWeight, edge.getWeight());
			}
		}
		return minWeight == Double.POSITIVE_INFINITY ? Double.MAX_VALUE : minWeight;
	}

	private void merge(SuperPixel S1, SuperPixel S2) {
		// merge the smaller superPixel into the bigger superpixel and delete the other node
		if (S1.size() <= S2.size()) {
			S2.addPixels(S1.getPixels());
			segments.remove(S1);
			System.out.println("Merged segment " + S1.getRoot() + " into segment " + S2.getRoot());
		} else {
			S1.addPixels(S2.getPixels());
			segments.remove(S2);
			System.out.println("Merged segment " + S2.getRoot() + " into segment " + S1.getRoot());
		}
	}
	

	private double diff(Pixel p1, Pixel p2) {
		int[] rgb1 = p1.getRGB();
		int[] rgb2 = p2.getRGB();

		int dr = rgb1[0] - rgb2[0];
		int dg = rgb1[1] - rgb2[1];
		int db = rgb1[2] - rgb2[2];

		double distSq = dr * dr + dg * dg + db * db;

		return distSq / (256.0 * 256.0 * 3); // normalize
	}

	private ArrayList<Edge> buildSuperPixelEdges(UnionFind uf) {
		Dictionary<String, Edge> uniqueEdges = new Dictionary<>(edges.size());

		for (Edge e : edges) {
			int[] vertices = e.getVertices();

			int root1 = uf.find(vertices[0]);
			int root2 = uf.find(vertices[1]);

			if (root1 == root2) {
				continue;
			}

			int a = Math.min(root1, root2);
			int b = Math.max(root1, root2);

			String key = a + "-" + b;

			IEntry<String, Edge> entry = uniqueEdges.find(key);

			if (entry == null) {
				Edge superEdge = new Edge(a, b);
				superEdge.setWeight(e.getWeight());
				uniqueEdges.insert(key, superEdge);
			} else {
				Edge existing = entry.getValue();
				if (e.getWeight() < existing.getWeight()) {
					existing.setWeight(e.getWeight());
					uniqueEdges.replace(key, existing);
				}
			}
		}

		ArrayList<Edge> result = new ArrayList<>();
		Iterator<IEntry<String, Edge>> iterator = uniqueEdges.entries();

		while (iterator.hasNext()) {
			IEntry<String, Edge> entry = iterator.next();
			result.addLast(entry.getValue());
		}
		return result;
	}
}