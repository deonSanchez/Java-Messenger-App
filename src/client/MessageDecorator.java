package client;


import javafx.scene.text.Text;

import java.io.Serializable;

//MessageDecorator abstract class
public abstract class MessageDecorator implements Message, Serializable {
    public abstract Text getStyledText();
}
