package clientgui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import message.ChatroomMessage;
import message.Message;
import message.PrivateMessage;
import client.Client;

public class ChatWindow extends JFrame implements ClientGUI, Observer {
	
	private static final long serialVersionUID = 1L;
	private static final String PROGRAM_NAME = "ChatServer";
	
	private Map<String, ChatRoomPanel> chatrooms;
	private Client client;
	
	/*
	 * GUI components
	 */
	private JPanel mainPanel;
	private JList participantsList;
	private JScrollPane scrollPane;
	private JTabbedPane tabbedPane;
	private JTextField messageInputField;
	private JButton sendButton;
	private JPanel southPanel;
	private JMenuBar menuBar;
	
	public ChatWindow(Client client) {
		this.client = client;
		client.setChatWindow(this);
		chatrooms = new HashMap<String, ChatRoomPanel>();
		
		initGUI();
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
		addWindowListener(new ChatWindowCloseListener());

		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout(2, 2));
		
		participantsList = new JList(new String[0]);
		scrollPane = new JScrollPane(participantsList);
		mainPanel.add(scrollPane, BorderLayout.WEST);

		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener(new TabChanged());
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
		ChatRoomPanel c = new ChatRoomPanel();
		client.joinRoom(name);
		chatrooms.put(name, c);
		tabbedPane.addTab(name, c);
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem fileQuit = new JMenuItem("Quit");
		fileQuit.addActionListener(new CloseActionListener(this));
		fileMenu.add(fileQuit);
		
		JMenu serverMenu = new JMenu("Server");
		
		JMenuItem serverDisconnect = new JMenuItem("Disconnect from server");
		serverDisconnect.setEnabled(false);
		
		JMenuItem serverJoinRoom = new JMenuItem("Join room...");
		serverJoinRoom.addActionListener(new JoinRoomActionListener(this));
		
		serverMenu.add(serverDisconnect);
		serverMenu.add(serverJoinRoom);
		
		menuBar.add(fileMenu);
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
			
			// putMessage(m);
			
			client.sendMessage(m);
		}
		
	}

	private class ChatWindowCloseListener extends WindowAdapter {
		
		public void windowClosing(WindowEvent e) {
			System.out.println("Exit-button pressed");
			client.disconnect();
		}
		
	}
	
	/**
	 * To be used by quit-buttons and -menu items. Just sends a Window Closing
	 * event to window, and from there {@link ChatWindowCloseListener} takes
	 * care of the rest.
	 */
	private class CloseActionListener implements ActionListener {
		private ChatWindow parent;
		
		public CloseActionListener(ChatWindow parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			WindowEvent we = new WindowEvent(parent, WindowEvent.WINDOW_CLOSING);
			Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
		}
		
	}
	
	private class JoinRoomActionListener implements ActionListener {
		private ChatWindow parent;
		
		public JoinRoomActionListener(ChatWindow parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String room = JOptionPane.showInputDialog(parent, "Which room do"
					+ " you want to join?",
					"Join chatroom",
					JOptionPane.PLAIN_MESSAGE);
			room = room.trim();
			if (room != null && !room.isEmpty()) {
				joinChatroom(room);
			}
		}
		
	}
	
	private class TabChanged implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent ce) {
			int index = tabbedPane.getSelectedIndex();
			String room = tabbedPane.getTitleAt(index);
			participantsList.setListData(client.getChatRoomParticipants(room));
		}
		
	}
	
	protected void sendMessage(Message msg) {
		client.sendMessage(msg);
	}

	@Override
	public void putMessage(ChatroomMessage msg) {
		chatrooms.get(msg.getRoom()).putMessage(msg);
	}
	
	public void repaint() {
		super.repaint();
		int index = tabbedPane.getSelectedIndex();
		if (index == -1 && tabbedPane.getTabCount() > 0) {
			index = 0;
			tabbedPane.setSelectedIndex(index);
		} else if (index == -1) {
			participantsList.setListData(new String[0]);
			System.err.println("Something's goofy");
			return;
		}
		String room = tabbedPane.getTitleAt(index);
		participantsList.setListData(client.getChatRoomParticipants(room));
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ChatroomMessage) {
			putMessage((ChatroomMessage) arg);
		} else if (arg instanceof PrivateMessage) {
			
		}
	}

}
