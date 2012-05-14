package clientgui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import message.FileAcceptMessage;

import client.Client;
import client.FileReceiverThread;

public class FileTransferDialog extends JDialog {

	private JLabel text;
	private JButton accept;
	private JButton deny;
	private JPanel buttonPanel;
	private JProgressBar progress;
	private Client client;
	private String from;
	private String filename;
	private long size;
	private int fileID;
	
	public FileTransferDialog(Client client, String from, String filename, long size, int fileID) {
		this.client = client;
		this.from = from;
		this.filename = filename;
		this.size = size;
		this.fileID = fileID;
		initDialog();
	}
	
	private void initDialog() {
		accept = new JButton("Accept");
		accept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				acceptAction();
			}
		});
		
		deny = new JButton("Deny");
		deny.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				denyAction();
			}
		});
		
		text = new JLabel(from + " is sending you " + filename + " (" + size + " bytes).");
		progress = new JProgressBar();
		progress.setEnabled(false);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(text, BorderLayout.NORTH);
		c.add(progress, BorderLayout.CENTER);
		c.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.add(accept);
		buttonPanel.add(deny);
		
		pack();
		setVisible(true);		
	}
	
	private void acceptAction() {
		FileAcceptMessage fam = new FileAcceptMessage();
		fam.setTo(from);
		fam.setPort(1994);
		fam.setFileID(fileID);
		client.sendMessage(fam);
		FileReceiverThread frt = new FileReceiverThread(1994, "implement", filename, size);
		frt.registerDialog(this);
		frt.start();
	}
	
	private void denyAction() {
		this.dispose();
	}
	
	public synchronized void setProgress(int percent) {
		progress.setValue(percent);
	}

	public void setDone() {
		this.dispose();
	}
}
