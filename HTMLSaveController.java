/* Author: Luigi Vincent */
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class HTMLSaveController implements Initializable {
	@FXML
	private Button saveButton;
	@FXML
	private Button openButton;
	@FXML
	private TextField htmlField;
	@FXML
	private TextField filenameField;
	@FXML
	private ProgressBar progressBar;

	private String destinationFile;

	@Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("HTML Saver view successfully loaded.");
    }

    public void handleSaveRequest() {
    	String filename = filenameField.getText();
    	String url = htmlField.getText();
    	if (url.isEmpty()) {
			errorAlert("URL missing", "Please paste in website address");
		} else if (filename.isEmpty()) {
			errorAlert("No filename", "Please enter a file name to save html");
		} else {
			destinationFile = filename + ".html";
			try (Writer destination = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destinationFile), "UTF-8"))) {
				savePageSource(url, destination);
			} catch (IOException ioe) {
				System.err.println("Could not write to " + filename + '.');
			}
		}
    }

    public void handleOpenRequest() {
    	try {
    		Desktop.getDesktop().open(new File(destinationFile));
    	} catch (IOException ioe) {
    		System.err.println(destinationFile + " not found.");
    	}
    }

    private void errorAlert(String message, String infoMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(message);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

	private void savePageSource(String url, Writer destination) throws IOException {
    	Task saveTask = new Task() {
    		@Override
    		protected Void call() throws IOException {
    			updateProgress(0, 100);
    			openButton.setDisable(true);
    			
    			try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
    				progressBar.setCursor(Cursor.WAIT);
    				destination.write(reader.lines().collect(Collectors.joining("\n")));
	    			
				} catch (IOException ioe) {
					throw new UncheckedIOException(ioe);
				} 
				progressBar.setCursor(Cursor.DEFAULT);
				updateProgress(1, 1);
				openButton.setDisable(false);
				return null;
    		}
    	};

    	progressBar.progressProperty().bind(saveTask.progressProperty());
    	Thread savingThread = new Thread(saveTask);
    	savingThread.start();
    	try {
    		savingThread.join();
    	} catch (InterruptedException ie) {
    		ie.printStackTrace();
    	}
	}
}