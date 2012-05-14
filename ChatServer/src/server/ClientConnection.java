package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.sun.swing.internal.plaf.synth.resources.synth;

import message.Message;
import common.Mailbox;

public class ClientConnection {
	
	private static int userNbr = 0;
	
	private String nick;
	private Mailbox<Message> outgoing;
	private Socket socket;
	private ArrayList<Broadcastable> connections;
	
	/** Storage class for a client connection. Holds mailbox
	 *  for outgoing messages, and the users current nickname.
	 *  Nickname defaults to "Anon#userNbr".
	 *  
	 * @param outgoing	The mailbox for messages to this client.
	 */
	public ClientConnection(Mailbox<Message> outgoing, Socket s) {
		this.outgoing = outgoing;
		this.socket = s;
		connections = new ArrayList<Broadcastable>();
		nick = "Anon" + userNbr++;
	}
	
	public synchronized void setNick(String s) {
		this.nick = s;
	}
	
	public synchronized String getNick() {
		return this.nick;
	}
	
	public synchronized void sendMsg(Message m) {
		outgoing.put(m);
	}
	
	public synchronized void addConnection(Broadcastable conn) {
		connections.add(conn);
	}
	
	public synchronized void removeConnection(Broadcastable conn) {
		connections.remove(conn);
	}
	
	public synchronized ArrayList<Broadcastable> getConnections() {
		return connections;
	}
	
	public synchronized String getHostname() {
		return socket.getInetAddress().getHostAddress();
	}
	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
