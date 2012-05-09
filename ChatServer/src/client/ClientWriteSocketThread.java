package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * This class writes messages to the associated Client-object.
 * The grand plan is to let each client thread have its own message-
 * storage, thus eliminating any blocks on the thread which distributes
 * the messages to all clients.
 */
public class ClientWriteSocketThread extends Thread {
	
	private Socket socket;
	private boolean active;
	private BufferedWriter out;
	
	public ClientWriteSocketThread(Socket s) {
		socket = s;
		active = true;
		try {			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up output stream for server " 
					+ socket.getInetAddress());
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (active) {
			try {
				out.write("ASD");
			} catch (IOException e) {
				System.out.println("Failed sending output to server " 
						+ socket.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
