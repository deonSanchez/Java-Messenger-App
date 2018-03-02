package client;

import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

public class ConnectionManagerFacade extends Thread {

	//ConnectionManagerFacade field declaration
    private String hostName;
    private int portNumber;
    private ChatListObservable chatList;
    private JavaFXController controller;

	// socket input and output objects
    private ObjectOutputStream out;
    private ObjectInputStream in;

	//class constructor injecting ipaddress/hostName of the server, JavaFXController object for the view
    public ConnectionManagerFacade(String hostName, int portNumber, ChatListObservable chatList, JavaFXController controller) throws IOException {
		//field initialization
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.chatList = chatList;
        this.controller = controller;

        initializeConnection();
        sendToServer(new MessageConcrete(controller.username.getText(), "Joined the room!"));
    }

	//This method sets up a connection to the Server
    private void initializeConnection() throws IOException {
		//instantiate socket object and assign network input and output stream
        Socket socket = new Socket(hostName, portNumber);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

	//this method reads the response from the server through socket connection
    public void run() {
        try {
            Object fromServer;
			//loops indefinitely until the server returns Bye string
            while ((fromServer = in.readObject()) != null) {
                showMessageLocally((Message) fromServer);
                if (fromServer.toString().equals("Bye"))
                    break;
            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName + ":" + portNumber);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            // TODO: Better exception handling
            e.printStackTrace();
        }
    }

	// this method sends message to the server
    public void sendToServer(final Message msg) {

        // Sending the message to the server
        try {
			//initialze message obect
            Message finalMessage = msg;

			//determine if to apply hightlight format to message
            if(controller.formatHighlight.isSelected()) {
                finalMessage = new MessageColorDecorator(finalMessage);
            }

			//determine if to apply bold format to message
            if(controller.formatBold.isSelected()) {
                finalMessage = new MessageBoldDecorator(finalMessage);
            }
			//sends message to server and display on the view
            out.writeObject(finalMessage);
            showMessageLocally(finalMessage);
        } catch (Exception e) {
            // TODO: Better exception handling
            e.printStackTrace();
        }
    }

	//Add message to a list using the main application thread
    private void showMessageLocally(final Message msg) {
        Platform.runLater(() -> chatList.addMessage(msg));
    }
}
