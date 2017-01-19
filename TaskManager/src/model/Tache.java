package model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tache {
	private StringProperty title = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	
	
	private int priorite;
	private int etat;
	private Utilisateur owner;
	private Utilisateur performer;
	

	
	public Tache(String title, String description){
		this.setDescription(description);
		this.setTitle(title);
	}
	
	/* Getters and setters*/
	public final String getTitle() {
		return this.title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public StringProperty getTitleProperty() {
		return this.title;
	}
	
	public StringProperty getDescriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.description.get();
	}


	public void setDescription(String texte) {
		this.description.set(texte);
	}

}
