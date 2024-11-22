package Sandbox;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import javax.swing.BorderFactory;
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
	JLabel currentTurnLabel;
	PieceUI[][] board;
	JButton draw;
	JButton playAgain;
	JButton forfeit;
    int team;
    Timer blackTimer;
	Timer whiteTimer;
    private long whiteStartTime, blackStartTime;
    private double whiteRemainingSeconds = 600; // 10 minutes
    private double blackRemainingSeconds = 600;
    private double timeInTimer = 0;
    
	GameInfoPanel(int playerTeam, CardLayout cardlayout, JPanel cardLayoutPanel, CardLayout gameCardLayout, JPanel gameCardLayoutPanel, JPanel gameWindowPanel) {
		this.team = playerTeam;
		
		setLayout(new GridLayout(4, 1, 0, 0));
		setBackground(hexToColor("312E2B"));
		blackRemainingSeconds = 10*60;
		whiteRemainingSeconds = 10*60 + 1;
		
		blackTimer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	blackRemainingSeconds--;
	        	timeInTimer++;
	            updateBlackDisplay();
	            if (blackRemainingSeconds <= 0) {
	            	String status = checkWinCon(2);
	            	blackTimerLabel.setText("");
            		whiteTimerLabel.setText("");
	            	endGame(status);
	            }
	        }

	    });
		
		whiteTimer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	whiteRemainingSeconds--;
	        	timeInTimer++;
	            updateWhiteDisplay();
	            if (whiteRemainingSeconds <= 0) {
	            	String status = checkWinCon(1);
	            	blackTimerLabel.setText("");
            		whiteTimerLabel.setText("");
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
			
		currentTurnLabel = new JLabel("White to Move");
		currentTurnLabel.setFont(new Font("Arial", Font.BOLD, 24));
		currentTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		currentTurnLabel.setForeground(Color.white);
		
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
				 endGame("lose");
			}
			
		});
		
		draw = new JButton("Draw");
		springLayout.putConstraint(SpringLayout.NORTH, draw, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, draw, -139, SpringLayout.EAST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, draw, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, draw, -35, SpringLayout.EAST, buttonPanel);
		draw.setBorder(raisedbevel);

		
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
			add(currentTurnLabel);
			add(buttonPanel);
			add(whiteTimerLabel);
		}
		
		else {
			add(whiteTimerLabel);
			add(currentTurnLabel);
			add(buttonPanel);
			add(blackTimerLabel);
		}
		
	} 
	public void setBoard(PieceUI[][] board) {
		this.board = board;
	}
	public void updateCurTurn() {
		for(Piece piece : ChessEngine.enpassantList)
		{
			piece.enPassant = false;
		}
		ChessEngine.enpassantList.clear();
		if(currentTurnLabel.getText().equals("White to Move")) {
			currentTurnLabel.setText("Black to Move");
		}
		else {
			currentTurnLabel.setText("White to Move");
		}
	}
	protected String checkWinCon(int timerTeam) {
		// Get the pieces of the player who still has time (opponent of timerTeam)
		HashSet<String> remainingPlayerPieces = new HashSet<>();
		int winningTeam = (timerTeam == 1) ? 2 : 1;
		
		// Count pieces for the player who still has time
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(board[i][j].curPiece != null && !board[i][j].curPiece.isEmpty) {
					if(board[i][j].curPiece.team == winningTeam) {
						remainingPlayerPieces.add(board[i][j].curPiece.type);
					}
				}
			}
		}

		// Check if the player with remaining time has sufficient material to win
		boolean insufficientMaterial = isInsufficientMaterial(remainingPlayerPieces);
		
		if(insufficientMaterial) {
			return "Draw"; // Not enough material to win
		}
		
		// Return win/lose based on which team ran out of time
		return (timerTeam == team) ? "Defeat" : "Victory";
	}

	private boolean isInsufficientMaterial(HashSet<String> pieces) {
		// Remove pawns and major pieces as they can always deliver checkmate
		if(pieces.contains("Pawn") || pieces.contains("Queen") || pieces.contains("Rook")) {
			return false;
		}
		
		// Only king left
		if(pieces.size() == 1) {
			return true;
		}
		
		// King + single minor piece
		if(pieces.size() == 2 && 
			(pieces.contains("Bishop") || pieces.contains("Knight"))) {
			return true;
		}
		
		// King + two knights is insufficient material
		if(pieces.size() == 2 && pieces.contains("Knight")) {
			int knightCount = 0;
			for(String piece : pieces) {
				if(piece.equals("Knight")) knightCount++;
			}
			if(knightCount == 2) return true;
		}
		
		return false;
	}

	public void endGame(String endGameStatus) {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				board[i][j].setEnabled(false);
			}
		}


		currentTurnLabel.setText(endGameStatus);
	
		

		whiteTimer.stop();
		blackTimer.stop();
		
		forfeit.setEnabled(false);
		draw.setEnabled(false);
		forfeit.setVisible(false);
		draw.setVisible(false);
		playAgain.setEnabled(true);
		playAgain.setVisible(true);
	}
	private void updateWhiteDisplay() {
		int minutes = (int) (whiteRemainingSeconds / 60);
		int seconds = (int) (whiteRemainingSeconds % 60);
		whiteTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
	}
	private void updateBlackDisplay() {
		int minutes = (int) (blackRemainingSeconds / 60);
		int seconds = (int) (blackRemainingSeconds % 60);
		blackTimerLabel.setText(String.format("%02d:%02d", minutes, seconds));
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
	
	public synchronized void stopTimer(int Team) {
        if (Team == 1) {
        	long elapsedNanos = System.nanoTime() - whiteStartTime;
        	double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        	whiteRemainingSeconds -= (elapsedSeconds - timeInTimer);
            updateWhiteDisplay();

            whiteTimer.stop();
        } else {
        	long elapsedNanos = System.nanoTime() - blackStartTime;
        	double elapsedSeconds = elapsedNanos / 1_000_000_000.0;
        	blackRemainingSeconds -= (elapsedSeconds - timeInTimer);
        	updateBlackDisplay();
            blackTimer.stop();
        }
    }
    
    public synchronized void startTimer(int Team) 
    {   
    	timeInTimer = 0;
        if (Team == 1) {
            whiteTimer.start();
            
        	whiteStartTime = System.nanoTime();
           
        } else {
            blackTimer.start();

        	blackStartTime = System.nanoTime();
        }
    }
}
