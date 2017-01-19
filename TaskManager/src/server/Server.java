package server;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Task;
import model.User;

public final class Server{
	
	private List<Task> lTache = new ArrayList<Task>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Task> observableListTaches = FXCollections.observableList(lTache);

	
	private List<User> lUsers = new ArrayList<User>();
	
	// Now add observability by wrapping it with ObservableList.
	private ObservableList<User> observableListUsers = FXCollections.observableList(lUsers);

	private final static Server serverInstance = new Server();

	public static Server getInstance() {
		return serverInstance;
	}

	// Add keyword to the observable map
	public void addTask(Task t) {
		this.observableListTaches.add(t);
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
