package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Task;
import server.Server;

public class ControllerTask implements Initializable{
	private Server s;
	
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
	private Label Task_performer;
	
	Task currentTask;
	
	public ControllerTask(Task t){
		this.currentTask = t;
	}
	
	@FXML
	public void editTask(ActionEvent event) throws IOException {
		// TODO open task editor
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Task_description.setText(currentTask.getDescription());
		s = Server.getInstance();
	}

}
