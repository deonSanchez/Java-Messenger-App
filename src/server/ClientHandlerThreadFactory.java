package server;

import java.net.Socket;

import static java.util.UUID.randomUUID;

class ClientHandlerThreadFactory {
    private static ClientHandlerThreadFactory chtf;

    private ClientHandlerThreadFactory() {
        // TODO: In this factory, handle "rooms", etc.
    }

	//this is to create ClientHandler object using
    ClientHandler create(Server server, Socket clientSocket) {
        return new ClientHandler(server, clientSocket, randomUUID().toString());
    }

	//This is to return Singleton object of ClientHandlerThreadFactory
    static synchronized ClientHandlerThreadFactory getInstance() {
        if (chtf == null) {
            chtf = new ClientHandlerThreadFactory();
        }
        return chtf;
    }
}
