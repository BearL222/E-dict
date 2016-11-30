
package application.client;

import application.share.Connect;
import debug.Debug;
import java.io.*;
import java.net.Socket;
import javafx.stage.Stage;
import net.SocketStream;

public class DictEvent extends DictUI implements AutoCloseable {
	private static SocketStream server = null;
	
	public static void main(String[] args) {
		try {
			SocketStream server = new SocketStream(
				new Socket("127.0.0.1", Connect.SERVER_PORT));
			DictEvent.server = server;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (server == null) System.exit(Debug.ERROR_IO);
		
		server.println("Hello server.");
		if (Debug.DEBUG) System.out.println("Client: Send.");
		
		System.exit(Debug.ERROR_SUCCESS);
		
		// launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeComponent(primaryStage);
	}
	
	@Override
	public void close() {
		server.close();
	}
}
