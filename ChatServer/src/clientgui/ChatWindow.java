package clientgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;

import message.Message;

import common.Mailbox;

public class ChatWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Mailbox<Message> inbox;
	private Mailbox<Message> outbox;

	public ChatWindow() {
		inbox = new Mailbox<Message>();
		outbox = new Mailbox<Message>();
		
		initGUI();

		try {
			new Client("localhost", 1234, inbox, outbox);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		setTitle("ChatServer: <server-name>"); // TODO Get the servers name
//		setSize(800, 600);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
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
		
		ActionListener sendListener = new SendMessageListener();
		textField.addActionListener(sendListener);
		button.addActionListener(sendListener);
		
		setVisible(true);
	}
	
	private class SendMessageListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	public static void main(String[] args) {
		new ChatWindow();
	}

}
