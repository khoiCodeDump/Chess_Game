package Sandbox;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Chess_Bot {
    private static final int MAX_DEPTH = 6; //this is always an even number greater than 0
    public static int team;
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

        HashMap<Piece, HashSet<Integer>> legalPiecesMoves = ChessEngine.CheckKingSafety(side > 0 ? King : Board.King, true);
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
        
        Board.addPosition(board);
     
    }

    private void undoMove(int[] move) {
        Board.positionHistory.remove(Board.positionHistory.size() - 1);
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
            // Checkmate or stalemate
            return isInCheck(side) ? -99999 * side : 0;
        }

        for (int[] move : moves) {
            makeMove(move);
            int fromRow = move[0], fromCol = move[1], toRow = move[2], toCol = move[3];
            int score;
            
            // If we're in check after making our move, it's illegal
            if (isInCheck(side) ) {
                undoMove(move);
                continue; // Skip this move and try next one
            }

            if (board[toRow][toCol].type.equals("Pawn")) {
                // Pawn promotion
                if ((side > 0 && toRow == 7) || (side < 0 && toRow == 0)) {
                    Piece tempPawn = board[toRow][toCol];
                    score = Integer.MIN_VALUE;
                    
                    for (String promoPiece : new String[]{"Queen", "Rook", "Bishop", "Knight"}) {
                        board[toRow][toCol] = new Piece(promoPiece, toRow, toCol, team);
                        
                        if (ChessEngine.CheckKingSafety(Board.King, true).isEmpty()) {
                            score = 99999 * side; // Checkmate value keeps same sign as side
                            break;
                        } else {
                            int promotionScore = -negamax(depth - 1, -beta, -alpha, -side);
                            score = Math.max(score, promotionScore);
                        }
                    }
                    board[toRow][toCol] = tempPawn;
                }
                // Double pawn push
                else if ((side > 0 && toRow == 3 && fromRow == 1) || 
                         (side < 0 && toRow == 4 && fromRow == 6)) {
                    board[toRow][toCol].enPassant = true;
                    score = -negamax(depth - 1, -beta, -alpha, -side);
                    board[toRow][toCol].enPassant = false;
                }
                // En passant capture
                else if (Math.abs(toRow - fromRow) == 1 && Math.abs(toCol - fromCol) == 1) {
                    int captureRow = (side > 0) ? toRow + 1 : toRow - 1;
                    Piece capturedPawn = board[captureRow][toCol];
                    
                    if (capturedPawn != null && !capturedPawn.isEmpty && 
                        capturedPawn.enPassant && capturedPawn.team != team) {
                        Piece tempPiece = board[captureRow][toCol];
                        board[captureRow][toCol] = new Piece("", captureRow, toCol, 0);
                        score = -negamax(depth - 1, -beta, -alpha, -side);
                        board[captureRow][toCol] = tempPiece;
                    } else {
                        score = -negamax(depth - 1, -beta, -alpha, -side);
                    }
                }
                // Normal pawn move
                else {
                    score = -negamax(depth - 1, -beta, -alpha, -side);
                }
            }
            // Non-pawn moves
            else {
                score = -negamax(depth - 1, -beta, -alpha, -side);
            }
            
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
        if(board[king.i][king.j].isEmpty)
        {
            return true;
        }
        return !ChessEngine.performChecks(king.i, king.j, king.team).isEmpty();
    }

    private int evaluatePosition() {
        // Check for threefold repetition
        if (Board.isThreefoldRepetition()) {
            return 0;
        }

        int score = 0;
        boolean isEndgame = isEndgame();

        // Material and position evaluation
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (!piece.isEmpty) {
                    int multiplier = (piece.team == team) ? 1 : -1;
                    int pieceValue = getPieceValue(piece.type);
                    int positionBonus = 0;

                    switch (piece.type) {
                        case "Pawn":
                            positionBonus = evaluatePawn(i, j, piece.team);
                            break;
                        case "Knight":
                            positionBonus = evaluateKnight(i, j);
                            break;
                        case "Bishop":
                            positionBonus = evaluateBishop(i, j);
                            if (hasBishopPair(piece.team)) positionBonus += 30;
                            break;
                        case "Rook":
                            positionBonus = evaluateRook(i, j, piece.team);
                            break;
                        case "Queen":
                            positionBonus = evaluateQueen(i, j, isEndgame);
                            break;
                        case "King":
                            if (isEndgame) {
                                positionBonus = evaluateKingEndgame(i, j);
                            } else {
                                positionBonus = evaluateKingMiddlegame(i, j, piece.team);
                            }
                            break;
                    }
                    score += multiplier * (pieceValue + positionBonus);
                }
            }
        }

        return score;
    }

    private boolean isEndgame() {
        int pieceCount = 0;
        boolean queenExists = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board[i][j];
                if (!piece.isEmpty && !piece.type.equals("King") && !piece.type.equals("Pawn")) {
                    pieceCount++;
                    if (piece.type.equals("Queen")) queenExists = true;
                }
            }
        }
        return !queenExists || pieceCount <= 6;
    }

    private int evaluatePawn(int row, int col, int pawnTeam) {
        int score = 0;
        
        // Passed pawn bonus
        if (isPassedPawn(row, col, pawnTeam)) score += 50;
        
        // Doubled pawn penalty
        if (isDoubledPawn(col, pawnTeam)) score -= 20;
        
        // Isolated pawn penalty
        if (isIsolatedPawn(col, pawnTeam)) score -= 15;
        
        int rankFromPromotion = (pawnTeam == 1) ? row : 7 - row;
        score += (6 - rankFromPromotion) * 5;
        
        // Center control bonus
        if (col >= 2 && col <= 5) score += 10;
        
        return score;
    }

    private int evaluateKnight(int row, int col) {
        // Knights are stronger in the center
        int centerDistance = (int) (Math.abs(3.5 - row) + Math.abs(3.5 - col));
        return 30 - (centerDistance * 5);
    }

    private int evaluateBishop(int row, int col) {
        int score = 0;
        // Bishops control more squares from center
        int centerDistance = (int) (Math.abs(3.5 - row) + Math.abs(3.5 - col));
        score += 20 - (centerDistance * 4);
        
        // Mobility bonus
        score += countBishopMobility(row, col) * 5;
        
        return score;
    }

    private int evaluateRook(int row, int col, int rookTeam) {
        int score = 0;
        
        // Rook on open file
        if (isOpenFile(col)) score += 25;
        
        // Rook on semi-open file
        if (isSemiOpenFile(col, rookTeam)) score += 15;
        
        // Rook on seventh rank
        if ((rookTeam == 1 && row == 1) || (rookTeam == 2 && row == 6)) score += 20;
        
        return score;
    }

    private int evaluateQueen(int row, int col, boolean isEndgame) {
        int score = 0;
        
        // Queen mobility
        score += countQueenMobility(row, col) * 2;
        
        // Early development penalty (early game only)
        if (!isEndgame && (row == 0 || row == 7)) score -= 10;
        
        return score;
    }

    private int evaluateKingMiddlegame(int row, int col, int kingTeam) {
        int score = 0;
        
        // King safety
        if (kingTeam == 1) {
            // White king
            if (row >= 6) score += 50; // Back rank bonus
            if (col >= 2 && col <= 5) score -= 30; // Center penalty
        } else {
            // Black king
            if (row <= 1) score += 50; // Back rank bonus
            if (col >= 2 && col <= 5) score -= 30; // Center penalty
        }
        
        // Pawn shield bonus
        score += evaluatePawnShield(row, col, kingTeam);
        
        return score;
    }

    private int evaluateKingEndgame(int row, int col) {
        // King should be active in endgame
        int centerDistance = (int) (Math.abs(3.5 - row) + Math.abs(3.5 - col));
        return 50 - (centerDistance * 10);
    }

    private boolean isPassedPawn(int row, int col, int pawnTeam) {
        int direction = (pawnTeam == 1) ? -1 : 1;
        
        // Check if there are any enemy pawns ahead in same file or adjacent files
        for (int r = row + direction; r >= 0 && r < 8; r += direction) {
            for (int c = Math.max(0, col-1); c <= Math.min(7, col+1); c++) {
                if (!board[r][c].isEmpty && 
                    board[r][c].type.equals("Pawn") && 
                    board[r][c].team != pawnTeam) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isDoubledPawn(int col, int team) {
        int pawnCount = 0;
        for (int i = 0; i < 8; i++) {
            if (!board[i][col].isEmpty && 
                board[i][col].type.equals("Pawn") && 
                board[i][col].team == team) {
                pawnCount++;
            }
        }
        return pawnCount > 1;
    }

    private boolean isIsolatedPawn(int col, int team) {
        // Check adjacent files for friendly pawns
        for (int c = Math.max(0, col-1); c <= Math.min(7, col+1); c++) {
            if (c == col) continue;
            for (int r = 0; r < 8; r++) {
                if (!board[r][c].isEmpty && 
                    board[r][c].type.equals("Pawn") && 
                    board[r][c].team == team) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOpenFile(int col) {
        for (int i = 0; i < 8; i++) {
            if (!board[i][col].isEmpty && board[i][col].type.equals("Pawn")) {
                return false;
            }
        }
        return true;
    }

    private boolean isSemiOpenFile(int col, int team) {
        boolean friendlyPawn = false;
        boolean enemyPawn = false;
        
        for (int i = 0; i < 8; i++) {
            if (!board[i][col].isEmpty && board[i][col].type.equals("Pawn")) {
                if (board[i][col].team == team) {
                    friendlyPawn = true;
                } else {
                    enemyPawn = true;
                }
            }
        }
        return !friendlyPawn && enemyPawn;
    }

    private int evaluatePawnShield(int kingRow, int kingCol, int kingTeam) {
        int score = 0;
        int pawnRow = (kingTeam == 1) ? kingRow + 1 : kingRow - 1;
        
        if (pawnRow < 0 || pawnRow >= 8) return 0;
        
        // Check pawns in front of king
        for (int c = Math.max(0, kingCol-1); c <= Math.min(7, kingCol+1); c++) {
            if (!board[pawnRow][c].isEmpty && 
                board[pawnRow][c].type.equals("Pawn") && 
                board[pawnRow][c].team == kingTeam) {
                score += 10;
            }
        }
        
        return score;
    }

    private boolean hasBishopPair(int team) {
        int bishops = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board[i][j].isEmpty && 
                    board[i][j].type.equals("Bishop") && 
                    board[i][j].team == team) {
                    bishops++;
                }
            }
        }
        return bishops >= 2;
    }

    private int countBishopMobility(int row, int col) {
        return countDiagonalMobility(row, col);
    }

    private int countQueenMobility(int row, int col) {
        return countDiagonalMobility(row, col) + countStraightMobility(row, col);
    }

    private int countDiagonalMobility(int row, int col) {
        int mobility = 0;
        int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c].isEmpty) mobility++;
                else break;
                r += dir[0];
                c += dir[1];
            }
        }
        return mobility;
    }

    private int countStraightMobility(int row, int col) {
        int mobility = 0;
        int[][] directions = {{0,1}, {0,-1}, {1,0}, {-1,0}};
        
        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];
            while (r >= 0 && r < 8 && c >= 0 && c < 8) {
                if (board[r][c].isEmpty) mobility++;
                else break;
                r += dir[0];
                c += dir[1];
            }
        }
        return mobility;
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
        
        if(callerPiece.type.equals("King") && fromRow == 0 && toRow == 0)
        {
        	
    		if(toCol == 1 || toCol == 2)
    		{
    			Piece rook = Board.pieces[0][0];
    			Board.board[0][0].botUpdatePiece(Board.emptyPiece);
    			Board.board[0][toCol + 1].botUpdatePiece(rook);
    		}
    		else if(toCol == 5 || toCol == 6)
    		{
    			Piece rook = Board.pieces[7][7];
    			Board.board[7][7].botUpdatePiece(Board.emptyPiece);
    			Board.board[0][toCol - 1].botUpdatePiece(rook);
    		}
        	
        }
        else if(callerPiece.type.equals("Pawn"))
        {
        	if(fromRow == 1 && toRow == 3)
        	{
        		callerPiece.enPassant = true;
        	}
        } 
        else if(toRow == 7 && callerPiece.type.equals("Pawn"))
        {
        	//Pawn promotion
        	board[fromRow][fromCol] = Board.emptyPiece;
        	String[] promoPieces = {"Knight", "Bishop", "Rook", "Queen"};
        	for(String promoPiece : promoPieces)
        	{
            	board[toRow][toCol] = new Piece(promoPiece, toRow, toCol, team);
            	callerPiece = board[toRow][toCol];
        		if(ChessEngine.CheckKingSafety(Board.King , true).isEmpty()) {
            		break;
            	}
        	}
        }
        callerPiece.hasMoved = true;
//        System.out.println("\nUpdate game UI: Current Board State:");
//		for(int i=0; i<8; i++) {
//			System.out.print((8-i) + " |"); 
//			for(int j=0; j<8; j++) {
//				String pieceStr = PieceManager.formatPieceString(board[i][j]);
//      		System.out.printf(" %-8s|", pieceStr);
//			}
//			 System.out.println();
//		}
//		System.out.println();
        
        Board.board[toRow][toCol].botUpdatePiece(callerPiece);
        callerPieceUI.botUpdatePiece(Board.emptyPiece);
        
//        System.out.println("\nMake Move Current Board State:");
//		for(int i=0; i<8; i++) {
//			System.out.print((8-i) + " |"); 
//			for(int j=0; j<8; j++) {
//				String pieceStr = PieceManager.formatPieceString(board[i][j]);
//      		System.out.printf(" %-8s|", pieceStr);
//			}
//			 System.out.println();
//		}
//		System.out.println();
        Board.addPosition(board);
        
        if (Board.isThreefoldRepetition()) {
			Client.gameInfoWindow.endGame("draw");
			return;
		}
        ChessEngine.currentSelectedPiece = null;
        ChessEngine.turn = (ChessEngine.turn == 1) ? 2 : 1;

        
    }
}

