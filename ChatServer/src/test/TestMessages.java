package test;

import message.ChatroomMessage;
import message.ConnectMessage;
import message.DisconnectMessage;
import message.JoinMessage;
import message.Message;
import message.NickMessage;
import message.PartMessage;
import message.PrivateMessage;

import org.junit.Test;

import junit.framework.TestCase;

public class TestMessages extends TestCase {
	
	@Test
	public void testPM() {
		PrivateMessage msg = (PrivateMessage) Message.fromJSON("{ \"pm\": { \"to\": \"u2\", \"msg\": \"hej\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getMsg(), "hej");
		assertEquals(msg.getTo(), "u2");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testCM() {
		ChatroomMessage msg = (ChatroomMessage) Message.fromJSON("{ \"cm\": { \"room\": \"kryptoanarki\", \"msg\": \"hallå\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getMsg(), "hallå");
		assertEquals(msg.getRoom(), "kryptoanarki");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testJoin() {
		JoinMessage msg = (JoinMessage) Message.fromJSON("{ \"join\": { \"room\": \"kryptoanarki\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getRoom(), "kryptoanarki");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testPart() {
		PartMessage msg = (PartMessage) Message.fromJSON("{ \"part\": { \"room\": \"kryptoanarki\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getRoom(), "kryptoanarki");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testConnect() {
		ConnectMessage msg = (ConnectMessage) Message.fromJSON("{ \"connect\": { \"nick\": \"u\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getNick(), "u");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testDisonnect() {
		DisconnectMessage msg = (DisconnectMessage) Message.fromJSON("{ \"disconnect\": { \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}
	
	@Test
	public void testNick() {
		NickMessage msg = (NickMessage) Message.fromJSON("{ \"nick\": { \"nick\": \"v\" } }");
		assertEquals(msg.getNick(), "v");
	}

}
