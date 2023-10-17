

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

public class Client {
	static int team;
    static Board game;
     
    static JFrame frame, gameWindow;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
        System.out.println("Connected to server");
        ObjectOutputStream oos =  new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        System.out.println("Reading team info");
        
        //Waiting for PlayerThread to initialize Game.java
        //Game.java writeInt() to the Client
       
        frame = new LobbyWindow(ois, oos);
        EventQueue.invokeLater(new Runnable() {
 			public void run() {
 				try {
 					frame.setVisible(true);
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 			}
 		});
        while(true) {
             System.out.println("In Client loop");
             //read the server response message
//             ois = new ObjectInputStream(socket.getInputStream());
             Data message = (Data) ois.readObject();
             System.out.println("Message: " + message);
             if(message.command.equals("Move")) {
            	 
            	 int callerI = message.callerI;
            	 int callerJ = message.callerJ;
            	 int i = message.i;
            	 int j = message.j;
            	 
            	 game.pieces[message.callerI][message.callerJ].updatePiece("Empty", 0);
         		
            	 game.pieces[message.i][message.j].updatePiece(message.callerType, message.callerTeam);
            	 game.turn[0] = team;
//            	 game.gameInfo.startTimer();
             }
//             else if(message.command.equals("Start Timer")) {
//            	 long timeToStart = message.time;
//            	 long unixTime = System.currentTimeMillis() / 1000L;
//            	 while(unixTime<timeToStart)unixTime = System.currentTimeMillis() / 1000L;
//            	 game.gameInfo.startTimer();
//            	 
//             }
             else if(message.command.equals("End")) {
            	 team = -1;
            	 game = null;
            	 frame.setEnabled(true);
            	 frame.setVisible(true);
            	 gameWindow.dispose();
            	 
             }
             else if(message.command.equals("Start")) {
            	 
            	 team = message.team;
            	 game = new Board(team, ois, oos);
            	 frame.setEnabled(false);
            	 frame.setVisible(false);
            	 gameWindow = new JFrame(); 
            	 if(team == 1)gameWindow.setTitle("Team White");
                 else gameWindow.setTitle("Team Black");
            	 gameWindow.setContentPane(game);
            	 gameWindow.setBounds(100, 100, 831, 384);
                 System.out.println("Created board");
                 EventQueue.invokeLater(new Runnable() {
         			public void run() {
         				try {
         					gameWindow.setVisible(true);
         				} catch (Exception e) {
         					e.printStackTrace();
         				}
         			}
         		});
             }
            
        }
       
        
    }
}
