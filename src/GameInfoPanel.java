import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class GameInfoPanel extends JPanel {
	JLabel blackTimerLabel;
	JLabel whiteTimerLabel; 
	JLabel currentTurn;
	Piece[][] board;
    private int blackremainingSeconds, whiteremainingSeconds, team;
    Timer blackTimer, whiteTimer;
    
	GameInfoPanel(int playerTeam) {
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
	            	if(checkWinCon().equals("draw")){
	            		blackTimerLabel.setText("Draw");
	            		whiteTimerLabel.setText("Draw");
	            	}
	            	else {
	            		blackTimerLabel.setText("Lose");
	            		whiteTimerLabel.setText("Win");
	            	}
	            	endGame();
	            }
	        }

	    });
		
		whiteTimer = new Timer(1000, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	whiteremainingSeconds--;
	            updateDisplay(whiteremainingSeconds, whiteTimerLabel);
	            if (whiteremainingSeconds <= 0) {
	            	if(checkWinCon().equals("draw")){
	            		blackTimerLabel.setText("Draw");
	            		whiteTimerLabel.setText("Draw");
	            	}
	            	else {
	            		blackTimerLabel.setText("Win");
	            		whiteTimerLabel.setText("Lose");
	            	}
	            	endGame();
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
		
		JButton forfeit = new JButton("Forfeit");
		springLayout.putConstraint(SpringLayout.NORTH, forfeit, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, forfeit, 35, SpringLayout.WEST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, forfeit, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, forfeit, 139, SpringLayout.WEST, buttonPanel);
		
		JButton draw = new JButton("Draw");
		springLayout.putConstraint(SpringLayout.NORTH, draw, 60, SpringLayout.NORTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.WEST, draw, -139, SpringLayout.EAST, buttonPanel);
		springLayout.putConstraint(SpringLayout.SOUTH, draw, -60, SpringLayout.SOUTH, buttonPanel);
		springLayout.putConstraint(SpringLayout.EAST, draw, -35, SpringLayout.EAST, buttonPanel);
		
		forfeit.setBackground(hexToColor("#779952"));
		forfeit.setForeground(Color.white);
		forfeit.setFocusable(false);
		draw.setBackground(hexToColor("#779952"));
		draw.setForeground(Color.white);
		draw.setFocusable(false);
		
		buttonPanel.add(forfeit);
		buttonPanel.add(draw);
		buttonPanel.setBackground(hexToColor("312E2B"));
		
		if(team == 1) {
			add(blackTimerLabel);
		}
		else add(whiteTimerLabel);
		
		add(currentTurn);
		add(buttonPanel);
		if(team == 1) {
		    add(whiteTimerLabel);
		}
		else add(blackTimerLabel);
		
	} 
	public void setBoard(Piece[][] board) {
		this.board = board;
	}
	public void updateCurTurn() {
		if(currentTurn.getText().equals("White to Move")) {
			currentTurn.setText("Black to Move");
		}
		else {
			currentTurn.setText("White to Move");
		}
	}
	protected String checkWinCon() {
		HashSet<String> opponent_pieces = new HashSet<>();
		if(opponent_pieces.size() == 1) {
			return "draw";
		}
		HashSet<String> required_piece_types_to_draw = new HashSet<>();
		for(int i=0; i<8; i++) {
			for(int j=0; j<8;j++) {
				opponent_pieces.add(this.board[i][j].type);
			}
		}
		
		required_piece_types_to_draw.add("Bishop");
		required_piece_types_to_draw.add("Knight");
		int counter = 0;
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
	private void endGame() {
		
	}
	public void startTimer(int team) {
		if(team ==1) {
			whiteTimer.start();
			blackTimer.stop();
		}
		else {
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
