package image_segmentation;

public class Pixel {
	private int ID;
    private int x;
    private int y;
    private int[] RGB;

    /**
     * Constructs a new Pixel.
     * Initializes the pixel with default values (0, 0) for coordinates and (0, 0, 0) for RGB.
     */
    public Pixel() {
        this.x = 0;
        this.y = 0;
        this.ID = 0;
        this.RGB = new int[]{0, 0, 0};
    }
    /**
     * Constructs a new Pixel.
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @param rgb the RGB component array for this pixel
     */
    public Pixel(int x, int y, int[] rgb) {
        this.x = x;
        this.y = y;
        this.ID = y*255 + x;
        this.RGB = rgb;
    }

    /**
     * Updates the RGB component array for this pixel.
     *
     * @param rgb the new RGB component array
     */
    public void setRGB(int[] rgb) {
        this.RGB = rgb;
    }

    /**
     * Updates the pixel coordinates.
     *
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void setCoodinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the current pixel coordinates.
     *
     * @return an array containing {x, y}
     */
    public int[] getCoordinate() {
        return new int[]{this.x, this.y};
    }

    /**
     * Returns the RGB component array for this pixel.
     *
     * @return the RGB values stored for this pixel
     */
    public int[] getRGB() {
        return this.RGB;
    }
    
    public int getID() {
    	return this.ID;
    }
}
