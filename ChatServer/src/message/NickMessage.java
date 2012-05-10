package message;

public class NickMessage extends Message {
	public static final String TYPE = "nick";
	
	public NickMessage() {
		super.type = TYPE;
	}
	
	public void setNick(String s) {
		values.put("nick", s);
	}
	
	public String getNick() {
		return values.get("nick");
	}
	
}
