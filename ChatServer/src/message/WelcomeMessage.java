package message;

public class WelcomeMessage extends Message {
	public static final String TYPE = "welcome";
	
	public WelcomeMessage() {
		super.type = TYPE;
	}
	
	public void setName(String s) {
		values.put("name", s);
	}
	
	public String getNick() {
		return values.get("name");
	}

}
