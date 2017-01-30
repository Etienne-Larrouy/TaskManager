package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task implements Serializable {
	private StringProperty title = new SimpleStringProperty();
	private StringProperty description = new SimpleStringProperty();
	private StringProperty state = new SimpleStringProperty();
	private StringProperty deadLine = new SimpleStringProperty();
	private StringProperty creationDate = new SimpleStringProperty();
	private StringProperty priority = new SimpleStringProperty();
	private int id;

	private User owner;
	private User performer;

	public Task(String title, String description, User owner, User performer, String priority, String deadLine,
			int id) {
		this.setDescription(description);
		this.setTitle(title);
		this.setState("In Progress");
		this.setPriority(priority);
		this.setDeadLine(deadLine);
		this.creationDate.set(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		this.owner = owner;
		this.setPerformer(performer);
		this.id = id;
	}

	public Task(String title, String description, User owner, User performer, String priority, String deadLine, int id,
			String state, String creationDate) {
		this.setDescription(description);
		this.setTitle(title);
		this.setState(state);
		this.setPriority(priority);
		this.setDeadLine(deadLine);
		this.setCreationDate(creationDate);
		this.owner = owner;
		this.setPerformer(performer);
		this.id = id;
	}

	public void setId(int id){
		this.id = id;
	}
	/* Getters and setters */

	// Owner
	public User getOwner() {
		return owner;
	}

	// Performer
	public User getPerformer() {
		return performer;
	}

	public void setPerformer(User performer) {
		this.performer = performer;
	}

	// Title
	public final String getTitle() {
		return this.title.get();
	}

	@Override
	public String toString() {
		return "Task [title=" + title + ", description=" + description + ", state=" + state + ", deadLine=" + deadLine
				+ ", creationDate=" + creationDate + ", priority=" + priority + ", id=" + id + ", owner=" + owner
				+ ", performer=" + performer + "]";
	}

	public final void setTitle(String title) {
		this.title.set(title);
	}

	public final StringProperty getTitleProperty() {
		return this.title;
	}

	// Description
	public final StringProperty getDescriptionProperty() {
		return this.description;
	}

	public final String getDescription() {
		return this.description.get();
	}

	public final void setDescription(String description) {
		this.description.set(description);
	}

	// State
	public final void setState(String state) {
		this.state.set(state);
	}

	public final StringProperty getStateProperty() {
		return this.state;
	}

	public final String getState() {
		return this.state.get();
	}

	// DeadLine
	public final void setDeadLine(String deadLine) {
		this.deadLine.set(deadLine);
	}

	public final StringProperty getDeadLineProperty() {
		return this.deadLine;
	}

	public final String getDeadline() {
		return this.deadLine.get();
	}

	// CreationDate
	public final void setCreationDate(String creationDate) {
		this.creationDate.set(creationDate);
	}

	public final StringProperty getCreationDateProperty() {
		return this.creationDate;
	}

	public final String getCreationDate() {
		return this.creationDate.get();
	}

	// Priority
	public final void setPriority(String priority) {
		this.priority.set(priority);
	}

	public final StringProperty getPriorityProperty() {
		return this.priority;
	}

	public final String getPriority() {
		return this.priority.get();
	}

	public int getId() {
		return this.id;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		oos.writeObject(this.getTitle());
		oos.writeObject(this.getDescription());
		oos.writeObject(this.getState());
		oos.writeObject(this.getDeadline());
		oos.writeObject(this.getCreationDate());
		oos.writeObject(this.getPriority());
		oos.writeObject(this.getPerformer());
		oos.writeObject(this.getOwner());

		oos.writeInt(this.id);

	}

	private void readObject(ObjectInputStream in) throws IOException {
		try {
			this.title = new SimpleStringProperty();
			this.description = new SimpleStringProperty();
			this.state = new SimpleStringProperty();
			this.deadLine = new SimpleStringProperty();
			this.creationDate = new SimpleStringProperty();
			this.priority = new SimpleStringProperty();

			this.title.set((String) in.readObject());
			this.description.set((String) in.readObject());
			this.state.set((String) in.readObject());
			this.deadLine.set((String) in.readObject());
			this.creationDate.set((String) in.readObject());
			this.priority.set((String) in.readObject());
			this.performer = (User) in.readObject();
			this.owner = (User) in.readObject();
			this.id = in.readInt();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}