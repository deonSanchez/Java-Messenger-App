package client;

import javafx.scene.text.Text;

import java.io.Serializable;

public class MessageConcrete implements Message, Serializable {

//field declaration
    private String sender;
    private String message;

	// constructor with sender and message as the argument
    public MessageConcrete (String sender, String message) {
        this.sender = sender;
        this.message = message;
    }
	//reconstruct message and append sender information to it
    @Override
    public Text getStyledText() {
        return new Text(sender + ": " + message);
    }

	//convert message object to string
    public String toString() {
        return message;
    }
}
