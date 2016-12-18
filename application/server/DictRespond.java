
package application.server;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import debug.Debug;
import net.SocketStream;

public class DictRespond extends Thread implements AutoCloseable {
	
	public SocketStream client = null;
	public DB sql = null;
	
	public DictRespond(Socket socket, DB sql) {
		try {
			SocketStream client = new SocketStream(socket);
			this.client = client;
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (client == null) System.exit(Debug.ERROR_IO);
		
		this.sql = sql;
		
		String[] msgRecv = client.readArray();
		if (Debug.DEBUG) System.out.println("Server: Recv: " + msgRecv[0]);
		
		client.printArray(new String[] { "Hello", "World", "!" });
	}
	
	@Override
	public void run() {
		
		String word = "";
		while (true) {
			String[] msgRecv = client.readArray();
			if (msgRecv == null) break;
			if (Debug.DEBUG) System.out.println("Server: Recv: " + msgRecv[0]);
			
			try {
				if (msgRecv.length > 0) {
					if (msgRecv[0].compareTo("Search") == 0) {
						word = msgRecv[1];
						client.printArray(
							new String[] { new Search(0, word).getMeaning(),
								"23", new Search(1, word).getMeaning(), "31",
								new Search(2, word).getMeaning(), "12" });
						
					} else if (msgRecv[0].compareTo("AddLike") == 0) {
						sql.setZan(Integer.parseInt(msgRecv[1]), word);
						
					} else if (msgRecv[0].compareTo("SignIn") == 0) {
						
					} else if (msgRecv[0].compareTo("SignUp") == 0) {
						
					} else if (msgRecv[0].compareTo("SignOut") == 0) {
						
					}
				}
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
		}
		if (Debug.DEBUG) System.out.println("Server: Thread exit.");
	}
	
	@Override
	public void close() {
		client.close();
	}
}
