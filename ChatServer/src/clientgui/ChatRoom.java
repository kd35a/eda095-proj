package clientgui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import message.ChatroomMessage;

public class ChatRoom extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea text;
	
	public ChatRoom() {
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
	
	public void putMessage(ChatroomMessage m) {
		text.append(m.getFrom() + ": " + m.getMsg() + "\n");
	}

}
