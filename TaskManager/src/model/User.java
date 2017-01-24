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

	transient private StringProperty username = new SimpleStringProperty();

	public User(String username, int id) {
		this.username.set(username);
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
		oos.writeUTF(this.getUsername());
		oos.writeInt(this.id);

	}

	private void readObject(ObjectInputStream in) throws IOException {

		System.out.println(in.readUTF());
		this.id = in.readInt();

	}

}