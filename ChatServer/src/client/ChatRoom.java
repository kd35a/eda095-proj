package client;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
	private ArrayList<String> participants;
	
	public ChatRoom() {
		participants = new ArrayList<String>();
	}
	
	public void setParticipants(List<String> participants) {
		participants = new ArrayList<String>(participants);
	}
	
	public ArrayList<String> getParticipants() {
		return participants;
	}

}
