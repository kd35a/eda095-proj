package clientgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import message.Message;
import message.MessageFactory;
import client.Client;

import common.Mailbox;

public class ChatWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Mailbox<Message> inbox;
	private Mailbox<Message> outbox;
	
	/*
	 * GUI components
	 */
	private JPanel mainPanel;
	private JList chatRoomList;
	private JScrollPane scrollPane;
	private JTextArea chatMessages;
	private JTextField messageInputField;
	private JButton sendButton;
	private JPanel southPanel;
	
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

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new MyWindow());

		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout(2, 2));

		String[] data = { "#abc", "#foo", "#bar", "#abc", "#foo", "#bar",
				"#abc", "#foo", "#bar", "#abc", "#foo", "#bar", "#abc", "#foo",
				"#bar", "#abc", "#foo", "#bar" };
		chatRoomList = new JList(data);
		scrollPane = new JScrollPane(chatRoomList);
		mainPanel.add(scrollPane, BorderLayout.WEST);

		chatMessages = new JTextArea();
		chatMessages.setText("Looolllkjn<aefsjölnasföljnwew\n");
		mainPanel.add(chatMessages, BorderLayout.CENTER);
		pack();

		southPanel = new JPanel(new BorderLayout(2, 2));
		
		messageInputField = new JTextField();
		southPanel.add(messageInputField, BorderLayout.CENTER);

		sendButton = new JButton("Send message");
		southPanel.add(sendButton, BorderLayout.EAST);

		mainPanel.add(southPanel, BorderLayout.SOUTH);

		ActionListener sendListener = new SendMessageListener();
		messageInputField.addActionListener(sendListener);
		sendButton.addActionListener(sendListener);

		setVisible(true);
	}

	private class SendMessageListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String s = messageInputField.getText();
			Message m = MessageFactory.create(s);
			outbox.put(m);
		}
		
	}

	private class MyWindow extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {
			System.out.println("Exit-button pressed");
			// TODO Close connection to server here
		}
		
	}

	public static void main(String[] args) {
		new ChatWindow();
	}

}
