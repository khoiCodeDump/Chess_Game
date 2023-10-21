import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameInfoPanel extends JPanel {
	JLabel blackTimerLabel;
	JLabel whiteTimerLabel; 
//	boolean black, white;
	Piece[][] board;
    private int blackremainingSeconds, whiteremainingSeconds, team, currentTurn;
    HashSet<Piece> teamPiece;
    Timer blackTimer, whiteTimer;
	GameInfoPanel(int playerTeam, ObjectInputStream in, ObjectOutputStream out) {
		this.board = board;
		setLayout(new GridLayout(2, 1, 0, 0));
		this.team = team;
		this.teamPiece = teamPiece;
		this.currentTurn = currentTurn;
		blackremainingSeconds = 30*60;
		whiteremainingSeconds = 30*60;
		
	
//		for(int i=0; i<8; i++) {
//			for(int j=0; j<8; j++) {
//				if(board[i][j].team == 1) {
//					if(!whites.containsKey(board[i][j].type)) {
//						whites.put(board[i][j].type, new ArrayList<int[]>());
//					}
//					whites.get(board[i][j].type).add(new int[] {i, j});
//				}
//				else if(board[i][j].team == 2) {
//					if(!blacks.containsKey(board[i][j].type)) {
//						blacks.put(board[i][j].type, new ArrayList<int[]>());
//					}
//					blacks.get(board[i][j].type).add(new int[] {i, j});
//				}
//				
//			}
//		}
		
		
//		blackTimer = new Timer(1000, new ActionListener() {
//	        @Override
//	        public void actionPerformed(ActionEvent e) {
//	        	blackremainingSeconds--;
//	            updateDisplay(blackremainingSeconds, blackTimerLabel);
//	            if (blackremainingSeconds <= 0) {
//	            	if (blackTimer != null && blackTimer.isRunning()) {
//	        			blackTimer.stop();
//	    				
//	    				for(ArrayList<int[]> positions : blacks.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(false);
//	    					}
//	    				}
//	    				for(ArrayList<int[]> positions : whites.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(true);
//	    					}
//	    				}
//	    				whiteTimer.start();
//	        		}
//	            	if(whiteremainingSeconds <=0 ) {
//	            		for(ArrayList<int[]> positions : whites.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(false);
//	    					}
//	    				}
//	            		endGame();
//	            	}
//	            	
//	            }
//	        }
//	    });
		
//		whiteTimer = new Timer(1000, new ActionListener() {
//	        @Override
//	        public void actionPerformed(ActionEvent e) {
//	        	whiteremainingSeconds--;
//	            updateDisplay(whiteremainingSeconds, whiteTimerLabel);
//	            if (whiteremainingSeconds <= 0) {
//	            	if (whiteTimer != null && whiteTimer.isRunning()) {
//	        			whiteTimer.stop();
//	        			for(ArrayList<int[]> positions : whites.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(false);
//	    					}
//	    				}
//	    				for(ArrayList<int[]> positions : blacks.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(true);
//	    					}
//	    				}
//	        		}
//	            	if(blackremainingSeconds <=0) {
//	            		for(ArrayList<int[]> positions : blacks.values()) {
//	    					for(int[] pos : positions) {
//	    						board[pos[0]][pos[1]].setEnabled(false);
//	    					}
//	    				}
//	            		endGame();
//	            	}
//	            }
//	        }
//	    });
//		blackTimerLabel = new JLabel("30:00");
//		whiteTimerLabel = new JLabel("30:00");
//		blackTimerLabel.setFont(new Font("Arial", Font.BOLD, 24));
//		whiteTimerLabel.setFont(new Font("Arial", Font.BOLD, 24));
//		blackTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		whiteTimerLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//	    add(blackTimerLabel);
//	    add(whiteTimerLabel);
	    
		
	} 
	private void endGame() {
		
	}
	
	private void updateDisplay(int remainingSeconds, JLabel timeLabel) {
	    int minutes = remainingSeconds / 60;
	    int seconds = remainingSeconds % 60;

	    String time = String.format("%02d:%02d", minutes, seconds); 
	    timeLabel.setText(time);
	}

	public void startTimer() {
		if(blackremainingSeconds > 0 && whiteremainingSeconds > 0) {
			if(whiteTimer.isRunning()) {
				whiteTimer.stop();
				blackTimer.start();
			}
			else {
				blackTimer.stop();
				whiteTimer.start();
			}
			if(currentTurn == team) {
				for(Piece piece : teamPiece) {
					
				}
			}
			
		}
	}
}
