import preprocessing.Preprocessing;
import image_segmentation.*;
import datastructures.ArrayList;
import Transformer.GraphSummariser;

import java.awt.image.BufferedImage;

public class Test {

    public static void main(String[] args) {

        try {
            // STEP 1: Load images
            Preprocessing prep = new Preprocessing();
            ArrayList<BufferedImage> images = prep.readImages("images");

            System.out.println("Image was read! :D");
            // Take first image
            BufferedImage img = images.get(0);

            img = prep.resizeImage(img); //resize the image to 256x256

            // STEP 2: Convert to Pixel[][]
            Pixel[][] pixelImage = prep.ImageAs2DPixel(img);
            System.out.println("Converted to Pixel :D");

            for(int i = 0; i < pixelImage.length; i++) {
                for(int j = 0; j < pixelImage[0].length; j++) {
                    Pixel p = pixelImage[i][j];
                    //System.out.println("Pixel ID: " + p.getID() + " | RGB: (" + p.getRGB()[0] + ", " + p.getRGB()[1] + ", " + p.getRGB()[2] + ")");
                }
            }

            int width = img.getWidth();
            int height = img.getHeight();
            
            System.out.println("Width: " + width + "   | Height: " + height);

            // STEP 3: Create graph
            Graph graph = new Graph(pixelImage, width, height);
            System.out.println("Graph was created :D");

            // STEP 4: Run segmentation
            System.out.println("creating segments...");
            graph.createSegments();
            System.out.println("Segments created :D");
            System.out.println(graph.getSegments().getSize() + " segments were created! :D");
            
            System.out.println("creating edges...");
            graph.createEdges();
            System.out.println("Edges created :D");
            System.out.println(graph.getEdges().size() + " edges were created! :D");

            System.out.println("Image segmentation in process...");
            graph.segmentation();

            System.out.println("Summarising the graph...");
            GraphSummariser summariser = new GraphSummariser(graph);

            System.out.println("Generating CSV...");
            summariser.generateCSV();

            System.out.println("CSV generated! :D");

            // STEP 5: Print superpixels
            System.out.println("===== SUPERPIXELS =====");

            for (SuperPixel sp : graph.getSegments()) {
                int id = sp.getRoot();
                double[] rgb = sp.getMeanRGB();

                System.out.println( "ID: " + id + " | Mean RGB: (" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + ")");
            }

            // STEP 6: Print connections between superpixels
            System.out.println("\n===== SUPERPIXEL CONNECTIONS =====");

            // for (Edge e : graph.getEdges()) {
            //     int[] v = e.getVertices();

            //     SuperPixel s1 = null;
            //     SuperPixel s2 = null;

            //     // find which superpixel each vertex belongs to
            //     for (SuperPixel sp : graph.getSegments()) {
            //         if (sp.find(v[0])) s1 = sp;
            //         if (sp.find(v[1])) s2 = sp;
            //     }

            //     // only print connections between DIFFERENT superpixels
            //     if (s1 != null && s2 != null && s1 != s2) {
            //         System.out.println(
            //             "SuperPixel " + s1.getRoot() +
            //             " <--> " + s2.getRoot()
            //         );
            //     }
            // }
            System.out.println("Final number of Superpixels: " + graph.getSegments().getSize());
            System.out.println("Final number of Edges: " + graph.getEdges().size());
            System.out.println("Test Completed Successfully! :D");

        } catch (Exception e) {
            System.err.print("Test Failed : (error details below)\n");
            e.printStackTrace();
        }
    }
}