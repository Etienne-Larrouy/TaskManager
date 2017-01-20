package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Task;
import server.Server;

public class ControllerApp implements Initializable{
	private Server s;

	@FXML
	private FlowPane App_flowPane;

	@FXML
	private Label PreviewTask_Title;

	@FXML
	private Label PreviewTask_Author;

	@FXML
	private Label PreviewTask_State;

	@FXML
	private Label PreviewTask_DeadLine;

	@FXML
	private Label PreviewTask_Performer;

	@FXML
	private Label PreviewTask_CreationDate;

	@FXML
	private ImageView App_add;

	@FXML
	public void createNewTask(MouseEvent event) throws IOException {
		if (event.getSource() == App_add) {
			Stage stage = null;
			Parent root = null;
			stage = (Stage) App_add.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../view/NewTask.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		s = Server.getInstance();

		//Add existing tasks
		for (Task t : s.getObservableListTasks()) {
			if(t.getOwner().equals(s.getUserSession()) || t.getPerformer().equals(s.getUserSession())){
				try {

					GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

					// Set text to labels
					((Label) tache.getChildren().get(0)).setText(t.getTitle());
					((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
					((Label) tache.getChildren().get(2)).setText(t.getState());
					((Label) tache.getChildren().get(3)).setText(t.getDeadline());
					((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());
					((Label) tache.getChildren().get(5)).setText(t.getCreationDate());

					// Bind label to model
					t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());
					t.getOwner().getUsernameProperty().bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
					t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
					t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
					t.getPerformer().getUsernameProperty().bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
					t.getCreationDateProperty().bindBidirectional(((Label) tache.getChildren().get(5)).textProperty());

					// Add task to FlowPanel
					App_flowPane.getChildren().add(tache);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		s.getObservableListTasks().addListener((ListChangeListener<Task>) change -> {
			if(App_flowPane!=null) {
				//TODO
				//				while (change.next()) {
				//					// for (Note remitem : change.getRemoved()) {
				//					// System.out.println("suppr");
				//					// }
				//					for (Task t : change.getAddedSubList()) {
				//						
				//					}
				//
				//				}
			}
		});
	}
}
