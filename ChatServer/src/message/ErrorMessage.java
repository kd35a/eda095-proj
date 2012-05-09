package message;

public class ErrorMessage extends Message {
	
	public ErrorMessage() {
		super.type = "error";
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return values.get("msg");
	}

}
