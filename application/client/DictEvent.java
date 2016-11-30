
package application.client;

import application.share.Connect;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.stage.Stage;
import net.SocketStream;

public class DictEvent extends DictUI {
	public static void main(String[] args) {
		// launch(args);
		try (SocketStream server = new SocketStream(
			new Socket("127.0.0.1", Connect.SERVER_PORT))) {
			server.println("Hello Socket");
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
