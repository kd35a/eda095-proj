package test;

import message.MessageFactory;
import message.PrivateMessage;

import org.junit.Test;

import junit.framework.TestCase;

public class TestMessages extends TestCase {
	
	@Test
	public void testPM() {
		PrivateMessage msg = (PrivateMessage) MessageFactory.create("{ \"pm\": { \"to\": \"u2\", \"msg\": \"hej\", \"time\": \"2012-05-09 11:35:22 +0200\" } }");
		assertEquals(msg.getMsg(), "hej");
		assertEquals(msg.getTo(), "u2");
		assertEquals(msg.getTime(), "2012-05-09 11:35:22 +0200");
	}

}
