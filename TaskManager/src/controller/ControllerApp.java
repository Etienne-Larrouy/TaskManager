package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Task;
import server.Client;

public class ControllerApp implements Initializable {

	@FXML
	private FlowPane App_flowPane;

	@FXML
	private TextField App_search;

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
	private MenuButton App_Options;

	@FXML
	private MenuItem App_Disconnection;

	@FXML
	private MenuItem App_About;

	@FXML
	private Button App_printButton;

	@FXML
	private GridPane App_main;

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
		// Load tasks
		Client.getInstance().getTasks();

		EventHandler<ActionEvent> action = menuItemAction();

		App_Disconnection = new MenuItem("Disconnection");
		App_About = new MenuItem("About");

		App_Options.getItems().clear();
		App_Options.getItems().addAll(App_About, App_Disconnection);

		App_Disconnection.setOnAction(action);
		App_About.setOnAction(action);

		App_printButton.setText("Print");
		App_printButton.setAlignment(Pos.CENTER);

		// Add existing tasks
		for (Task t : Client.getInstance().getObservableListTasks()) {

			try {

				GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

				// Set text to labels
				((Label) tache.getChildren().get(0)).setText(t.getTitle());
				((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
				((Label) tache.getChildren().get(2)).setText(t.getState());
				((Label) tache.getChildren().get(3)).setText(t.getDeadline());
				((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());
				((Label) tache.getChildren().get(5)).setText(t.getCreationDate());
				((Text) tache.getChildren().get(6)).setText(Integer.toString(t.getId()));

				// Bind label to model
				t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());
				t.getOwner().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
				t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
				t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
				t.getPerformer().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
				t.getCreationDateProperty().bindBidirectional(((Label) tache.getChildren().get(5)).textProperty());

				// Add task to FlowPanel
				App_flowPane.getChildren().add(tache);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		Client.getInstance().getObservableListTasks().addListener((ListChangeListener<Task>) change -> {
			if (App_flowPane != null) {
				App_flowPane.getChildren().remove(0, App_flowPane.getChildren().size());
				System.out.println("remove");
			
				// Add existing tasks
				for (Task t : Client.getInstance().getObservableListTasks()) {
					System.out.println(t.getId());
					try {

						GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

						// Set text to labels
						((Label) tache.getChildren().get(0)).setText(t.getTitle());
						((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
						((Label) tache.getChildren().get(2)).setText(t.getState());
						((Label) tache.getChildren().get(3)).setText(t.getDeadline());
						((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());
						((Label) tache.getChildren().get(5)).setText(t.getCreationDate());
						((Text) tache.getChildren().get(6)).setText(Integer.toString(t.getId()));

						// Bind label to model
						t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());
						t.getOwner().getUsernameProperty()
						.bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
						t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
						t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
						t.getPerformer().getUsernameProperty()
						.bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
						t.getCreationDateProperty().bindBidirectional(((Label) tache.getChildren().get(5)).textProperty());

						// Add task to FlowPanel
						App_flowPane.getChildren().add(tache);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

	}
	

	private EventHandler<ActionEvent> menuItemAction() {
		return new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				MenuItem mItem = (MenuItem) event.getSource();
				switch (mItem.getText()) {
				case "Disconnection":
					try {
						handleDisconnect();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "About":
					System.out.println("About");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("About");
					alert.setHeaderText(null);
					alert.setContentText("Programmation r�seau - ENSIM 2016/2017");
					alert.showAndWait();
					break;
				}
			}
		};
	}

	public void handleDisconnect() throws IOException {
		Client.getInstance().disconnect();

		Stage stage = null;
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/TaskManager.fxml"));

		stage = (Stage) App_flowPane.getScene().getWindow();

		root = (Parent) loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	@FXML
	public void handleRefresh(ActionEvent event) throws IOException{
		Stage stage = null;
		Parent root = null;
		stage = (Stage) App_flowPane.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
	}
	@FXML
	public void handleSearch(ActionEvent event) throws IOException {
		App_flowPane.getChildren().remove(0, App_flowPane.getChildren().size());

	
		for (Task t : Client.getInstance().getObservableListTasks()) {
			if(t.getState().contains(App_search.getText()) || t.getPriority().contains(App_search.getText()) || t.getPerformer().getUsername().contains(App_search.getText()) || t.getDescription().contains(App_search.getText()) || t.getTitle().contains(App_search.getText()) || App_search.getText().isEmpty()){
				GridPane tache = FXMLLoader.load(getClass().getResource("../view/PreviewTask.fxml"));

				// Set text to labels
				((Label) tache.getChildren().get(0)).setText(t.getTitle());
				((Label) tache.getChildren().get(1)).setText(t.getOwner().getUsername());
				((Label) tache.getChildren().get(2)).setText(t.getState());
				((Label) tache.getChildren().get(3)).setText(t.getDeadline());
				((Label) tache.getChildren().get(4)).setText(t.getPerformer().getUsername());
				((Label) tache.getChildren().get(5)).setText(t.getCreationDate());
				((Text) tache.getChildren().get(6)).setText(Integer.toString(t.getId()));

				// Bind label to model
				t.getTitleProperty().bindBidirectional(((Label) tache.getChildren().get(0)).textProperty());
				t.getOwner().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(1)).textProperty());
				t.getStateProperty().bindBidirectional(((Label) tache.getChildren().get(2)).textProperty());
				t.getDeadLineProperty().bindBidirectional(((Label) tache.getChildren().get(3)).textProperty());
				t.getPerformer().getUsernameProperty()
				.bindBidirectional(((Label) tache.getChildren().get(4)).textProperty());
				t.getCreationDateProperty().bindBidirectional(((Label) tache.getChildren().get(5)).textProperty());

				// Add task to FlowPanel
				App_flowPane.getChildren().add(tache);
			}
			
		}

	}

	@FXML
	public void handlePrint(ActionEvent event) throws IOException {
		final PrinterJob printerJob = PrinterJob.createPrinterJob();
		// Affichage de la boite de dialog de configuration de l'impression.
		if (printerJob.showPrintDialog(App_printButton.getScene().getWindow())) {
			//			final JobSettings settings = printerJob.getJobSettings();
			//			final PageLayout pageLayout = settings.getPageLayout();
			//			final double pageWidth = pageLayout.getPrintableWidth();
			//			final double pageHeight = pageLayout.getPrintableHeight();
			if (printerJob.printPage(App_printButton.getParent())) {
				System.out.println("test2");
				printerJob.endJob();
			}
		}
	}
}
