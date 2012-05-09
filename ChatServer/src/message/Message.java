package message;

import java.util.Map;

public abstract class Message {
	
	protected String type;
	protected Map<String, String> values;
	
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
	
	public void setValues(Map<String, String> values) {
		this.values = values;
	}

}
