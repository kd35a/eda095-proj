package server;

import common.Mailbox;

public class MessageConsumerThread extends Thread {
	
	private Mailbox messages;
	private boolean active;
	
	public MessageConsumerThread(Mailbox messages) {
		this.messages = messages;
		this.active = false;
	}
	
	public void run() {
		while (active) {
			String msg = (String) messages.get(); // Tries to fetch message
			
		}
	}

}
