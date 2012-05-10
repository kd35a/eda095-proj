package message;

public class PartMessage extends Message {
	public static final String TYPE = "part";
	
	public PartMessage() {
		super.type = TYPE;
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return (String) values.get("room");
	}

}
