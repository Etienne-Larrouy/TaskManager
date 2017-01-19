package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Task;
import model.User;
import server.Server;

public class ControllerNewTask implements Initializable {
	private Server s;


	@FXML
	private Button NewTask_createTask;

	@FXML
	private AnchorPane NewTask_Pane;

	@FXML
	public void createTask(ActionEvent event) throws IOException {

		if (event.getSource() == NewTask_createTask) {
			Stage stage = null;
			Parent root = null;
			stage = (Stage) NewTask_createTask.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

		User owner = new User("Roger RABBIT");
		User performer = new User("Clément CHOLLET");
		Task t1 = new Task("Titre", "Description", owner, performer, "En Cours", "12/02/2016");
		s.addTask(t1);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		s = Server.getInstance();
		
		//Add users to choices
		for(User u : s.getObservableListUsers()){
			((ComboBox<String>)NewTask_Pane.getChildren().get(9)).getItems().add(u.getUsername());
		}
		
		
	}

}
