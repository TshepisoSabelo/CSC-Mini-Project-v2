package image_segmentation;
import datastructures.ArrayList;

/**
 * Represents a super-pixel (segment) in image segmentation.
 * 
 * <p>A SuperPixel is a cluster of connected pixels that share similar
 * characteristics. This class manages a group of pixels, maintains their
 * mean RGB values, and tracks internal differences for segmentation algorithms.</p>
 * 
 * @author CSC03A Mini Project Team
 * @version 1.0
 */
public class SuperPixel{
	/** The ID of the root pixel in this segment */
	private int root;
	
	/** Mean RGB values for all pixels in this segment */
    private double[] meanRGB;
    
    /** Number of pixels in this segment */
    private int size;
    
    /** Maximum internal difference within this segment */
    private double internalDiff;
    
    /** List of all pixels in this segment */
    private ArrayList<Pixel> pixels;

    /**
     * Constructs a new SuperPixel initialized with a root pixel.
     * 
     * <p>The root pixel becomes the first member of this segment, and its
     * RGB values are used to initialize the mean RGB.</p>
     * 
     * @param root the initial pixel for this segment
     */
    public SuperPixel(Pixel root){
        this.meanRGB = new double[]{root.getRGB()[0], root.getRGB()[1], root.getRGB()[2]};
        this.pixels = new ArrayList<>();
        this.pixels.addlast(root);
        this.size++;
        this.root = root.getID();
    }

    /**
     * Adds a single pixel to this segment and updates the mean RGB.
     * 
     * @param p the pixel to add to this segment
     */
    public void addPixel(Pixel p){
        this.pixels.addlast(p);
        calcMeanRGB(p);
        this.size++;
    }
    
    /**
     * Adds multiple pixels to this segment.
     * 
     * @param newPixels an ArrayList of pixels to add to this segment
     */
    public void addPixels(ArrayList<Pixel> newPixels) {
    	for(Pixel p: newPixels) {
    		addPixel(p);
    	}
    }
    
    /**
     * Returns the mean RGB values of all pixels in this segment.
     * 
     * @return array containing mean [R, G, B] values
     */
    public double[] getMeanRGB() {
    	return meanRGB;
    }
    
    /**
     * Calculates the internal difference (maximum edge weight) within this segment.
     * 
     * <p>Searches for the first edge connected to this segment and uses its
     * weight as the internal difference threshold.</p>
     * 
     * @param edges the sorted list of edges in the graph
     * @return the internal difference threshold for this segment
     */
    public double internalDiff(ArrayList<Edge> edges) {
    	//look for the first appearance of the first pixel in this segment
    	//because the edges are sorted by weight
    	edges.reverse();
    	for(Edge e: edges) {
    		int[] vertices = e.getVertices();
    		if(pixels.get(0).getID() == vertices[0] || pixels.get(0).getID() == vertices[1]) {
    			internalDiff = e.getWeight();
    		}
    	}
    	return internalDiff;
    }
    
    /**
     * Returns the number of pixels in this segment.
     * 
     * @return the size of this segment
     */
    public int size() {
    	return this.size;
    }
    
    /**
     * Returns the internal difference threshold of this segment.
     * 
     * @return the internal difference value
     */
    public double getInternalDiff() {
    	return this.internalDiff;
    }
    
    /**
     * Returns the root pixel of this segment.
     * 
     * @return the first pixel added to this segment
     */
    public Pixel rootPixel() {
    	return pixels.get(0);
    }
    
    /**
     * Returns all pixels contained in this segment.
     * 
     * @return an ArrayList of all pixels in this segment
     */
    public ArrayList<Pixel> getPixels(){
    	return this.pixels;
    }
    
    /**
     * Checks if a pixel with the given ID exists in this segment.
     * 
     * @param s the ID of the pixel to search for
     * @return true if the pixel exists in this segment, false otherwise
     */
    public boolean find(int s) {
    	for(Pixel p: pixels) {
    		if(s == p.getID()) return true;
    	}
    	return false;
    }
    
    /**
     * Sets the root pixel ID for this segment.
     * 
     * @param s the ID to set as the root
     */
    public void setRoot(int s) {
    	this.root = s;
    }
    
    /**
     * Returns the root pixel ID of this segment.
     * 
     * @return the ID of the root pixel
     */
    public int getRoot() {
    	return this.root;
    }
    
    /**
     * Helper function to calculate the updated mean RGB values.
     * 
     * <p>Updates mean RGB by averaging the current mean with the new pixel's RGB.</p>
     * 
     * @param p the pixel whose RGB values should be incorporated
     */
    private void calcMeanRGB(Pixel p)
    {
    	// Update mean RGB values
        for(int i = 0; i < 3; i++){
            meanRGB[i] = (meanRGB[i] + p.getRGB()[i]) / 2;
        }
    }
}