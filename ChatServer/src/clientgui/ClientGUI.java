package clientgui;

import message.ChatroomMessage;
import message.PrivateMessage;

public interface ClientGUI {
	
	public void putMessage(ChatroomMessage msg);
	public void putMessage(PrivateMessage msg);

}
