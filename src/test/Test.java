package test;

import com.sun.javafx.application.PlatformImpl;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.text.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.net.URL;
import java.util.*;
import javafx.scene.paint.Color;

class Test {

	// Mock out the JavaFXController so that we can directly control what happens in our tests
	class MockController extends client.JavaFXController {

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			username = new TextField("TestUsername");
			formatBold = new CheckMenuItem();
			formatHighlight = new CheckMenuItem();
		}

	}

	// This output stream just keeps the latest written data in a string for easy retrieval
	class CustomOStreamKeepsLastData extends OutputStream {

		// Stores output
		private String current_output;

		// Saves a byte array to the output string
		@Override
		public void write(byte[] b) {
			current_output = new String(b);
		}

		// Saves a byte array to the output string
		@Override
		public void write(byte[] b, int off, int len) {
			write(b);
		}

		@Override
		public void write(int b) {
			throw new UnsupportedOperationException("CustomOStream.write(int b) invoked");
		}

		// Gets the current data
		public String getCurrentOutput() {
			return current_output;
		}

	}

	// Custom print stream to properly utilize the custom output stream
	class CustomPrintStream extends PrintStream {

		CustomPrintStream() {
			super (new CustomOStreamKeepsLastData());
		}

		@Override
		public void print(String s) {
			try {
				out.write(s.getBytes());
			} catch (Exception e) {}
		}

		@Override
		public void println(String s) {
			try {
				out.write((s + "\n").getBytes());
			} catch (Exception e) {}
		}

		public CustomOStreamKeepsLastData getOut() {
			return (CustomOStreamKeepsLastData)out;
		}

	}

	// Runs our tests
	public void run() throws IOException {

		// Makes Java think JavaFX has started up so that we don't actually have
		//  to start an application on the thread we want to test on.
		PlatformImpl.startup(() -> {});

		// Save output stream for later
		PrintStream old_out = System.out;

		// Replace stdout with a custom output stream
		CustomOStreamKeepsLastData costream = new CustomOStreamKeepsLastData();
		System.setOut(new CustomPrintStream());

		// Launch a server
		server.Server server_inst = new server.Server(4444, 4);
		Thread launch_server = new Thread(
			new Runnable() {
				@Override
				public void run() {
					server_inst.runServer();
				}
			}
		);
		launch_server.start();

		// Test the ConnectionManagerFacade class
		boolean pass = WhiteBoxConnectionManagerFacade(server_inst);

		// Display test results
		old_out.println("WhiteBoxConnectionManagerFacade:\n\t" + (pass ? "Passed" : "Failed"));

		// Test the Message classes
		pass = WhiteBoxMessages(server_inst);

		// Display test results
		old_out.println("WhiteBoxMessages:\n\t" + (pass ? "Passed" : "Failed"));

		// Replace the old output stream for the remainder of execution
		System.setOut(old_out);
	}

	// Test the ConnectionManagerFacade class
	public boolean WhiteBoxConnectionManagerFacade(server.Server server_inst) throws IOException {

		// Make a fake controller to feed into our test ConnectionManagerFacade
		MockController mc = new MockController();
		mc.initialize(null, null);
		client.ConnectionManagerFacade cmf = new client.ConnectionManagerFacade(
			"localhost", 4444,
			new client.ChatListObservable(),
			mc
		);

		// Send a test message to our server instance
		cmf.sendToServer(new client.MessageConcrete("TestUsername", "Test Message"));

		// Wait for the message transfer to complete
		try {
			Thread.sleep(500);
		} catch (Exception e) {}

		// Get the output produced by the server
		String output = ((CustomPrintStream)(System.out)).getOut().getCurrentOutput();

		// Test that the server received the message
		return output.contains("Test Message");
	}

	public boolean WhiteBoxMessages(server.Server server_inst) {
		client.Message msg = new client.MessageConcrete("TestUsername", "Test Message");

		msg = new client.MessageBoldDecorator(msg);
		msg = new client.MessageColorDecorator(msg);

		Text txt = msg.getStyledText();

		return txt.getText().contains("Test Message") &&
			   txt.getText().contains("TestUsername") &&
			   txt.getFont().getStyle().toLowerCase().contains("bold") &&
			   txt.getFill() == Color.BLUE;
	}

}
