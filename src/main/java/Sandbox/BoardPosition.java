package Sandbox;

public class BoardPosition {
    private final String position;
    
    public BoardPosition(Piece[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (piece.isEmpty) {
                    sb.append("-");
                } else {
                    // First letter of color (W/B) + first letter of piece type
                    sb.append(piece.team == 1 ? "W" : "B")
                      .append(piece.type);
                }
            }
        }
        this.position = sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BoardPosition)) return false;
        return position.equals(((BoardPosition) obj).position);
    }
    
    @Override
    public int hashCode() {
        return position.hashCode();
    }
}