package client;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

//this class sets the color of the message
public class MessageColorDecorator extends MessageDecorator {

    private Message delegate;

	//constructor
    public MessageColorDecorator(Message delegate) {
        this.delegate = delegate;
    }

	//this method set the color of the message Text to blue
    @Override
    public Text getStyledText() {
        Text styledText = delegate.getStyledText();
        styledText.setFill(Color.BLUE);
        return styledText;
    }
}
