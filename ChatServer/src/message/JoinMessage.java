package message;

public class JoinMessage extends Message {
	
	public JoinMessage() {
		super.type = "join";
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return values.get("room");
	}

}
