package client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
	
public class ChatClient {
		
	public static final int PORT = 13;
	public static Scanner scannerIn = new Scanner(System.in);
    
	
	public static void main(String[] args) {
		String hostname = args.length > 0 ? args[0] : "localhost";
		
		try (Socket socket = new Socket(hostname, PORT)) {	
			Thread listenThread = new ServerListenerThread(socket);
			listenThread.start();
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			// prompt for each client
			System.out.print("Please enter a username: ");
			String username = scannerIn.nextLine();
			out.println(username);
			
			System.out.println("You are now connected to the chat server");
		
			boolean hasQuit = false;
			while (hasQuit == false) {
				System.out.print("> ");
                String userMessage = scannerIn.nextLine();
                // allowing the client to disconnect
                if(userMessage.equalsIgnoreCase("\\q")) {
                	hasQuit = true;
                }
                else {
                    out.println(userMessage);
                }
            }
		} 
		// exception handling for errors
		catch (UnknownHostException e) {
			System.out.println("Can't connect to chat server");
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Disconnected from the chat server, goodbye");
	}
	
	// threads the listener for server messages.
	public static class ServerListenerThread extends Thread {

		private Socket connection;
		
		ServerListenerThread(Socket connection){
			this.connection = connection;
		}
		
		@Override
		public void run() {		
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while(true) {
					String message = in.readLine();
					if(message != null && message.length() > 0) {
						System.out.println(message);
						System.out.print("> ");
					}
				}
			}
			catch(IOException ex) {
				//ignore
			}
			finally {
				try {
					connection.close();
				}
				catch(IOException e) {
					// ignore
				}
			}
		}
	}
}
