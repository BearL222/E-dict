
package application.server;

import java.io.IOException;
import java.net.Socket;
import debug.Debug;
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
		if (client == null) System.exit(Debug.ERROR_IO);
	}
	
	@Override
	public void run() {
		if (Debug.DEBUG) System.out.println("Server: Recv: " + client.readLine());
	}
	
	@Override
	public void close() {
		client.close();
	}
}
