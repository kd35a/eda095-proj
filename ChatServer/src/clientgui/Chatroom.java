package clientgui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import message.ChatroomMessage;

public class Chatroom extends JPanel {
	
	private JTextArea text;
	
	public Chatroom() {
		super();
		
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
		
		text = new JTextArea();
		text.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(text);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addMessage(ChatroomMessage m) {
		text.append(m.getFrom() + ": " + m.getMsg() + "\n");
	}

}
