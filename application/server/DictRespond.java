
package application.server;

import java.io.IOException;
import java.net.Socket;

import application.share.Convert;
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
		String[] msgRecv = Convert.getRecv(client.readLine());
		if (Debug.DEBUG) System.out.println("Server: Recv: " + msgRecv[0]);
		
		String[] msgSend =
			new String[] { new Search(0, msgRecv[0]).getMeaning(),
				new Search(1, msgRecv[0]).getMeaning(),
				new Search(2, msgRecv[0]).getMeaning() };
		client.println(Convert.getSend(msgSend));
	}
	
	@Override
	public void close() {
		client.close();
	}
}
