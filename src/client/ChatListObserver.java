package client;

import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Connects observable chat list to JavaFX's specific ObservableList
 */
public class ChatListObserver implements Observer {

    private ObservableList<Text> fxMessageData;

	//class constructor
    ChatListObserver (ObservableList<Text> fxMessageData) {
        this.fxMessageData = fxMessageData;
    }
	
	// this method is called when there is a new message entry added by the ChatListObserable class
    @Override
    public void update(Observable observable, Object o) {
        @SuppressWarnings("unchecked")
		//cast incoming messages to ArrayList
        ArrayList<Message> updatedMessages = (ArrayList<Message>) o;

        fxMessageData.clear();
		//loop through message lists and show in the UI control
        for(Message m : updatedMessages) {
            fxMessageData.add(m.getStyledText());
        }
    }
}
