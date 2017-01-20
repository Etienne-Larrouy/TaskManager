package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.layout.FlowPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Server;

public class ControllerPreviewTask implements Initializable{
	private Server s;
	
	@FXML
	private Text TaskPreview_id;
	
	@FXML
	public void openTask(MouseEvent event) throws IOException {
		Stage stage = null;
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Task.fxml"));
		
	
		ControllerTask controller = new ControllerTask(s.getObservableListTasks().get(Integer.parseInt(TaskPreview_id.getText())));
	        // Set it in the FXMLLoader
		loader.setController(controller);

		stage = (Stage) ((GridPane)event.getSource()).getScene().getWindow();
		
		root = (Parent)loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		s = Server.getInstance();
	}

}
