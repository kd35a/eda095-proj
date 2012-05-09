package message;

public class PrivateMessage extends Message {
	
	
	public PrivateMessage () {
		super.type = "pm";
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

}
