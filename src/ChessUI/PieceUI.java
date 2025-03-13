package ChessUI;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import Network.Client;
import Network.Data;
import ChessEngine.*;

public class PieceUI extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Piece curPiece;
	int i, j;
	public PieceUI(Piece piece, int i, int j)
	{
		
		curPiece = piece;
		this.i = i;
		this.j = j;
		
		int team = piece.team;
    	BufferedImage pieceImage = null;
		if(team == 1) {
			pieceImage = generateChessPieceImage(piece.type, Color.WHITE);
			ImageIcon imageIcon = new ImageIcon(pieceImage);
			setIcon(imageIcon);
		}
		else if(team == 2) {
			pieceImage = generateChessPieceImage(piece.type, Color.BLACK);
			ImageIcon imageIcon = new ImageIcon(pieceImage);
			setIcon(imageIcon);
		}
		addActionListener(new PieceActionListener(this, i, j));
			
		
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
    public void updatePiece(Piece piece, boolean isEmpty, boolean pawnPromo) {
    	ChessEngine.board[i][j] = piece;    
		
    	curPiece = piece;
    	try {
    		if(isEmpty)
    		{
    			Client.oos.writeObject(new Data("Update", i, j));
    		}
    		else if(pawnPromo)
    		{
    			Client.oos.writeObject(new Data("Update", i, j, piece.type));
    		}
    		else if(curPiece.enPassant)
    		{
    			Client.oos.writeObject(new Data("En Passant", i, j, piece.i, piece.j));
    		}
    		else 
    		{
    			Client.oos.writeObject(new Data("Update", i, j, piece.i, piece.j));
    		}
			Client.oos.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
    	piece.i = i;
    	piece.j = j;
    	if(piece.team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.WHITE)));
		}
		else if(piece.team == 2) setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.BLACK)));
		else setIcon(null);
    	
    
    }
    public void botUpdatePiece(Piece piece) {
    	
    	ChessEngine.board[i][j] = piece;
    	
    	curPiece = piece;
    	
    	piece.i = i;
    	piece.j = j;
    	if(piece.team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.WHITE)));
		}
		else if(piece.team == 2) setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.BLACK)));
		else setIcon(null);
    	
    	
    }
    public void ClientUpdatePiece(Piece piece) {
    	
    	ChessEngine.board[i][j] = piece;
    	
    	curPiece = piece;
    	piece.i = i;
    	piece.j = j;
    	if(piece.team == 1) {
    		setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.WHITE)));
		}
		else if(piece.team == 2) setIcon( new ImageIcon(generateChessPieceImage(piece.type, Color.BLACK)));
		else setIcon(null);

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
