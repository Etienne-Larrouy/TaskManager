package model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User
{
	private int id;
	private ArrayList<Task> lTache = new ArrayList<Task>();
	private StringProperty username = new SimpleStringProperty(); 
	
	public User(String username){
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