package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import message.Message;

import common.Mailbox;
/**
 * A Chat server. Is currently run by the main thread, and accepts connections
 * from multiple clients.
 */
public class Server {
	private String serverName; // TODO: USE ME!
	private int port;
	private Mailbox<Message> messages;
	private ConcurrentHashMap<String, ClientConnection> clientList;

	public Server(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;
		this.messages = new Mailbox<Message>();
		this.clientList = new ConcurrentHashMap<String, ClientConnection>();
		
		/* Sets up the consumer thread, which will poll messages
		 * from the mailbox which all clients send to. */
		MessageConsumerThread consumer = new MessageConsumerThread(serverName, clientList, messages);
		consumer.start();
	}

	public void start() {
		try {
			ServerSocket ss = new ServerSocket(port);
			while (true) {
				/*
				 * Gets a socket to the client by accepting the connection, and
				 * creates a Client object.
				 */
				Socket s = ss.accept();
				
				/* Create the corresponding client connection */
				Mailbox<Message> outgoing = new Mailbox<Message>();
				ClientConnection cc = new ClientConnection(outgoing, s);
				clientList.put(cc.getNick(), cc);
				System.out.println("Client " + cc.getNick() + " connected.");
				/* Starting threads */
				Thread read = new ServerReadSocketThread(s, messages, cc);
				read.start();
				Thread write = new ServerWriteSocketThread(s, outgoing);
				write.start();
			}
		} catch (IOException e) {
			System.err.println("Failed setting up server socket.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Usage: <name> <port>");
		}
		String name = args[0];
		int port = Integer.parseInt(args[1]);
		Server s = new Server(name, port);
		s.start();
	}

}
