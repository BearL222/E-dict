
package application.server;

import application.share.Connect;
import java.io.IOException;
import java.net.ServerSocket;

public class DictServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(Connect.SERVER_PORT)) {
			while (true) /*for (int i = 0; i < 10; ++i)*/ {
				new DictRespond(server.accept()).run();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
