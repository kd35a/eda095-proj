package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Storage-class for a connected client. Handles the read- and write- threads.
 * This class could later on hold more interesting information, such as
 * nickname, chatrooms etc.
 */
public class Client {

	private Socket socket;

	public Client(String host, int port) throws IOException {
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
		Thread t;
		t = new ClientReadSocketThread(socket);
		t.start();
		t = new ClientWriteSocketThread(socket);
		t.start();
	}

	public static void main(String[] args) {
		try {
			Client c = new Client("localhost", 1234);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
