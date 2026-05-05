package preprocessing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;

import datastructures.*;
import image_segmentation.Pixel;

import javax.imageio.ImageIO;

/**
 * The Preprocessing class is responsible for preparing images before they undergo
 * segmentation. It provides functionality to load images from a directory, inspect
 * their dimensions, resize them to a standard size (256x256), and convert them
 * from RGB to grayscale.
 */
public class Preprocessing<T>
{
   private  ArrayList<BufferedImage> images;
   
   /**
    * Default constructor
    */
   public Preprocessing()
	{

	   this.images=new ArrayList<BufferedImage>();
   }

   
   /**
    * Overloaded constructor
    * @param images
    */
   //constructor :
	public Preprocessing(ArrayList<BufferedImage> images)
	{
 
	    this.images = images;
    }


	//#####################Functions################################

	public  void  getsize(ArrayList<BufferedImage> list) 
	{
		for(int i=0;i<list.size();i++)
		{
			BufferedImage imgBufferedImage=list.get(i);
			System.out.println("Width:"+imgBufferedImage.getWidth()+"Height:"+imgBufferedImage.getHeight());
		}
	}
	/**
	 * This method takes in an file and returns a list of buffered Images
	 * @param file The name of the file that contains the images that the user inserted 
	 * @return 
	 * @throws IOException 
	 */
	public  ArrayList<BufferedImage> readImages(String file) throws IOException
	{ 
		//Read in all the images and store into  a list .

		//create an array of files:
		File[] array=new File(file).listFiles((dir, name) -> name.endsWith(".jpg")|| name.endsWith(".png") || name.endsWith(".jpeg"));
		ArrayList<BufferedImage> listImagesList=new ArrayList<BufferedImage>();
		int count=0;
		for(File eachfile: array)
		{
			//image loading 
			BufferedImage image=ImageIO.read(eachfile);	
			//add each image into the list
			listImagesList.add(count, image);
			count++;
		}

		return listImagesList;	
		
	}
	/**
	 * Method to resize the image as 256x256 to ensure consistency in images 
	 * @param image Buffered image to be resized
	 */
	public  BufferedImage resizeImage(BufferedImage image)
	{
		//image must be a 256x256 :
		
		//check the size of the image first:
		if(checksize(image))
		{
			return image;
		}
		else 
		{
			BufferedImage resized = BilinearInterpolation(image);
			return resized;
		}
			
	}
	
   /**
    * This method performs Bilinear Interpolation on a bufferedImage-This is to change the size of the image to be a 256x256
    * @param img The image that will be resized
    * @return The resized image 
    */
   public  BufferedImage BilinearInterpolation(BufferedImage img)
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
   /**
    * Converts a list of RGB images to their grayscale equivalents
    * @param The ArrayList of RGB BufferedImage objects to be converted
    * @return A new  ArrayList<BufferedImage> containing the grayscale versions of the input images
    */
   public  ArrayList<BufferedImage> ConvertToGrayScale(ArrayList<BufferedImage> list)
   {
   //create a copy 
		ArrayList<BufferedImage> copy=new ArrayList<BufferedImage>();
		for(int i=0;i<list.size();i++)
		{
			BufferedImage imgBufferedImage=new BufferedImage(list.get(i).getWidth(),list.get(i).getHeight(),list.get(i).getType());
			//imgBufferedImage.getGraphics().drawImage(imgBufferedImage, 0, 0, null);
			copy.add(i,imgBufferedImage);
		}
    	//read each image and convert to gray scale :
		int count=0;
    	for(BufferedImage image: list)
    	{
    		//perform a copy:
    		BufferedImage imgBufferedImage=new BufferedImage(image.getWidth(),image.getHeight(),image.getType());
    		
    		for(int r=0;r<image.getWidth();r++)
    		{
    			for(int c=0;c<image.getHeight();c++)
    			{
    				int RGB=image.getRGB(r,c);
    				int red=(RGB>>16) & 0xFF;
    				int green=(RGB>>8) & 0xFF;
    				int blue=RGB &0xFF;
    				int brightness=(int) (0.299*red + 0.587*green + 0.114*blue);
    				//shift values back:
    				brightness=(255<<24) |(brightness<<16) |( brightness<<8) |brightness;
    				imgBufferedImage.setRGB(r,c,brightness);
    			}
    	    }
    		copy.add(count++,imgBufferedImage);
    	}
        //return the copy
    	return copy;
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

               int pixel = image.getRGB(c, r); // ⚠️ note (x, y) = (col, row)

               int red   = cm.getRed(pixel);
               int green = cm.getGreen(pixel);
               int blue  = cm.getBlue(pixel);

			   Pixel pixelObj = new Pixel(r, c, new int[]{red, green, blue});
               PixelImage[r][c] = pixelObj;
           }
       }

       return PixelImage;
   }
   //##################Helper Functions#######################
   
   /**
	 *Helper function: Checks whether a given image has dimensions of exactly 256x256 pixels
	 * @param The BufferedImage to be checked
	 * @return Boolean value indicating if the image is a 256x256 or not 
	 */
  private  boolean checksize(BufferedImage image)
  {
  	if(image.getWidth()<=256 && image.getHeight()<=256)
  	{
  		return true ;
  	}
  	return false;
  }
  
  
}
