package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import message.Message;

import common.Mailbox;

public class Client {

	private Socket socket;

	public Client(String host, int port) throws IOException {
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
		
		Mailbox<Message> inbox = new Mailbox<Message>();
		Mailbox<Message> outbox = new Mailbox<Message>();
		
		Thread t;
		t = new ClientReadSocketThread(socket, inbox);
		t.start();
		t = new ClientWriteSocketThread(socket, outbox);
		t.start();
	}

	public static void main(String[] args) {
		try {
			new Client("localhost", 1234);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
