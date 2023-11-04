import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class PieceActionListener implements ActionListener {
	String type;
	int team, i, j, callerTeam;
	Piece[][] board;
	String callerType;
	int callerI, callerJ;
	private HashSet<Piece> listOfMoveableMoves;
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
			if(callerType.equals("King") && board[i][j].gameWindow.history.isEmpty() && this.callerI == 7 && Math.abs(callerJ-j) == 2) {
				if(callerTeam == 1 && this.callerJ == 4) {
					if(this.j == 2) {
						updatePiece(0);
						castleUpdatePiece(i, j+1, 7, 0, callerTeam);
						board[i][j].gameWindow.history.add(board[7][0].type);
						board[i][j].gameWindow.history.add(callerType);

					}
					else if(this.j==6) {
						updatePiece(0);
						castleUpdatePiece(i, j-1, 7, 7, callerTeam);
						board[i][j].gameWindow.history.add(board[7][7].type);
						board[i][j].gameWindow.history.add(callerType);

					}
				}
				else if(callerTeam == 2 && this.callerJ == 3) {
					if(this.j==1) {
						updatePiece(0);
						castleUpdatePiece(i, j+1, 7, 0, callerTeam);
						board[i][j].gameWindow.history.add(board[7][0].type);
						board[i][j].gameWindow.history.add(callerType);

					}
					else if(this.j==5) {
						updatePiece(0);
						castleUpdatePiece(i, j-1, 7, 7, callerTeam);
						board[i][j].gameWindow.history.add(board[7][7].type);
						board[i][j].gameWindow.history.add(callerType);

					}
				}
			}
			else if(callerType.equals("Pawn") && i==0) { 
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
						options.dispose();
						updatePiece("Knight");
					}
					
				});
				
				options.setVisible(true);
			} //end if
			else {
				updatePiece(0);
				checkForCheckMate((callerTeam ==1) ? 2:1);
			}
			
			if(board[i][j].type.equals("Rook") || board[i][j].type.equals("King")) board[i][j].gameWindow.history.add(board[i][j].type);
			


		}
		else if(board[i][j].playerTeam != this.team) {
			return;
		}
		else if(this.team != board[i][j].currentTurn[0]) {
			return;
		}
		else {
//			board[i][j].setBackground(hexToColor("#ffff33"));
//			if(!board[i][j].type.equals("King")) {
//				if(checkKingSafety(board[i][j]) == false) return;
//			}
			if(!board[i][j].curPieces.isEmpty() && !board[i][j].curPieces.contains(board[i][j])) {
				for(Piece pieceToMoveTo : board[i][j].curPieces) {
					pieceToMoveTo.listener.unsetOverride();
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
	        	
	        	if( board[i][j].gameWindow.history.isEmpty()) {
	        		HashSet<Piece> legalMovesForCastle = new HashSet<>();

	        		if(board[i][j-1].team == 0 && board[i][j-2].team == 0) {
	        			legalMovesForCastle.add(board[i][j-1]);
	        			performChecks(board[i][j-1], board[i][j].team, legalMovesForCastle);
	        			if(legalMovesForCastle.size() > 0) {
	        				legalMovesForCastle.clear();
	        				legalMovesForCastle.add(board[i][j-2]);
		        			performChecks(board[i][j-2], board[i][j].team, legalMovesForCastle);
	        			}
	        			if(legalMovesForCastle.size()> 0) {
	        				board[i][j-2].listener.setOverride(type, team, i, j);
	    	        		board[i][j].curPieces.add(board[i][j-2]);
	        			}
	        		}
	        		legalMovesForCastle.clear();
	        		if(board[i][j+1].team == 0 && board[i][j+2].team == 0) {
	        			legalMovesForCastle.add(board[i][j+1]);
	        			performChecks(board[i][j+1], board[i][j].team, legalMovesForCastle);
	        			if(legalMovesForCastle.size() > 0) {
	        				legalMovesForCastle.clear();
	        				legalMovesForCastle.add(board[i][j+2]);
		        			performChecks(board[i][j+2], board[i][j].team, legalMovesForCastle);
	        			}
	        			if(legalMovesForCastle.size()> 0) {
	        				board[i][j+2].listener.setOverride(type, team, i, j);
	    	        		board[i][j].curPieces.add(board[i][j+2]);
	        			}
	        		}
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
			}
	        	
			else if(type.equals("Bishop")) {
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
			if(!board[i][j].type.equals("King")) {
				//pass in the oponents team piece that is checking the king
				if(checkKingSafety(board[i][j]) == false) return;
				
			}
	        
		}
	} 


	private HashSet<Piece> checkForCheckMate(int team) {
		Piece king = null;
		int row=0, col=0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].team == team && board[i][j].type.equals("King")) {
					king = board[i][j];
					row=i;
					col=j;
					break;
				}
			}
		}
		//Check which pieces around the king are able to move to. The check only assume pieces with the same team as illegal moves.
		HashSet<Piece> legalMoves = new HashSet<>();
		HashSet<Piece> legalMoves_clone = new HashSet<>();

		legalMoves.add(king);
		legalMoves_clone.add(king);
		
		if(col+1 < 8 && board[row][col+1].team != king.team) {
			legalMoves.add(board[row][col+1]);
			legalMoves_clone.add(board[row][col+1]);

		}
		if(col-1 > -1 && board[row][col-1].team != king.team) {
			legalMoves.add(board[row][col-1]);
			legalMoves_clone.add(board[row][col-1]);

		}
		if(row+1 < 8 && board[row+1][col].team != king.team) {
			legalMoves.add(board[row+1][col]);
			legalMoves_clone.add(board[row+1][col]);

		}
		if(row-1 > -1 && board[row-1][col].team != king.team) {
			legalMoves.add(board[row-1][col]);
			legalMoves_clone.add(board[row-1][col]);

		}
		
		if(row+1 < 8 && col+1 <8 && board[row+1][col+1].team != king.team) {
			legalMoves.add(board[row+1][col+1]);
			legalMoves_clone.add(board[row+1][col+1]);

		}
		if(row+1 < 8 && col-1 > -1 && board[row+1][col-1].team != king.team) {
			legalMoves.add(board[row+1][col-1]);
			legalMoves_clone.add(board[row+1][col-1]);

		}
		if(row-1 > -1 && col-1 > -1 && board[row-1][col-1].team != king.team) {
			legalMoves.add(board[row-1][col-1]);
			legalMoves_clone.add(board[row-1][col-1]);

		}
		if(row-1 > -1 && col+1 < 8 && board[row-1][col+1].team != king.team) {
			legalMoves.add(board[row-1][col+1]);
			legalMoves_clone.add(board[row-1][col+1]);

		}
		Piece checkingPiece = null;
		for(Piece piece : legalMoves_clone) {
			if(piece.type.equals("King")) {
				checkingPiece = performChecks(piece, king.team, legalMoves);
			}
			else if(legalMoves.contains(piece)) performChecks(piece, king.team, legalMoves);
		}
		if(!legalMoves.contains(king)) {
			System.out.println("Legal moves does not contain king so the king in check");
			HashSet<Piece> checkingRoute = new HashSet<>();
			HashSet<Piece> checkingRoute_clone = new HashSet<>();
			checkingRoute.add(checkingPiece);
			checkingRoute_clone.add(checkingPiece);
			
			if(checkingPiece.type.equals("Knight")) return checkingRoute;

			int row_diff = checkingPiece.i-row;
			int col_diff = checkingPiece.j-col;
			int row_diff_abs = Math.abs(row_diff);
			int col_diff_abs = Math.abs(col_diff);
			if(row_diff_abs==col_diff_abs) { //this means the king is diagonal relative to the piece.
				//checking piece to king.
				if(col_diff < 0 && row_diff < 0 ) {
					int startPointX = checkingPiece.i;
					int startPointY = checkingPiece.j;
					for(; startPointX < row && startPointY < col; startPointX++, startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);
					}
				}
				//king to checking piece
				else if(col_diff > 0 && row_diff > 0){
					int startPointX = row+1;
					int startPointY = col+1;
					for(; startPointX < 8 && startPointY < 8; startPointX++, startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);
					}
				}
				//checking piece to king
				else if(col_diff < 0 && row_diff > 0) { 
					int startPointX = checkingPiece.i;
					int startPointY = checkingPiece.j;
					for(; startPointX < row && startPointY < col; startPointX--, startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
				//king to checking piece
				else {
					int startPointX = row-1;
					int startPointY = col+1;
					for(; startPointX > -1 && startPointY < 8; startPointX-- , startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
			}
			else if(col_diff==0) {
				//checking piece to king. The checking piece is inclusive
				if(row_diff < 0) {
					int startPointX = checkingPiece.i;
					int startPointY = col;
					for(; startPointX < row ; startPointX++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
				else {
					//king to checking piece. King is exclusive
					int startPointX = row+1;
					int startPointY = col;
					for(; startPointX < 8 ; startPointX++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
			}
			else if(row_diff==0) {
				if(col_diff < 0) {
					//checking piece to king. Checking piece is inclusive
					int startPointX = row;
					int startPointY = checkingPiece.j;
					for(; startPointY < col; startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
				else {
					//king to checking piece. King is exclusive
					int startPointX = row;
					int startPointY = col+1; 
					for(; startPointY < 8; startPointY++) {
						checkingRoute.add(board[startPointX][startPointY]);
						checkingRoute_clone.add(board[startPointX][startPointY]);

					}
				}
			}
			for(Piece piece : checkingRoute_clone) {
				performChecks(piece, king.team, checkingRoute );
			}
			if(legalMoves.size() == 0 && checkingRoute_clone.size() == checkingRoute.size()) {
				//checkmate
				king.gameWindow.endGame("win");
			}
			else {
				HashSet<Piece> tempSet = new HashSet<>();
				for(Piece piece : checkingRoute_clone) {
					if(!checkingRoute.contains(piece)) tempSet.add(piece);
				}
				return tempSet;
			}
		}
		return null;
	}
	private Piece performChecks(Piece piece, int kingTeam, HashSet<Piece> legalMoves) {
		int col = piece.j;
		int row = piece.i;
		Piece curPieceVert = null;
		//Vertical
		//Top -> king[row+1][col]
		for(int i=0; i<piece.i; i++) {
			
			if(board[i][col].team == 0) continue;
			curPieceVert = board[i][col];
		}
		if(curPieceVert != null) {
			if(curPieceVert.team != kingTeam && (curPieceVert.type.equals("Queen") || curPieceVert.type.equals("Rook"))) {
				legalMoves.remove(piece);
				if(piece.type.equals("King")) return curPieceVert;
				return null;
			
			}
		}
		//Vertical
		//king[row+1][col] -> bottom
		
		for(int i=piece.i+1; i < 8; i++) {
			if(board[i][col].team == 0) continue;
			if(board[i][col].team == kingTeam) break;
			
			if(board[i][col].team != kingTeam ) {
				if(board[i][col].type.equals("Queen") || board[i][col].type.equals("Rook")) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][col];
					return null;
				}
				break;
			}
			
		}		
		
		Piece curPieceHorz = null;

		//Horizontal
		//Left -> Right
		for(int j=0; j<piece.j; j++) {

			if(board[row][j].team == 0) continue;
			curPieceHorz = board[row][j];
				
		}
		if(curPieceHorz != null) {
			if(curPieceHorz.team != kingTeam && (curPieceHorz.type.equals("Queen") || curPieceHorz.type.equals("Rook"))) {
				legalMoves.remove(piece);
				if(piece.type.equals("King")) return curPieceHorz;
				return null;
			
			}
		}
		for(int j=piece.j+1; j < 8; j++) {
			if(board[row][j].team == 0) continue;
			if(board[row][j].team == kingTeam) break;
			
			if(board[row][j].team != kingTeam ) {
				if(board[row][j].type.equals("Queen") || board[row][j].type.equals("Rook")) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[row][j];
					return null;
				}
				break;
			}
			
		}		
				
		//Diagonal check
				
		//Bottom-Right. This direction, pawns do not matter since it can not create a check
		 //do not remove this line
		for(int i=piece.i+1, j=piece.j+1; i<8 && j <8; i++, j++) {
			if(board[i][j].team == 0) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam) { 
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop") ) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][j];

					return null;
				}
				break;
			}
		}
		
				
		//Top-left
		for(int i=piece.i-1, j=piece.j-1; i > -1 && j > -1; i--, j--) {
			if(board[i][j].team == 0) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop") || board[i][j].type.equals("Pawn") ) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][j];

					return null;
				}
				break;
			}
		}
				
				
		//Bottom-Left
		for(int i=row+1, j=col-1; i < 8 && j > -1; i++, j--) {
			if(board[i][j].team == 0) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop")) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][j];

					return null;
				}
				break;
			}
		}
				
		//Top-right	
		for(int i=row-1, j=col+1; i > -1 && j < 8; i--, j++) {
			if(board[i][j].team == 0) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop") || board[i][j].type.equals("Pawn") ) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][j];

					return null;
				}
				break;
			}
		}
		
		//Knight check
		int[][] possibleCombs = { {2,1}, {-2,-1}, {-2,1}, {2,-1}, {1,2}, {-1,-2}, {1,-2}, {-1,2}};
		for(int[] comb : possibleCombs) {
			int i=piece.i+comb[0];
			int j=piece.j+comb[1];
			try {
				if(board[i][j].type.equals("Knight") && board[i][j].team != kingTeam) {
					legalMoves.remove(piece);
					if(piece.type.equals("King")) return board[i][j];

					return null;
				}	
			} catch(Exception e) {
				continue;
			}
			
		}
		return null;
		
	}

	private boolean checkKingSafety(Piece piece) {
		//Pinning logic
		//Returning false == can not move piece
		//Returning true == can move piece
		HashSet<Piece> checkingRoute = checkForCheckMate(piece.team);
		
		if(checkingRoute != null) {
			System.out.println("In list of moveables, Checking route: " + checkingRoute.size());
			listOfMoveableMoves = new HashSet<>(); //this set is only created when the king is in check
			for(Piece tempPiece : piece.curPieces) {
				if(checkingRoute.contains(tempPiece)) {
					listOfMoveableMoves.add(tempPiece);
				}
				else {
					board[tempPiece.i][tempPiece.j].listener.unsetOverride();
				}
			}
			if(listOfMoveableMoves.size() > 0) return true;
			else return false;
		}
		
		int oppositeTeam = (piece.team == 1) ? 2 : 1;
		int row = 0, col = 0;
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].type.equals("King") && board[i][j].team == piece.team) {
					row = i;
					col = j;
					break;
				}
			}
		}
		int row_diff = piece.i-row;
		int col_diff = piece.j-col;
		int row_diff_abs = Math.abs(row_diff);
		int col_diff_abs = Math.abs(col_diff);
		if(row_diff_abs==col_diff_abs) { //this means the king is diagonal relative to the piece.
			//pieces that could kill the checking piece are queen bishop and pawn
			return kingSafetyDiagonalCheck(piece, row, col, col_diff, row_diff, oppositeTeam);
		} //pieces that could kill the checking piece are queen rook
		else if(col_diff==0) return kingSafetyVertCheck(piece, row, col, col_diff, row_diff, oppositeTeam);
		else if(row_diff==0) return kingSafetyHorzCheck(piece, row, col, col_diff, row_diff, oppositeTeam);
		
		return true;
	}
	private boolean kingSafetyHorzCheck(Piece piece, int row, int col, int col_diff, int row_diff, int oppositeTeam) {
		HashSet<Piece> tempSet = new HashSet<>();

		//king is right of piece
		if(col_diff < 0) {
			int startPointX = piece.i;
			int startPointY = 0;
			Piece curOpponentPiece = null;
			Piece curTeamPiece = null;
			int loopCounter = 0;
			int curOpPieceCounter = 0;
			int curTeamPieceCounter = 0;
			for(; startPointY < col; startPointY++, loopCounter++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(board[startPointX][startPointY].team == oppositeTeam) {
					curOpPieceCounter = loopCounter;
					curOpponentPiece = board[startPointX][startPointY];
					
				}
				else {
					curTeamPieceCounter = loopCounter;
					curTeamPiece = board[startPointX][startPointY];
				}
				
			}
			if(curOpponentPiece == null) return true;
			if(curTeamPiece != piece) return true;
			if( curTeamPieceCounter < curOpPieceCounter) return true; 
			if(curOpponentPiece.type.equals("Queen") || curOpponentPiece.type.equals("Rook") ) {
				if(piece.type.equals("Queen") || piece.type.equals("Rook") ) {
					for(Piece temp : piece.curPieces) {
						if(!tempSet.contains(temp)) temp.listener.unsetOverride();
					}

					return true;
				}
				return false;
			}
		}
		else {
			//the king is left of piece
			int startPointX = row;
			int startPointY = col+1;
			boolean requiredPieceFound =false;
			for(; startPointY < 8; startPointY++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(!requiredPieceFound) {
					if(board[startPointX][startPointY] == piece ) {
						requiredPieceFound = true;
					}
					else return true;
					continue;
				}
				if(board[startPointX][startPointY].team == oppositeTeam && (board[startPointX][startPointY].type.equals("Queen") || board[startPointX][startPointY].type.equals("Rook"))) {
					if(piece.type.equals("Queen") || piece.type.equals("Rook") ) {
						for(Piece temp : piece.curPieces) {
							if(!tempSet.contains(temp)) temp.listener.unsetOverride();
						}

						return true;
					}
					return false;
				}
				else return true;
			}
		}
		return true;
	}
	private boolean kingSafetyVertCheck(Piece piece, int row, int col, int col_diff, int row_diff, int oppositeTeam) {
		//the king is under the piece
		HashSet<Piece> tempSet = new HashSet<>();

		if(row_diff < 0) {
			int startPointX = 0;
			int startPointY = piece.j;
			Piece curOpponentPiece = null;
			Piece curTeamPiece = null;
			int loopCounter = 0;
			int curOpPieceCounter = 0;
			int curTeamPieceCounter = 0;
			for(; startPointX < row ; startPointX++, loopCounter++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(board[startPointX][startPointY].team == oppositeTeam) {
					curOpPieceCounter = loopCounter;
					curOpponentPiece = board[startPointX][startPointY];
					
				}
				else {
					curTeamPieceCounter = loopCounter;
					curTeamPiece = board[startPointX][startPointY];
				}
				
			}
			if(curOpponentPiece == null) return true;
			if(curTeamPiece != piece) return true;
			if( curTeamPieceCounter < curOpPieceCounter) return true; 
			if(curOpponentPiece.type.equals("Queen") || curOpponentPiece.type.equals("Rook") ) {
				if(piece.type.equals("Queen") || piece.type.equals("Rook") ) {
					for(Piece temp : piece.curPieces) {
						if(!tempSet.contains(temp)) temp.listener.unsetOverride();
					}

					return true;
				}
				return false;
			}
		}
		else {
			//the king is above the piece
			int startPointX = row+1;
			int startPointY = col;
			boolean requiredPieceFound =false;
			for(; startPointX < 8 ; startPointX++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(!requiredPieceFound) {
					if(board[startPointX][startPointY] == piece ) {
						requiredPieceFound = true;
					}
					else return true;
					continue;
				}
				if(board[startPointX][startPointY].team == oppositeTeam && (board[startPointX][startPointY].type.equals("Queen") || board[startPointX][startPointY].type.equals("Rook"))) {
					if(piece.type.equals("Queen") || piece.type.equals("Rook") ) {
						for(Piece temp : piece.curPieces) {
							if(!tempSet.contains(temp)) temp.listener.unsetOverride();
						}

						return true;
					}
					return false;
				}
				else return true;
			}
		}
		return true;
	}
	private boolean kingSafetyDiagonalCheck(Piece piece, int row, int col, int col_diff, int row_diff, int oppositeTeam) {
		int subtract_to_get_startpoint = Math.min(piece.i, piece.j);
		HashSet<Piece> tempSet = new HashSet<>();
		//King is bottom right relative to the piece
		//loop starts from edge of map
		if(col_diff < 0 && row_diff < 0 ) {
			int startPointX = piece.i-subtract_to_get_startpoint;
			int startPointY = piece.j-subtract_to_get_startpoint;
			Piece curOpponentPiece = null;
			Piece curTeamPiece = null;
			int loopCounter = 0;
			int curOpPieceCounter = 0;
			int curTeamPieceCounter = 0;
			for(; startPointX < row && startPointY < col; startPointX++, startPointY++, loopCounter++) {
				tempSet.add(board[startPointX][startPointY]);
				if(board[startPointX][startPointY].team == 0) continue;

				if(board[startPointX][startPointY].team == oppositeTeam) {
					curOpPieceCounter = loopCounter;
					curOpponentPiece = board[startPointX][startPointY];
					
				}
				else {
					curTeamPieceCounter = loopCounter;
					curTeamPiece = board[startPointX][startPointY];
				}
				
			}
			if(curOpponentPiece == null) return true;
			if(curTeamPiece != piece) return true;
			if( curTeamPieceCounter < curOpPieceCounter) return true; 
			if(curOpponentPiece.type.equals("Queen") || curOpponentPiece.type.equals("Bishop") ) {
				if(piece.type.equals("Queen") || piece.type.equals("Bishop") || piece.type.equals("Pawn")) {
					for(Piece temp : piece.curPieces) {
						if(!tempSet.contains(temp)) temp.listener.unsetOverride();
					}

					return true;
				}
				return false;
			}
		}
		//King piece is top-left relative to the piece
		//loop starts from king
		else if(col_diff > 0 && row_diff > 0){
			int startPointX = row+1;
			int startPointY = col+1;
			boolean requiredPieceFound =false;
			for(; startPointX < 8 && startPointY < 8; startPointX++, startPointY++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(requiredPieceFound == false) {
					if(board[startPointX][startPointY] == piece ) {
						tempSet.remove(piece);
						requiredPieceFound = true;
					}
					else return true;
					continue;
				}
				if(board[startPointX][startPointY].team == oppositeTeam && (board[startPointX][startPointY].type.equals("Queen") || board[startPointX][startPointY].type.equals("Bishop"))) {
					if(piece.type.equals("Queen") || piece.type.equals("Bishop") ) {
						for(Piece temp : piece.curPieces) {
							if(!tempSet.contains(temp)) temp.listener.unsetOverride();
						}

						return true;
					}
					return false;
				}
				else return true;
			}
		}
		//King piece is top-right relative to the piece
		//loop starts from edge of map. Loop ends at the king
		else if(col_diff < 0 && row_diff > 0) { 
			int startPointX = piece.i-subtract_to_get_startpoint;
			int startPointY = piece.j+subtract_to_get_startpoint;
			Piece curOpponentPiece = null;
			Piece curTeamPiece = null;
			int loopCounter = 0;
			int curOpPieceCounter = 0;
			int curTeamPieceCounter = 0;
			for(; startPointX < row && startPointY < col; startPointX--, startPointY++, loopCounter++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(board[startPointX][startPointY].team == oppositeTeam) {
					curOpPieceCounter = loopCounter;
					curOpponentPiece = board[startPointX][startPointY];
					
				}
				else {
					curTeamPieceCounter = loopCounter;
					curTeamPiece = board[startPointX][startPointY];
				}
				
			}
			if(curOpponentPiece == null) return true;
			if(curTeamPiece != piece) return true;
			if( curTeamPieceCounter < curOpPieceCounter) return true; 
			if(curOpponentPiece.type.equals("Queen") || curOpponentPiece.type.equals("Bishop") ) {
				if(piece.type.equals("Queen") || piece.type.equals("Bishop") ) {
					for(Piece temp : piece.curPieces) {
						if(!tempSet.contains(temp)) temp.listener.unsetOverride();
					}

					return true;
				}
				return false;
			}
		}
		else {
			//King piece is bottom-left relative to the piece
			//loop starts from king
			System.out.println("King is bottom-left");
			int startPointX = row-1;
			int startPointY = col+1;
			boolean requiredPieceFound =false;
			for(; startPointX > -1 && startPointY < 8; startPointX-- , startPointY++) {
				tempSet.add(board[startPointX][startPointY]);

				if(board[startPointX][startPointY].team == 0) continue;

				if(requiredPieceFound == false) {
					if(board[startPointX][startPointY] == piece ) {
						tempSet.remove(piece);
						requiredPieceFound = true;
					}
					else return true;
					continue;
				}
				
				if(board[startPointX][startPointY].team == oppositeTeam && (board[startPointX][startPointY].type.equals("Queen") || board[startPointX][startPointY].type.equals("Bishop"))) {
					if(piece.type.equals("Queen") || piece.type.equals("Bishop") || piece.type.equals("Pawn")) {
						for(Piece temp : piece.curPieces) {
							if(!tempSet.contains(temp)) temp.listener.unsetOverride();
						}

						return true;
					}
					return false;
				}
				else return true;
			}
		}
		
		return true;
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
	private void castleUpdatePiece(int rookI, int rookJ, int emptyI, int emptyJ, int team) {
		try {
			board[i][j].out.writeObject(new Data("Castle_update", rookI, rookJ, emptyI, emptyJ, team , "Rook", -1 ));
			
			board[i][j].out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		board[rookI][rookJ].castleUpdatePiece(callerTeam);
		board[emptyI][emptyJ].updatePiece("Empty", 0);
	}
}