package model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Utilisateur
{
	private int id;
	private ArrayList<Tache> lTache = new ArrayList<Tache>();
	private StringProperty username = new SimpleStringProperty(); 
	
	public Utilisateur(String username){
		this.username.set(username);
	}
	/* Getters and setters*/
	public final String getUsername() {
		return this.username.get();
	}

	public StringProperty getUsernameProperty() {
		return this.username;
	}

}