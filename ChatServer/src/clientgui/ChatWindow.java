package clientgui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public ChatWindow() {
		setTitle("ChatServer: <server-name>"); // TODO Get the servers name
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new MyWindow());
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(2, 2));
		
		String[] data = {"#abc", "#foo", "#bar", "#abc", "#foo", "#bar", "#abc", "#foo", "#bar", "#abc", "#foo", "#bar", "#abc", "#foo", "#bar", "#abc", "#foo", "#bar"};
		JList list = new JList(data);
		JScrollPane scrollPane = new JScrollPane(list);
		panel.add(scrollPane, BorderLayout.WEST);
		
		JTextArea textArea = new JTextArea();
		textArea.setText("Looolllkjn<aefsjölnasföljnwew\n");
		panel.add(textArea, BorderLayout.CENTER);
		pack();
		
		JPanel southPanel = new JPanel(new BorderLayout(2, 2));
		
		JTextField textField = new JTextField();
		southPanel.add(textField, BorderLayout.CENTER);
		
		JButton button = new JButton("Send message");
		southPanel.add(button, BorderLayout.EAST);
		
		panel.add(southPanel, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
		ChatWindow cw = new ChatWindow();
		cw.setVisible(true);
	}
	
	private class MyWindow extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {
			System.out.println("Exit-button pressed");
			// TODO Close connection to server here
		}
		
	}
	
}
