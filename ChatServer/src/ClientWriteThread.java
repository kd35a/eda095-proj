import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * This class writes messages to the associated Client-object.
 * The grand plan is to let each client thread have its own message-
 * storage, thus eliminating any blocks on the thread which distributes
 * the messages to all clients.
 */
public class ClientWriteThread extends Thread {
	private Socket s;
	private boolean active;
	private BufferedWriter bw;
	protected Mailbox mailbox;
	private Client client;
	
	public ClientWriteThread(Socket s, Client client) {
		this.s = s;
		this.client = client;
		this.active = true;
		this.mailbox = new Mailbox();
		
		try {			
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up outputstream for client " 
					+ s.getInetAddress());
			e.printStackTrace();
		}
	}
	
	public void run() {
		while (active) {
			try {
				bw.write(mailbox.getMessage());
			} catch (IOException e) {
				System.out.println("Failed sending output to client " 
						+ s.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
