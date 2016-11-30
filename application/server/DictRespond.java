
package application.server;

import java.io.IOException;
import java.net.Socket;
import net.SocketStream;

public class DictRespond extends Thread implements AutoCloseable {
	
	public SocketStream client = null;
	
	public DictRespond(Socket socket) {
		try {
			SocketStream client = new SocketStream(socket);
			this.client = client;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (client == null) System.exit(0);
	}
	
	@Override
	public void run() {
		System.out.println(client.readLine());
	}

	@Override
	public void close() {
		System.out.println("Close");
		client.close();
	}
}
