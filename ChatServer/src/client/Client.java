package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import message.Message;

import common.Mailbox;

public class Client {

	private String serverName;
	private Socket socket;
	private Mailbox<Message> inbox;
	private Mailbox<Message> outbox;

	public Client(String host, int port) throws IOException {
		serverName = host;
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
		inbox = new Mailbox<Message>();
		outbox = new Mailbox<Message>();
		
		Thread t;
		t = new ClientReadSocketThread(socket, inbox);
		t.start();
		t = new ClientWriteSocketThread(socket, outbox);
		t.start();
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public void sendMessage(Message msg) {
		outbox.put(msg);
	}

}
