package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import server.Client;

public class Controller {

	@FXML
	private TextField TaskManager_username;

	@FXML
	private PasswordField TaskManager_password;

	@FXML
	private Text statusbar;

	@FXML
	private Button TaskManager_connexion;

	@FXML
	private Button TaskManager_register;

	@FXML
	public void handleConnectKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			this.connect();
		}
	}

	private void connect() {
		if (TaskManager_username.getText().isEmpty())
			statusbar.setText("Error username is empty");
		else if (TaskManager_password.getText().isEmpty())
			statusbar.setText("Error password is empty");
		else {
			String pw = TaskManager_password.getText();
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				// Add password bytes to digest
				md.update(pw.getBytes());
				// Get the hash's bytes
				byte[] bytes = md.digest();
				// This bytes[] has bytes in decimal format;
				// Convert it to hexadecimal format
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < bytes.length; i++) {
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}
				// Get complete hashed password in hex format
				pw = sb.toString();
				// Crypt password
				// Create MessageDigest
				if (Client.getInstance().connect(TaskManager_username.getText(), pw)) {
					Stage stage = null;
					Parent root = null;
					stage = (Stage) TaskManager_connexion.getScene().getWindow();
					root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));

					Scene scene = new Scene(root);

					stage.setScene(scene);
					stage.show();
				} else {
					statusbar.setText("Wrong username, password or user already connected elsewhere");
				}

			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}

		}
	}

	@FXML
	public void handleConnect(ActionEvent event) {
		this.connect();
	}

	@FXML
	public void handleRegister(ActionEvent event) throws IOException {
		if (event.getSource() == TaskManager_register) {
			Stage stage = null;
			Parent root = null;
			stage = (Stage) TaskManager_register.getScene().getWindow();
			root = FXMLLoader.load(getClass().getResource("../view/Register.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}

	}
}