package client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Optional;
import java.util.ResourceBundle;

//this is Javafx controller class for view sample.fxml
public class JavaFXController implements Initializable{

	// fields decaration
    private String user;
    private Optional<Pair<String, String>> result;
    public TextField username;

    private ConnectionManagerFacade cmf;
    private ChatListObservable chatListObservable;

	//JavaFx controls injection from sample.fxml file
    @FXML
    public CheckMenuItem formatHighlight;

    @FXML
    public CheckMenuItem formatBold;

    @FXML
    private ListView<Text> chatMain;

    @FXML
    private TextField chatEnter;

	//the controller initialize method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
		//instantiate the needed list and objects
        ObservableList<Text> fxMessageData = FXCollections.observableArrayList();
        ChatListObserver chatListObserver = new ChatListObserver(fxMessageData);

        chatMain.setItems(fxMessageData);
		// add list to observer object
        chatListObservable = new ChatListObservable();
        chatListObservable.addObserver(chatListObserver);
    }

	// this method is called when attempting to connect to the server
    @FXML
    private void joinServer(ActionEvent event) {
        Dialog<Pair<String, String>> pairDialog = new Dialog<>();
        pairDialog.setTitle("Join Server");
        pairDialog.setHeaderText("Welcome to the Messenger App");
		
		// set button text
        ButtonType submitButton = new ButtonType("submit", ButtonBar.ButtonData.OK_DONE);
        pairDialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);
		
		//set gridpane properties
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
		
		//set username text field control properties
        username = new TextField();
        username.setPromptText("username");
        TextField serverIP = new TextField();
        serverIP.setText("localhost:4444");
		
		// add the controls to the grid
        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Server IP: "), 0, 1);
        grid.add(serverIP, 1, 1);

		// determine if to disable checkbox checkinfo
        Node checkInfo = pairDialog.getDialogPane().lookupButton(submitButton);
        checkInfo.setDisable(true);
		
		//add a listerner event to username text field control
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            checkInfo.setDisable(newValue.trim().isEmpty());
        });

        pairDialog.getDialogPane().setContent(grid);
		
		// run on a separate UI thread
        Platform.runLater(() -> username.requestFocus());
		
		//set dialog information
        pairDialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButton) {
                return new Pair<>(username.getText(), serverIP.getText());
            }
            return null;
        });

        result = pairDialog.showAndWait();

		// attempt to check if server ip address is supplied
        result.ifPresent(usernameServerIP -> {
            if (result.isPresent()) {
                String[] serverUrl = usernameServerIP.getValue().split(":");

				//validates the server url and displays an alert if url is not valid
                if (serverUrl.length != 2) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error:");
                    alert.setContentText("Could not parse URL. Please try again");
                    alert.showAndWait();
                    return;
                }

                try {
					//establishing connection to the server through the connectionmanagerfacade object
                    cmf = new ConnectionManagerFacade(serverUrl[0], Integer.parseInt(serverUrl[1]), chatListObservable, this);
                    cmf.start();
                } catch (IOException e) {
					// this shows an alert error message to the user
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error:");
                    alert.setContentText("Could not get connection to server. Please try again");
                    alert.showAndWait();
                }

            } else {
				// this shows an alert error message to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error:");
                alert.setContentText("Please enter a server to join.");
                alert.showAndWait();
            }
        });
    }

	//this method is called with the close button is clicked
    @FXML
    private void closeButton (ActionEvent event) {
		//to exit the application
        System.exit(0);
    }

	//this method is called when the user clicks the send message button
    @FXML
    private void handleButtonAction(ActionEvent event) {
		//sends message to the server
        MessageConcrete message = new MessageConcrete(username.getText(), chatEnter.getText());
        cmf.sendToServer(message);
        chatEnter.clear();
    }

	//this method is called when the user presses the enter key to send message
    @FXML
    public void handleKeyPressed(KeyEvent ke) {
		//checks if key pressed is enter key
        if (ke.getCode() == (KeyCode.ENTER)) {
			//send message
            Message message = new MessageConcrete(username.getText(), chatEnter.getText());
            cmf.sendToServer(message);
            chatEnter.clear();
        }
    }
}
