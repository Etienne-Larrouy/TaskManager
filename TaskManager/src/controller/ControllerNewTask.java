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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Tache;
import model.Utilisateur;
import server.Server;

public class ControllerNewTask implements Initializable {
	private Server s;


	@FXML
	private Button NewTask_createTask;

	

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

		Utilisateur owner = new Utilisateur("Roger RABBIT");
		Utilisateur performer = new Utilisateur("Clément CHOLLET");
		Tache t1 = new Tache("Titre", "Description", owner, performer, "En Cours", "12/02/2016");
		s.addTache(t1);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		s = Server.getInstance();
	}

}
