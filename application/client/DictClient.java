
package application.client;

import application.share.Connect;
import application.share.DictInfo;
import debug.Debug;
import net.SocketStream;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import javafx.stage.Stage;

public class DictClient extends DictUI implements AutoCloseable {
	public static void main(String[] args) {
		if (Debug.DEBUG)
			System.out.println(System.getProperty("user.dir"));
		if (Debug.DEBUG)
			System.out.println(Arrays.toString(args));

		try {
			SocketStream server = new SocketStream(new Socket("114.212.131.118", Connect.SERVER_PORT));
			DictClient.server = server;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (server == null)
			System.exit(Debug.ERROR_IO);

		String[] msgSend = new String[] { "apple" };
		server.printArray(msgSend);
		if (Debug.DEBUG)
			System.out.println("Hello server.");

		String[] msgRecv = server.readArray();
		System.out.println(msgRecv[0]);
		System.out.println(msgRecv[1]);
		System.out.println(msgRecv[2]);

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initializeComponent(primaryStage);
	}

	@Override
	public void close() {
		server.close();
	}

	@Override
	protected void btnSearch(String s) {
//		if (s.compareTo("") == 0) {
//			return;
//		}
		String[] msgRecv = server.readArray(new String[] { "Search", s });

		if (msgRecv.length == DictInfo.info.length * 3) {
			setCardsMsg(msgRecv);
		}
		updateCards();
	}

	@Override
	protected void btnUser() {
		new SignUI(server, lblUser).start();
	}

	@Override
	protected void btnShare() {
		new ShareUI(server, lblUser.getText(), txtInput);
	}

}
