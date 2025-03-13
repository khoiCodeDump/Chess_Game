package Network;
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

import ChessEngine.Board;
import ChessEngine.ChessEngine;
import ChessEngine.Chess_Bot;
import ChessEngine.Piece;
import ChessUI.GameInfoPanel;
import ChessUI.LobbyWindow;
import ChessUI.PieceUI;


public class Client {
	private static class Checkboard extends JPanel{

		private static final long serialVersionUID = 1L;
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
	public static int team;
    static Board game;
    public static GameInfoPanel gameInfoWindow;
    static JFrame gameWindow;
    public static JPanel gameWindowPanel;
	static JPanel lobbyWindow;
	static JPanel cardLayoutPanel;
	static JPanel gameCardLayoutPanel;
    static CardLayout cardlayout, gameCardLayout;
    public static ObjectOutputStream oos;
    static ObjectInputStream ois;
    public static Chess_Bot chessBot;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = new Socket(host.getHostName(), 9876);
        oos =  new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        
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
             Data message = (Data) ois.readObject();
             
             if(message.command.equals("Draw_request")) {
            	 //0 no
            	 //1 yes
            	 //2 cancel
            	 if(message.team ==2 || message.team == 0) {
            		 for(int i=0; i<8; i++) {
                		 for(int j=0; j<8; j++) {
                			 Board.board[i][j].setEnabled(true);
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
            			 Board.board[i][j].setEnabled(false);
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
                			 Board.board[i][j].setEnabled(true);
                		 }
                	 }
            	 }
             }
             else if(message.command.equals("Update") || message.command.equals("En Passant")) {
            	 if(message.pawnPromo)
            	 {
            		 int[] dest = moveReflection(message.i, message.j );  
            		 Board.board[dest[0]][dest[1]].ClientUpdatePiece(new Piece(message.type, dest[0], dest[1], message.team));
            	 }
            	 else if(message.isEmpty)
            	 {
            		 int[] dest = moveReflection(message.i, message.j );  
            		 Board.board[dest[0]][dest[1]].ClientUpdatePiece(Board.emptyPiece);
            	 }
            	 else
            	 {
            		 int[] caller_dest = moveReflection(message.callerI, message.callerJ);
                	 int[] dest = moveReflection(message.i, message.j );  
                	 PieceUI callerPiece = Board.board[caller_dest[0]][caller_dest[1]];
                	 if(message.enPassant) callerPiece.curPiece.enPassant = true;
                	 Board.board[dest[0]][dest[1]].ClientUpdatePiece(callerPiece.curPiece);
            	 }
            	 
             }
             else if(message.command.equals("End")) {
            	//0 Draw
     			//1 Win
     			//2 Lose
        		gameInfoWindow.endGame(message.team);
        		socket.close();
             }
             else if(message.command.equals("Start")) {
            	 CreateBoard(message.team, null);
             }
             else if(message.command.equals("Update_Turn")) {
            	 ChessEngine.CheckKingSafety(Board.King, false);
            	 ChessEngine.turn = (ChessEngine.turn == 1) ? 2 : 1;
             	if(message.team == 1) {
         			Client.gameInfoWindow.whiteTimer.stop();
         			Client.gameInfoWindow.blackTimer.start();
         		}
         		else {
         			Client.gameInfoWindow.whiteTimer.start();
         			Client.gameInfoWindow.blackTimer.stop();
         			
         		}
             	Client.gameInfoWindow.updateCurTurn();
             }
        }
       
        
    }
    public static void CreateBoard(int t, Chess_Bot bot)
    {
    	((LobbyWindow) lobbyWindow).resetTimer();
		team = t;
		if(team == 1)gameWindow.setTitle("Team White");
		else gameWindow.setTitle("Team Black");

		gameInfoWindow = new GameInfoPanel(team, cardlayout, cardLayoutPanel, gameCardLayout, gameCardLayoutPanel, gameWindowPanel);
		gameInfoWindow.setLocation(960, 0);
		gameInfoWindow.setSize(300, 800);
		cardLayoutPanel.add(gameInfoWindow, 1);
		
		game = new Board(team, gameInfoWindow);
		game.setLocation(0, 0);
		game.setSize(960, 800);
		gameCardLayoutPanel.add(game, 1);
	
    	if(bot != null)
    	{
    		chessBot = bot;
//    		bot.SetBoard(game);
    	}
	   	 
	   	 cardlayout.next(cardLayoutPanel);
	   	 gameCardLayout.next(gameCardLayoutPanel);
	   	 gameInfoWindow.startTimer(1);
    }
    private static int[] moveReflection(int i, int j) {

	   	int[] closest_center_point = {0, 0};
	   	int[][] center_points = {{3,3}, {3,4}, {4,3}, {4,4}};
	   	int minNumSteps = 10;
	   	 for(int[] arr : center_points ) {
	   		 int steps = Math.abs(arr[0]-i) + Math.abs(arr[1]-j);

	   		 if(minNumSteps > steps) {
	   			 closest_center_point = arr;
	   			minNumSteps = steps;
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