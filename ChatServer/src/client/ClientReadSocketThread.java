package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This thread handles the ingoing communication with the client. What we
 * probably want this class to do:
 * 
 * 1. Read message from the BufferedReader. 2. Store message in some global
 * storage. 3. Let producer thread pick up the message and send it to the
 * concerned parties.
 */
public class ClientReadSocketThread extends Thread {

	private Socket socket;
	private boolean active; // TODO: USE ME!
	private BufferedReader in;

	public ClientReadSocketThread(Socket s) {
		socket = s;
		active = true;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up input stream for server "
					+ socket.getInetAddress());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (active) {
			try {
				System.out.println(in.readLine());
			} catch (IOException e) {
				System.out.println("Failed getting input from server "
						+ socket.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
