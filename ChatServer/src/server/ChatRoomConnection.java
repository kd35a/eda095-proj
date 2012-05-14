package server;

import java.util.ArrayList;
import java.util.List;

import message.Message;

public class ChatRoomConnection implements Broadcastable {
	
	private String name;
	private ArrayList<ClientConnection> clientConnections;
	
	public ChatRoomConnection(String name) {
		this.name = name;
		clientConnections = new ArrayList<ClientConnection>();
	}
	
	public void addConnection(ClientConnection cc) {
		clientConnections.add(cc);
	}

	public void removeConnection(ClientConnection cc) {
		clientConnections.remove(cc);
	}
	
	@Override
	public void broadcast(Message msg) {
		for (ClientConnection cc : clientConnections) {
			cc.sendMsg(msg);
		}
	}
	
	public List<String> getParticipants() {
		List<String> participants = new ArrayList<String>();
		for (ClientConnection client : clientConnections) {
			String nick = client.getNick();
			participants.add(nick);
		}
		return participants;
	}

}
