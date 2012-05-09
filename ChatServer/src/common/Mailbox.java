package common;

import java.util.ArrayDeque;
import java.util.Queue;

public class Mailbox<T> {

	private Queue<T> messageQueue;

	public Mailbox() {
		messageQueue = new ArrayDeque<T>();
	}

	public synchronized void put(T msg) {
		messageQueue.add(msg);
		notifyAll();
	}

	public synchronized T get() {
		while (messageQueue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println("Failed when waiting for message");
				e.printStackTrace();
			}
		}
		return messageQueue.poll();
	}

}
