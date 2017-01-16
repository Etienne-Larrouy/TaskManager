package controller;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private TextField username;

	@FXML
	private PasswordField password;

	@FXML
	private Text statusbar;

	@FXML
	private PasswordField Register_repeatPassword;

	@FXML
	private PasswordField Register_password;

	@FXML
	private PasswordField Register_username;
	
	@FXML
	private PasswordField Register_statusbar;

	public void handleConnect(ActionEvent event) {
		if (username.getText().isEmpty())
			statusbar.setText("Error username is empty");
		else if (password.getText().isEmpty())
			statusbar.setText("Error password is empty");
		else
			statusbar.setText("Connected");

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

		if (!Register_repeatPassword.getText().equals(Register_password)) {
			Register_statusbar.setText("Error : Passwords don't match");
	
		} else {
			XMLOutputFactory factory = XMLOutputFactory.newInstance();

			try {
				XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter("BD/users.xml"));

				writer.writeStartDocument();
				writer.writeStartElement("document");
				writer.writeStartElement("data");
				writer.writeAttribute("name", "value");
				writer.writeEndElement();
				writer.writeEndElement();
				writer.writeEndDocument();

				writer.flush();
				writer.close();

			} catch (XMLStreamException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
