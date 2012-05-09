package client;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import message.Message;

import common.Mailbox;

public class ClientWriteSocketThread extends Thread {

	private boolean active;
	private Socket socket;
	private PrintWriter out;
	private Mailbox<Message> in;

	public ClientWriteSocketThread(Socket s, Mailbox<Message> box) {
		active = true;
		socket = s;
		in = box;
		try {
			out = new PrintWriter(new BufferedOutputStream(socket
					.getOutputStream()), true);
		} catch (IOException e) {
			System.err.println("Failed setting up output stream for server "
					+ socket.getInetAddress());
			e.printStackTrace();
		}
	}

	public void run() {
		while (active) {
			Message m = in.get();
			out.println(m.toJSON());
		}
	}

}
