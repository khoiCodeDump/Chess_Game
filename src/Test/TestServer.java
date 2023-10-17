package Test;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;


public class TestServer {
	 private static ServerSocket server;
	    //socket server port on which it will listen
	    private static int port = 9999;
	    public static void main(String args[]) throws IOException, ClassNotFoundException{
	        //create the socket server object
	        server = new ServerSocket(port);
	        server.setReuseAddress(true);
	        
	      
	        System.out.println("Waiting for the client request");
	        Socket s = server.accept();
	        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
	        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
	        
	        out.writeInt(1);
	        out.flush();
	        
//	        ObjectStream objStr = new ObjectStream(s);
	        DataStream dataStr = new DataStream(s);
//	        objStr.start();
	        dataStr.start();
	    
//	        TestPlayer2 test2 = new TestPlayer2(player2);
//	        EventQueue.invokeLater(new Runnable() {
//				public void run() {
//					try {
//						TestPlayer1 test1 = new TestPlayer1(player1);
//						test1.setVisible(true);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
//			});
	         //convert ObjectInputStream object to String
//	         String message = (String) ois.readObject();
//	         System.out.println("Message Received: " + message);
//	         //create ObjectOutputStream object
//	         ObjectOutputStream oos = new ObjectOutputStream(connections[i].getOutputStream());
//	         //write object to Socket
//	         oos.writeObject("Hi Client "+message);
//	         //close resources
//	         ois.close();
//	         oos.close();
//	         socket.close();
	        
	           
	        
	        
	        //close the ServerSocket object
//	        server.close();
	        
	        
	    }
	   
	    
	   
}




