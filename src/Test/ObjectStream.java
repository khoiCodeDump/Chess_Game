package Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectStream extends Thread{
	ObjectInputStream in;
	ObjectOutputStream out;
	ObjectStream(Socket s){
		try {
			in = new ObjectInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Initializing objectstreams error");
		}
		
	}
	public void run() {
		while(true) {
        	TestData data;
			try {
				data = (TestData) in.readObject();
				System.out.println(data);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
				System.out.println("ObjectStream error");
			}
        	
        }
	}
}
