package server;

import message.Message;
import common.Mailbox;

public class ClientConnection {
	
	private static int userNbr = 0;
	
	private String nick;
	private Mailbox<Message> outgoing;
	
	/** Storage class for a client connection. Holds mailbox
	 *  for outgoing messages, and the users current nickname.
	 *  Nickname defaults to "Anon#userNbr".
	 *  
	 * @param outgoing	The mailbox for messages to this client.
	 */
	public ClientConnection(Mailbox<Message> outgoing) {
		this.outgoing = outgoing;
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
}
