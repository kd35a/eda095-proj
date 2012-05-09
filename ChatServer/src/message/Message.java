package message;

public class Message {
	
	private String message;
	
	public Message(String s) {
		message = s;
	}
	
	public String toJSON() {
		return message;
	}

}
