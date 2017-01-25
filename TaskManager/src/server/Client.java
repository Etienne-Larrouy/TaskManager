package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.xml.transform.TransformerException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import model.User;

public final class Client {


	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Task> observableListTasks;

	private User userSession;

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<User> observableListUsers;

	private final static Client clientInstance = new Client();
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private  ObjectInputStream oiStream;
	private  ObjectOutputStream ooStream;

	public static Client getInstance() {
		return clientInstance;
	}

	private Client() {

		Socket clientSocket;
		try {
			clientSocket = new Socket("localhost", 6789);
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			oiStream = new ObjectInputStream(clientSocket.getInputStream());
			ooStream = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Load users from xml
	@SuppressWarnings("unchecked")
	public void getUsers() {
		// get all user nodes
		try {
			outToServer.writeBytes("getUsers "+userSession.getUsername()+"\n");
			this.observableListUsers = FXCollections.observableList((List<User>) oiStream.readObject());
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
	@SuppressWarnings("unchecked")
	public void getTasks() {
		// get all tasks nodes
		try {
			outToServer.writeBytes("getTasks "+userSession.getUsername()+"\n");
			this.observableListTasks = FXCollections.observableList((List<Task>) oiStream.readObject());
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

	// Sen new task to the serve
	public void addTask(Task t) throws TransformerException {
		try {
			outToServer.writeBytes("addTask\n");
			ooStream.writeObject(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void editTask(Task t) {
		try {
			outToServer.writeBytes("editTask\n");
			ooStream.writeObject(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
