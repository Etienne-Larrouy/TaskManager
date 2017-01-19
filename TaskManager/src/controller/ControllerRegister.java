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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import server.Server;

public class ControllerRegister implements Initializable{
	private Server s;
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
		 s = Server.getInstance();

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
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("BD/users.xml"));
			// Username empty
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
			} else if (getUser(doc, Register_username.getText()) != null) {
				Register_statusbar.setText("Error : Username already exists");
			} else {
				Register_statusbar.setText("");

				Element rootElement = null;
				NodeList nodelist = doc.getElementsByTagName("users");

				if (nodelist.getLength() >= 0) {
					rootElement = (Element) nodelist.item(0);
				} else {
					rootElement = doc.createElement("users");
					doc.appendChild(rootElement);
				}

				// user elements
				Element user = doc.createElement("user");
				rootElement.appendChild(user);

				// username elements
				Element username = doc.createElement("username");
				username.appendChild(doc.createTextNode(Register_username.getText()));
				user.appendChild(username);

				// Crypt password
				// Create MessageDigest instance for MD5
				String pw = Register_password.getText();
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
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

				// password elements
				Element password = doc.createElement("password");
				password.appendChild(doc.createTextNode(pw));
				user.appendChild(password);

				// write the content into xml file
				TransformerFactory transformerFactory = TransformerFactory.newInstance();

				Transformer transformer = transformerFactory.newTransformer();

				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File("BD/users.xml"));
				transformer.transform(source, result);

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
		} catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
			e.printStackTrace();
		}
	}

}
