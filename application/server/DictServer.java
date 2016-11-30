
package application.server;

import application.share.Connect;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class DictServer {
	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(Connect.serverPort)) {
			BufferedReader reader = new BufferedReader(
				new InputStreamReader(server.accept().getInputStream()));
			String s;
			while ((s = reader.readLine()) == null);
			System.out.println(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
