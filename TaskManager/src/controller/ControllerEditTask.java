

package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import model.Task;
import model.User;
import server.Server;

public class ControllerEditTask implements Initializable {
	private Server s;

	@FXML
	private TextArea EditTask_description;

	@FXML
	private Label EditTask_author;

	@FXML
	private Label EditTask_creationDate;

	@FXML
	private ComboBox<String> EditTask_priority;

	@FXML
	private RadioButton EditTask_state_in_progress;

	@FXML
	private RadioButton EditTask_state_suspended;

	@FXML
	private RadioButton EditTask_state_finished;

	@FXML
	private DatePicker EditTask_deadline;

	@FXML
	private ComboBox<String> EditTask_performer;

	Task currentTask;

	public ControllerEditTask(Task t) {
		this.currentTask = t;
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		s = Server.getInstance();
		
		//Radio button state
		final ToggleGroup group = new ToggleGroup();
		
		EditTask_state_in_progress.setText("In Progress");
		EditTask_state_in_progress.setToggleGroup(group);

		EditTask_state_suspended.setText("Suspended");
		EditTask_state_suspended.setToggleGroup(group);
		 
		EditTask_state_finished.setText("Finished");
		EditTask_state_finished.setToggleGroup(group);
		
		switch(currentTask.getState()){
			case "In Progress":
				EditTask_state_in_progress.setSelected(true);
				break;
			case "Suspended":
				EditTask_state_suspended.setSelected(true);
				break;
			case "Finished":
				EditTask_state_finished.setSelected(true);
				break;
		}
		
		//Add users to choices
		for(User u : s.getObservableListUsers()){
			EditTask_performer.getItems().add(u.getUsername());
		}
	
		EditTask_priority.getItems().addAll("Low", "Normal", "High", "Urgent");
		
		//Selected choice from task
		EditTask_performer.getSelectionModel().select(currentTask.getPerformer().getUsername());
		EditTask_priority.getSelectionModel().select(currentTask.getPriority());
		
		// Init value of labels
		EditTask_description.setText(currentTask.getDescription());
		EditTask_author.setText(currentTask.getOwner().getUsername());
		EditTask_creationDate.setText(currentTask.getCreationDate());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		dtf = dtf.withLocale(Locale.FRENCH);
		LocalDate date = LocalDate.parse(currentTask.getDeadline(), dtf);
		EditTask_deadline.setValue(date);


	}

}

