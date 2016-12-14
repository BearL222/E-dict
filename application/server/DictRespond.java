
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
		
		String[] msgRecv = client.readArray();
		if (Debug.DEBUG) System.out.println("Server: Recv: " + msgRecv[0]);
		
		client.printArray(new String[] { "Hello", "World", "!" });
	}
	
	@Override
	public void run() {
		while (true) {
			String[] msgRecv = client.readArray();
			if (Debug.DEBUG) System.out.println("Server: Recv: " + msgRecv[0]);
			
			if (msgRecv.length > 0) {
				if (msgRecv[0].compareTo("Search") == 0) {
					client.printArray(
						new String[] { new Search(0, msgRecv[1]).getMeaning(),
							"23", new Search(1, msgRecv[1]).getMeaning(), "31",
							new Search(2, msgRecv[1]).getMeaning(), "12" });
				}
			}
		}
	}
	
	@Override
	public void close() {
		client.close();
	}
}
