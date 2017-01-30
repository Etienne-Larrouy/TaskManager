package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Client;

public class ControllerRegister {
	@FXML
	private Text Register_statusbar;

	@FXML
	private PasswordField Register_repeatPassword;

	@FXML
	private PasswordField Register_password;

	@FXML
	private TextField Register_username;

	@FXML
	private Button Register_register;

	@FXML
	public void handleKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER){
			this.handleSignUp(new ActionEvent());
		}
	}


	@FXML
	public void handleSignUp(ActionEvent event) {

		try {
			// Write user and password to xml
			String message;
			if (Register_username.getText().isEmpty()) {
				Register_statusbar.setText("Error : Username is empty");
			}
			// Password empty
			else if (Register_repeatPassword.getText().isEmpty()) {
				Register_statusbar.setText("Error : Password empty");
			}
			// Passwords don't match
			else if (!Register_repeatPassword.getText().equals(Register_password.getText())) {
				Register_statusbar.setText("Error : Passwords don't match");
			} else {
				if ((message = Client.getInstance().signUp(Register_username.getText(), Register_password.getText()))
						.equals("registered")) {

					Stage stage = null;
					Parent root = null;
					stage = (Stage) Register_register.getScene().getWindow();
					root = FXMLLoader.load(getClass().getResource("../view/TaskManager.fxml"));
					Scene scene = new Scene(root);
					stage.setScene(scene);
					stage.show();

				} else {
					Register_statusbar.setText("Error : " + message);
				}
			}
		} catch (IOException | NullPointerException e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Server Error");
			alert.setHeaderText("No server found");
			alert.setContentText(
					"Cannot connect, server seems to be unavailable.\nTry to start the server before to try to connect or register.");

			alert.showAndWait();
		}
	}

	@FXML
	public void handleReturn(MouseEvent event) throws IOException {
		Stage stage = null;
		Parent root = null;
		stage = (Stage) Register_register.getScene().getWindow();
		root = FXMLLoader.load(getClass().getResource("../view/TaskManager.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}
