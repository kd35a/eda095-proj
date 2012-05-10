package message;

public class JoinMessage extends Message {
	public static final String TYPE = "join";
	
	public JoinMessage() {
		super.type = TYPE;
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return (String) values.get("room");
	}

}
