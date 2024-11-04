package Sandbox;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;



public class Client {
	private static class Checkboard extends JPanel{
		/**
		 * 
		 */
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
	static int team;
    static Board game;
    static GameInfoPanel gameInfoWindow;
    static JFrame gameWindow;
    static JPanel gameWindowPanel, lobbyWindow, cardLayoutPanel, gameCardLayoutPanel;
    static CardLayout cardlayout, gameCardLayout;
    static Chess_Bot chessBot;
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{      
        
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

   	 	lobbyWindow = new LobbyWindow();
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
    	}
	   	 
	   	 cardlayout.next(cardLayoutPanel);
	   	 gameCardLayout.next(gameCardLayoutPanel);
	   	 gameInfoWindow.startTimer(team);
    }
  
}