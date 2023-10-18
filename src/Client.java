

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
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
            	 int min_distance = Integer.MAX_VALUE;
            	 int caller_min_distance = Integer.MAX_VALUE;
            	 
            	 int[] closest_center_point = {0, 0};
            	 int[] caller_closest_center_point = {0, 0};
            	 int[][] center_points = {{3,3}, {3,4}, {4,3}, {4,4}};
            	 
            	 for(int[] arr : center_points ) {
            		 int distance = (int) Math.hypot(i-arr[0], j-arr[1]);
            		 int caller_distance = (int) Math.hypot(callerI-arr[0], callerJ-arr[1]);
            		 if(min_distance > distance) {
            			 closest_center_point = arr;
            			 min_distance = distance;
            		 }  		 
            		 if(caller_min_distance > caller_distance) {
            			 caller_closest_center_point = arr;
            			 caller_min_distance = caller_distance;
            		 }
            	 }
            	 int[] movement = {Math.abs(closest_center_point[0]-i), Math.abs(closest_center_point[1]-j)};
            	 int[] caller_movement = {Math.abs(caller_closest_center_point[0]-callerI), Math.abs(caller_closest_center_point[1]-callerJ)};
            	 System.out.println(Arrays.toString(caller_movement));

            	 int[] dest = {0,0};
            	 int[] caller_dest = {0,0};
            	 if(closest_center_point == center_points[0]) {
            		 
            		 dest[0] = 4+movement[0];
            		 dest[1] = 4+movement[1]; 
            	 }
            	 else if(closest_center_point == center_points[1]) {
            		 dest[0] = 4+movement[0];
            		 dest[1] = 3-movement[1];
            		 
            	 }
            	 else if(closest_center_point == center_points[2]) {
            		 dest[0] = 3-movement[0];
            		 dest[1] = 4+movement[1];
            		
            	 }
            	 else if(closest_center_point == center_points[3]){
            		 dest[0] = 3-movement[0];
            		 dest[1] = 3-movement[1];
            		
            	 }
            	 if(caller_closest_center_point == center_points[0]) {
        			 caller_dest[0] = 4+caller_movement[0];
            		 caller_dest[1] = 4+caller_movement[1];
        		 }
            	 else if(caller_closest_center_point == center_points[1]) {
        			 caller_dest[0] = 4+caller_movement[0];
            		 caller_dest[1] = 3-caller_movement[1];
        		 } 
            	 else  if(caller_closest_center_point == center_points[2]) {
        			 caller_dest[0] = 3-caller_movement[0];
            		 caller_dest[1] = 4+caller_movement[1];
        		 }
            	 else  if(caller_closest_center_point == center_points[3]) {
        			 caller_dest[0] = 3-caller_movement[0];
            		 caller_dest[1] = 3-caller_movement[1];
        		 }
            	 game.pieces[caller_dest[0]][caller_dest[1]].updatePiece("Empty", 0);
         		
            	 game.pieces[dest[0]][dest[1]].updatePiece(message.callerType, message.callerTeam);
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
            	 gameWindow.setBounds(100, 100, 960, 800);
            	 gameWindow.setResizable(false);
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
