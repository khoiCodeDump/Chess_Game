package Sandbox;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class Board extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Piece[][] pieces;
	static PieceUI[][] board;
	GameInfoPanel gameInfo;
	static int playerTeam;
	JPanel gamePanel;
	static Piece emptyPiece;
	static Piece King;
	static List<BoardPosition> positionHistory;

	public Board(int playerTeam, GameInfoPanel gameInfoWindow) {
		Board.playerTeam = playerTeam;
		setLayout(new BorderLayout(0, 0));
		gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(8, 8, 0, 0));
		add(gamePanel);
		board = new PieceUI[8][8];
		pieces = new Piece[8][8];
		int oppositeTeam = (playerTeam == 1) ? 2 : 1;
		emptyPiece = new Piece();
	
		for(int i=0; i<8; i++) {
			if(i==0) {
				Piece curPiece = new Piece("Rook", i, 0, oppositeTeam);
				pieces[i][0] = curPiece;
				board[i][0] = new PieceUI(curPiece, i, 0);
				
				curPiece = new Piece("Knight", i, 1, oppositeTeam);
				pieces[i][1] = curPiece;
				board[i][1] = new PieceUI(curPiece, i, 1);
				
				curPiece = new Piece("Bishop", i, 2, oppositeTeam);
				pieces[i][2] = curPiece;
				board[i][2] = new PieceUI(curPiece, i, 2);
				
				if(playerTeam ==1) {
					curPiece = new Piece("Queen", i, 3, oppositeTeam);
					pieces[i][3] = curPiece;
					board[i][3] = new PieceUI(curPiece, i, 3);
					
					curPiece = new Piece("King", i, 4, oppositeTeam);
					pieces[i][4] = curPiece;
					board[i][4] = new PieceUI(curPiece, i, 4);
				}
				else {
					curPiece = new Piece("King", i, 3, oppositeTeam);
					pieces[i][3] = curPiece;
					board[i][3] = new PieceUI(curPiece, i, 3);
					
					curPiece = new Piece("Queen", i, 4, oppositeTeam);
					pieces[i][4] = curPiece;
					board[i][4] = new PieceUI(curPiece, i, 4);
				}
				
				curPiece = new Piece("Bishop", i, 5, oppositeTeam);
				pieces[i][5] = curPiece;
				board[i][5] = new PieceUI(curPiece, i, 5);
				
				curPiece = new Piece("Knight", i, 6, oppositeTeam);
				pieces[i][6] = curPiece;
				board[i][6] = new PieceUI(curPiece, i, 6);
				
				curPiece = new Piece("Rook", i, 7, oppositeTeam);
				pieces[i][7] = curPiece;
				board[i][7] = new PieceUI(curPiece, i, 7);
				gamePanel.add(board[i][0]);
				gamePanel.add(board[i][1]);
				gamePanel.add(board[i][2]);
				gamePanel.add(board[i][3]);
				gamePanel.add(board[i][4]);
				gamePanel.add(board[i][5]);
				gamePanel.add(board[i][6]);
				gamePanel.add(board[i][7]);
			}
			else if(i==1) {
				Piece curPiece;
				for(int j=0; j<8; j++) {
					curPiece = new Piece("Pawn", i, j, oppositeTeam) ;
					pieces[i][j] = curPiece;
					board[i][j] = new PieceUI(curPiece, i , j);
					gamePanel.add(board[i][j]);
				}
			}
			else if(i==board.length-1) {
				Piece curPiece = new Piece("Rook", i, 0, playerTeam);
				pieces[i][0] = curPiece;
				board[i][0] = new PieceUI(curPiece, i, 0);
				
				curPiece = new Piece("Knight", i, 1, playerTeam);
				pieces[i][1] = curPiece;
				board[i][1] = new PieceUI(curPiece, i, 1);
				
				curPiece = new Piece("Bishop", i, 2, playerTeam);
				pieces[i][2] = curPiece;
				board[i][2] = new PieceUI(curPiece, i, 2);
				
				if(playerTeam ==1) {
					curPiece = new Piece("Queen", i, 3,playerTeam);
					pieces[i][3] = curPiece;
					board[i][3] = new PieceUI(curPiece, i, 3);
					
					curPiece = new Piece("King", i, 4, playerTeam);
					pieces[i][4] = curPiece;
					board[i][4] = new PieceUI(curPiece, i, 4);
					King = curPiece;
				}
				else {
					curPiece = new Piece("King", i, 3, playerTeam);
					pieces[i][3] = curPiece;
					board[i][3] = new PieceUI(curPiece, i, 3);
					King = curPiece;
					
					curPiece = new Piece("Queen", i, 4, playerTeam);
					pieces[i][4] = curPiece;
					board[i][4] = new PieceUI(curPiece, i, 4);
				}
				
				curPiece = new Piece("Bishop", i, 5,playerTeam);
				pieces[i][5] = curPiece;
				board[i][5] = new PieceUI(curPiece, i, 5);
				
				curPiece = new Piece("Knight", i, 6, playerTeam);
				pieces[i][6] = curPiece;
				board[i][6] = new PieceUI(curPiece, i, 6);
				
				curPiece = new Piece("Rook", i, 7, playerTeam);
				pieces[i][7] = curPiece;
				board[i][7] = new PieceUI(curPiece, i, 7);
				gamePanel.add(board[i][0]);
				gamePanel.add(board[i][1]);
				gamePanel.add(board[i][2]);
				gamePanel.add(board[i][3]);
				gamePanel.add(board[i][4]);
				gamePanel.add(board[i][5]);
				gamePanel.add(board[i][6]);
				gamePanel.add(board[i][7]);
			}
			else if(i==board.length-2) {
				Piece curPiece;
				for(int j=0; j<8; j++) {
					curPiece = new Piece("Pawn", i, j, playerTeam) ;
					pieces[i][j] = curPiece;
					board[i][j] = new PieceUI(curPiece, i, j);
					gamePanel.add(board[i][j]);
				}
			}
			else {
				
				for(int j=0; j<8; j++) {
					pieces[i][j] = emptyPiece; //empty pieces
					PieceUI pieceUI = new PieceUI(emptyPiece, i, j);

					board[i][j] = pieceUI;
					
					gamePanel.add(board[i][j]);
				}
			}	
		}
		ChessEngine.board = pieces;
		gameInfoWindow.setBoard(board);
		positionHistory = new ArrayList<>();
		positionHistory.add(new BoardPosition(pieces));
