package message;

public class DisconnectMessage extends Message {
	public static final String TYPE = "disconnect";
	
	public DisconnectMessage() {
		super.type = TYPE;
	}

}
