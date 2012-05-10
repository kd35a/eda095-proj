package message;

public class PrivateMessage extends Message {
	public static final String TYPE = "pm";
	
	
	public PrivateMessage () {
		super.type = TYPE;
	}
	
	public void setTo(String s) {
		values.put("to", s);
	}
	
	public String getTo() {
		return values.get("to");
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return values.get("msg");
	}
	
	public void setSender(String s) {
		values.put("sender", s);
	}
	
	public String getSender() {
		return values.get("sender");
	}

}
