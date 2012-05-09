package message;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Message {
	
	protected String type;
	protected Map<String, String> values;
	
	public String toJSON() {
		JSONObject msg = new JSONObject();
		JSONObject attr = new JSONObject();
		attr.putAll(values);
		msg.put(type, attr);
		return msg.toJSONString();
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
	
	public static Message fromJSON(String s) {
		try {
			JSONParser p = new JSONParser();
			JSONObject msgObj = (JSONObject) p.parse(s);
			if (msgObj.keySet().size() != 1)
				throw new ParseException(ParseException.ERROR_UNEXPECTED_EXCEPTION);
			String type = (String) msgObj.keySet().toArray()[0];

			Message msg;
			if (type.equals("pm"))
				msg = new PrivateMessage();
			else if (type.equals("cm"))
				msg = new ChatroomMessage();
			else if (type.equals("join"))
				msg = new JoinMessage();
			else if (type.equals("part"))
				msg = new PartMessage();
			else if (type.equals("connect"))
				msg = new ConnectMessage();
			else if (type.equals("disconnect"))
				msg = new DisconnectMessage();
			else if (type.equals("nick"))
				msg = new NickMessage();
			else
				throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
			
			JSONObject attr = (JSONObject) msgObj.get(type);
			msg.setValues((Map<String, String>) attr);
			return msg;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}
