package server;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Tache;

public class Server
{
	private List<Tache> lTache= new ArrayList<Tache>();

	// Now add observability by wrapping it with ObservableList.
	ObservableList<Tache> observableListTaches = FXCollections.observableList(lTache);


	// Add keyword to the observable map
	public void addTache(Tache n) {
		this.observableListTaches.add(n);
	}

	public ObservableList<Tache> getObservableList() {
		return observableListTaches;
	}
}
