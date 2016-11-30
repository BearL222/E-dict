
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
			System.out.println(reader.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
