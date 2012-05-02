import java.net.Socket;

/**
 * Storage-class for a connected client. Handles the read- and write-
 * threads. This class could later on hold more interesting information,
 * such as nickname, chatrooms etc.
 */
public class Client {
	private Socket sock;
	private ClientReadThread crt;
	private ClientWriteThread cwt;
	
	public Client(Socket sock) {
		this.sock = sock;
		crt = new ClientReadThread(sock, this);
		cwt = new ClientWriteThread(sock, this);
		crt.start();
		cwt.start();
	}
	
	/**
	 * Not sure that we will use this. The plan was to go through the 
	 * Client-object in order to push the message all the way to the 
	 * ClientWriteThread. It is not very pretty at the moment.
	 */
	public synchronized void sendMessage(String msg) {
		cwt.mailbox.addMessage(msg);
	}

}
