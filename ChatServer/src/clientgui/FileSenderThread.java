package clientgui;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class FileSenderThread extends Thread {
	
	private File file;
	private Socket s;
	
	public FileSenderThread(File f) {
		this.file = f;
	}

	public void setReceiver(String host, int port) {
		try {
			s = new Socket(host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[4096];

			while (fis.read(buffer) > 0) {
				dos.write(buffer);
			}

			fis.close();
			dos.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
