package server;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Tache;

public final class Server{
	
	private List<Tache> lTache = new ArrayList<Tache>();

	// Now add observability by wrapping it with ObservableList.
	private ObservableList<Tache> observableListTaches = FXCollections.observableList(lTache);

	private final static Server serverInstance = new Server();

	public static Server getInstance() {
		return serverInstance;
	}

	// Add keyword to the observable map
	public void addTache(Tache n) {
		this.observableListTaches.add(n);
	}

	public ObservableList<Tache> getObservableList() {
		return observableListTaches;
	}
}
