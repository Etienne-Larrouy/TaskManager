package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import model.User;

public final class Client {

	private List<Task> lTache = new ArrayList<Task>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Task> observableListTasks = FXCollections.observableList(lTache);

	private User userSession;
	private List<User> lUsers = new ArrayList<User>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<User> observableListUsers = FXCollections.observableList(lUsers);

	private final static Client clientInstance = new Client();
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private  ObjectInputStream oiStream;

	public static Client getInstance() {
		return clientInstance;
	}

	private Client() {

		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", 6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			oiStream = new ObjectInputStream(clientSocket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Load users from xml
	public void getUsers() {
		// get all user nodes
		try {
			outToServer.writeBytes("getUsers\n");
			this.observableListUsers = (ObservableList<User>) oiStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean connect(String name, String pw) {
		
		try {
			outToServer.writeBytes("connect "+name+" "+ pw +"\n");
			outToServer.flush();
			this.userSession = (User) oiStream.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this.userSession == null){
			return false;
		}
		else{
			return true;
		}
	}

	// Load users from xml
	public void getTasks() {
		// get all tasks nodes
		try {
			outToServer.writeBytes("getUsers\n");
			this.observableListTasks = (ObservableList<Task>) oiStream.readObject();
			outToServer.flush();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String signUp(String username, String pw){
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
		try {
			outToServer.writeBytes("signUp "+username+" "+pw+"\n");
			outToServer.flush();
			return inFromServer.readLine();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Server error";
		
	}
	
	
	public User getUserFromUsername(String username) {
		for (User u : this.getObservableListUsers()) {
			if (u.getUsername().equals(username))
				return u;
		}

		return null;
	}

	// Add keyword to the observable map
	public void addTask(Task t) throws TransformerException {

	}

	// Init user session
	public void initUserSession(User u) {
		this.userSession = u;
	}

	public User getUserSession() {
		return this.userSession;
	}

	public ObservableList<Task> getObservableListTasks() {
		return observableListTasks;
	}

	// Add keyword to the observable map
	public void addUser(User u, String pw) throws TransformerException {

	}

	public ObservableList<User> getObservableListUsers() {
		return observableListUsers;
	}


}
