import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
//import org.apache.commons.io.output.ByteArrayOutputStream;

public class Piece extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String type;
	int team, i, j,playerTeam;
	int[] currentTurn;
	Piece[][] board;
	GameInfoPanel gameWindow;
	PieceActionListener listener;
	ObjectOutputStream out;
	ObjectInputStream in;
	HashSet<Piece> curPieces;
	JPanel gamePanel;
	Piece(String type, int i, int j, int team, Piece[][] board, ObjectOutputStream oos, ObjectInputStream ois, int playerTeam, int[] currentTurn, HashSet<Piece> curPieces, GameInfoPanel gameInfoWindow, JPanel gamePanel){
		this.gamePanel = gamePanel;
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
		 this.gameWindow = gameInfoWindow;
		this.out = oos;
		this.in = ois;
		 this.team = team;
		 this.board = board;
		 this.i = i;
		 this.j = j;
		 
	}
	 public BufferedImage transcodeSVGToBufferedImage(File file, int width, int height) {
	        // Create a PNG transcoder.
	        Transcoder t = new PNGTranscoder();

	        // Set the transcoding hints.
	        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
	        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);
	        try (FileInputStream inputStream = new FileInputStream(file)) {
	            // Create the transcoder input.
	            TranscoderInput input = new TranscoderInput(inputStream);

	            // Create the transcoder output.
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            TranscoderOutput output = new TranscoderOutput(outputStream);

	            // Save the image.
	            t.transcode(input, output);

	            // Flush and close the stream.
	            outputStream.flush();
	            outputStream.close();

	            // Convert the byte stream into an image.
	            byte[] imgData = outputStream.toByteArray();
	            return ImageIO.read(new ByteArrayInputStream(imgData));

	        } catch (IOException | TranscoderException e) {
	            
	        }
	        return null;
	    }
	public BufferedImage generateChessPieceImage(String pieceType, Color color) {

		switch (pieceType) {
        case "King":
        	return (color == Color.WHITE) ? transcodeSVGToBufferedImage(new File("images/wK.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bK.svg"), 64, 64);
        case "Queen":
        	return (color == Color.WHITE) ? transcodeSVGToBufferedImage(new File("images/wQ.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bQ.svg"), 64, 64);

        case "Rook":
        	return (color == Color.WHITE) ? transcodeSVGToBufferedImage(new File("images/wR.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bR.svg"), 64, 64);

        case "Bishop":
        	return (color == Color.WHITE) ? transcodeSVGToBufferedImage(new File("images/wB.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bB.svg"), 64, 64);

        case "Knight":
        	return (color == Color.WHITE) ? transcodeSVGToBufferedImage(new File("images/wN.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bN.svg"), 64, 64);

        case "Pawn":
        	return (color == Color.white) ? transcodeSVGToBufferedImage(new File("images/wP.svg"), 64, 64) : transcodeSVGToBufferedImage(new File("images/bP.svg"), 64, 64);

        default:
            return null;
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
    	if(team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(type, Color.WHITE)));
		}
		else if(team == 2) setIcon( new ImageIcon(generateChessPieceImage(type, Color.BLACK)));
		else setIcon(null);

    	removeActionListener(this.listener);
    	setActionListener(new PieceActionListener(type, i, j, team, board));
    	
    }
    public void updatePiece(String type) {
    	this.type = type;
    	
    	if(team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(type, Color.WHITE)));
    		
		}
		else if(team == 2) setIcon( new ImageIcon(generateChessPieceImage(type, Color.BLACK)));
    
    	removeActionListener(this.listener);
    	setActionListener(new PieceActionListener(type, i, j, team, board));
    	
    }
}