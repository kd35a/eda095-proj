package message;

import java.util.List;

public class ListParticipantsMessage extends Message {
	public static final String TYPE = "list";
	
	public ListParticipantsMessage() {
		super.type = TYPE;
	}
	
	public void setParticipants(List<String> list) {
		values.put("participants", list);
	}
	
	public List<String> getParticipants() {
		return (List<String>) values.get("participants");
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return (String) values.get("room");
	}

}
