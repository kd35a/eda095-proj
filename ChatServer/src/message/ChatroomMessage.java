package message;

public class ChatroomMessage extends Message {
	
	public ChatroomMessage() {
		super.type = "cm";
	}
	
	public void setRoom(String s) {
		values.put("room", s);
	}
	
	public String getRoom() {
		return values.get("room");
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return values.get("msg");
	}

}
