package message;

public class PartMessage extends Message {
	
	public PartMessage() {
		super.type = "part";
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return values.get("room");
	}

}
