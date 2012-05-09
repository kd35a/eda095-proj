package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
<<<<<<< HEAD
import java.util.ArrayList;

import common.Mailbox;

import client.Client;
=======
>>>>>>> e1d857973060979f5187127c94f06f8bb741cb2b

/**
 * A Chat server. Is currently run by the main thread, and accepts connections
 * from multiple clients.
 */
public class Server {

	private String serverName; // TODO: USE ME!
	private int port;
	private Mailbox messages;

	public Server(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;
		this.messages = new Mailbox();
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
				Thread t = new ServerReadSocketThread(s, messages);
				t.start();
			}
		} catch (IOException e) {
			System.err.println("Failed setting up server socket.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server s = new Server("Quakenet", 4444);
		s.start();
	}

}
