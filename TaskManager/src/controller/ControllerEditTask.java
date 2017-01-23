

package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.Task;
import model.User;
import server.Client;

public class ControllerEditTask implements Initializable {
	private Client s = Client.getInstance();

	@FXML
	private TextArea EditTask_description;

	@FXML
	private TextField EditTask_Title;

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
	Stage stage;

	public ControllerEditTask(Task t, Stage s) {
		this.currentTask = t;
		this.stage = s;
	}

	@FXML
	public void saveChanges(ActionEvent event){
		
		this.currentTask.setDeadLine(EditTask_deadline.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		this.currentTask.setDescription(EditTask_description.getText());
		this.currentTask.setPerformer(s.getObservableListUsers().get(EditTask_performer.getSelectionModel().getSelectedIndex()));
		this.currentTask.setPriority(EditTask_priority.getSelectionModel().getSelectedItem());
		if(EditTask_state_in_progress.isSelected())
			this.currentTask.setState(EditTask_state_in_progress.getText());
		else if(EditTask_state_suspended.isSelected())
			this.currentTask.setState(EditTask_state_suspended.getText());
		else if(EditTask_state_finished.isSelected())
			this.currentTask.setState(EditTask_state_finished.getText());
		this.currentTask.setTitle(EditTask_Title.getText());

		try {

			Parent root = null;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Task.fxml"));

			ControllerTask controller = new ControllerTask(currentTask);
			// Set it in the FXMLLoader
			loader.setController(controller);

			root = (Parent) loader.load();

			Scene scene = new Scene(root);
			this.stage.setScene(scene);
			this.stage.show();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		s = Client.getInstance();

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

		EditTask_Title.setText(currentTask.getTitle());


	}

}

