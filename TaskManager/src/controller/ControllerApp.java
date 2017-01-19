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
import model.Tache;
import server.Server;

public class ControllerApp implements Initializable{
	private Server s;
	
	@FXML
	private FlowPane App_flowPane;
	

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
		for (Tache t : s.getObservableList()) {
			try {

				GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

				// Set text to labels
				((Label) tache.getChildren().get(0)).setText(t.getTitle());
				((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
				((Label) tache.getChildren().get(2)).setText(t.getState());
				((Label) tache.getChildren().get(3)).setText(t.getDeadline());
				((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());

				// Bind label to model
				t.getOwner().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
				t.getPerformer().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
				t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
				t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
				t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());

				// Add task to FlowPanel
				App_flowPane.getChildren().add(tache);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		s.getObservableList().addListener((ListChangeListener<Tache>) change -> {
			if(App_flowPane!=null) {
				while (change.next()) {
					// for (Note remitem : change.getRemoved()) {
					// System.out.println("suppr");
					// }
					for (Tache t : change.getAddedSubList()) {
						try {

							GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

							// Set text to labels
							((Label) tache.getChildren().get(0)).setText(t.getTitle());
							((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
							((Label) tache.getChildren().get(2)).setText(t.getState());
							((Label) tache.getChildren().get(3)).setText(t.getDeadline());
							((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());

							// Bind label to model
							t.getOwner().getUsernameProperty()
							.bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
							t.getPerformer().getUsernameProperty()
							.bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
							t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
							t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
							t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());

							// Add task to FlowPanel
							App_flowPane.getChildren().add(tache);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}
			}
		});
	}
}
