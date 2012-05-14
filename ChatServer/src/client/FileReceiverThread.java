package client;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JProgressBar;

import clientgui.FileTransferDialog;

public class FileReceiverThread extends Thread {
	
	private ServerSocket ss;
	private int port;
	private String fromHost;
	private String filename;
	private long size;
	private FileTransferDialog ftd;
	
	public FileReceiverThread(int port, String fromHost, String filename, long size) {
		try {
			ss = new ServerSocket(port);
			this.port = port;
			this.fromHost = fromHost;
			this.filename = filename;
			this.size = size;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		boolean done = false;
		Socket clientSock;
		System.out.println("Started file receiver thread.");
		while (!done) {
			try {
				clientSock = ss.accept();
				//if (clientSock.getInetAddress().getHostAddress().equals(fromHost)) {
					saveFile(clientSock);
					clientSock.close();
					done = true;
				//}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveFile(Socket clientSock) throws IOException {
		DataInputStream dis = new DataInputStream(clientSock.getInputStream());
		FileOutputStream fos = new FileOutputStream(filename);
		byte[] buffer = new byte[4096];
		
		int read = 0;
		double totalRead = 0;
		float percentDone = 0;
		long remaining = size;
		while((read = dis.read(buffer, 0, (int) Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			percentDone = (float) totalRead / size;
			if (ftd != null) {
				ftd.setProgress(Math.round(percentDone * 100));
			}
			fos.write(buffer, 0, read);
		}
		
		fos.close();
		dis.close();
		ftd.setDone();
	}

	public void registerDialog(FileTransferDialog ftd) {
		this.ftd = ftd;
	}

}
