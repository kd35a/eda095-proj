package message;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public abstract class Message {
	
	protected String type;
	protected JSONObject values;
	
	public Message() {
		values = new JSONObject();
	}
	
	public String toJSON() {
		JSONObject msg = new JSONObject();
		JSONObject attr = new JSONObject();
		attr.putAll(values);
		msg.put(type, attr);
		return msg.toJSONString();
	}
	
	public String getType() {
		return type;
	}
	
	public void setTime(String s) {
		values.put("time", s);
	}
	
	public String getTime() {
		return (String) values.get("time");
	}

	public void setFrom(String s) {
		values.put("from", s);
	}
	
	public String getFrom() {
		return (String) values.get("from");
	}
	public void setValues(JSONObject values) {
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
			else if (type.equals("welcome"))
				msg = new WelcomeMessage();
			else if (type.equals("list"))
				msg = new ListParticipantsMessage();
			else if (type.equals("error"))
				msg = new ErrorMessage();
			else if (type.equals("fileinit"))
				msg = new FileInitMessage();
			else if (type.equals("fileacc"))
				msg = new FileAcceptMessage();
			else
				throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
			
			JSONObject attr = (JSONObject) msgObj.get(type);
			msg.setValues(attr);
			
			return msg;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected int safeLongToInt(long l) {
	    return (int) Math.min(Integer.MAX_VALUE, l);
	}

}
