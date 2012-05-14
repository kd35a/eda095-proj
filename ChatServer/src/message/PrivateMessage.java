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
		return (String) values.get("to");
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return (String) values.get("msg");
	}

}
