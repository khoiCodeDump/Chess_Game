package Sandbox;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

public class PieceUI extends JButton{
	
	private static final long serialVersionUID = 1L;
	Piece curPiece;
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
	 public BufferedImage transcodeSVGToBufferedImage(InputStream inputStream, int width, int height) {
        if (inputStream == null) return null;
        
        Transcoder t = new PNGTranscoder();
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);
        
        try {
            TranscoderInput input = new TranscoderInput(inputStream);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outputStream);
            
            t.transcode(input, output);
            outputStream.flush();
            outputStream.close();
            
            byte[] imgData = outputStream.toByteArray();
            return ImageIO.read(new ByteArrayInputStream(imgData));
        } catch (IOException | TranscoderException e) {
            e.printStackTrace();
        }
        return null;
    }
	public BufferedImage generateChessPieceImage(String pieceType, Color color) {
		String resourcePath = null;
		
		// Determine resource path
		switch (pieceType) {
			case "King":
				resourcePath = color == Color.WHITE ? "/images/wK.svg" : "/images/bK.svg";
				break;
			case "Queen":
				resourcePath = color == Color.WHITE ? "/images/wQ.svg" : "/images/bQ.svg";
				break;
			case "Rook":
				resourcePath = color == Color.WHITE ? "/images/wR.svg" : "/images/bR.svg";
				break;
			case "Bishop":
				resourcePath = color == Color.WHITE ? "/images/wB.svg" : "/images/bB.svg";
				break;
			case "Knight":
				resourcePath = color == Color.WHITE ? "/images/wN.svg" : "/images/bN.svg";
				break;
			case "Pawn":
				resourcePath = color == Color.WHITE ? "/images/wP.svg" : "/images/bP.svg";
				break;
		}

		if (resourcePath == null) {
			System.err.println("Invalid piece type: " + pieceType);
			return null;
		}

		// Get resource as stream
		InputStream inputStream = PieceUI.class.getResourceAsStream(resourcePath);
		
		if (inputStream == null) {
			// Try alternative path
			inputStream = PieceUI.class.getResourceAsStream(resourcePath.substring(1));
		}
		
		if (inputStream == null) {
			System.err.println("Failed to load resource: " + resourcePath);
			return null;
		}

		try {
			return transcodeSVGToBufferedImage(inputStream, 64, 64);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updatePiece(Piece piece, boolean isEmpty, boolean pawnPromo) {
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
