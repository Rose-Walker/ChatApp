package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
	
	// port 13 used to accept socket connections.
	public final static int PORT = 13;
	// HashMap to store multiple clients
	public static Map<String, Socket> connectedClients = new HashMap<>();
	
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(PORT)) {
			System.out.println("Listening for connections on port " + PORT);
			// if a connection to the port is received, accept & prompt for username
			while (true) {
				try {
					Socket connection = server.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					String username = in.readLine();
					
					
					System.out.println("Client with username '" + username + "', connected from " + connection.getInetAddress()+ ":" + connection.getPort());
					Thread clientListenerThread = new ChatClientListenerThread(username, connection);
					clientListenerThread.start();
					connectedClients.put(username, connection);
				}
				catch(IOException ex) {
				}
			}
		}
		catch (IOException ex) {
			System.err.println(ex);
		}
	} 	
	
	// Threads to allow for the multiple clients on the server
	public static class ChatClientListenerThread extends Thread {

		private Socket connection;
		private String username;
		
		ChatClientListenerThread(String username, Socket connection){
			this.connection = connection;
			this.username = username;
		}
		
		@Override
		public void run() {
			try {			
				PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while(true) {
					String clientMessage = in.readLine();
					// only allows user to send message if it includes characters
					// also will display the clients username next to their messages
					if(clientMessage != null && clientMessage.length() > 0) {
						String messageToSend = "[" + username +"]" + " - " + clientMessage;
						System.out.println(messageToSend);
						
						for(String otherUsername : connectedClients.keySet()) {
							//Ensuring the message isn't sent back to the person who originally sent it
							if(!otherUsername.equals(this.username)) {
								Socket otherClientConnection = connectedClients.get(otherUsername);
								PrintWriter clientOutput = new PrintWriter(otherClientConnection.getOutputStream(), true);
								clientOutput.println(messageToSend);
							}
						}
					}
					out.println("");
				}

			}
			catch(IOException ex) {
				System.err.println(ex);
			}
			finally {
				try {
					System.out.println("Removing client with username: " + username);
					connectedClients.remove(username);
					connection.close();
				}
				catch(IOException e) {
					// ignore
				}
			}
		}
	}
	
	
}
