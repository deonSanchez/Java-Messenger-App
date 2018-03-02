package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//this is the main class of JavaFx application it is an entry point
public class Main extends Application {

	//the start Method performs initial javafx configuration
    @Override
    public void start(Stage primaryStage) throws Exception{

		//Instantiate fxmlLoader object and specify the view to be displayed on the screen
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("sample.fxml"));
        Parent root = fxmlLoader.load();
		// set the title of the view 
        primaryStage.setTitle("Instant Messenger");
		//set the width and height
        primaryStage.setScene(new Scene(root, 600, 500));

        // When close button is pressed, it will stop the client and close the app
        primaryStage.setOnCloseRequest((event) -> {
            Platform.exit();
            System.exit(0);
        });
		
		// this displays the loaded view
        primaryStage.show();
        fxmlLoader.getController();
    }

	//this method will be called when launched from command line
    public static void main(String[] args) {
        launch(args);
    }
}
