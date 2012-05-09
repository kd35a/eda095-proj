package clientgui;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import message.ChatroomMessage;

public class Chatroom extends JPanel {
	
	private JTextArea text;
	
	public Chatroom() {
		super();
		
		initGUI();
	}
	
	private void initGUI() {
		text = new JTextArea();
		add(text);
	}
	
	public void addMessage(ChatroomMessage m) {
		text.append(m.getSender() + ": " + m.getMsg() + "\n");
	}

}
