package server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import message.Message;

import common.Mailbox;

public class ServerWriteSocketThread extends Thread {
	
	private Socket socket;
	private Mailbox<Message> outgoing;
	private boolean active;
	private PrintWriter out;
	
	public ServerWriteSocketThread(Socket s, Mailbox<Message> outgoing) {
		this.socket = s;
		this.outgoing = outgoing;
		this.active = true;
		try {
			this.out = new PrintWriter(new BufferedOutputStream(socket
					.getOutputStream()), true);
		} catch (IOException e) {
			System.err.println("Failed setting up output stream for client "
				+ socket.getInetAddress());
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (active) {
			Message msg = outgoing.get();
			out.println(msg.toJSON());
			System.out.println("Just sent " + msg.toJSON() + " to " + socket.getInetAddress());
			if (out.checkError() && active && !socket.isClosed()) {
				System.err.println("Could not write to out-stream:");
			} else if (socket.isClosed()) {
				return;
			}
		}
	}

	public void disconnect() {
		active = false;
		out.close();
	}

}
