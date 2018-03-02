package client;

import java.util.ArrayList;
import java.util.Observable;

public class ChatListObservable extends Observable{

    private ArrayList<Message> messages;

	// constructor
    public ChatListObservable() {
        messages = new ArrayList<>();
    }

	// this method adds the message to a list of available messages and notifies all connected observers
    void addMessage(Message message) {
        messages.add(message);
        setChanged();
        notifyObservers(messages);
    }
}
