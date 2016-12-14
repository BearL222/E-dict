
package net;

import java.io.*;
import java.net.Socket;

public class SocketStream implements AutoCloseable {
	private final PrintStream printer;
	private final BufferedReader reader;
	
	public SocketStream(Socket socket) throws IOException {
		printer = new PrintStream(socket.getOutputStream());
		reader =
			new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void print(String str) {
		printer.print(str);
	}
	
	public void println(String str) {
		printer.println(str);
	}
	
	public String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String readLine(String str) {
		println(str);
		return readLine();
	}
	
	public void printArray(String[] msg) {
		println(Convert.getSend(msg));
	}
	
	public String[] readArray() {
		return Convert.getRecv(readLine());
	}
	
	public String[] readArray(String[] msg) {
		printArray(msg);
		return readArray();
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
