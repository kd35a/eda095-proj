package message;

import java.util.HashMap;

public abstract class Message {
	
	protected String type;
	protected HashMap<String, String> values;
	
	public String toJSON() {
		/* TODO: Use JSON lib and do toString */
		return null;
	}
	
	public void setTime(String s) {
		values.put("time", s);
	}
	
	public String getTime() {
		return values.get("time");
	}
	
	public void setValues(HashMap<String, String> values) {
		this.values = values;
	}

}
