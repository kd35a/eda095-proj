package message;

public class ConnectMessage extends Message {
	
	public ConnectMessage() {
		super.type = "connect";
	}
	
	public void setNick(String s) {
		values.put("nick", s);
	}
	
	public String getNick() {
		return (String) values.get("nick");
	}

}
