import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This thread handles the ingoing communication with the client.
 * What we probably want this class to do:
 * 
 * 1. Read message from the BufferedReader.
 * 2. Store message in some global storage.
 * 3. Let producer thread pick up the message and
 *    send it to the concerned parties.
 */
public class ClientReadThread extends Thread {
	private Socket s;
	private boolean active; 	// TODO: USE ME!
	private BufferedReader br;	// BufferedReader == no fuzz when 
								// working with strings
	private Client client;
	
	public ClientReadThread(Socket s, Client client) {
		this.s = s;
		this.client = client;
		this.active = true; 
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up streams for client " 
					+ s.getInetAddress());
			e.printStackTrace();
		}
	}
	
	/**
	 * Polls input from the connected client. Currently only
	 * prints out the input data. Want to do something fancy
	 * with it (read: store it)
	 */
	public void run() {
		while (active) {
			try {
				System.out.println(br.readLine());
			} catch (IOException e) {
				System.out.println("Failed getting input from client " 
						+ s.getInetAddress());
				e.printStackTrace();
			}
		}
	}

}
