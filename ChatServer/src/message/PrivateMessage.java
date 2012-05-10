package message;

public class PrivateMessage extends Message {
	
	
	public PrivateMessage () {
		super.type = "pm";
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
	
	public void setSender(String s) {
		values.put("sender", s);
	}
	
	public String getSender() {
		return (String) values.get("sender");
	}

}
