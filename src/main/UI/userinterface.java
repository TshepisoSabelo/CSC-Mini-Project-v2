package UI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class userinterface extends Application
{
	GridPane gpane = new GridPane();
	private List<File> selectedFiles = new ArrayList<>();
	
	private int rows = 0;
	private int cols = 0;
	
	
	public void start(Stage stage) 
	{	
		gpane.setHgap(10);
		gpane.setVgap(10);
		gpane.setPadding(new Insets(10));
		
		
		Label intro = new Label("Welcome o the maize disease detction system");
		Label uploadPrompt = new Label("Please upload images of your maize leaves");
		
		Button btnUpload = new Button("Upload Images");
		//Activate functionality behind the button
		btnUpload.setOnAction(e -> selectImages(stage));
		
		Button btnConfirm = new Button("Submit Images");
		//Activate the functionality behind the button
		btnConfirm.setOnAction(e -> submitImages());

		HBox hButtons = new HBox(10, btnUpload, btnConfirm);
		hButtons.setPadding(new Insets(10));
		
		GridPane root = new GridPane();
		root.setVgap(10);
		
		root.add(intro, 0, 1);
		root.add(uploadPrompt, 0,  2);
		
		root.add(hButtons,  1,  3);
		root.add(gpane, 2, 4);
		
		
		Scene scene = new Scene(root, 500, 500);
		stage.setTitle("Maize Disease Dietection");
		stage.setScene(scene);
		stage.show();
	}
	
	//upload function
	public void selectImages(Stage stage)
	{
		FileChooser filechooser = new FileChooser();
		filechooser.setTitle("Select your files");;
	
		filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
		
		List<File> files = filechooser.showOpenMultipleDialog(stage);
		
		if(files != null)
		{
			for(File f : files)
			{
				selectedFiles.add(f);
				previewImages(f);
			}
		}
	}
	
	//Image preview method
	public void previewImages(File file)
	{
		Image image = new Image(file.toURI().toString());
		ImageView imgPreview = new ImageView(image);
		
		imgPreview.setFitHeight(150);
		imgPreview.setFitWidth(150);
		imgPreview.setPreserveRatio(true);
		
		gpane.add(imgPreview, cols, rows);
		
		cols++;
		if(cols == 4)
		{
			cols = 0;
			rows++;
		}
	}
	
	public void submitImages() {
	    try {
	        Path destination = Paths.get("images");

	        // Create folder only if it does not exist
	        if (!Files.exists(destination)) {
	            Files.createDirectories(destination);
	        }

	        for (File f : selectedFiles) {
	            Path source = f.toPath();
	            Path target = destination.resolve(f.getName());

	            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

	            System.out.println("Saved: " + f.getName());
	        }

	        gpane.getChildren().clear();
	        selectedFiles.clear();

	        cols = 0;
	        rows = 0;

	        System.out.println("Upload complete!");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static void main(String[] args) 
	{
		launch(args);
	}

}
