import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Client {
	private static class Checkboard extends JPanel{
		static final int CHECKER_WIDTH= 120;
		static final int CHECKER_HEIGHT = 100;
		Checkboard(){
			setSize(960, 800);
		}
		@Override
		public void paintComponent(Graphics g) {
			Color themePieceWhite = hexToColor("#edeed1");
			g.setColor(themePieceWhite);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(hexToColor("#779952"));
			for (int x = 0; x < 8; x++) {
				int y = (x%2 == 0) ? 1 : 0;
                for (; y < 8; y += 2) {
                    g.fillRect(x*CHECKER_WIDTH, y*CHECKER_HEIGHT, CHECKER_WIDTH, CHECKER_HEIGHT);
                }
            }
		}
		public Color hexToColor(String hex) {
	        // Remove the "#" symbol if present
	        if (hex.startsWith("#")) {
	            hex = hex.substring(1);
	        }

	        // Parse the hex string to get RGB values
	        int red = Integer.parseInt(hex.substring(0, 2), 16);
	        int green = Integer.parseInt(hex.substring(2, 4), 16);
	        int blue = Integer.parseInt(hex.substring(4, 6), 16);

	        // Create and return the Color object
	        return new Color(red, green, blue);
	    }
	}
	static int team;
    static Board game;
    static GameInfoPanel gameInfoWindow;
    static JFrame gameWindow;
    static JPanel gameWindowPanel, lobbyWindow, cardLayoutPanel, gameCardLayoutPanel;
    static CardLayout cardlayout, gameCardLayout;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
        ObjectOutputStream oos =  new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        
        //Waiting for PlayerThread to initialize Game.java
        //Game.java writeInt() to the Client
        gameWindow = new JFrame();
   	 	gameWindow.setBounds(100, 100, 1260, 839);
   	 	
   	    gameWindowPanel = new JPanel();
   	    gameWindowPanel.setLayout(null);
   	    gameWindow.setContentPane(gameWindowPanel);

   	    cardlayout = new CardLayout();
   	    cardLayoutPanel = new JPanel();
   	    cardLayoutPanel.setLayout(cardlayout);
   	    cardLayoutPanel.setLocation(960, 0);
   	    cardLayoutPanel.setSize(300, 800);
   	    
   	    gameInfoWindow = new GameInfoPanel(team);
   	    gameInfoWindow.setLocation(960, 0);
   	    gameInfoWindow.setSize(300, 800);

   	 	lobbyWindow = new LobbyWindow(ois, oos);
   	 	lobbyWindow.setLocation(960, 0);
   	 	lobbyWindow.setSize(300, 800);
   	 	
   	 	cardLayoutPanel.add(lobbyWindow, 0);

   	 	cardLayoutPanel.add(gameInfoWindow, 1);
//   	 	gameWindowPanel.add(lobbyWindow);
   	 	
   	 	gameCardLayout = new CardLayout();
   	 	gameCardLayoutPanel = new JPanel();
   	 	gameCardLayoutPanel.setLayout(gameCardLayout);
   	 	gameCardLayoutPanel.setLocation(0,0);
   	 	gameCardLayoutPanel.setSize(960, 800);
   	 	
	   	Checkboard boardPlaceHolder = new Checkboard();
	   	boardPlaceHolder.setLocation(0,0);
	   	boardPlaceHolder.setSize(960, 800);
	   	
	   	gameCardLayoutPanel.add(boardPlaceHolder, 0);
	   	
	   	gameWindowPanel.add(gameCardLayoutPanel);
	   	gameWindowPanel.add(cardLayoutPanel);
	   	
        EventQueue.invokeLater(new Runnable() {
 			public void run() {
 				try {
 					gameWindow.setVisible(true);
 				} catch (Exception e) {
 					e.printStackTrace();
 				}
 			}
 		});
        while(true) {
             //read the server response message
//             ois = new ObjectInputStream(socket.getInputStream());
             Data message = (Data) ois.readObject();
             if(message.command.equals("Pawn_Promo")) {
            	 int[] dest = moveReflection(message.i, message.j);
            	 game.pieces[dest[0]][dest[1]].updatePiece(message.callerType);
            	 if(gameInfoWindow.whiteTimer.isRunning()) gameInfoWindow.startTimer(2);
            	 else gameInfoWindow.startTimer(1);
            	 gameInfoWindow.updateCurTurn();
             }
             else if(message.command.equals("Move")) {
            	 
            	 int[] caller_dest = moveReflection(message.callerI, message.callerJ);
            	 int[] dest = moveReflection(message.i, message.j );
            	 game.pieces[caller_dest[0]][caller_dest[1]].updatePiece("Empty", 0);
         		
            	 game.pieces[dest[0]][dest[1]].updatePiece(message.callerType, message.callerTeam);
            	 game.turn[0] = team;
            	 
            	 if(message.pawnPromo == 0) {
            		 if(gameInfoWindow.whiteTimer.isRunning()) gameInfoWindow.startTimer(2);
                	 else gameInfoWindow.startTimer(1);
            		 gameInfoWindow.updateCurTurn();
            	 }
            	
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
            	 gameWindow.dispose();
            	 
             }
             else if(message.command.equals("Start")) {
            	 
            	 team = message.team;
            	
            	
            	 game = new Board(team, ois, oos, gameInfoWindow);

//            	 gameWindowPanel.setBounds(100, 100, 1110, 800);

            	 if(team == 1)gameWindow.setTitle("Team White");
                 else gameWindow.setTitle("Team Black");
  
            	 gameCardLayoutPanel.add(game, 1);
            	 game.setLocation(0, 0);
         		 game.setSize(960, 800);
         		 
            	 cardlayout.next(cardLayoutPanel);
            	 gameCardLayout.next(gameCardLayoutPanel);
            	 gameInfoWindow.startTimer(1);
//         		 gameInfoWindow.setLocation(960, 0);
//         		 gameInfoWindow.setSize(300, 800);
//         		 gameWindowPanel.add(gameInfoWindow);
         		 
//            	 gameWindow.setResizable(false);
            	 
                 System.out.println("Created board");
                 
                
             }
            
        }
       
        
    }
    private static int[] moveReflection(int i, int j) {

	   	double min_distance = Double.MAX_VALUE;
	   	 
	   	int[] closest_center_point = {0, 0};
	   	int[][] center_points = {{3,3}, {3,4}, {4,3}, {4,4}};
	   	 
	   	 for(int[] arr : center_points ) {
	   		 double distance = Math.hypot(i-arr[0], j-arr[1]);
	   		 if(min_distance > distance) {
	   			 closest_center_point = arr;
	   			 min_distance = distance;
	   		 }  		 

	   	 }
	   	 int[] movement = {Math.abs(closest_center_point[0]-i), Math.abs(closest_center_point[1]-j)};
	
	   	 int[] dest = {0,0};
	
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
	   	 return dest;
    }
}