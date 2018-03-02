package client;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//this class sets the formatting of a message as bold
public class MessageBoldDecorator extends MessageDecorator {

    private Message delegate;

	// constructor
    public MessageBoldDecorator(Message delegate) {
        this.delegate = delegate;
    }

	//this method sets the bold formatting of the Text
    @Override
    public Text getStyledText() {
        Text styledText = delegate.getStyledText();;
        Font f = styledText.getFont();
        styledText.setFont(Font.font(f.getFamily(), FontWeight.BOLD, f.getSize()));
        return styledText;
    }
}
