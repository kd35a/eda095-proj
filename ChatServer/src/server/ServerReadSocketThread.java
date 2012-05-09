package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReadSocketThread extends Thread {

	private Socket socket;
	private boolean active; // TODO: USE ME!
	private BufferedReader in;

	public ServerReadSocketThread(Socket s) {
		socket = s;
		active = true;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up input stream for client "
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
				System.out.println("Failed getting input from client "
						+ socket.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
