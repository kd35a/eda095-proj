package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import message.ChatroomMessage;
import message.ConnectMessage;
import message.DisconnectMessage;
import message.JoinMessage;
import message.ListParticipantsMessage;
import message.Message;
import message.NickMessage;
import message.PartMessage;
import message.PrivateMessage;
import message.WelcomeMessage;

import common.Mailbox;

public class ClientReadSocketThread extends Thread {

	private boolean active;
	private Socket socket;
	private BufferedReader in;
	private Mailbox<Message> inbox;
	private Client client;

	public ClientReadSocketThread(Client client, Socket s, Mailbox<Message> inbox) {
		this.client = client;
		active = true;
		socket = s;
		this.inbox = inbox;
		try {
			in = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		} catch (IOException e) {
			System.err.println("Failed setting up input stream for server "
					+ socket.getInetAddress());
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (active) {
			try {
				String line = in.readLine();
				if (line == null) {
					// TODO: handle server shut down
					socket.close();
					active = false;
					break;
				}		
				Message msg = Message.fromJSON(line);
				System.out.println("received " + msg.toJSON());
				String type = msg.getType();
				if (type.equals(PrivateMessage.TYPE))
					consume((PrivateMessage) msg);
				else if (type.equals(ChatroomMessage.TYPE))
					consume((ChatroomMessage) msg);
				else if (type.equals(JoinMessage.TYPE))
					consume((JoinMessage) msg);
				else if (type.equals(PartMessage.TYPE))
					consume((PartMessage) msg);
				else if (type.equals(ConnectMessage.TYPE))
					consume((ConnectMessage) msg);
				else if (type.equals(DisconnectMessage.TYPE))
					consume((DisconnectMessage) msg);
				else if (type.equals(NickMessage.TYPE))
					consume((NickMessage) msg);
				else if (type.equals(WelcomeMessage.TYPE))
					consume((WelcomeMessage) msg);
				else if (type.equals(ListParticipantsMessage.TYPE))
					consume((ListParticipantsMessage) msg);
				else
					System.err.println("Unknown message. Doing nothing.");
			} catch (IOException e) {
				if (active == true) {
					System.out.println("Failed getting input from server "
							+ socket.getInetAddress());
					e.printStackTrace();
				}
			}
		}
	}

	private void consume(WelcomeMessage msg) {
		client.setNick(msg.getNick());
		String newNickname = client.getNewNickname();
		if (newNickname != null && !newNickname.equals("")) {
			NickMessage nm = new NickMessage();
			nm.setFrom(msg.getNick());
			nm.setNick(newNickname);
			client.sendMessage(nm);
			client.setNewNickname("");
		}
	}

	private void consume(NickMessage msg) {
		client.renameChatRoomParticipant(msg.getFrom(), msg.getNick());
	}

	private void consume(DisconnectMessage msg) {
		// TODO Auto-generated method stub
		
	}

	private void consume(ConnectMessage msg) {
		// TODO Auto-generated method stub
		
	}

	private void consume(PartMessage msg) {
		client.removeChatRoomParticipant(msg.getRoom(), msg.getFrom());
		// TODO Also put a message in chat-window?
	}

	private void consume(JoinMessage msg) {
		client.addChatRoomParticipant(msg.getRoom(), msg.getFrom());
	}

	private void consume(PrivateMessage msg) {
		// TODO Auto-generated method stub
		
	}

	private void consume(ChatroomMessage msg) {
		// client.getChatWindow().putMessage(msg);
		client.handleMessage(msg);
	}
	
	private void consume(ListParticipantsMessage msg) {
		client.setChatRoomParticipants(msg.getRoom(), msg.getParticipants());
	}

	public void disconnect() {
		try {
			active = false;
			in.close();
		} catch (IOException e) {
			System.err.println("Could not close read-buffer.");
			e.printStackTrace();
		}
	}

}
