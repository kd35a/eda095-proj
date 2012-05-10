package clientgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import message.ChatroomMessage;
import message.Message;
import client.Client;

import common.Mailbox;

public class ChatWindow extends JFrame {
	private static final String PROGRAM_NAME = "ChatServer";
	private static final long serialVersionUID = 1L;
	
	private Mailbox<Message> inbox;
	private Mailbox<Message> outbox;
	private List<Chatroom> chatrooms;
	private Client client;
	
	/*
	 * GUI components
	 */
	private JPanel mainPanel;
	private JList chatRoomList;
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
	private JTextField messageInputField;
	private JButton sendButton;
	private JPanel southPanel;
	private JMenuBar menuBar;
	
	public ChatWindow(String host, int port) {
		inbox = new Mailbox<Message>();
		outbox = new Mailbox<Message>();
		chatrooms = new ArrayList<Chatroom>();
		
		try {
			client = new Client(host, port, inbox, outbox);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initGUI();
		
		joinChatroom("Room 1");
		joinChatroom("Room 2");
	}

	private void initGUI() {
		String title;
		if (client == null) {
			title = PROGRAM_NAME + ": " + "Not connected.";
		} else {
			title = PROGRAM_NAME + ": " + client.getServerName();
		}
		setTitle(title);
		setSize(800, 600);

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

		tabbedPane = new JTabbedPane();
		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		southPanel = new JPanel(new BorderLayout(2, 2));
		
		messageInputField = new JTextField();
		southPanel.add(messageInputField, BorderLayout.CENTER);

		sendButton = new JButton("Send message");
		southPanel.add(sendButton, BorderLayout.EAST);

		mainPanel.add(southPanel, BorderLayout.SOUTH);

		ActionListener sendListener = new SendMessageListener();
		messageInputField.addActionListener(sendListener);
		sendButton.addActionListener(sendListener);
		
		initMenu();

		setVisible(true);
	}
	
	private void joinChatroom(String name) {
		Chatroom c = new Chatroom();
		chatrooms.add(c);
		tabbedPane.addTab(name, c);
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		
		JMenu serverMenu = new JMenu("Server");
		JMenuItem serverDisconnect = new JMenuItem("Disconnect from server");
		serverMenu.add(serverDisconnect);
		
		menuBar.add(serverMenu);
		setJMenuBar(menuBar);
	}

	private class SendMessageListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = messageInputField.getText();
			messageInputField.setText("");
			
			int index = tabbedPane.getSelectedIndex();
			String room = tabbedPane.getTitleAt(index); 
			
			ChatroomMessage m = new ChatroomMessage();
			m.setMsg(msg);
			m.setRoom(room);
			m.setSender("Ola-Conny");
			
			chatrooms.get(index).addMessage(m);
			
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
		if (args.length < 2) {
			System.out.println("Usage: java ChatWindow <host> <port>");
		} else {			
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			new ChatWindow(host, port);
		}
	}

}
