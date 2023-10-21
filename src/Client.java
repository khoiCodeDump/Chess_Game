import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
public class Client {
	static int team;
    static Board game;
    static GameInfoPanel gameInfoWindow;
    static JFrame frame, gameWindow;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
        ObjectOutputStream oos =  new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        
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
             //read the server response message
//             ois = new ObjectInputStream(socket.getInputStream());
             Data message = (Data) ois.readObject();
             if(message.command.equals("Move")) {
            	 
            	 int callerI = message.callerI;
            	 int callerJ = message.callerJ;
            	 int i = message.i;
            	 int j = message.j;
            	 double min_distance = Double.MAX_VALUE;
            	 double caller_min_distance = Double.MAX_VALUE;
            	 
            	 int[] closest_center_point = {0, 0};
            	 int[] caller_closest_center_point = {0, 0};
            	 int[][] center_points = {{3,3}, {3,4}, {4,3}, {4,4}};
            	 
            	 for(int[] arr : center_points ) {
            		 double distance = Math.hypot(i-arr[0], j-arr[1]);
            		 double caller_distance = Math.hypot(callerI-arr[0], callerJ-arr[1]);
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
            	 else if(caller_closest_center_point == center_points[2]) {
        			 caller_dest[0] = 3-caller_movement[0];
            		 caller_dest[1] = 4+caller_movement[1];
        		 }
            	 else if(caller_closest_center_point == center_points[3]) {
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
            	 gameWindow = new JFrame();
            	 game = new Board(team, ois, oos, gameWindow);
            	 frame.setEnabled(false);
            	 frame.setVisible(false);
            	  
            	 if(team == 1)gameWindow.setTitle("Team White");
                 else gameWindow.setTitle("Team Black");
            	 gameWindow.setContentPane(game);
            	 gameWindow.setBounds(100, 100, 1110, 800);
            	 game.setLocation(0, 0);
         		 game.setSize(960, 800);
         		 gameInfoWindow = new GameInfoPanel(team, ois, oos);
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