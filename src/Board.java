import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.GridLayout;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Board extends JPanel {
	Piece[][] pieces;
	GameInfoPanel gameInfo;
	int playerTeam;
	int[] turn;
	public Board(int playerTeam, ObjectInputStream in, ObjectOutputStream out) {
		HashSet<Piece> teamPiece = new HashSet<>();
		HashSet<Piece> curPieces = new HashSet<>();
		this.playerTeam = playerTeam;
		int oppositeTeam = (playerTeam == 1) ? 2 : 1;
		setLayout(new BorderLayout(0, 0));
		setBounds(100, 100, 831, 384);
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(8, 8, 0, 0));
		add(gamePanel);
		pieces = new Piece[8][8];
		turn = new int[] {1};
		
		for(int i=0; i<8; i++) {
			if(i==0) {
				pieces[i][0] = new Piece("Rook", i, 0, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][0].setActionListener(new PieceActionListener("Rook", i, 0, oppositeTeam, pieces));
				
				pieces[i][1] = new Piece("Knight", i, 1, oppositeTeam, pieces,out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][1].setActionListener(new PieceActionListener("Knight", i, 1, oppositeTeam, pieces));
				
				pieces[i][2] = new Piece("Bishop", i, 2, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][2].setActionListener(new PieceActionListener("Bishop", i, 2, oppositeTeam, pieces));
				
				pieces[i][3] = new Piece("Queen", i, 3, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][3].setActionListener(new PieceActionListener("Queen", i, 3, oppositeTeam, pieces));
				
				pieces[i][4] = new Piece("King", i, 4, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][4].setActionListener(new PieceActionListener("King", i, 4, oppositeTeam, pieces));
				
				pieces[i][5] = new Piece("Bishop", i, 5, oppositeTeam, pieces,out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][5].setActionListener(new PieceActionListener("Bishop", i, 5, oppositeTeam, pieces));
				
				pieces[i][6] = new Piece("Knight", i, 6, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][6].setActionListener(new PieceActionListener("Knight", i, 6, oppositeTeam, pieces));
				
				pieces[i][7] = new Piece("Rook", i, 7, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
				pieces[i][7].setActionListener(new PieceActionListener("Rook", i, 7, oppositeTeam, pieces));
				gamePanel.add(pieces[i][0]);
				gamePanel.add(pieces[i][1]);
				gamePanel.add(pieces[i][2]);
				gamePanel.add(pieces[i][3]);
				gamePanel.add(pieces[i][4]);
				gamePanel.add(pieces[i][5]);
				gamePanel.add(pieces[i][6]);
				gamePanel.add(pieces[i][7]);
			}
			else if(i==1) {
				for(int j=0; j<8; j++) {
					pieces[i][j] = new Piece("Pawn", i, j, oppositeTeam, pieces, out, in, oppositeTeam, turn, curPieces) ;
					pieces[i][j].setActionListener(new PieceActionListener("Pawn", i, j, oppositeTeam, pieces));
					gamePanel.add(pieces[i][j]);
				}
			}
			else if(i==pieces.length-1) {
				pieces[i][0] = new Piece("Rook", i, 0, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][0].setActionListener(new PieceActionListener("Rook", i, 0, playerTeam, pieces));
				
				pieces[i][1] = new Piece("Knight", i, 1, playerTeam, pieces,out, in, playerTeam, turn, curPieces) ;
				pieces[i][1].setActionListener(new PieceActionListener("Knight", i, 1, playerTeam, pieces));
				
				pieces[i][2] = new Piece("Bishop", i, 2, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][2].setActionListener(new PieceActionListener("Bishop", i, 2, playerTeam, pieces));
				
				pieces[i][3] = new Piece("Queen", i, 3, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][3].setActionListener(new PieceActionListener("Queen", i, 3, playerTeam, pieces));
				
				pieces[i][4] = new Piece("King", i, 4, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][4].setActionListener(new PieceActionListener("King", i, 4, playerTeam, pieces));
				
				pieces[i][5] = new Piece("Bishop", i, 5, playerTeam, pieces,out, in, playerTeam, turn, curPieces) ;
				pieces[i][5].setActionListener(new PieceActionListener("Bishop", i, 5, playerTeam, pieces));
				
				pieces[i][6] = new Piece("Knight", i, 6, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][6].setActionListener(new PieceActionListener("Knight", i, 6, playerTeam, pieces));
				
				pieces[i][7] = new Piece("Rook", i, 7, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
				pieces[i][7].setActionListener(new PieceActionListener("Rook", i, 7, playerTeam, pieces));
				
				teamPiece.add(pieces[i][0]);
				teamPiece.add(pieces[i][1]);
				teamPiece.add(pieces[i][2]);
				teamPiece.add(pieces[i][3]);
				teamPiece.add( pieces[i][4]);
				teamPiece.add( pieces[i][5]);
				teamPiece.add( pieces[i][6]);
				teamPiece.add( pieces[i][7]);
				gamePanel.add(pieces[i][0]);
				gamePanel.add(pieces[i][1]);
				gamePanel.add(pieces[i][2]);
				gamePanel.add(pieces[i][3]);
				gamePanel.add(pieces[i][4]);
				gamePanel.add(pieces[i][5]);
				gamePanel.add(pieces[i][6]);
				gamePanel.add(pieces[i][7]);
			}
			else if(i==pieces.length-2) {
				for(int j=0; j<8; j++) {
					pieces[i][j] = new Piece("Pawn", i, j, playerTeam, pieces, out, in, playerTeam, turn, curPieces) ;
					pieces[i][j].setActionListener(new PieceActionListener("Pawn", i, j, playerTeam, pieces));
					teamPiece.add(pieces[i][j]);
					gamePanel.add(pieces[i][j]);
				}
			}
			else {
				for(int j=0; j<8; j++) {
					pieces[i][j] = new Piece("Empty", i, j, 0, pieces, out, in, playerTeam, turn, curPieces) ;
					pieces[i][j].setActionListener(new PieceActionListener("Empty", i, j, 0, pieces));
					gamePanel.add(pieces[i][j]);
				}
			}	
		}
//		 #779952
//	     #edeed1
		Color themePiece = hexToColor("#779952");
		Color themePieceWhite = hexToColor("#edeed1");
		Color curColor = themePieceWhite;
		for(int a=0; a<8; a++) {
			Color curColumnColor = curColor;
			for(int b=0; b<8; b++) {
				pieces[a][b].setBackground(curColumnColor);
				pieces[a][b].setBorder(new EmptyBorder(0, 0, 0, 0));
				if(curColumnColor == themePiece) curColumnColor = themePieceWhite;
				else curColumnColor = themePiece;
			}
			if(curColor == themePiece) curColor = themePieceWhite;
			else curColor = themePiece;
		}
		gameInfo = new GameInfoPanel(pieces, playerTeam, teamPiece, 1);
		add(gameInfo, BorderLayout.EAST);
		
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
