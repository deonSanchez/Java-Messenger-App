package server;

import client.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	//Server class fields declaration
    private int portNumber;
    private int maxClients; // TODO: enforce maximum number of clients
    private ArrayList<ClientHandler> clients;

	//Server class constructor
    public Server(int portNumber, int maxClients) {
		//initializing the class fields
        this.portNumber = portNumber;
        this.maxClients = maxClients;
        this.clients = new ArrayList<>();
    }

	// this method sends message to all subscribed clients on the server
    void broadcast(Message message, String senderUuid) {
		//loop through all clients
        for (ClientHandler client : clients) {
            if(!client.getUuid().equals(senderUuid)) {
                client.sendToClient(message);
            }
        }
    }

	//this method sets up the server object
    public void runServer() {
        try {
			//initialization of ServerSocket object to listen on the specified port
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Listening for clients on port " + portNumber + "...");
            //Listen for incoming connection indefinitely
			while (true) {
                Socket clientSocket = serverSocket.accept();

                // Spawn a new thread for the new client.
                ClientHandler ch = ClientHandlerThreadFactory.getInstance().create(this, clientSocket);
                clients.add(ch);
                ch.start();
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
