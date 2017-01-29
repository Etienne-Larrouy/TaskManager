package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import model.Task;
import model.User;

public class Server {

	private List<Task> lTasks = new ArrayList<Task>();

	private List<User> lUsers = new ArrayList<User>();

	private List<User> lUsersConnected = new ArrayList<User>();

	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document docUsers;
	private Document docTasks;

	private static ServerSocket welcomeSocket;

	public Server() {

		docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
			docUsers = docBuilder.parse(new File("BD/users.xml"));
			docTasks = docBuilder.parse(new File("BD/tasks.xml"));

		} catch (ParserConfigurationException | SAXException | IOException e1) {
			e1.printStackTrace();
		}

		this.loadUsers();
		this.loadTasks();

	}

	public void addUserConnected(User u) {
		this.lUsersConnected.add(u);
	}

	public void disconnectUser(User u) {
		this.lUsersConnected.remove(u);
	}

	public boolean alreadyConnected(User u) {
		return this.lUsersConnected.contains(u);
	}

	public List<Task> getlTasks(String username) {
		List<Task> listTasks = new ArrayList<Task>();

		for (Task t : this.lTasks) {
			System.out.println(t.getTitle());
			if (t.getOwner().getUsername().equals(username) || t.getPerformer().getUsername().equals(username)) {
				listTasks.add(t);
			}
		}

		return listTasks;
	}

	public List<User> getlUsers() {
		return lUsers;
	}

	public void loadUsers() {
		// get all user nodes
		NodeList users = docUsers.getElementsByTagName("user");

		int nbUsers = users.getLength();

		for (int i = 0; i < nbUsers; i++) {

			Element user = (Element) users.item(i);
			this.lUsers
					.add(new User(user.getElementsByTagName("username").item(0).getTextContent(), this.lUsers.size()));
		}

		// get all tasks nodes
		NodeList tasks = docUsers.getElementsByTagName("task");
		int nbTasks = tasks.getLength();

		for (int i = 0; i < nbTasks; i++) {
			Element task = (Element) tasks.item(i);
			this.lUsers
					.add(new User(task.getElementsByTagName("username").item(0).getTextContent(), this.lTasks.size()));
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
			this.lTasks.add(new Task(title, desc, this.getUserFromUsername(author), this.getUserFromUsername(performer),
					priority, deadline, i, state, creationDate));
		}
	}

	public User getUserFromUsername(String username) {
		for (User u : this.lUsers) {
			if (u.getUsername().equals(username))
				return u;
		}

		return null;
	}

	// Add keyword to the observable map
	public synchronized void addTask(Task t) throws TransformerException {
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

		this.lTasks.add(t);
	}

	// Add keyword to the observable map
	public synchronized String addUser(String name, String pw) throws TransformerException {
		Element rootElement = null;
		NodeList nodelist = docUsers.getElementsByTagName("users");
		for (User u : this.lUsers) {
			if (u.getUsername().equals(name)) {
				return "Username already exists\n";
			}
		}

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
		username.appendChild(docUsers.createTextNode(name));
		user.appendChild(username);

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

		this.lUsers.add(new User(name, this.lUsers.size()));
		return "registered\n";
	}

	public User connect(String name, String pw) {
		// Verify user and password from xml

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("BD/users.xml"));

			Element user = getUser(doc, name);

			if (user != null) {

				if (pw.equals(user.getElementsByTagName("password").item(0).getTextContent())) {
					// Get User object
					for (User u : this.lUsers) {
						if (u.getUsername().equals(name)) {
							if (!this.alreadyConnected(u)) {
								System.out.println("User found");
								addUserConnected(u);
								return u;
							}
						}
					}
				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Impossible to connect");
		return null;
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

	public static void main(String argv[]) throws Exception {
		Server s = new Server();

		welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("New Client");
			new Thread(new ServerThread(connectionSocket, s)).start();
		}
	}

	public synchronized void editTask(Task t) {

		try {
			Task tEdit = this.lTasks.get(t.getId());
			tEdit.setCreationDate(t.getCreationDate());
			tEdit.setDeadLine(t.getDeadline());
			tEdit.setDescription(t.getDescription());
			tEdit.setPerformer(t.getPerformer());
			tEdit.setPriority(t.getPriority());
			tEdit.setState(t.getState());
			tEdit.setTitle(t.getTitle());

			NodeList nodelist = docTasks.getElementsByTagName("task");

			Element task = (Element) nodelist.item(t.getId());

			// title element
			task.getChildNodes().item(0).setTextContent(t.getTitle());

			// author element
			task.getChildNodes().item(1).setTextContent(t.getOwner().getUsername());

			// performer element
			task.getChildNodes().item(2).setTextContent(t.getPerformer().getUsername());

			// deadline element
			task.getChildNodes().item(3).setTextContent(t.getDeadline());

			// creationDate element
			task.getChildNodes().item(4).setTextContent(t.getCreationDate());

			// state element
			task.getChildNodes().item(5).setTextContent(t.getState());

			// priority element
			task.getChildNodes().item(6).setTextContent(t.getPriority());

			// description element
			task.getChildNodes().item(6).setTextContent(t.getDescription());

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(docTasks);
			StreamResult result = new StreamResult(new File("BD/tasks.xml"));
			transformer.transform(source, result);

			transformer = transformerFactory.newTransformer();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public synchronized void removeTask(int t) {
		try {
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			this.lTasks.remove(t);

			Element rootElement = null;
			rootElement = (Element) docTasks.getElementsByTagName("tasks").item(0);

			Node task = rootElement.getChildNodes().item(t);

			rootElement.removeChild(task);

			DOMSource source = new DOMSource(docTasks);
			StreamResult result = new StreamResult(new File("BD/tasks.xml"));
			transformer.transform(source, result);

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
