package controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javax.xml.transform.TransformerException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Task;
import model.User;
import server.Client;

public class ControllerNewTask implements Initializable {
	private Client c;

	@FXML
	private Button NewTask_createTask;

	@FXML
	private AnchorPane NewTask_Pane;

	@FXML
	private ComboBox<String> NewTask_performer;

	@FXML
	private TextField NewTask_name;

	@FXML
	private TextArea NewTask_description;

	@FXML
	private ComboBox<String> NewTask_priority;

	@FXML
	private DatePicker NewTask_deadline;

	@FXML
	private Text NewTask_statusbar;

	@FXML
	public void createTask(ActionEvent event) throws IOException {

		if (event.getSource() == NewTask_createTask) {
			if (NewTask_name.getText().isEmpty()) {
				NewTask_statusbar.setText("Title empty, choose a title");
			} else if (NewTask_deadline.getValue() == null) {
				NewTask_statusbar.setText("Deadline is empty, choose a deadLine");
			} else {
				// Create new Task and send it to the server
				Task t1 = new Task(NewTask_name.getText(), NewTask_description.getText(), c.getUserSession(),
						c.getObservableListUsers().get(NewTask_performer.getSelectionModel().getSelectedIndex()),
						NewTask_priority.getSelectionModel().getSelectedItem(),
						NewTask_deadline.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
						c.getObservableListTasks().size());
				try {
					c.addTask(t1);
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Change scene
				Stage stage = null;
				Parent root = null;
				stage = (Stage) NewTask_createTask.getScene().getWindow();
				root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));
				Scene scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		c = Client.getInstance();

		c.getUsers();
		// Add users to choices
		for (User u : c.getObservableListUsers()) {
			NewTask_performer.getItems().add(u.getUsername());
		}

		NewTask_priority.getItems().addAll("Low", "Normal", "High", "Urgent");

		// Select first choice
		NewTask_performer.getSelectionModel().selectFirst();
		NewTask_priority.getSelectionModel().selectFirst();
	}

	@FXML
	public void handleReturn(MouseEvent event) throws IOException {
		Stage stage = null;
		Parent root = null;
		stage = (Stage) NewTask_createTask.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
