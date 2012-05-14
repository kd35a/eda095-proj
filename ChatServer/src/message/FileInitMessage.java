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
	
	public void setSize(long i) {
		values.put("size", i);
	}
	
	public long getSize() {
		return (Long) values.get("size");
	}
	
	public void setTo(String s) {
		values.put("to", s);
	}
	
	public String getTo() {
		return (String) values.get("to");
	}

	public void setFileID(int i) {
		values.put("fileid", i);
	}
	
	public int getFileID() {
		return safeLongToInt((Long) values.get("fileid"));
	}

}
