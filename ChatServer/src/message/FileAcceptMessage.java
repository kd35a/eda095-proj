package message;

public class FileAcceptMessage extends Message {
	public static final String TYPE = "fileacc";
	
	public FileAcceptMessage() {
		super.type = TYPE;
	}
	
	public void setHost(String s) {
		values.put("host", s);
	}
	
	public String getHost() {
		return (String) values.get("host");
	}
	
	public void setPort(int i) {
		values.put("port", i);
	}
	
	public int getPort() {
		return (Integer) values.get("port");
	}
	
	public void setTo(String s) {
		values.put("to", s);
	}
	
	public String getTo() {
		return (String) values.get("to");
	}

}
