 import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.ImageIcon;

public class PieceActionListener implements ActionListener {
	String type;
	int team, i, j, callerTeam;
	Piece[][] board;
	String callerType;
	int callerI, callerJ;

	PieceActionListener(String type, int i, int j, int team, Piece[][] board){
		this.type = type;
		this.team = team;
		 this.board = board;
		 this.i = i;
		 this.j = j;
		 this.callerI = -1;
		 this.callerJ = -1;
	}
	public void setOverride(String newType, int newTeam, int callerI, int callerJ) {
		callerType = newType;
		this.callerTeam = newTeam;
		this.callerI = callerI;
		this.callerJ = callerJ;
	}
	public void unsetOverride() {
		callerType = null;
		callerTeam = 0; 
		this.callerI = -1;
		this.callerJ = -1;
		board[i][j].setBackground(new Color(160, 120, 60));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(callerType != null) {
			updatePiece();
		}
		else if(board[i][j].playerTeam != this.team) return;
		else if(this.team != board[i][j].currentTurn[0]) return;
		else {
			if(!board[i][j].curPieces.isEmpty() && !board[i][j].curPieces.contains(board[i][j])) {
				for(Piece setColor : board[i][j].curPieces) {
					setColor.setBackground(new Color(160, 120, 60));
					setColor.listener.unsetOverride();
				}
				board[i][j].curPieces.clear();
				
			}
			if(type.equals("King")) {
				if(i+1 < 8 && board[i+1][j].team != this.team) {
	        		board[i+1][j].listener.setOverride(type, team, i, j);
	        		board[i+1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+1][j]);
	        		
	        	}
	        	if(i-1 > -1 && board[i-1][j].team != this.team) {
	        		board[i-1][j].listener.setOverride(type, team, i, j);
	        		board[i-1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-1][j]);

	        	}
	        	if(j-1 > -1 && board[i][j-1].team != this.team) {
	        		board[i][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][j-1]);

	        	}
	        	if(j+1 < 8 && board[i][j+1].team != this.team) {
	        		board[i][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][j+1]);

	        	}
	        	
	        	if(i+1 <8 && j+1 < 8 && board[i+1][j+1].team != this.team) {
	        		board[i+1][j+1].listener.setOverride(type, team, i, j);
	        		board[i+1][j+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+1][j+1]);

	        	}
	        	if(i-1 > -1 && j-1 > -1 && board[i-1][j-1].team != this.team) {
	        		board[i-1][j-1].listener.setOverride(type, team, i, j);
	        		board[i-1][j-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-1][j-1]);

	        	}
	        	if(i+1 <8 && j-1 > -1 && board[i+1][j-1].team != this.team) {
	        		board[i+1][j-1].listener.setOverride(type, team, i, j);
	        		board[i+1][j-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+1][j-1]);

	        	}
	        	if(i-1 > -1 && j+1 < 8 && board[i-1][j+1].team != this.team) {
	        		board[i-1][j+1].listener.setOverride(type, team, i, j);
	        		board[i-1][j+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-1][j+1]);

	        	}
			}
			else if(type.equals("Queen")) {
				for(int a=i; a+1 < 8 && board[a+1][j].team != this.team; a++) {
	        		
	        		board[a+1][j].listener.setOverride(type, team, i, j);
	        		board[a+1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][j]);
	        		
	        	}
	        	for(int a=i; a-1 > -1 && board[a-1][j].team != this.team; a--) {
	        		board[a-1][j].listener.setOverride(type, team, i, j);
	        		board[a-1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][j]);
	        		
	        	}
	        	for(int a=j; a-1 > -1 && board[i][a-1].team != this.team; a--) {
	        		board[i][a-1].listener.setOverride(type, team, i, j);
	        		board[i][a-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][a-1]);

	        	}
	        	for(int a=j; a+1 < 8 && board[i][a+1].team != this.team; a++) {
	        		board[i][a+1].listener.setOverride(type, team, i, j);
	        		board[i][a+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][a+1]);

	        	}
	        	
	        	for(int a=i, b=j; a+1 <8 && b+1 < 8 && board[i+1][j+1].team != this.team; a++, b++) {
	        		board[a+1][b+1].listener.setOverride(type, team, i, j);
	        		board[a+1][b+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][b+1]);

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b-1 > -1 && board[a-1][b-1].team != this.team; a--, b--) {
	        		board[a-1][b-1].listener.setOverride(type, team, i, j);
	        		board[a-1][b-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][b-1]);

	        	}
	        	for(int a=i, b=j; a+1 <8 && b-1 > -1 && board[a+1][b-1].team != this.team; a++, b--) {
	        		board[a+1][b-1].listener.setOverride(type, team, i, j);
	        		board[a+1][b-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][b-1]);

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b+1 < 8 && board[a-1][b+1].team != this.team; a--, b++) {
	        		board[a-1][b+1].listener.setOverride(type, team, i, j);
	        		board[a-1][b+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][b+1]);

	        	}
			}
	        
			else if(type.equals("Rook")) {
				for(int a=i; a+1 < 8 && board[a+1][j].team != this.team; a++) {
	        		
	        		board[a+1][j].listener.setOverride(type, team, i, j);
	        		board[a+1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][j]);

	        	}
	        	for(int a=i; a-1 > -1 && board[a-1][j].team != this.team; a--) {
	        		board[a-1][j].listener.setOverride(type, team, i, j);
	        		board[a-1][j].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][j]);

	        	}
	        	for(int a=j; a-1 > -1 && board[i][a-1].team != this.team; a--) {
	        		board[i][a-1].listener.setOverride(type, team, i, j);
	        		board[i][a-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][a-1]);

	        	}
	        	for(int a=j; a+1 < 8 && board[i][a+1].team != this.team; a++) {
	        		board[i][a+1].listener.setOverride(type, team, i, j);
	        		board[i][a+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i][a+1]);

	        	}
			}
	        	
			else if(type.equals("Bishop")) {
				for(int a=i, b=j; a+1 <8 && b+1 < 8 && board[i+1][j+1].team != this.team; a++, b++) {
	        		board[a+1][b+1].listener.setOverride(type, team, i, j);
	        		board[a+1][b+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][b+1]);

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b-1 > -1 && board[a-1][b-1].team != this.team; a--, b--) {
	        		board[a-1][b-1].listener.setOverride(type, team, i, j);
	        		board[a-1][b-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][b-1]);


	        	}
	        	for(int a=i, b=j; a+1 <8 && b-1 > -1 && board[a+1][b-1].team != this.team; a++, b--) {
	        		board[a+1][b-1].listener.setOverride(type, team, i, j);
	        		board[a+1][b-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a+1][b-1]);


	        	}
	        	for(int a=i, b=j; a-1 > -1 && b+1 < 8 && board[a-1][b+1].team != this.team; a--, b++) {
	        		board[a-1][b+1].listener.setOverride(type, team, i, j);
	        		board[a-1][b+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[a-1][b+1]);

	        	}
			}
	        	
			else if(type.equals("Knight")) {
	            if(i+2 < 8 && j + 1 < 8 && board[i+2][j+1].team != this.team) {
	            	board[i+2][j+1].listener.setOverride(type, team, i, j);
	            	board[i+2][j+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+2][j+1]);


	            }
	            if(i+2 < 8 && j - 1 > -1 && board[i+2][j-1].team != this.team) {
	            	board[i+2][j-1].listener.setOverride(type, team, i, j);
	            	board[i+2][j-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+2][j-1]);

	            }
	            if(i-2 > -1 && j + 1 < 8 && board[i-2][j+1].team != this.team) {
	            	board[i-2][j+1].listener.setOverride(type, team, i, j);
	            	board[i-2][j+1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-2][j+1]);
	        		
	            }
	            if(i-2 > -1 && j -1 > -1 && board[i-2][j-1].team != this.team) {
	            	board[i-2][j-1].listener.setOverride(type, team, i, j);
	            	board[i-2][j-1].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-2][j-1]);

	            }
	            if(i+1 < 8 && j + 2 < 8 && board[i+1][j+2].team != this.team) {
	            	board[i+1][j+2].listener.setOverride(type, team, i, j);
	            	board[i+1][j+2].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+1][j+2]);

	            }
	            if(i-1 >-1 && j + 2 < 8 && board[i-1][j+2].team != this.team) {
	            	board[i-1][j+2].listener.setOverride(type, team, i, j);
	            	board[i-1][j+2].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-1][j+2]);

	            }
	            if(i+1 < 8 && j - 2 > -1 && board[i+1][j-2].team != this.team) {
	            	board[i+1][j-2].listener.setOverride(type, team, i, j);
	            	board[i+1][j-2].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i+1][j-2]);

	            }
	            if(i-1 >-1 && j - 2 > -1 && board[i-1][j-2].team != this.team) {
	            	board[i-1][j-2].listener.setOverride(type, team, i, j);
	            	board[i-1][j-2].setBackground(Color.GREEN);
	        		board[i][j].curPieces.add(board[i-1][j-2]);

	            }
			}
			else if(type.equals("Pawn")) {
				
			}
	        
		}
	} 


	private void updatePiece() {
			try {
				System.out.println("Writting move to PlayerThread");
				board[i][j].out.writeObject(new Data("Move", i, j, callerI, callerJ, callerTeam, callerType ));
				board[i][j].out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			board[callerI][callerJ].updatePiece("Empty", 0);
			board[i][j].updatePiece(callerType, callerTeam);

			for(Piece setColor : board[i][j].curPieces) {
				setColor.setBackground(new Color(160, 120, 60));
				setColor.listener.unsetOverride();
			}

			board[i][j].curPieces.clear();
		
		
		
		
		
	}
}
