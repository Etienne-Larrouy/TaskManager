package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import model.Utilisateur;

public class Controller {
	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Text statusbar;

	@FXML
	private Text Register_statusbar;

	@FXML
	private PasswordField Register_repeatPassword;

	@FXML
	private PasswordField Register_password;

	@FXML
	private TextField Register_username;

	public void handleConnect(ActionEvent event) {
		if (username.getText().isEmpty())
			statusbar.setText("Error username is empty");
		else if (password.getText().isEmpty())
			statusbar.setText("Error password is empty");
		else{
			// Verify user and password from xml
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder;
				docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(new File("BD/users.xml"));
				if(canConnect(doc, username.getText(), password.getText())){
					//TODO
					statusbar.setText("Connected");
				}
				else{
					statusbar.setText("Wrong password or username");
				}
			
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void handleRegister(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../view/Register.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));

			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

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


				/*
				 * Document doc = docBuilder.newDocument();
				 */
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



			}
		} catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
			e.printStackTrace();
		}

	}

	private Element getUser(Document doc, String username) {

		NodeList users = doc.getElementsByTagName("user");
		int nbUsers = users.getLength();

		for (int i = 0; i < nbUsers; i++) {
			Element user = (Element) users.item(i);

			if (user.getElementsByTagName("username").item(0).getTextContent().equals(username)) {
				return user;
			}
		}

		return null;
	}
	
	private boolean canConnect(Document doc, String username, String password) {
		
		Element user = getUser(doc, username);
	
		if(user == null){
			return false;
		}
		else{
			// Crypt password
			// Create MessageDigest instance for MD5
			String pw = password;
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
			
			if(pw.equals(user.getElementsByTagName("password").item(0).getTextContent())){
				return true;
			}
			else{
				return false;
			}
		}
	}

	private List<Utilisateur> loadUsersFromXML() {

		ArrayList<Utilisateur> listUsers = new ArrayList<Utilisateur>();

		try {
			// Open document
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;

			docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.parse(new File("BD/users.xml"));

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return listUsers;
	}

}
