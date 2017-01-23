package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

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

public class ControllerRegister implements Initializable {
	private Client s;
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		s = Client.getInstance();

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
				if((message = Client.getInstance().signUp(Register_username.getText(), Register_password.getText()))== "OK"){
				
					if (event.getSource() == Register_register) {
						Stage stage = null;
						Parent root = null;
						stage = (Stage) Register_register.getScene().getWindow();
						root = FXMLLoader.load(getClass().getResource("../view/TaskManager.fxml"));
						Scene scene = new Scene(root);
						stage.setScene(scene);
						stage.show();
					}
				}
				else{
					Register_statusbar.setText("Error : "+message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
