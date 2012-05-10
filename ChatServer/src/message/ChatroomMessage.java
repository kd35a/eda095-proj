package message;

public class ChatroomMessage extends Message {
	public static final String TYPE = "cm";
	
	public ChatroomMessage() {
		super.type = TYPE;
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return (String) values.get("room");
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return (String) values.get("msg");
	}

}
