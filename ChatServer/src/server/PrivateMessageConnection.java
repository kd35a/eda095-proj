package server;

import message.Message;

public class PrivateMessageConnection implements Broadcastable {
	
	private ClientConnection cc;

	public PrivateMessageConnection(ClientConnection cc) {
		this.cc = cc;
	}
	
	@Override
	public void addConnection(ClientConnection cc) {
		/* Do nothing */
	}

	@Override
	public void broadcast(Message msg) {
		cc.sendMsg(msg);
	}

	@Override
	public void removeConnection(ClientConnection cc) {
		/* Do nothing */
	}

}
