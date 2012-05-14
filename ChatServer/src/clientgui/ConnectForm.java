package clientgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.Client;

import message.ConnectMessage;
import message.NickMessage;

public class ConnectForm extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private JTextField hostField;
	private JTextField portField;
	private JTextField nickField;
	
	public ConnectForm() {
	}
	
	public void initGUI() {
		setTitle("Connect to server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel hostLabel = new JLabel("Host:");
		JLabel portLabel = new JLabel("Port:");
		JLabel nickLabel = new JLabel("Nickname:");
		
		hostField = new JTextField("localhost", 15);
		portField = new JTextField("1234", 15);
		nickField = new JTextField("", 15);
		
		JButton connectButton = new JButton("Connect");
		
		ConnectListener cl = new ConnectListener(this);
		hostField.addActionListener(cl);
		portField.addActionListener(cl);
		nickField.addActionListener(cl);
		connectButton.addActionListener(cl);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(hostLabel)
						.addComponent(portLabel)
						.addComponent(nickLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(hostField)
						.addComponent(portField)
						.addComponent(nickField)
						.addComponent(connectButton))
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(hostLabel)
						.addComponent(hostField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(portLabel)
						.addComponent(portField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(nickLabel)
						.addComponent(nickField))
				.addComponent(connectButton)
				);
		
		setLocationRelativeTo(null);
		setVisible(true);
		pack();
	}

	@Override
	public void run() {
		initGUI();
	}
	
	public static void main(String[] args) {
		new Thread(new ConnectForm()).start();
	}
	
	private class ConnectListener implements ActionListener {
		private ConnectForm parent;
		
		public ConnectListener(ConnectForm parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				String host = hostField.getText();
				int port = Integer.parseInt(portField.getText());
				String nick = nickField.getText().trim();
				
				if (port < 0 || port > 65535)
					throw new NumberFormatException();
				
				Client client = null;
				try {
					client = new Client(host, port);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(parent, "Could not connect to server.", "Connection error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				ChatWindow cw = new ChatWindow(client);
				client.addObserver(cw);
				
				ConnectMessage cm = new ConnectMessage();
				cw.sendMessage(cm);
				
				if (!nick.equals("")) {
					NickMessage nm = new NickMessage();
					nm.setNick(nick);
					cw.sendMessage(nm);
				}
				
				parent.setVisible(false);
			} catch (NumberFormatException exception) {
				JOptionPane.showMessageDialog(parent,
						"The value entered in the port field is of wrong\n"
						+ "format, should be integer between 0 and\n65535,"
						+ " was: " + portField.getText(),
						"Port not number",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		
	}
	
}
