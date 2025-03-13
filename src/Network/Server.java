package Network;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


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
	        	 Socket clientSocket = server.accept();
	        	 PlayerThread player = new PlayerThread(clientSocket, lobby);
	        	 player.start();	        	 
	        }

	        
	    }
	   
}



