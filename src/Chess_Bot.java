import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Chess_Bot {
    private static final int MAX_DEPTH = 4;
    private static int team;
    private HashMap<String, Piece> capturedPieces;
    private static Piece[][] board;
    public static Piece King;
    private int moveCounter = 0;

    public Chess_Bot(int Team) {
        team = Team;
        capturedPieces = new HashMap<>();
    }
    public static void SetBoard(Piece[][] board)
    {
    	Chess_Bot.board = board;
    	for(int i=0; i<8; i++)
    	{
    		for(int j=0; j<8; j++)
    		{
    			Piece piece = board[i][j];
    			if(piece.type.equals("King") && piece.team == team)
    			{
        			King = piece;
    			}
    		}
    	}
    }

    public void CalculateMove() {

        long startTime = System.currentTimeMillis();
        int[] move = iterativeDeepening(startTime);
        
        if (move != null) {
            UpdateGameUI(move[0], move[1], move[2], move[3]);
        }
    }

    private int[] iterativeDeepening(long startTime) {
        int[] bestMove = null;
        int maxTimeMs = 5000; // 5 seconds max
        
        for (int depth = 1; depth <= MAX_DEPTH; depth++) {
            if (System.currentTimeMillis() - startTime > maxTimeMs) {
                break;
            }
            
            int[] move = findBestMove(depth);
            if (move != null) {
                bestMove = move;
            }
        }
        
        return bestMove;
    }

    private int[] findBestMove(int depth) {
    	int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
    	
        List<int[]> moves = generateLegalMoves(team);
        for (int[] move : moves) {
            makeMove(move);
            int score = -negamax(depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, -team);
            undoMove(move);
            
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    private List<int[]> generateLegalMoves(int side) {
    	
        List<int[]> moves = new ArrayList<>();

        HashMap<Piece, HashSet<Integer>> legalPiecesMoves = PieceManager.CheckKingSafety(side > 0 ? King : Board.King, true);
        // Use ChessBot piecesLegalMoves map
        legalPiecesMoves.forEach((piece, legalMoves) -> {
            if (legalMoves != null) {
                legalMoves.forEach(move -> {
                    int toRow = move / 10;
                    int toCol = move % 10;
                    moves.add(new int[]{piece.i, piece.j, toRow, toCol});
                });
            }
        });
        
        return moves;
    }

    private void makeMove(int[] move) {
        int fromRow = move[0], fromCol = move[1], toRow = move[2], toCol = move[3];
        
        // Create unique key combining move counter and positions
        String uniqueKey = moveCounter + ":" + fromRow + "," + fromCol + "->" + toRow + "," + toCol;
        moveCounter++;
        
        // Store the captured piece with unique key
        capturedPieces.put(uniqueKey, board[toRow][toCol]);
        
        // Move the piece on the board copy
        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = Board.emptyPiece;
        
        // Update piece position
        board[toRow][toCol].i = toRow;
        board[toRow][toCol].j = toCol;
    }

    private void undoMove(int[] move) {
        int fromRow = move[0], fromCol = move[1], toRow = move[2], toCol = move[3];
        
        // Recreate the same unique key
        moveCounter--;  // Decrement counter to get back to the correct key
        String uniqueKey = moveCounter + ":" + fromRow + "," + fromCol + "->" + toRow + "," + toCol;
        
        // Move the piece back
        board[fromRow][fromCol] = board[toRow][toCol];
        board[toRow][toCol] = capturedPieces.remove(uniqueKey);
        
        // Restore original position
        board[fromRow][fromCol].i = fromRow;
        board[fromRow][fromCol].j = fromCol;
    }

    private int negamax(int depth, int alpha, int beta, int side) {
        if (depth == 0) {
            return evaluatePosition() * side;
        }

        List<int[]> moves = generateLegalMoves(side);
        if (moves.isEmpty()) {
            return isInCheck(side) ? -99999 : 0; // Checkmate or stalemate
        }

        for (int[] move : moves) {
            makeMove(move);
            int score = -negamax(depth - 1, -beta, -alpha, -side);
            undoMove(move);
            
            if (score >= beta) {
                return beta;
            }
            alpha = Math.max(alpha, score);
        }
        
        return alpha;
    }

    private boolean isInCheck(int side) {
        // Use PieceManager's check detection
        Piece king = side > 0 ? King : Board.King;
        return !PieceManager.performChecks(king.i, king.j, side).isEmpty();
    }

    private int evaluatePosition() {
        int score = 0;
        
        // Material and position evaluation
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (!piece.isEmpty) {
                    int pieceValue = getPieceValue(piece.type);
                    score += (piece.team == team ? 1 : -1) * pieceValue;
                }
            }
        }
        
        return score;
    }

    private int getPieceValue(String type) {
        switch (type) {
            case "Pawn": return 100;
            case "Knight": return 320;
            case "Bishop": return 330;
            case "Rook": return 500;
            case "Queen": return 900;
            case "King": return 20000;
            default: return 0;
        }
    }

    private void UpdateGameUI(int fromRow, int fromCol, int toRow, int toCol) {
    	
        PieceUI callerPieceUI = Board.board[fromRow][fromCol];
        Piece callerPiece = callerPieceUI.curPiece;
        
        Board.board[toRow][toCol].botUpdatePiece(callerPiece);
        callerPieceUI.botUpdatePiece(Board.emptyPiece);

        PieceManager.currentSelectedPiece = null;
        PieceManager.turn = (PieceManager.turn == 1) ? 2 : 1;
        
        // Update timers
        if (team == 1) {
            Client.gameInfoWindow.whiteTimer.stop();
            Client.gameInfoWindow.blackTimer.start();
        } else {
            Client.gameInfoWindow.whiteTimer.start();
            Client.gameInfoWindow.blackTimer.stop();
        }
        Client.gameInfoWindow.updateCurTurn();
        
        PieceManager.CheckKingSafety(Board.King, false);
        
    }
}

