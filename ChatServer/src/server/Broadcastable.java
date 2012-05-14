package server;

import message.Message;

public interface Broadcastable {

	public void broadcast(Message msg);
	
	public void addConnection(ClientConnection cc);

	public void removeConnection(ClientConnection cc);
	
}
