 import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

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
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(callerType != null) {
			if(callerType.equals("Pawn") && i==0) {
				updatePiece(1);
				System.out.println("In dialog creation");
				JDialog options = new JDialog();
				options.setLocationRelativeTo(board[i][j].gamePanel);
				options.setLayout(new GridLayout(2, 2, 0, 0));
				options.setSize(220, 200);
				options.setResizable(false);
				options.setUndecorated(true);
				JButton queen = new JButton();
				if(callerTeam ==1) {
					queen.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Queen", Color.WHITE)));
				}
				else {
					queen.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Queen", Color.BLACK)));
				}
				options.add(queen);
				queen.addActionListener( new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						options.dispose();
						updatePiece("Queen");
					}
					
				});
			
				
				JButton rook = new JButton();
				if(callerTeam ==1) {
					rook.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Rook", Color.WHITE)));
				}
				else {
					rook.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Rook", Color.BLACK)));
				}
				options.add(rook);
				rook.addActionListener( new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						options.dispose();
						updatePiece("Rook");
					}
					
				});
			
			
				JButton bishop = new JButton();
				if(callerTeam ==1) {
					bishop.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Bishop", Color.WHITE)));
				}
				else {
					bishop.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Bishop", Color.BLACK)));
				}
				
				options.add(bishop);
				bishop.addActionListener( new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						options.dispose();
						updatePiece("Bishop");
					}
					
				});

			
			
				JButton knight = new JButton();
				if(callerTeam ==1) {
					knight.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Knight", Color.WHITE)));
				}
				else {
					knight.setIcon(new ImageIcon(board[i][j].generateChessPieceImage("Knight", Color.BLACK)));
				}
				options.add(knight);
				knight.addActionListener( new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						options.dispose();
						updatePiece("Knight");
					}
					
				});
				
				options.setVisible(true);
			} //end if
			else {
				updatePiece(0);

			}

		}
		else if(board[i][j].playerTeam != this.team) {
			return;
		}
		else if(this.team != board[i][j].currentTurn[0]) {
			return;
		}
		else {
//			board[i][j].setBackground(hexToColor("#ffff33"));
			
			if(!board[i][j].curPieces.isEmpty() && !board[i][j].curPieces.contains(board[i][j])) {
				for(Piece setColor : board[i][j].curPieces) {
					setColor.listener.unsetOverride();
				}
				board[i][j].curPieces.clear();
				
			}
			if(type.equals("King")) {
				if(i+1 < 8 && board[i+1][j].team != this.team) {
	        		board[i+1][j].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+1][j]);
	        		
	        	}
	        	if(i-1 > -1 && board[i-1][j].team != this.team) {
	        		board[i-1][j].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-1][j]);

	        	}
	        	if(j-1 > -1 && board[i][j-1].team != this.team) {
	        		board[i][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i][j-1]);

	        	}
	        	if(j+1 < 8 && board[i][j+1].team != this.team) {
	        		board[i][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i][j+1]);

	        	}
	        	
	        	if(i+1 <8 && j+1 < 8 && board[i+1][j+1].team != this.team) {
	        		board[i+1][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+1][j+1]);

	        	}
	        	if(i-1 > -1 && j-1 > -1 && board[i-1][j-1].team != this.team) {
	        		board[i-1][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-1][j-1]);

	        	}
	        	if(i+1 <8 && j-1 > -1 && board[i+1][j-1].team != this.team) {
	        		board[i+1][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+1][j-1]);

	        	}
	        	if(i-1 > -1 && j+1 < 8 && board[i-1][j+1].team != this.team) {
	        		board[i-1][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-1][j+1]);

	        	}
			}
			else if(type.equals("Queen")) {
				for(int a=i; a+1 < 8; a++) {
	        		if(board[a+1][j].team==0) {
	        			board[a+1][j].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][j]);
	        		}
	        		else if(board[a+1][j].team != this.team) {
	        			board[a+1][j].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][j]);
		        		break;
	        		}
	        		else if(board[a+1][j].team == this.team) {
	        			break;
	        		} 
	        		
	        	}
	        	for(int a=i; a-1 > -1 ; a--) {
	        		if(board[a-1][j].team == 0) {
	        			board[a-1][j].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][j]);
	        		}
	        		else if(board[a-1][j].team != this.team) {
	        			board[a-1][j].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][j]);
		        		break;
	        		}
	        		else if(board[a-1][j].team == this.team) {
	        			break;
	        		}        		
	        	}
	        	for(int a=j; a-1 > -1; a--) {
	        		
	        		if(board[i][a-1].team == 0) {
	        			board[i][a-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[i][a-1]);
	        		}
	        		else if(board[i][a-1].team != this.team) {
	        			board[i][a-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[i][a-1]);
		        		break;
	        		}
	        		else if(board[i][a-1].team == this.team) {
	        			break;
	        		}      

	        	}
	        	for(int a=j; a+1 < 8; a++) {
	        		
	        		if(board[i][a+1].team == 0) {
	        			board[i][a+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[i][a+1]);
	        		}
	        		else if(board[i][a+1].team != this.team) {
	        			board[i][a+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[i][a+1]);
		        		break;
	        		}
	        		else if(board[i][a+1].team == this.team) {
	        			break;
	        		}      

	        	}	        	
	        	for(int a=i, b=j; a+1 <8 && b+1 < 8; a++, b++) {
	        		
	        		if(board[a+1][b+1].team == 0) {
	        			board[a+1][b+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][b+1]);
	        		}
	        		else if(board[a+1][b+1].team != this.team) {
	        			board[a+1][b+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][b+1]);
		        		break;
	        		}
	        		else if(board[a+1][b+1].team == this.team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b-1 > -1; a--, b--) {
	        		
	        		if(board[a-1][b-1].team == 0) {
	        			board[a-1][b-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][b-1]);
	        		}
	        		else if(board[a-1][b-1].team != this.team) {
	        			board[a-1][b-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][b-1]);
		        		break;
	        		}
	        		else if(board[a-1][b-1].team == this.team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i, b=j; a+1 <8 && b-1 > -1; a++, b--) {
	        		
	        		if(board[a+1][b-1].team == 0) {
	        			board[a+1][b-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][b-1]);
	        		}
	        		else if(board[a+1][b-1].team != this.team) {
	        			board[a+1][b-1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a+1][b-1]);
		        		break;
	        		}
	        		else if(board[a+1][b-1].team == this.team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b+1 < 8; a--, b++) {
	        		
	        		if(board[a-1][b+1].team == 0) {
	        			board[a-1][b+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][b+1]);
	        		}
	        		else if(board[a-1][b+1].team != this.team) {
	        			board[a-1][b+1].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[a-1][b+1]);
		        		break;
	        		}
	        		else if(board[a-1][b+1].team == this.team) {
	        			break;
	        		}      

	        	}
			}
	        
			else if(type.equals("Rook")) {
				for(int a=i; a+1 < 8 && board[a+1][j].team != this.team; a++) {
	        		
	        		board[a+1][j].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a+1][j]);
	        		

	        	}
	        	for(int a=i; a-1 > -1 && board[a-1][j].team != this.team; a--) {
	        		board[a-1][j].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a-1][j]);

	        	}
	        	for(int a=j; a-1 > -1 && board[i][a-1].team != this.team; a--) {
	        		board[i][a-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i][a-1]);

	        	}
	        	for(int a=j; a+1 < 8 && board[i][a+1].team != this.team; a++) {
	        		board[i][a+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i][a+1]);

	        	}
			}
	        	
			else if(type.equals("Bishop")) {
				for(int a=i, b=j; a+1 <8 && b+1 < 8 && board[i+1][j+1].team != this.team; a++, b++) {
	        		board[a+1][b+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a+1][b+1]);

	        	}
	        	for(int a=i, b=j; a-1 > -1 && b-1 > -1 && board[a-1][b-1].team != this.team; a--, b--) {
	        		board[a-1][b-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a-1][b-1]);


	        	}
	        	for(int a=i, b=j; a+1 <8 && b-1 > -1 && board[a+1][b-1].team != this.team; a++, b--) {
	        		board[a+1][b-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a+1][b-1]);


	        	}
	        	for(int a=i, b=j; a-1 > -1 && b+1 < 8 && board[a-1][b+1].team != this.team; a--, b++) {
	        		board[a-1][b+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[a-1][b+1]);

	        	}
			}
	        	
			else if(type.equals("Knight")) {
				System.out.println("In Knight");
	            if(i+2 < 8 && j + 1 < 8 && board[i+2][j+1].team != this.team) {
	            	board[i+2][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+2][j+1]);


	            }
	            if(i+2 < 8 && j - 1 > -1 && board[i+2][j-1].team != this.team) {
	            	board[i+2][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+2][j-1]);

	            }
	            if(i-2 > -1 && j + 1 < 8 && board[i-2][j+1].team != this.team) {
	            	board[i-2][j+1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-2][j+1]);
	        		
	            }
	            if(i-2 > -1 && j -1 > -1 && board[i-2][j-1].team != this.team) {
	            	board[i-2][j-1].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-2][j-1]);

	            }
	            if(i+1 < 8 && j + 2 < 8 && board[i+1][j+2].team != this.team) {
	            	board[i+1][j+2].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+1][j+2]);

	            }
	            if(i-1 >-1 && j + 2 < 8 && board[i-1][j+2].team != this.team) {
	            	board[i-1][j+2].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-1][j+2]);

	            }
	            if(i+1 < 8 && j - 2 > -1 && board[i+1][j-2].team != this.team) {
	            	board[i+1][j-2].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i+1][j-2]);

	            }
	            if(i-1 >-1 && j - 2 > -1 && board[i-1][j-2].team != this.team) {
	            	board[i-1][j-2].listener.setOverride(type, team, i, j);
	        		board[i][j].curPieces.add(board[i-1][j-2]);

	            }
			}
			else if(type.equals("Pawn")) {
				if(i==6) {
					if(board[i-2][j].team == 0) {
						board[i-2][j].listener.setOverride(type, team, i, j);
		        		board[i][j].curPieces.add(board[i-2][j]);
	        		}
				}
				if(board[i-1][j].team == 0) {
					board[i-1][j].listener.setOverride(type, team, i, j);
		        	board[i][j].curPieces.add(board[i-1][j]);
				}
				if(j+1 < 8 && board[i-1][j+1].team != 0 && board[i-1][j+1].team != this.team) {
					board[i-1][j+1].listener.setOverride(type, team, i, j);
		        	board[i][j].curPieces.add(board[i-1][j+1]);
				}
				if(j-1 > -1 && board[i-1][j-1].team != 0 && board[i-1][j-1].team != this.team) {
					board[i-1][j-1].listener.setOverride(type, team, i, j);
		        	board[i][j].curPieces.add(board[i-1][j-1]);
				}
					
				
			}
	        
		}
	} 


	private void updatePiece(int update) {
			try {
				board[i][j].out.writeObject(new Data("Move", i, j, callerI, callerJ, callerTeam, callerType, update ));
				
				board[i][j].out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(update==0) {
				if(callerTeam == 1) {
					board[i][j].gameWindow.whiteTimer.stop();
					board[i][j].gameWindow.blackTimer.start();
					board[i][j].gameWindow.updateCurTurn();
				}
				else {
					board[i][j].gameWindow.whiteTimer.start();
					board[i][j].gameWindow.blackTimer.stop();
					board[i][j].gameWindow.updateCurTurn();
				}
			}
			
			
			board[callerI][callerJ].updatePiece("Empty", 0);
			board[i][j].updatePiece(callerType, callerTeam);

			for(Piece setColor : board[i][j].curPieces) {
				setColor.listener.unsetOverride();
			}

			board[i][j].curPieces.clear();
	}
	private void updatePiece(String type) {
		try {
			board[i][j].out.writeObject(new Data("Pawn_Promo", i, j, -1, -1, team, type, -1 ));
			
			board[i][j].out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(team == 1) {
			board[i][j].gameWindow.whiteTimer.stop();
			board[i][j].gameWindow.blackTimer.start();
			board[i][j].gameWindow.updateCurTurn();
		}
		else {
			board[i][j].gameWindow.whiteTimer.start();
			board[i][j].gameWindow.blackTimer.stop();
			board[i][j].gameWindow.updateCurTurn();


		}		
		board[i][j].updatePiece(type);
	}
}