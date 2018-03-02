package server;

import client.Message;
import client.MessageConcrete;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

	// class properties declaration
    private Server server;
    private Socket clientSocket;
    private String uuid;

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean readWriteInitialized;

	//Class constructor putting the Server and Socket objects with the uniqueIdentifier
    ClientHandler(Server server, Socket clientSocket, String uuid) {
		//class initialization
        this.server = server;
        this.clientSocket = clientSocket;
        this.uuid = uuid;
    }

	//
    String getUuid() {
        return uuid;
    }

    public void run() {
        System.out.printf("Server connected with client %s\n", this.uuid);

        try {
			//ObjectOutputStream instantiation, for getting outputs of the socket connection
            out = new ObjectOutputStream(clientSocket.getOutputStream());
			//ObjectInputStream instantiation, for getting user inputs to pass to the socket connection
            in = new ObjectInputStream(clientSocket.getInputStream());
            readWriteInitialized = true;

            // Initiate conversation with client
            sendToClient(new MessageConcrete("Server", "Connected to server"));

            Object inputLine;
			//Loop until no message is supplied
            while ((inputLine = in.readObject()) != null) {

                Message input = (Message) inputLine;

                System.out.println(uuid + ": " + input);

                // Server received a message from the client - now broadcast it to the entire "room"
                server.broadcast(input, uuid);

                // Close streams and terminate thread.
                if (input.toString().equals("exit")) {
                    out.close();
                    in.close();
                    return;
                }

            }
        } catch (IOException e) {
            readWriteInitialized = false;
        } catch (ClassNotFoundException e) {
            // Handle later
            e.printStackTrace();
        }
    }

    /**
     * Used to send information to this specific client.
     *
     * @param message the showMessage to be sent.
     */
    void sendToClient(Message message) {
        if (readWriteInitialized) {
            try {
				//send message through socket
                out.writeObject(message);
            } catch (IOException e) {
                // TODO: Better exception handling
                e.printStackTrace();
            }
        } else {
            System.err.printf("Connection with client %s uninitialized, showMessage cannot be sent.", uuid);
        }
    }
}
