package preprocessing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;

import datastructures.*;
import image_segmentation.Pixel;

import javax.imageio.ImageIO;

/**
 * The Preprocessing class is responsible for preparing images before they undergo
 * segmentation. It provides functionality to load images from a directory, inspect
 * their dimensions, resize them to a standard size (256x256), and convert them
 * from RGB to grayscale.
 */
public class Preprocessing
{
  
   
   /**
    * Default constructor
    */
   public Preprocessing()
	{

	   
   }
	//#####################Functions################################


	/**
	 * This method takes in an file and returns a list of buffered Images
	 * @param file The name of the file that contains the images that the user inserted 
	 * @return 
	 * @throws IOException 
	 */
	public BufferedImage readImages(String file) throws IOException
	{
	    File folder = new File(file);

	    File[] array = folder.listFiles((dir, name) ->
	            name.endsWith(".jpg") ||
	            name.endsWith(".png") ||
	            name.endsWith(".jpeg"));
       if(array==null || array.length==0)
       {
    	   throw  new IOException("No Images Found");
       }
	   

	    for (File eachfile : array)
	    {
	        BufferedImage image = null;

	        try {
	            image = ImageIO.read(eachfile);
	        } catch (Exception e) {
	            
	            continue;
	        }

	        if (image == null) {
	            continue;
	        }

	        // force conversion:
	        BufferedImage rgbImage = new BufferedImage(
	                image.getWidth(),
	                image.getHeight(),
	                BufferedImage.TYPE_INT_RGB
	        );

	        Graphics2D g = rgbImage.createGraphics();
	        g.drawImage(image, 0, 0, null);
	        g.dispose();
	        return rgbImage;//return first picture.
	        
	    }
		throw new IOException("No valid image found");
	 
	 
	}
		


	/**
	 * Method to resize the image as 256x256 to ensure consistency in images 
	 * @param image Buffered image to be resized
	 */
	public  BufferedImage resizeImage(BufferedImage image)
	{
	
			BufferedImage resized = BilinearInterpolation(image);
			return resized;
		
			
	}
	
   /**
    * Method returns a 2D array of the pixel class that represents the images 
    * @param image Image being converted to a 2D 
    * @return
    */
   
   public Pixel[][] ImageAs2DPixel(BufferedImage image)
   {
       int height = image.getHeight();
       int width = image.getWidth();

       Pixel[][] PixelImage = new Pixel[height][width];

       ColorModel cm = image.getColorModel();

       for (int r = 0; r < height; r++) {
           for (int c = 0; c < width; c++) {

               int pixel = image.getRGB(c, r); 

               int red   = cm.getRed(pixel);
               int green = cm.getGreen(pixel);
               int blue  = cm.getBlue(pixel);

			   Pixel pixelObj = new Pixel(r, c, new int[]{red, green, blue});
               PixelImage[r][c] = pixelObj;
           }
       }

       return PixelImage;
   }

 //###################Helper Function########################
   /**
    * This method performs Bilinear Interpolation on a bufferedImage-This is to change the size of the image to be a 256x256
    * @param img The image that will be resized
    * @return The resized image 
    */
   private  BufferedImage BilinearInterpolation(BufferedImage img)
   {
   	//create a new image
   	BufferedImage newimageBufferedImage=new BufferedImage(256, 256,BufferedImage.TYPE_INT_RGB);
   	//now let us resize it .
   	Graphics2D gimGraphics=newimageBufferedImage.createGraphics();
		gimGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		gimGraphics.drawImage(img,0,0,256,256,null);
		gimGraphics.dispose();
		return newimageBufferedImage;
   	
   }
 
  
  
}
