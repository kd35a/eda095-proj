package message;

public class WelcomeMessage extends Message {
	
	public WelcomeMessage() {
		super.type = "welcome";
	}
	
	public void setName(String s) {
		values.put("name", s);
	}
	
	public String getName() {
		return (String) values.get("name");
	}

}
