import java.util.LinkedList;
import java.util.Queue;


public class Mailbox {
	
	private Queue<String> messageQueue;
	
	public Mailbox() {
		messageQueue = new LinkedList<String>();
	}
	
	public synchronized void addMessage(String msg) {
		messageQueue.add(msg);
		notifyAll();
	}
	
	public synchronized String getMessage() {
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
