package client;

import java.util.List;
import java.util.Vector;

public class ChatRoom {
	private Vector<String> participants;
	
	public ChatRoom() {
		participants = new Vector<String>();
	}
	
	public void setParticipants(List<String> participants) {
		this.participants = new Vector<String>(participants);
	}
	
	public Vector<String> getParticipants() {
		return participants;
	}

	public void addParticipant(String name) {
		participants.add(name);
	}

	public void removeParticipant(String name) {
		participants.remove(name);
	}

}
