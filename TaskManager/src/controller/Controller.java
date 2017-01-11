package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller {
	@FXML
	private TextField username;

	@FXML
	private TextField password;

	@FXML
	private Text statusbar;

	public void handleConnect(ActionEvent event){
		if(username.getText().isEmpty())
			statusbar.setText("Error username is empty");
		else if(password.getText().isEmpty())
			statusbar.setText("Error password is empty");
		else
			statusbar.setText("Connected");

	}

	public void handleRegister(ActionEvent event){

		try {

	
			 FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
             Parent root1 = (Parent) fxmlLoader.load();
             Stage stage = new Stage();
             stage.setScene(new Scene(root1));
   
             stage.show();
	
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
