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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.Task;

public class ControllerTask implements Initializable{
	
	@FXML
	private TextArea Task_description;
	
	@FXML
	private Label Task_author;
	
	@FXML
	private Label Task_creationDate;
	
	@FXML
	private Label Task_priority;
	
	@FXML
	private Label Task_state;
	
	@FXML
	private Label Task_deadline;
	
	@FXML
	private Label Task_Title;
	
	@FXML
	private Label Task_performer;
	
	Task currentTask;
	
	public ControllerTask(Task t){
		this.currentTask = t;
	}
	
	@FXML
	public void editTask(ActionEvent event) throws IOException {
		Stage stage = null;
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/TaskEdit.fxml"));

		stage = (Stage) Task_description.getScene().getWindow();
		
		ControllerEditTask controller = new ControllerEditTask(currentTask, stage);
		// Set it in the FXMLLoader
		loader.setController(controller);


		root = (Parent) loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//Init value of labels
		Task_description.setText(currentTask.getDescription());
		Task_author.setText(currentTask.getOwner().getUsername());
		Task_creationDate.setText(currentTask.getCreationDate());
		Task_priority.setText(currentTask.getPriority());
		Task_state.setText(currentTask.getState());
		Task_deadline.setText(currentTask.getDeadline());
		Task_performer.setText(currentTask.getPerformer().getUsername());
		Task_Title.setText(currentTask.getTitle());
		
	}

}