package image_segmentation;
import datastructures.UnionFind;
import datastructures.ArrayList;
import datastructures.DoublyLinkedList;
import datastructures.Node;

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
		for(int i = 0; i< width; i++) {
			for(int j = 0; j<height; j++) {
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
		            int id2 = image[x+1][y].getID();
		            double w = diff(image[x][y], image[x+1][y]);
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
	 * <p>Edges are processed in the order defined by {@link #createEdges()};
	 * in the current implementation the list is reversed after sorting,
	 * so higher-weight edges are visited first.</p>
	 *
	 * <p>The merge decision compares the boundary weight between two segments
	 * against a threshold derived from each segment's internal difference.</p>
	 */
	public void segmentation() {
		UnionFind uf = new UnionFind(segments.getSize());
		
		for(Edge e: edges) {
			int[] vertices = e.getVertices();
			SuperPixel S1 = null;
			SuperPixel S2 = null;
			Node<SuperPixel> S1_node = null;
			Node<SuperPixel> S2_node = null;
			
			// Iterate through all segments to find those containing the edge vertices
			for(SuperPixel S : segments) {
				if(S != null && S.find(vertices[0])) {
					S1 = S;
				}
				if(S != null && S.find(vertices[1])) {
					S2 = S;
				}
			}
			
			if(S1 != null && S2 != null && shouldMerge(S1, S2)) {
				merge(S1, S2);
				uf.union(vertices[0], vertices[1]);
			}
		}
	}
	
	/**
	 * Merges two segments into a single segment.
	 *
	 * <p>The smaller {@link SuperPixel} is absorbed into the larger one,
	 * and the merged-away segment is removed from the active list.</p>
	 *
	 * @param S1 the first segment
	 * @param S2 the second segment
	 * @param S1_node the node containing S1
	 * @param S2_node the node containing S2
	 */
	public void merge(SuperPixel S1, SuperPixel S2) {
		// merge the smaller superPixel into the bigger superpixel and delete the other node
		if (S1.size() <= S2.size()) {
			S2.addPixels(S1.getPixels());
			Node<SuperPixel> node = new Node<>(S1);
			segments.remove(node);
		} else {
			S1.addPixels(S2.getPixels());
			Node<SuperPixel> node = new Node<>(S2);
			segments.remove(node);
		}
	}

	///////////////////////////helper methods///////////////////////////
	
	/**
	 * Computes the minimum connecting edge weight between two segments.
	 *
	 * @param S1 the first segment
	 * @param S2 the second segment
	 * @return the smallest edge weight linking pixels in the two segments
	 */
	private double segmentDiff(SuperPixel S1, SuperPixel S2) {
		ArrayList<Pixel> S1_pixels = S1.getPixels();
		ArrayList<Pixel> S2_pixels = S2.getPixels();
		ArrayList<Double> weights = new ArrayList<>(); //weights of connected edges between the two segments
		
		for(Pixel s1_pixel: S1_pixels) {
			for(Pixel s2_pixel: S2_pixels) {
				if (isEdge(s1_pixel, s2_pixel) != -1) {
					int index  = isEdge(s1_pixel, s2_pixel);
					weights.addLast(edges.get(index).getWeight());
				}
			}
		}
		
		weights.reverse();
		double minWeight = weights.get(0);
		return minWeight;
	}
	
	/**
	 * Determines whether two segments should be merged.
	 *
	 * @param S1 the first segment
	 * @param S2 the second segment
	 * @return true if the boundary between segments is below the merge threshold
	 */
	private boolean shouldMerge(SuperPixel S1, SuperPixel S2) {
		double mInt = Math.min(S1.getInternalDiff() + threshold(S1), S2.getInternalDiff() + threshold(S2));
		if(segmentDiff(S1, S2) <= mInt) {
			return true;
		}
		return false;
	}
	
	/**
	 * Computes the merge threshold for a segment.
	 *
	 * @param S the segment
	 * @return threshold adjustment based on the segment size
	 */
	private double threshold(SuperPixel S) {
		return (0.2 / S.size());
	}
	
	/**
	 * Returns the index of the edge connecting two pixels, if one exists.
	 *
	 * @param p1 the first pixel
	 * @param p2 the second pixel
	 * @return edge index or -1 when no connecting edge is found
	 */
	private int isEdge(Pixel p1, Pixel p2) {
		for(int i = 0; i< edges.size(); i++) {
			if (edges.get(i).checkEdge(p1.getID(), p2.getID())){
				return i;
			}
		}
		return -1;
	}
	/**
	 * Calculates the Euclidean distance between two pixels based on their
	 * coordinates and RGB color values. This distance is used as the weight
	 * for edges connecting the pixels.
	 * @param p1 the first pixel
	 * @param p2 the second pixel
	 * @return the Euclidean distance between the two pixels
	 */
	private double diff(Pixel p1, Pixel p2) {
		int p1_x = p1.getCoordinate()[0];
		int p2_x = p2.getCoordinate()[0];
		int p1_y = p1.getCoordinate()[1];
		int p2_y = p2.getCoordinate()[1];
		int[] p1_RGB = p1.getRGB();
		int[] p2_RGB = p2.getRGB();
		
		double diff = Math.sqrt((p1_x - p2_x)^2 + 
								(p1_y - p2_y)^2 +
								(p1_RGB[0] - p2_RGB[0])^2 +
								(p1_RGB[1] - p2_RGB[1])^2 +
								(p1_RGB[2] - p2_RGB[2])^2);
		return diff;
	}
}