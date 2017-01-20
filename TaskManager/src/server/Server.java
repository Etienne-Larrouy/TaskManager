package server;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import model.User;

public final class Server {

	private List<Task> lTache = new ArrayList<Task>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Task> observableListTasks = FXCollections.observableList(lTache);

	private User userSession;
	private List<User> lUsers = new ArrayList<User>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<User> observableListUsers = FXCollections.observableList(lUsers);

	private final static Server serverInstance = new Server();

	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document docUsers;
	private Document docTasks;

	public static Server getInstance() {
		return serverInstance;
	}

	private Server() {
		try {

			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			docUsers = docBuilder.parse(new File("BD/users.xml"));
			docTasks = docBuilder.parse(new File("BD/tasks.xml"));

			this.loadUsers();
			this.loadTasks();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Load users from xml
	public void loadUsers() {
		// get all user nodes
		NodeList users = docUsers.getElementsByTagName("user");

		int nbUsers = users.getLength();

		for (int i = 0; i < nbUsers; i++) {

			Element user = (Element) users.item(i);
			this.observableListUsers.add(new User(user.getElementsByTagName("username").item(0).getTextContent(),
					this.getObservableListUsers().size()));
		}

		// get all tasks nodes
		NodeList tasks = docUsers.getElementsByTagName("task");
		int nbTasks = tasks.getLength();

		for (int i = 0; i < nbTasks; i++) {
			Element task = (Element) tasks.item(i);
			this.observableListUsers.add(new User(task.getElementsByTagName("username").item(0).getTextContent(),
					this.getObservableListTasks().size()));
		}
	}

	// Load users from xml
	public void loadTasks() {
		// get all user nodes
		NodeList tasks = docTasks.getElementsByTagName("task");

		int nbTasks = tasks.getLength();

		for (int i = 0; i < nbTasks; i++) {
			Element task = (Element) tasks.item(i);
			String title = task.getElementsByTagName("title").item(0).getTextContent();
			String author = task.getElementsByTagName("author").item(0).getTextContent();
			String desc = task.getElementsByTagName("description").item(0).getTextContent();
			String performer = task.getElementsByTagName("performer").item(0).getTextContent();
			String deadline = task.getElementsByTagName("deadline").item(0).getTextContent();
			String creationDate = task.getElementsByTagName("creationDate").item(0).getTextContent();
			String state = task.getElementsByTagName("state").item(0).getTextContent();
			String priority = task.getElementsByTagName("priority").item(0).getTextContent();
			this.observableListTasks.add(new Task(title, desc, this.getUserFromUsername(author),
					this.getUserFromUsername(performer), priority, deadline, i, state, creationDate));
		}
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
		Element rootElement = null;
		NodeList nodelist = docTasks.getElementsByTagName("tasks");

		if (nodelist.getLength() >= 0) {
			rootElement = (Element) nodelist.item(0);
		} else {
			rootElement = docTasks.createElement("tasks");
			docTasks.appendChild(rootElement);
		}

		// task elements
		Element task = docTasks.createElement("task");
		rootElement.appendChild(task);

		// title element
		Element title = docTasks.createElement("title");
		title.appendChild(docTasks.createTextNode(t.getTitle()));
		task.appendChild(title);

		// author element
		Element author = docTasks.createElement("author");
		author.appendChild(docTasks.createTextNode(t.getOwner().getUsername()));
		task.appendChild(author);

		// performer element
		Element performer = docTasks.createElement("performer");
		performer.appendChild(docTasks.createTextNode(t.getPerformer().getUsername()));
		task.appendChild(performer);

		// deadline element
		Element deadline = docTasks.createElement("deadline");
		deadline.appendChild(docTasks.createTextNode(t.getDeadline()));
		task.appendChild(deadline);

		// creationDate element
		Element creationDate = docTasks.createElement("creationDate");
		creationDate.appendChild(docTasks.createTextNode(t.getCreationDate()));
		task.appendChild(creationDate);

		// state element
		Element state = docTasks.createElement("state");
		state.appendChild(docTasks.createTextNode(t.getState()));
		task.appendChild(state);

		// priority element
		Element priority = docTasks.createElement("priority");
		priority.appendChild(docTasks.createTextNode(t.getPriority()));
		task.appendChild(priority);

		// priority element
		Element description = docTasks.createElement("description");
		description.appendChild(docTasks.createTextNode(t.getDescription()));
		task.appendChild(description);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(docTasks);
		StreamResult result = new StreamResult(new File("BD/tasks.xml"));
		transformer.transform(source, result);

		this.observableListTasks.add(t);
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
		Element rootElement = null;
		NodeList nodelist = docUsers.getElementsByTagName("users");

		if (nodelist.getLength() >= 0) {
			rootElement = (Element) nodelist.item(0);
		} else {
			rootElement = docUsers.createElement("users");
			docUsers.appendChild(rootElement);
		}

		// user elements
		Element user = docUsers.createElement("user");
		rootElement.appendChild(user);

		// username elements
		Element username = docUsers.createElement("username");
		username.appendChild(docUsers.createTextNode(u.getUsername()));
		user.appendChild(username);

		// Crypt password
		// Create MessageDigest instance for MD5

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
		Element password = docUsers.createElement("password");
		password.appendChild(docUsers.createTextNode(pw));
		user.appendChild(password);

		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();

		Transformer transformer = transformerFactory.newTransformer();

		DOMSource source = new DOMSource(docUsers);
		StreamResult result = new StreamResult(new File("BD/users.xml"));
		transformer.transform(source, result);

		this.observableListUsers.add(u);
	}

	public ObservableList<User> getObservableListUsers() {
		return observableListUsers;
	}
}
