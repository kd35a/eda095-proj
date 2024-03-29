package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import message.ChatroomMessage;
import message.ConnectMessage;
import message.DisconnectMessage;
import message.FileAcceptMessage;
import message.FileInitMessage;
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
	private Client client;

	public ClientReadSocketThread(Client client, Socket s) {
		this.client = client;
		active = true;
		socket = s;
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
				else if (type.equals(FileInitMessage.TYPE))
					consume((FileInitMessage) msg);
				else if (type.equals(FileAcceptMessage.TYPE))
					consume((FileAcceptMessage) msg);
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
		/*String newNickname = client.getNewNickname();
		if (newNickname != null && !newNickname.equals("")) {
			NickMessage nm = new NickMessage();
			nm.setFrom(msg.getNick());
			nm.setNick(newNickname);
			client.sendMessage(nm);
			client.setNewNickname("");
		}*/
	}

	private void consume(NickMessage msg) {
		client.renameChatRoomParticipant(msg.getFrom(), msg.getNick());
	}

	private void consume(DisconnectMessage msg) {
		client.removeChatRoomParticipant(msg.getFrom());
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
		if (!msg.getFrom().equals(client.getNick()))
			client.handleMessage(msg);
	}

	private void consume(ChatroomMessage msg) {
		if (!msg.getFrom().equals(client.getNick()))
			client.handleMessage(msg);
	}
	
	private void consume(ListParticipantsMessage msg) {
		client.setChatRoomParticipants(msg.getRoom(), msg.getParticipants());
	}

	private void consume(FileInitMessage msg) {
		/*
		 * TODO: Implement me.
		 * Someone wants to send a file to this client.
		 * We get info about the filename, size etc.
		 * If the client decides to accept the file, he
		 * sends a FileAcceptMessage to the sending client,
		 * and starts an accepting serversocket in a new thread.
		 */
		if (!msg.getFrom().equals(client.getNick()))
			client.handleMessage(msg);
	}
	
	private void consume(FileAcceptMessage msg) {
		/*
		 * TODO: Implement me.
		 * This client decides to accept a file. He
		 * sends his credentials (hostname and port) to
		 * the other client.
		 */
		if (!msg.getFrom().equals(client.getNick()))
			client.handleMessage(msg);
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
