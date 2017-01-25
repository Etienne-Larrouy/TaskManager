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

public class ServerThread implements Runnable{
	private BufferedReader inFromClient = null;

	private DataOutputStream outToClient = null;
	private ObjectOutputStream objectOutput = null;
	private ObjectInputStream objectInput = null;

	private Server s;

	public ServerThread(Socket connectionSocket, Server s) {
		this.s = s;
		try {
			inFromClient =  new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
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
			while(true){
				clientSentence = inFromClient.readLine();
				System.out.println("Recu : " + clientSentence);
				String[] parts = clientSentence.split(" ");
				switch(parts[0]){
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
						s.editTask((Task)objectInput.readObject());
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				case "addTask":
					try {
						s.addTask((Task)objectInput.readObject());
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
					objectOutput.writeObject(s.connect(parts[1], parts[2]));
					objectOutput.flush();
					break;
				default:
					System.out.println("Commande inconnue");
					objectOutput.writeBytes("Commande inconnue");
					break;
				}

			}


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

}
