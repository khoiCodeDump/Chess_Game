import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

public class GameInfoPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel blackTimerLabel;
	JLabel whiteTimerLabel; 
	JLabel currentTurn;
	PieceUI[][] board;
	JButton draw;
	JButton playAgain;
	JButton forfeit;
    private int blackremainingSeconds, whiteremainingSeconds, team;
    Timer blackTimer;
	Timer whiteTimer;
    
	GameInfoPanel(int playerTeam, CardLayout cardlayout, JPanel cardLayoutPanel, CardLayout gameCardLayout, JPanel gameCardLayoutPanel, JPanel gameWindowPanel) {
		this.team = playerTeam;
		
		setLayout(new GridLayout(4, 1, 0, 0));
		setBackground(hexToColor("312E2B"));
		blackremainingSeconds = 10*60;
		whiteremainingSeconds = 10*60 + 1;
		
		blackTimer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	blackremainingSeconds--;
	            updateDisplay(blackremainingSeconds, blackTimerLabel);
	            if (blackremainingSeconds <= 0) {
	            	String status = checkWinCon();
	            	if(status.equals("draw")){
	            		blackTimerLabel.setText("Draw");
	            		whiteTimerLabel.setText("Draw");
	            	}
	            	else {
	            		blackTimerLabel.setText("Lose");
	            		whiteTimerLabel.setText("Win");
	            	}
	            	endGame(status);
	            }
	        }

	    });
		
		whiteTimer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	whiteremainingSeconds--;
	            updateDisplay(whiteremainingSeconds, whiteTimerLabel);
	            if (whiteremainingSeconds <= 0) {
	            	String status = checkWinCon();
	            	if(status.equals("draw")){
	            		blackTimerLabel.setText("Draw");
	            		whiteTimerLabel.setText("Draw");
	            	}
	            	else {
	            		blackTimerLabel.setText("Win");
	            		whiteTimerLabel.setText("Lose");
	            	}
	            	endGame(status);
	            }
	        }
	    });
		blackTimerLabel = new JLabel("10:00");
		blackTimerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		blackTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		blackTimerLabel.setForeground(Color.white);

		whiteTimerLabel = new JLabel("10:00");
		whiteTimerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		whiteTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		whiteTimerLabel.setForeground(Color.white);

		currentTurn = new JLabel("White to Move");
		currentTurn.setFont(new Font("Arial", Font.BOLD, 24));
		currentTurn.setHorizontalAlignment(SwingConstants.CENTER);
		currentTurn.setForeground(Color.white);
		
	    JPanel buttonPanel = new JPanel();
	    SpringLayout springLayout = new SpringLayout();
	    buttonPanel.setLayout(springLayout);
		
	    Border raisedbevel = BorderFactory.createRaisedBevelBorder();
		forfeit = new JButton("Forfeit");
		springLayout.putConstraint(SpringLayout.NORTH, forfeit, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, forfeit, 35, SpringLayout.WEST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, forfeit, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, forfeit, 139, SpringLayout.WEST, buttonPanel);
		forfeit.setBorder(raisedbevel);
		forfeit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 endGame("lose");
			}
			
		});
		
		draw = new JButton("Draw");
		springLayout.putConstraint(SpringLayout.NORTH, draw, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, draw, -139, SpringLayout.EAST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, draw, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, draw, -35, SpringLayout.EAST, buttonPanel);
		draw.setBorder(raisedbevel);
		draw.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					Client.oos.writeObject(new Data("Draw", playerTeam));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JPanel loadingPanel = loadingPanel();
				loadingPanel.setLocation(960, 0);
				loadingPanel.setSize(350, 200);
				gameWindowPanel.add(loadingPanel);
			}
			
		});
		
		playAgain = new JButton("Play Again");
		springLayout.putConstraint(SpringLayout.NORTH, playAgain, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, playAgain, 50, SpringLayout.WEST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, playAgain, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, playAgain, 250, SpringLayout.WEST, buttonPanel);
		playAgain.setBorder(raisedbevel);
		playAgain.setEnabled(false);
		playAgain.setVisible(false);
		playAgain.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				 cardlayout.next(cardLayoutPanel);
            	 gameCardLayout.next(gameCardLayoutPanel);
            	 
			}
			
		});
		
		forfeit.setBackground(hexToColor("#779952"));
		forfeit.setForeground(Color.white);
		forfeit.setFocusable(false);
		draw.setBackground(hexToColor("#779952"));
		draw.setForeground(Color.white);
		draw.setFocusable(false);
		playAgain.setBackground(hexToColor("#779952"));
		playAgain.setForeground(Color.white);
		playAgain.setFocusable(false);
		
		buttonPanel.add(forfeit);
