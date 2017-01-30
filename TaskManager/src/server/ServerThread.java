package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.xml.transform.TransformerException;

import model.Task;
import model.User;

public class ServerThread implements Runnable {
	private BufferedReader inFromClient = null;

	private DataOutputStream outToClient = null;
	private ObjectOutputStream objectOutput = null;
	private ObjectInputStream objectInput = null;
	private Socket connectionSocket;
	private Server s;
	private User u;

	public ServerThread(Socket connectionSocket, Server s) {
		this.s = s;
		try {
			this.connectionSocket = connectionSocket;
			inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			objectOutput = new ObjectOutputStream(connectionSocket.getOutputStream());
			objectInput = new ObjectInputStream(connectionSocket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String clientSentence;
		try {
			while (!connectionSocket.isClosed()) {
				clientSentence = inFromClient.readLine();
				System.out.println("Recu : " + clientSentence);
				String[] parts = clientSentence.split(" ");
				switch (parts[0]) {
				case "disconnect":
					System.out.println("Fin du client");
					this.s.disconnectUser(u);
					connectionSocket.close();
					break;
				case "removeTask":
					s.removeTask(Integer.parseInt(parts[1]));
					break;
				case "getUsers":
					objectOutput.writeObject(s.getlUsers());
					break;
				case "getTasks":
					objectOutput.writeObject(s.getlTasks(parts[1]));
					break;
				case "editTask":
					try {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 Object o = objectInput.readObject();
						 System.out.println(o.toString());
						s.editTask((Task) o);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "addTask":
					try {
						s.addTask((Task) objectInput.readObject());
					} catch (ClassNotFoundException | TransformerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "signUp":
					System.out.println("Sign up");
					try {
						outToClient.writeBytes(s.addUser(parts[1], parts[2]));
						objectOutput.flush();
					} catch (TransformerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "connect":
					System.out.println("Connect");
					u = s.connect(parts[1], parts[2]);
					objectOutput.writeObject(u);
					objectOutput.flush();
					break;
				default:
					System.out.println("Commande inconnue");
					objectOutput.writeBytes("Commande inconnue");
					break;
				}

			}

		} catch (IOException e) {
			System.out.println("Client fermé");
			this.s.disconnectUser(u);
			try {
				connectionSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
