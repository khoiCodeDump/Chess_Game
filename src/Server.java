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


public class Server {
	 private static ServerSocket server;
	    //socket server port on which it will listen
	    private static int port = 9876;
	    public static void main(String args[]) throws IOException, ClassNotFoundException{
	        //create the socket server object
	        server = new ServerSocket(port);
	        server.setReuseAddress(true);
	        Lobby lobby = new Lobby();
 	        while(true) {
	        	 System.out.println("Waiting for the client request");
	        	 Socket s = server.accept();
	        	 PlayerThread player = new PlayerThread(s, lobby);
	        	 player.start();	        	 
	        }

	        
	    }
	    public synchronized void checkQueue(PlayerThread player) {
	    	
	    }
	    
	   
}



