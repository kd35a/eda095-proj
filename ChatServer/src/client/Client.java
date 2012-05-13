package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import message.JoinMessage;
import message.Message;

import common.Mailbox;

public class Client {
	private static SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy hh:mm:ss");

	private String serverName;
	private Socket socket;
	private Mailbox<Message> inbox;
	private Mailbox<Message> outbox;
	private String nickname;
	private HashMap<String, ChatRoom> chatRooms;

	public Client(String host, int port) throws IOException {
		serverName = host;
		InetAddress address = InetAddress.getByName(host);
		socket = new Socket(address, port);
		inbox = new Mailbox<Message>();
		outbox = new Mailbox<Message>();
		chatRooms = new HashMap<String, ChatRoom>();
		
		Thread t;
		t = new ClientReadSocketThread(this, socket, inbox);
		t.start();
		t = new ClientWriteSocketThread(socket, outbox);
		t.start();
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
	}
	
	public ArrayList<String> getChatRoomParticipants(String room) {
		return chatRooms.get(room).getParticipants();
	}

}
