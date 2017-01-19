package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import server.Server;

public class ControllerTask implements Initializable{
	private Server s;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		s = Server.getInstance();
	}

}
