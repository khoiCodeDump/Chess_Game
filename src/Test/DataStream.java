package Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DataStream extends Thread{
	DataInputStream in;
	DataOutputStream out;
	DataStream(Socket s){
		try {
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Initializing datastreams error");
		}
		
	}
	public void run() {
//		while(true) {
			try {
				Boolean val = in.readBoolean();
				
				System.out.println(val);
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("DataStream error");
//			}
		}
	}
}
