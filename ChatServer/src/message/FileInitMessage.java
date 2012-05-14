package message;

public class FileInitMessage extends Message {
	public static final String TYPE = "fileinit";
	
	public FileInitMessage() {
		super.type = TYPE;
	}
	
	public void setFilename(String s) {
		values.put("filename", s);
	}
	
	public String getFilename() {
		return (String) values.get("filename");
	}
	
	public void setSize(int i) {
		values.put("size", i);
	}
	
	public int getSize() {
		return (Integer) values.get("size");
	}
	
	public void setTo(String s) {
		values.put("to", s);
	}
	
	public String getTo() {
		return (String) values.get("to");
	}

}
