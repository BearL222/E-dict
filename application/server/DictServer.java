
package application.server;

import application.share.Connect;
import net.SocketStream;
import java.io.IOException;
import java.net.ServerSocket;

public class DictServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(Connect.SERVER_PORT)) {
			try (SocketStream client = new SocketStream(server.accept())) {
				System.out.println(client.readLine());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
