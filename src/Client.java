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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


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
        gameWindow.setResizable(false);
   	 	gameWindow.setBounds(100, 100, 1260, 839);
   	 	gameWindow.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
   	    gameWindowPanel = new JPanel();
   	    gameWindowPanel.setLayout(null);
   	    gameWindow.setContentPane(gameWindowPanel);
   	    
   	    gameCardLayout = new CardLayout();
	 	gameCardLayoutPanel = new JPanel();
	 	gameCardLayoutPanel.setLayout(gameCardLayout);
	 	gameCardLayoutPanel.setLocation(0,0);
	 	gameCardLayoutPanel.setSize(960, 800);
	 	
	   	Checkboard boardPlaceHolder = new Checkboard();
	   	boardPlaceHolder.setLocation(0,0);
	   	boardPlaceHolder.setSize(960, 800);
	   	
	   	gameCardLayoutPanel.add(boardPlaceHolder, 0);

   	    cardlayout = new CardLayout();
   	    cardLayoutPanel = new JPanel();
   	    cardLayoutPanel.setLayout(cardlayout);
   	    cardLayoutPanel.setLocation(960, 0);
   	    cardLayoutPanel.setSize(300, 800);

   	 	lobbyWindow = new LobbyWindow(ois, oos);
   	 	lobbyWindow.setLocation(960, 0);
   	 	lobbyWindow.setSize(300, 800);
   	 	
   	 	
   	 	cardLayoutPanel.add(lobbyWindow, 0);
	   	
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
             if(message.command.equals("En Passante")) {
            	 int[] reflectedPiece = moveReflection(message.i, message.j);
            	 game.pieces[reflectedPiece[0]][reflectedPiece[1]].enPassante = true;
            	 game.pieces[reflectedPiece[0]][reflectedPiece[1]].gameWindow.enpasseList.add(game.pieces[reflectedPiece[0]][reflectedPiece[1]]);
             }
             if(message.command.equals("Pawn_Promo")) {
            	 int[] dest = moveReflection(message.i, message.j);
            	 game.pieces[dest[0]][dest[1]].updatePiece(message.callerType);
            	 if(gameInfoWindow.whiteTimer.isRunning()) gameInfoWindow.startTimer(2);
            	 else gameInfoWindow.startTimer(1);
            	 gameInfoWindow.updateCurTurn();
             }
             else if(message.command.equals("Castle_update")) {
            	 int[] reflectedRook = moveReflection(message.i, message.j);
            	 int[] reflectedEmpty = moveReflection(message.callerI, message.callerJ);
            	 game.pieces[reflectedRook[0]][reflectedRook[1]].castleUpdatePiece(message.callerTeam);
            	 game.pieces[reflectedEmpty[0]][reflectedEmpty[1]].updatePiece("Empty", 0);
//            	 if(gameInfoWindow.whiteTimer.isRunning()) gameInfoWindow.startTimer(2);
//            	 else gameInfoWindow.startTimer(1);
//        		 gameInfoWindow.updateCurTurn();

             }
             else if(message.command.equals("Draw_request")) {
            	 //0 no
            	 //1 yes
            	 //2 cancel
            	 if(message.team ==2 || message.team == 0) {
            		 for(int i=0; i<8; i++) {
                		 for(int j=0; j<8; j++) {
                			 game.pieces[i][j].setEnabled(true);
                		 }
                	 }
            	 }
            	 else {
             		gameInfoWindow.endGame(0);
            	 }
             }
             else if(message.command.equals("Draw")) {
            	 int choice = JOptionPane.showConfirmDialog(gameCardLayoutPanel, "Draw?", "Draw request", JOptionPane.YES_NO_OPTION);
            	 for(int i=0; i<8; i++) {
            		 for(int j=0; j<8; j++) {
            			 game.pieces[i][j].setEnabled(false);
            		 }
            	 }
            	 
            	 if(choice == JOptionPane.YES_OPTION) {
            		 gameInfoWindow.endGame(0);
            		 //0 no
            		 //1 yes
            		 oos.writeObject(new Data("Draw_request", 1));
            	 }
            	 else {
            		 oos.writeObject(new Data("Draw_request", 0));

            		 for(int i=0; i<8; i++) {
                		 for(int j=0; j<8; j++) {
                			 game.pieces[i][j].setEnabled(true);
                		 }
                	 }
            	 }
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
            	
             }
             else if(message.command.equals("End")) {
            	//0 Draw
     			//1 Win
     			//2 Lose
        		gameInfoWindow.endGame(message.team);
             }
             else if(message.command.equals("Start")) {
            	 ((LobbyWindow) lobbyWindow).resetTimer();
            	 team = message.team;
            	 if(team == 1)gameWindow.setTitle("Team White");
                 else gameWindow.setTitle("Team Black");

            	 gameInfoWindow = new GameInfoPanel(team, cardlayout, cardLayoutPanel, gameCardLayout, gameCardLayoutPanel, gameWindowPanel);
            	 gameInfoWindow.setLocation(960, 0);
            	 gameInfoWindow.setSize(300, 800);
            	 cardLayoutPanel.add(gameInfoWindow, 1);
            	 
            	 game = new Board(team, ois, oos, gameInfoWindow);
            	 game.setLocation(0, 0);
         		 game.setSize(960, 800);
            	 gameCardLayoutPanel.add(game, 1);

            	 cardlayout.next(cardLayoutPanel);
            	 gameCardLayout.next(gameCardLayoutPanel);
            	 gameInfoWindow.startTimer(1);
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