//		 #779952
//	     #edeed1
		Color themePiece = hexToColor("#779952");
		Color themePieceWhite = hexToColor("#edeed1");
		Color curColor = themePieceWhite;
		for(int a=0; a<8; a++) {
			Color curColumnColor = curColor;
			for(int b=0; b<8; b++) {
				board[a][b].setBackground(curColumnColor);
				board[a][b].setBorder(new EmptyBorder(0, 0, 0, 0));
				board[a][b].setFocusable(false);
				if(curColumnColor == themePiece) curColumnColor = themePieceWhite;
				else curColumnColor = themePiece;
			}
			if(curColor == themePiece) curColor = themePieceWhite;
			else curColor = themePiece;
		}
		
		ChessEngine.Initialize();

		
	}
	public static Color hexToColor(String hex) {
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
	
	public static boolean isThreefoldRepetition() {
        if (positionHistory.size() < 6) return false; // Need at least 6 moves for 3 repetitions
        
        BoardPosition currentPosition = positionHistory.get(positionHistory.size() - 1);
        int repetitions = 0;
        
        for (BoardPosition position : positionHistory) {
            if (position.equals(currentPosition)) {
                repetitions++;
                if (repetitions >= 3) return true;
            }
        }
        return false;
    }
    
    public static void addPosition(Piece[][] board) {
        positionHistory.add(new BoardPosition(board));
    }
}