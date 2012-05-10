package clientgui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ConnectForm extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;
	private JTextField hostField;
	private JTextField portField;
	
	public ConnectForm() {
	}
	
	public void initGUI() {
		setTitle("Connect to server");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel hostLabel = new JLabel("Host:");
		JLabel portLabel = new JLabel("Port:");
		
		hostField = new JTextField("", 15);
		portField = new JTextField("1234", 15);
		
		JButton connectButton = new JButton("Connect");
		
		ConnectListener cl = new ConnectListener(this);
		hostField.addActionListener(cl);
		portField.addActionListener(cl);
		connectButton.addActionListener(cl);
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(hostLabel)
						.addComponent(portLabel))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(hostField)
						.addComponent(portField)
						.addComponent(connectButton))
				);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(hostLabel)
						.addComponent(hostField))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(portLabel)
						.addComponent(portField))
				.addComponent(connectButton)
				);
		
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
		
		@SuppressWarnings("unused")
		@Override
		public void actionPerformed(ActionEvent event) {
			try {
				String host = hostField.getText();
				int port = Integer.parseInt(portField.getText());
				if (port < 0 || port > 65535)
					throw new NumberFormatException();
				ChatWindow cw = new ChatWindow(host, port);
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
