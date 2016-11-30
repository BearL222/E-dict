
package application.client;

import application.share.Connect;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.stage.Stage;
import net.SocketStream;

public class DictEvent extends DictUI {
	private SocketStream server = null;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try (SocketStream server = new SocketStream(
			new Socket("127.0.0.1", Connect.SERVER_PORT))) {
			this.server = server;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (this.server == null) System.exit(0);
		
		initializeComponent(primaryStage);
	}
}
