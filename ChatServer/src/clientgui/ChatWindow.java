package clientgui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import message.NickMessage;
import message.PartMessage;
import message.PrivateMessage;
import client.Client;

public class ChatWindow extends JFrame implements ClientGUI, Observer {

	private static final long serialVersionUID = 1L;
	private static final String PROGRAM_NAME = "ChatServer";

	private Map<String, ChatPanel> chatrooms;
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
		chatrooms = new HashMap<String, ChatPanel>();

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

		participantsList = new JList(new String[] { "" });
		participantsList.setPreferredSize(new Dimension(200, 0));
		participantsList.addMouseListener(new ParticipantListListener(this,
				participantsList));
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

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void joinChatroom(String name) {
		ChatPanel c = new ChatPanel();
		client.joinRoom(name);
		name = "#" + name;
		chatrooms.put(name, c);
		int index = tabbedPane.getTabCount();
		tabbedPane.add(name, c);
		tabbedPane.setTabComponentAt(index, new TabComponent(name, tabbedPane));
	}

	public void joinPrivateRoom(String name) {
		ChatPanel c = new ChatPanel();
		chatrooms.put(name, c);
		int index = tabbedPane.getTabCount();
		tabbedPane.add(name, c);
		tabbedPane.setTabComponentAt(index, new TabComponent(name, tabbedPane));
	}

	private class TabComponent extends JPanel {

		private final JTabbedPane pane;

		private TabComponent(String name, final JTabbedPane pane) {
			super(new FlowLayout(FlowLayout.LEFT, 0, 0));

			this.pane = pane;

			setOpaque(false);

			JLabel label = new JLabel(name);
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			add(label);

			JButton button = new TabButton();
			add(button);

			setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		}

		private class TabButton extends JButton implements ActionListener {

			private TabButton() {
				int size = 17;
				setPreferredSize(new Dimension(size, size));

				setContentAreaFilled(false);
				setFocusable(false);
				setBorder(BorderFactory.createEtchedBorder());
				setBorderPainted(false);
				addMouseListener(buttonMouseListener);
				setRolloverEnabled(true);

				addActionListener(this);
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();

				g2.setStroke(new BasicStroke(2));
				g2.setColor(Color.BLACK);

				int delta = 6;
				g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight()
						- delta - 1);
				g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight()
						- delta - 1);
				g2.dispose();
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String room = null;
				int i = pane.indexOfTabComponent(TabComponent.this);
				if (i != -1) {
					room = pane.getTitleAt(i);
					pane.remove(i);
				}
				PartMessage msg = new PartMessage();
				msg.setRoom(room);
				client.sendMessage(msg);
			}

		}

	}

	private final static MouseListener buttonMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component component = e.getComponent();
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.setBorderPainted(false);
			}
		}
	};

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

		JMenuItem serverChangeNick = new JMenuItem("Change nickname");
		serverChangeNick.addActionListener(new ChangeNickActionListener(this));

		serverMenu.add(serverDisconnect);
		serverMenu.add(serverJoinRoom);
		serverMenu.add(serverChangeNick);

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
			if (index != -1) {
				String to = tabbedPane.getTitleAt(index);

				Message m = null;

				if (to.charAt(0) == '#') {
					ChatroomMessage cm = new ChatroomMessage();
					cm.setMsg(msg);
					cm.setRoom(to.substring(1));
					m = cm;
				} else {
					PrivateMessage pm = new PrivateMessage();
					pm.setMsg(msg);
					pm.setTo(to);
					m = pm;
				}

				client.sendMessage(m);
			}
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
					+ " you want to join?", "Join chatroom",
					JOptionPane.PLAIN_MESSAGE);
			if (room != null && !room.isEmpty()) {
				room = room.trim();
				joinChatroom(room);
			}
		}

	}

	private class ChangeNickActionListener implements ActionListener {
		private ChatWindow parent;

		public ChangeNickActionListener(ChatWindow parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String nick = JOptionPane.showInputDialog(parent, "New nickname: ",
					"New nickname", JOptionPane.PLAIN_MESSAGE);
			if (nick != null && !nick.isEmpty()) {
				nick = nick.trim();
				NickMessage nm = new NickMessage();
				nm.setFrom(client.getNick());
				nm.setNick(nick);
				client.sendMessage(nm);
			}
		}

	}

	private class TabChanged implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent ce) {
			int index = tabbedPane.getSelectedIndex();
			if (index != -1) {
				String room = tabbedPane.getTitleAt(index);
				Vector<String> data = client.getChatRoomParticipants(room);
				if (data != null)
					participantsList.setListData(data);
			} else {
				participantsList.setListData(new String[] { "" });
			}
		}

	}

	protected void sendMessage(Message msg) {
		client.sendMessage(msg);
	}

	@Override
	public void putMessage(ChatroomMessage msg) {
		chatrooms.get("#" + msg.getRoom()).putMessage(msg);
	}

	@Override
	public void putMessage(PrivateMessage msg) {
		chatrooms.get(msg.getTo()).putMessage(msg);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof ChatroomMessage) {
			putMessage((ChatroomMessage) arg);
		} else if (arg instanceof PrivateMessage) {
			putMessage((PrivateMessage) arg);
		} else if (arg == null) {
			int index = tabbedPane.getSelectedIndex();
			if (index != -1) {
				String room = tabbedPane.getTitleAt(index);
				participantsList.setListData(client
						.getChatRoomParticipants(room.substring(1)));
			}
		}
	}

}
