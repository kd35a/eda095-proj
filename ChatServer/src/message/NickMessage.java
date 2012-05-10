package message;

public class NickMessage extends Message {
	
	public NickMessage() {
		super.type = "nick";
	}
	
	public void setNick(String s) {
		values.put("nick", s);
	}
	
	public String getNick() {
		return values.get("nick");
	}
	
}