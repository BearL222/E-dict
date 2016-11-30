
package application.client;

import application.share.Connect;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.stage.Stage;

public class DictEvent extends DictUI {
	public static void main(String[] args) {
		// launch(args);
		try (Socket client = new Socket("127.0.0.1", Connect.SERVER_PORT)) {
			PrintStream writer = new PrintStream(client.getOutputStream());
			writer.println("Hello Socket");
			// writer.flush();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeComponent(primaryStage);
	}
}
