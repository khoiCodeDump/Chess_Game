package Test;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TestClient {
	 public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
	        //get the localhost IP address, if server is running on some other IP, you need to use that
	        InetAddress host = InetAddress.getLocalHost();
	        Socket socket = new Socket(host.getHostName(), 9999);
	        
	        System.out.println("Connected to server");
	        ObjectOutputStream oos =  new ObjectOutputStream(socket.getOutputStream());
	        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
	        
	        int team = ois.readInt();
	        
	        JFrame frame = new JFrame(); 
	        JButton test = new JButton("Click");
	        test.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					TestData data = new TestData();
					try {
//						oos.writeObject(data);
//						oos.flush();
						oos.writeBoolean(true);
						oos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
	        	
	        });
	        frame.add(test);
	        frame.setBounds(100, 100, 831, 384);
	        System.out.println("Created board");
	        EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						System.out.println("Starting board");
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
//	        while(true) {
//	             System.out.println("In TestClient loop");
//	             //read the server response message
////	             TestData message = (TestData) ois.readObject();
////	             System.out.println("Message: " + message);
//	             Boolean val = ois.readBoolean();
//	             System.out.println(val);
//	             
//	                 
////	             Thread.sleep(100);
//	        }
	       
	        
	    }
}
