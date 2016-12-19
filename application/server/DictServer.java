
package application.server;

import application.share.Connect;

import java.io.IOException;
import java.net.ServerSocket;

public class DictServer {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(Connect.SERVER_PORT)) {
			while (true) { /* for (int i = 0; i < 10; ++i) */
				// new DictRespond(server.accept(), null).run();
				try {
					new DictRespond(server.accept(), new DB()).run();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
