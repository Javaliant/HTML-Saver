/* Author: Luigi Vincent   
For the sake of practice and learning.
*/

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class HTMLSave extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Parent root = null;
    	try {
    		root = FXMLLoader.load(getClass().getResource("HTML_Save.fxml"));
    	} catch (IOException ioe) {
    		System.err.println("Could not load FXML");
    	}

		Scene scene = new Scene(root, Color.TRANSPARENT);
		scene.getStylesheets().add("html-save.css");

		stage.setScene(scene);
		stage.setTitle("HTML Saver");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		stage.show();
	}
}