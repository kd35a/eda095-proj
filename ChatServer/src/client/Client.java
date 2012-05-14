package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import clientgui.ChatWindow;

import message.DisconnectMessage;
import message.JoinMessage;
import message.Message;

import common.Mailbox;

public class Client extends Observable {
	private static SimpleDateFormat df = new SimpleDateFormat(
			"d MMM yyyy hh:mm:ss");

	private String serverName;
	private Socket socket;
	private Mailbox<Message> outbox;
	private String nickname = "";
	private String newNickname;
	private HashMap<String, ChatRoom> chatRooms;
	private ClientReadSocketThread clientRST;
	private ClientWriteSocketThread clientWST;
	private ChatWindow chatWindow;

	public Client(String host, int port) throws IOException {
		serverName = host;
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
		outbox = new Mailbox<Message>();
		chatRooms = new HashMap<String, ChatRoom>();

		clientRST = new ClientReadSocketThread(this, socket);
		clientRST.start();
		clientWST = new ClientWriteSocketThread(socket, outbox);
		clientWST.start();
	}

	public String getServerName() {
		return serverName;
	}

	public void sendMessage(Message msg) {
		outbox.put(msg);
	}

	public void setNick(String nick) {
		this.nickname = nick;
	}

	public String getNick() {
		return this.nickname;
	}

	public void joinRoom(String room) {
		ChatRoom cr = new ChatRoom();
		chatRooms.put(room, cr);
		JoinMessage msg = new JoinMessage();
		msg.setFrom(nickname);
		msg.setRoom(room);
		msg.setTime(df.format(new Date()));
		outbox.put(msg);
	}

	public void setChatRoomParticipants(String room, List<String> participants) {
		chatRooms.get(room).setParticipants(participants);
		setChanged();
		notifyObservers();
	}

	public void addChatRoomParticipant(String room, String name) {
		ChatRoom cr = chatRooms.get(room);
		cr.addParticipant(name);
		setChanged();
		notifyObservers();
	}

	public void removeChatRoomParticipant(String room, String name) {
		ChatRoom cr = chatRooms.get(room);
		cr.removeParticipant(name);
		setChanged();
		notifyObservers();
	}

	public void removeChatRoomParticipant(String name) {
		for (ChatRoom cr : chatRooms.values()) {
			cr.removeParticipant(name);
		}
		setChanged();
		notifyObservers();
	}

	public Vector<String> getChatRoomParticipants(String room) {
		ChatRoom cr = chatRooms.get(room);
		if (cr != null)
			return cr.getParticipants();
		return null;
	}

	public void disconnect() {
		try {
			outbox.put(new DisconnectMessage());
			clientRST.disconnect();
			clientWST.disconnect();
			socket.close();
		} catch (IOException e) {
			System.err.println("Problem closing connection to server.");
			e.printStackTrace();
		}
	}

	public void setChatWindow(ChatWindow chatWindow) {
		this.chatWindow = chatWindow;
	}

	public ChatWindow getChatWindow() {
		return chatWindow;
	}

	public void handleMessage(Message msg) {
		setChanged();
		notifyObservers(msg);
	}

	public void setNewNickname(String nick) {
		this.newNickname = nick;
	}

	public String getNewNickname() {
		return this.newNickname;
	}

	public void renameChatRoomParticipant(String oldNick, String newNick) {
		if (oldNick.equals(this.nickname)) {
			this.nickname = newNick;
		}
		for (ChatRoom cr : chatRooms.values()) {
			cr.renameChatRoomParticipant(oldNick, newNick);
		}
		chatWindow.repaint();
	}

}
