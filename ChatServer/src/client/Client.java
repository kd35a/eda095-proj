package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import clientgui.ChatWindow;

import message.Message;

import common.Mailbox;

public class Client {

	private Socket socket;

	public Client(String host, int port, Mailbox<Message> inbox,
			Mailbox<Message> outbox) throws IOException {
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);

		Thread t;
		t = new ClientReadSocketThread(socket, inbox);
		t.start();
		t = new ClientWriteSocketThread(socket, outbox);
		t.start();
	}

}
