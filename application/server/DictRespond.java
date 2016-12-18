
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
		if (client == null)
			System.exit(Debug.ERROR_IO);

		this.sql = sql;

		String[] msgRecv = client.readArray();
		if (Debug.DEBUG)
			System.out.println("Server: Recv: " + msgRecv[0]);

		client.printArray(new String[] { "Hello", "World", "!" });
	}

	@Override
	public void run() {

		String word = "";
		while (true) {
			String[] msgRecv = client.readArray();
			if (msgRecv == null)
				break;
			if (Debug.DEBUG)
				System.out.println("Server: Recv: " + msgRecv[0]);

			try {
				if (msgRecv.length > 0) {
					if (msgRecv[0].compareTo("Search") == 0) {
						word = msgRecv[1];
						client.printArray(
								new String[] { new Search(0, word).getMeaning(), sql.getZan(0, word).toString(),
										new Search(1, word).getMeaning(), sql.getZan(1, word).toString(),
										new Search(2, word).getMeaning(), sql.getZan(2, word).toString() });

					} else if (msgRecv[0].compareTo("AddLike") == 0) {
						int dictIndex = Integer.parseInt(msgRecv[1]);
						sql.setZan(dictIndex, word);
						client.printArray(new String[] { sql.getZan(dictIndex, word).toString() });

					} else if (msgRecv[0].compareTo("SignIn") == 0) {
						client.printArray(new String[] { sql.signinUser(msgRecv[1], msgRecv[2]).toString() });

					} else if (msgRecv[0].compareTo("SignUp") == 0) {
						client.printArray(new String[] { sql.signupUser(msgRecv[1], msgRecv[2]).toString() });

					} else if (msgRecv[0].compareTo("SignOut") == 0) {
						client.printArray(new String[] { sql.signoutUser(msgRecv[1]).toString() });

					} else if (msgRecv[0].compareTo("GetUser") == 0) {
						client.printArray(sql.getOnlineUser());

					}
				}
			} catch (NumberFormatException | SQLException e) {
				e.printStackTrace();
			}
		}
		if (Debug.DEBUG)
			System.out.println("Server: Thread exit.");
	}

	@Override
	public void close() {
		client.close();
	}
}