//		buttonPanel.add(draw);
		buttonPanel.add(playAgain);
		buttonPanel.setBackground(hexToColor("312E2B"));
		
		if(team == 1) {
			add(blackTimerLabel);
			add(currentTurn);
			add(buttonPanel);
			add(whiteTimerLabel);
		}
		
		else {
			add(whiteTimerLabel);
			add(currentTurn);
			add(buttonPanel);
			add(blackTimerLabel);
		}
		
	} 
	private JPanel loadingPanel() {
	    JPanel panel = new JPanel();
	    BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
	    panel.setLayout(layoutMgr);

	  
	    ImageIcon imageIcon = new ImageIcon("images/Rhombus.gif");
	    JLabel iconLabel = new JLabel();
	    iconLabel.setIcon(imageIcon);
	    imageIcon.setImageObserver(iconLabel);

	    JLabel label = new JLabel("Loading...");
	    panel.add(iconLabel);
	    panel.add(label);
	    JButton cancel = new JButton();
	    cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//2 cancel
					Client.oos.writeObject(new Data("Draw_request",2));
					panel.setEnabled(false);
					panel.setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
	    	
	    });
	    panel.add(cancel);
	    
	    return panel;
	}
	public void setBoard(PieceUI[][] board) {
		this.board = board;
	}
	public void updateCurTurn() {
		for(Piece piece : PieceManager.enpassantList)
		{
			piece.enPassant = false;
		}
		PieceManager.enpassantList.clear();
		if(currentTurn.getText().equals("White to Move")) {
			currentTurn.setText("Black to Move");
		}
		else {
			currentTurn.setText("White to Move");
		}
	}
	protected String checkWinCon() {
		HashSet<String> opponent_pieces = new HashSet<>();
		
		HashSet<String> required_piece_types_to_draw = new HashSet<>();
		required_piece_types_to_draw.add("Bishop");
		required_piece_types_to_draw.add("Knight");
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8;j++) {
				if(board[i][j].curPiece != null)
				{
					opponent_pieces.add(board[i][j].curPiece.type);
				}
			}
		}
		
		for(String opp_piece: opponent_pieces) {
			if(required_piece_types_to_draw.contains(opp_piece)) {
				required_piece_types_to_draw.remove(opp_piece);
			}
		}
		if(required_piece_types_to_draw.size() == 1) {
			if( opponent_pieces.size() >= 3) return "lose";
			return "draw";
			
		}
		return "lose";
		
	}
	public void endGame(String endGameStatus) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j].setEnabled(false);
			}
		}
		
		try {
			//0 Draw
			//1 Win
			//2 Lose
			if(endGameStatus.equals("draw")) {
				Client.oos.writeObject(new Data("End", 0));
				currentTurn.setText("Draw");
			}
			else if(endGameStatus.equals("win")) {
				Client.oos.writeObject(new Data("End", 2));
				currentTurn.setText("Victory");
			}
			
			else {
				Client.oos.writeObject(new Data("End", 1));
				currentTurn.setText("Defeat");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		whiteTimer.stop();
		blackTimer.stop();
		
		forfeit.setEnabled(false);
		draw.setEnabled(false);
		forfeit.setVisible(false);
		draw.setVisible(false);
		playAgain.setEnabled(true);
		playAgain.setVisible(true);
	}
	public void endGame(int endGameStatus) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j].setEnabled(false);
			}
		}
		//0 Draw
		//1 Win
		//2 Lose
		
		if(endGameStatus== 0) {
			currentTurn.setText("Draw");
		}
		else if(endGameStatus ==1) {
			currentTurn.setText("Victory");
		}
		else {
			currentTurn.setText("Defeat");
		}
		
		whiteTimer.stop();
		blackTimer.stop();
		
		forfeit.setEnabled(false);
		draw.setEnabled(false);
		forfeit.setVisible(false);
		draw.setVisible(false);
		playAgain.setEnabled(true);
		playAgain.setVisible(true);
	}
	public void startTimer(int team) {
		
		if(team ==1) {
			whiteTimer.start();
			blackTimer.stop();
		}
		else if (team==2){
			blackTimer.start();
			whiteTimer.stop();
		}
	}
	private void updateDisplay(int remainingSeconds, JLabel timeLabel) {
	    int minutes = remainingSeconds / 60;
	    int seconds = remainingSeconds % 60;

	    String time = String.format("%02d:%02d", minutes, seconds); 
	    timeLabel.setText(time);
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
