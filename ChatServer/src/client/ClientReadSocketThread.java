package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import message.Message;
import message.MessageFactory;

import common.Mailbox;

public class ClientReadSocketThread extends Thread {

	private boolean active;
	private Socket socket;
	private BufferedReader in;
	private Mailbox<Message> out;

	public ClientReadSocketThread(Socket s, Mailbox<Message> box) {
		active = true;
		socket = s;
		out = box;
		try {
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
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
				String line = in.readLine();
				Message m = MessageFactory.create(line);
				out.put(m);
			} catch (IOException e) {
				System.out.println("Failed getting input from server "
						+ socket.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
