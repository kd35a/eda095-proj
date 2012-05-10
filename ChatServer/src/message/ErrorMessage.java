package message;

public class ErrorMessage extends Message {
	public static final String TYPE = "error";
	
	public ErrorMessage() {
		super.type = TYPE;
	}
	
	public void setMsg(String s) {
		values.put("msg", s);
	}
	
	public String getMsg() {
		return (String) values.get("msg");
	}

}
