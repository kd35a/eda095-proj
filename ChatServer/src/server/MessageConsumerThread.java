package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import message.ChatroomMessage;
import message.ConnectMessage;
import message.DisconnectMessage;
import message.ErrorMessage;
import message.JoinMessage;
import message.ListParticipantsMessage;
import message.Message;
import message.NickMessage;
import message.PartMessage;
import message.PrivateMessage;
import message.WelcomeMessage;

import common.Mailbox;

public class MessageConsumerThread extends Thread {
	
	private String serverName;
	private Mailbox<Message> messages;
	private boolean active;
	private ConcurrentHashMap<String, ClientConnection> clientList;
	private ConcurrentHashMap<String, List<ClientConnection>> roomList;
	
	public MessageConsumerThread(String serverName, ConcurrentHashMap<String, ClientConnection> clientList, 
			Mailbox<Message> messages) {
		this.serverName = serverName;
		this.clientList = clientList;
		this.messages = messages;
		this.active = true;
		this.roomList = new ConcurrentHashMap<String, List<ClientConnection>>();
	}
	
	public void run() {
		while (active) {
			Message msg = messages.get();
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
			else
				System.err.println("Unknown message. Doing nothing.");
			
		}
	}
	
	private void consume(PrivateMessage m) {
		ClientConnection cc = clientList.get(m.getTo());
		if (cc == null) {
			cc = clientList.get(m.getFrom());
			ErrorMessage err = new ErrorMessage();
			err.setMsg("User " + m.getTo() + " not found.");
			cc.sendMsg(err);
			return;
		}
		cc.sendMsg(m);
		System.out.println("sent private message!");
	}
	
	private void consume(ChatroomMessage m) {
		String room = m.getRoom();
		List<ClientConnection> chatRoom = roomList.get(room);
		if (chatRoom == null) {
			return; // TODO: handle error, room does not exist
		}
		for (ClientConnection cc : chatRoom) {
			cc.sendMsg(m);
		}
	}
	
	private void consume(JoinMessage m) {
		// Get or create chat room
		String room = m.getRoom();
		List<ClientConnection> chatRoom = roomList.get(room);
		if (chatRoom == null) {
			chatRoom = new ArrayList<ClientConnection>();
			roomList.put(room, chatRoom);
		}
		
		// Broadcast join
		for (ClientConnection client : chatRoom) {
			client.sendMsg(m);
		}

		// Add client to room
		ClientConnection cc = clientList.get(m.getFrom());
		chatRoom.add(cc);
		
		// Gather list of participants
		List<String> participants = new ArrayList<String>();
		for (ClientConnection client : chatRoom) {
			String nick = client.getNick();
			participants.add(nick);
		}
		ListParticipantsMessage msg = new ListParticipantsMessage();
		msg.setParticipants(participants);
		cc.sendMsg(msg);
	}
	
	private void consume(PartMessage m) {
		/* TODO: Implement me! */
	}
	
	private void consume(ConnectMessage m) {
		ClientConnection cc = clientList.get(m.getFrom());
		WelcomeMessage wm = new WelcomeMessage();
		wm.setName(serverName);
		wm.setNick(cc.getNick());
		cc.sendMsg(wm);
	}
	
	private void consume(DisconnectMessage m) {
		/* TODO: Implement me! */
	}
	
	private void consume(NickMessage m) {
		String newNick = m.getNick();
		ClientConnection cc = clientList.get(m.getFrom());
		if (clientList.get(newNick) != null) {
			ErrorMessage err = new ErrorMessage();
			err.setMsg("Nickname " + newNick + " is taken.");
			cc.sendMsg(err);
			return;
		}
		clientList.remove(cc);
		cc.setNick(newNick);
		clientList.put(newNick, cc);
		/* TODO: Probably want to broadcast message to all
		 * clients associated to this one (chatrooms, pm-connections
		 * etc. )
		 */
	}

}
