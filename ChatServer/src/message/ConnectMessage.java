package message;

public class ConnectMessage extends Message {
	public static final String TYPE = "connect";
	
	public ConnectMessage() {
		super.type = TYPE;
	}
	
	public void setNick(String s) {
		values.put("nick", s);
	}
	
	public String getNick() {
		return (String) values.get("nick");
	}

}
