
package net;

import java.io.*;
import java.net.Socket;

public class SocketStream implements AutoCloseable {
	private final PrintStream printer;
	private final BufferedReader reader;
	
	public SocketStream(Socket socket) throws IOException {
		printer = new PrintStream(socket.getOutputStream());
		reader = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
	}
	
	public String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void print(String str) {
		printer.print(str);
	}
	
	public void println(String str) {
		printer.println(str);
	}
	
	@Override
	public void close() {
		printer.close();
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
