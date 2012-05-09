import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * A Chat server. Is currently run by the main thread,
 * and accepts connections from multiple clients. 
 */
public class Server {
	private String serverName; // TODO: USE ME!
	private int port;
	
	/*
	 * This list will be used by the Producer thread, which will
	 * add messages to be sent to the clients in their mailboxes.
	 */
	private ArrayList<Client> clientList;
	
	public Server(String serverName, int port) {
		this.serverName = serverName;
		this.port = port;
		this.clientList = new ArrayList<Client>();
	}
	
	public void start() {
		try {
			ServerSocket ss = new ServerSocket(port);
			while(true) {
				/* Gets a socket to the client by accepting the connection,
				 * and creates a Client object. */
				Socket s = ss.accept();
				Client c = new Client(s);
				clientList.add(c);
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
