import javax.swing.JButton;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Piece extends JButton{
	String type;
	int team, i, j,playerTeam;
	int[] currentTurn;
	Piece[][] board;
	Color pieceColor = new Color(160, 120, 60);
	PieceActionListener listener;
	ObjectOutputStream out;
	ObjectInputStream in;
	HashSet<Piece> curPieces;
	Piece(String type, int i, int j, int team, Piece[][] board, ObjectOutputStream oos, ObjectInputStream ois, int playerTeam, int[] currentTurn, HashSet<Piece> curPieces){
		this.curPieces = curPieces;
		this.type = type;
		this.playerTeam = playerTeam;
		this.currentTurn = currentTurn;
		BufferedImage pieceImage = null;
		if(team == 1) {
			pieceImage = generateChessPieceImage(type, Color.WHITE);
			 ImageIcon imageIcon = new ImageIcon(pieceImage);
			 setIcon(imageIcon);
		}
		else if(team == 2) {
			pieceImage = generateChessPieceImage(type, Color.BLACK);
			ImageIcon imageIcon = new ImageIcon(pieceImage);
			setIcon(imageIcon);
		}
		 
		this.out = oos;
		this.in = ois;
		 this.team = team;
		 this.board = board;
		 this.i = i;
		 this.j = j;
		 
//		 setBackground(new Color(245, 222, 179));
		 setBackground(new Color(160, 120, 60));
	}
	public BufferedImage generateChessPieceImage(String pieceType, Color foregroundColor) {
        int size = 64;  // Size of the image in pixels
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        
        // Draw the chess piece symbol
        g2d.setColor(foregroundColor);
        
        g2d.setFont(new Font("Monospaced", Font.BOLD, 48));
        String symbol = getChessPieceSymbol(pieceType);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int x = (size - fontMetrics.stringWidth(symbol)) / 2;
        int y = (size - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        g2d.drawString(symbol, x, y);

        g2d.dispose();

        return image;
    }

    private static String getChessPieceSymbol(String pieceType) {
        switch (pieceType) {
            case "King":
                return "\u2654";
            case "Queen":
                return "\u2655";
            case "Rook":
                return "\u2656";
            case "Bishop":
                return "\u2657";
            case "Knight":
                return "\u2658";
            case "Pawn":
                return "\u2659";
            default:
                return "";
        }
    }
	public void setActionListener(PieceActionListener listener) {
		this.addActionListener(listener);
		this.listener = listener;
	}
    public void updatePiece(String type, int team) {
    	this.type = type;
    	this.team = team;
    	if(team != 0) currentTurn[0] = (currentTurn[0] == 1) ? 2 : 1;
    	setBackground(new Color(160, 120, 60));
    	if(team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(type, Color.WHITE)));
		}
		else if(team == 2) setIcon( new ImageIcon(generateChessPieceImage(type, Color.BLACK)));
		else setIcon(null);
    	removeActionListener(this.listener);
    	setActionListener(new PieceActionListener(type, i, j, team, board));
    	
    }
}
