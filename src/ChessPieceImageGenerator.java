import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ChessPieceImageGenerator {

    public static void main(String[] args) {
        // Generate and display the chess piece images
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        // Create a JFrame to display the chess piece images
        JFrame frame = new JFrame("Chess Piece Images");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Generate the chess piece images
        for (ChessPieceType pieceType : ChessPieceType.values()) {
            BufferedImage pieceImage = generateChessPieceImage(pieceType, Color.WHITE, Color.BLACK);
            JLabel label = new JLabel(new ImageIcon(pieceImage));
            frame.add(label);
        }

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static BufferedImage generateChessPieceImage(ChessPieceType pieceType, Color foregroundColor, Color backgroundColor) {
        int size = 64;  // Size of the image in pixels
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Fill the background
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, size, size);

        // Draw the chess piece symbol
        g2d.setColor(foregroundColor);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 48));
        String symbol = getChessPieceSymbol(pieceType);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int x = (size - fontMetrics.stringWidth(symbol)) / 2;
        int y = (size - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();
        g2d.drawString(symbol, x, y);

        g2d.dispose();

        return image;
    }

    private static String getChessPieceSymbol(ChessPieceType pieceType) {
        switch (pieceType) {
            case KING:
                return "\u2654";
            case QUEEN:
                return "\u2655";
            case ROOK:
                return "\u2656";
            case BISHOP:
                return "\u2657";
            case KNIGHT:
                return "\u2658";
            case PAWN:
                return "\u2659";
            default:
                return "";
        }
    }

    enum ChessPieceType {
        KING,
        QUEEN,
        ROOK,
        BISHOP,
        KNIGHT,
        PAWN
    }
}
