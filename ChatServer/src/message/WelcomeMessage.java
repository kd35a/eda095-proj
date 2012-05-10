package message;

public class WelcomeMessage extends Message {
	public static final String TYPE = "welcome";
	
	public WelcomeMessage() {
		super.type = TYPE;
	}
	
	public void setName(String s) {
		values.put("name", s);
	}
	
	public String getName() {
		return (String) values.get("name");
	}

}
