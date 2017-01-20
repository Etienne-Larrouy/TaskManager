package server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import model.User;

public final class Server{

	private List<Task> lTache = new ArrayList<Task>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Task> observableListTaches = FXCollections.observableList(lTache);

	private User userSession;
	private List<User> lUsers = new ArrayList<User>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<User> observableListUsers = FXCollections.observableList(lUsers);

	private final static Server serverInstance = new Server();

	public static Server getInstance() {
		return serverInstance;
	}

	private Server(){
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
	
		Document doc = docBuilder.parse(new File("BD/users.xml"));
		
		// get all user nodes
		NodeList users = doc.getElementsByTagName("user");
		int nbUsers = users.getLength();

		for (int i = 0; i < nbUsers; i++) {
			Element user = (Element) users.item(i);
			this.addUser(new User(user.getElementsByTagName("username").item(0).getTextContent(), this.observableListTaches.size()));
		}
		
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Add keyword to the observable map
	public void addTask(Task t) {
		this.observableListTaches.add(t);
	}

	// Init user session
	public void initUserSession(User u) {
		this.userSession = u;
	}
	
	public User getUserSession() {
		return this.userSession;
	}

	public ObservableList<Task> getObservableListTasks() {
		return observableListTaches;
	}

	// Add keyword to the observable map
	public void addUser(User s) {
		this.observableListUsers.add(s);
	}

	public ObservableList<User> getObservableListUsers() {
		return observableListUsers;
	}
}
