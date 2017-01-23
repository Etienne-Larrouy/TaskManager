package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import server.Client;

public class Controller implements Initializable {

	private Client s;
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
	public void handleConnect(ActionEvent event) {
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
				// Create MessageDigest instance for MD5

				if (Client.getInstance().connect(TaskManager_username.getText(), pw)) {
					Stage stage = null;
					Parent root = null;
					stage = (Stage) TaskManager_connexion.getScene().getWindow();
					root = FXMLLoader.load(getClass().getResource("../view/App.fxml"));

					Scene scene = new Scene(root);

					stage.setScene(scene);
					stage.show();
				}
				else{
					statusbar.setText("Wrong username or password");
				}

			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}

		}
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

	private Element getUser(Document doc, String username) {

		// get all user nodes
		NodeList users = doc.getElementsByTagName("user");
		int nbUsers = users.getLength();

		for (int i = 0; i < nbUsers; i++) {
			Element user = (Element) users.item(i);

			// return user if username match
			if (user.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
				return user;
			}
		}

		return null;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		s = Client.getInstance();
	}
}