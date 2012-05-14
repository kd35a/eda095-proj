package server;

import java.util.concurrent.ConcurrentHashMap;

import message.ChatroomMessage;
import message.ConnectMessage;
import message.DisconnectMessage;
import message.ErrorMessage;
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

public class MessageConsumerThread extends Thread {

	private String serverName;
	private Mailbox<Message> messages;
	private boolean active;
	private ConcurrentHashMap<String, ClientConnection> clientList;
	private ConcurrentHashMap<String, ChatRoomConnection> roomList;

	public MessageConsumerThread(String serverName,
			ConcurrentHashMap<String, ClientConnection> clientList,
			Mailbox<Message> messages) {
		this.serverName = serverName;
		this.clientList = clientList;
		this.messages = messages;
		this.active = true;
		this.roomList = new ConcurrentHashMap<String, ChatRoomConnection>();
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
			else if (type.equals(FileInitMessage.TYPE))
				consume((FileInitMessage) msg);
			else if (type.equals(FileAcceptMessage.TYPE))
				consume((FileAcceptMessage) msg);
			else
				System.err.println("Unknown message. Doing nothing.");

		}
	}

	private void consume(PrivateMessage m) {
		ClientConnection to = clientList.get(m.getTo());
		ClientConnection from = clientList.get(m.getFrom());

		if (to == null) {
			from = clientList.get(m.getFrom());
			ErrorMessage err = new ErrorMessage();
			err.setMsg("User " + m.getTo() + " not found.");
			from.sendMsg(err);
			return;
		}

		/* Set up broadcastable connection link */
		to.addConnection(new PrivateMessageConnection(from));
		from.addConnection(new PrivateMessageConnection(to));

		to.sendMsg(m);
	}

	private void consume(ChatroomMessage m) {
		String room = m.getRoom();
		ChatRoomConnection chatRoom = roomList.get(room);
		if (chatRoom == null) {
			return; // TODO: handle error, room does not exist
		}
		chatRoom.broadcast(m);
	}

	private void consume(JoinMessage m) {
		// Get or create chat room
		String room = m.getRoom();
		ChatRoomConnection chatRoom = roomList.get(room);
		if (chatRoom == null) {
			chatRoom = new ChatRoomConnection(room);
			roomList.put(room, chatRoom);
		}

		// Broadcast join
		chatRoom.broadcast(m);

		// Add client to room
		ClientConnection cc = clientList.get(m.getFrom());
		chatRoom.addConnection(cc);
		cc.addConnection(chatRoom);

		// Gather list of participants
		ListParticipantsMessage msg = new ListParticipantsMessage();
		msg.setParticipants(chatRoom.getParticipants());
		msg.setRoom(room);
		cc.sendMsg(msg);
	}

	private void consume(PartMessage m) {
		// Remove from room
		String room = m.getRoom();
		ChatRoomConnection chatRoom = roomList.get(room);
		if (chatRoom == null) {
			return; // TODO: handle error, room does not exist
		}
		String nick = m.getFrom();
		ClientConnection cc = clientList.get(nick);
		chatRoom.removeConnection(cc);
		cc.removeConnection(chatRoom);

		// Broadcast part
		chatRoom.broadcast(m);
	}

	private void consume(ConnectMessage m) {
		ClientConnection cc = clientList.get(m.getFrom());

		if (m.getNick() != null && clientList.get(m.getNick()) != null) {
			ErrorMessage err = new ErrorMessage();
			err.setMsg("Nickname " + m.getNick() + " is taken.");
			cc.sendMsg(err);
			return;
		} else if (m.getNick() != null) {
			// Nick change
			clientList.remove(cc.getNick());
			cc.setNick(m.getNick());
			clientList.put(cc.getNick(), cc);
		}

		WelcomeMessage wm = new WelcomeMessage();
		wm.setName(serverName);
		wm.setNick(cc.getNick());
		cc.sendMsg(wm);
	}

	private void consume(DisconnectMessage m) {
		ClientConnection cc = clientList.get(m.getFrom());

		for (Broadcastable b : cc.getConnections()) {
			b.removeConnection(cc);
			b.broadcast(m);
		}

		cc.disconnect();
		clientList.remove(m.getFrom());
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
		clientList.remove(cc.getNick());
		cc.setNick(newNick);
		clientList.put(newNick, cc);

		for (Broadcastable b : cc.getConnections()) {
			b.broadcast(m);
		}
	}

	private void consume(FileInitMessage m) {
		ClientConnection cc = clientList.get(m.getTo());
		cc.sendMsg(m);
	}

	private void consume(FileAcceptMessage m) {
		ClientConnection cc = clientList.get(m.getTo());
		cc.sendMsg(m);
	}

}
