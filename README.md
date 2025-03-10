# Chat App
A chat app created using Java command line, this was created to demonstrate Java sockets and multi-threading.
The app was created with two pieces, a single Server and any number of Clients.

## Requirements
- Java SDK 17, if you do not have Java 17 you can download it from the official website found here
- Eclipse or another IDE of your choice that can run Java files/projects

## Setup Guide
1. Download or clone the repository
2. Once downloaded open the folder in your chosen IDE
3. Now open at least more instance of your chosen IDE

## User Guide
In one IDE, run the ChatServer class as the entry point.
In the other IDE run the ChatClient class as the entry point.

The Chat Server will listen for any incoming connections from a Chat Client, when it receives any messages from a client it will relay those messages to any other ChatClients connected.
In the IDE you are running the ChatClient with, you will be prompted for a username, enter a username and then you will be connected to the chat Server, you can repeat this process with anothe rinsatnce of your chosen IDE.
Once the ChatClient is connected you can send messages to the server, any other ChatClients can see and send messages to the server which is relayed to all other Clients.
