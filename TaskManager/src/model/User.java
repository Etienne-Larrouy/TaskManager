package model;

import java.awt.List;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.reflect.annotation.TypeAnnotation.LocationInfo.Location;

public class User implements Serializable {
	private int id;


	private StringProperty username;

	public User(String username, int id) {
		this.username = new SimpleStringProperty();
		this.username.set(username);
		this.id = id;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	/* Getters and setters */
	public final String getUsername() {
		return this.username.get();
	}

	public StringProperty getUsernameProperty() {
		return this.username;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.writeObject(this.getUsername());
		oos.writeInt(this.id);

	}

	private void readObject(ObjectInputStream in) throws IOException {
		this.username = new SimpleStringProperty();
		try {
		
			this.username.set((String)in.readObject());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		this.id = in.readInt();
	}

}