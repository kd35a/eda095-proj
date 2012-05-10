package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.simple.parser.ParseException;

import message.Message;

import common.Mailbox;

public class ServerReadSocketThread extends Thread {

	private Socket socket;
	private boolean active; // TODO: USE ME!
	private BufferedReader in;
	private Mailbox<Message> messages; // Mailbox shared by all client communication threads
	private ClientConnection cc;

	public ServerReadSocketThread(Socket s, Mailbox<Message> messages, ClientConnection cc) {
		this.socket = s;
		this.messages = messages;
		this.active = true;
		this.cc = cc;
		try {
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
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
				//Puts a message in the shared mailbox
				String input = in.readLine();
				if (input == null) {
					// TODO: handle client shut down
				}
				Message msg = Message.fromJSON(input);
				msg.setFrom(cc.getNick());
				System.out.println("received " + msg.toJSON());
				messages.put(msg);
			} catch (IOException e) {
				System.out.println("Failed getting input from client "
						+ socket.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
