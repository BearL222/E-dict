
package application.server;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;

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
		
		Map<Integer, Map<String, Boolean>> map = new HashMap<>();
		map.put(0, new HashMap<>());
		map.put(1, new HashMap<>());
		map.put(2, new HashMap<>());
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
								sql.getZan(0, word).toString(),
								map.get(0).get(word).toString(),
								new Search(1, word).getMeaning(),
								sql.getZan(1, word).toString(),
								map.get(1).get(word).toString(),
								new Search(2, word).getMeaning(),
								sql.getZan(2, word).toString(),
								map.get(2).get(word).toString(), });
						
					} else if (msgRecv[0].compareTo("Like") == 0) {
						int dictIndex = Integer.parseInt(msgRecv[1]);
						if (!map.get(dictIndex).containsKey(word)) {
							map.get(dictIndex).put(word, false);
						}
						
						if (map.get(dictIndex).get(word)) {
							sql.unZan(dictIndex, word);
							map.get(dictIndex).put(word, false);
						} else {
							sql.setZan(dictIndex, word);
							map.get(dictIndex).put(word, true);
						}
						
						client.printArray(new String[] {
							sql.getZan(dictIndex, word).toString(),
							map.get(word).toString(), });
						
					} else if (msgRecv[0].compareTo("SignIn") == 0) {
						client.printArray(new String[] { sql
							.signinUser(msgRecv[1], msgRecv[2]).toString() });
						
					} else if (msgRecv[0].compareTo("SignUp") == 0) {
						client.printArray(new String[] { sql
							.signupUser(msgRecv[1], msgRecv[2]).toString() });
						
					} else if (msgRecv[0].compareTo("SignOut") == 0) {
						client.printArray(new String[] {
							sql.signoutUser(msgRecv[1]).toString() });
						
					} else if (msgRecv[0].compareTo("GetUser") == 0) {
						client.printArray(sql.getOnlineUser());
						
					} else if (msgRecv[0].compareTo("Share") == 0) {
//						sql.setShareWord(msgRecv[1], msgRecv[2], msgRecv[3]);
						
					} else if (msgRecv[0].compareTo("GetWord") == 0) {
						String[][] shareWords = sql.getShareWord(msgRecv[1]);
						String[] msgSend = new String[shareWords.length];
						for (int i = 0; i < shareWords.length; ++i) {
							msgSend[i] = shareWords[i][0];
						}
						client.printArray(msgSend);
						
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
