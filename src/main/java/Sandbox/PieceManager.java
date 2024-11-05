package Sandbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Arrays;


public class PieceManager 
{
	static Piece[][] board;
	static HashMap<Piece, HashSet<Integer>> piecesLegalMoves; //this contains all the possible pieces that can move
	static Piece currentSelectedPiece; 
    static HashSet<Piece> enpassantList;
	static int turn;
	public static void Initialize()
	{
		turn = 1;
		piecesLegalMoves = new HashMap<>();
		enpassantList = new HashSet<>();
		
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				if(board[i][j].isEmpty) continue;
				Piece piece = board[i][j];
				if(piece.team != Board.playerTeam) continue;

				HashSet<Integer> pieceLegalMoves = GetLegalMoves(piece, Board.playerTeam);

				if(pieceLegalMoves.size() > 0)
				{
					piecesLegalMoves.put(piece, pieceLegalMoves);
				}
			}
		}
		
		piecesLegalMoves.put(Board.emptyPiece, null);
	}
	public static HashSet<Integer> GetLegalMoves(Piece piece, int team)
	{
		return GetLegalMoves(piece.i, piece.j, piece.type, team);
	}
	private static HashSet<Integer> GetLegalMoves(int i, int j, String type, int team)
	{
		HashSet<Integer> legalMoves = new HashSet<>();
		
		switch(type) {
			case "King":
				getLegalKingMoves(i, j, team, legalMoves);
				break;
			case "Queen":
				getLegalStraightMoves(i, j, team, legalMoves);
				getLegalDiagonalMoves(i, j, team, legalMoves);
				break;
			case "Rook":
				getLegalStraightMoves(i, j, team, legalMoves);
				break;
			case "Bishop":
				getLegalDiagonalMoves(i, j, team, legalMoves);
				break;
			case "Knight":
				getLegalKnightMoves(i, j, team, legalMoves);
				break;
			case "Pawn":
				getLegalPawnMoves(i, j, team, legalMoves);
				break;
		}
		
		return legalMoves;
	}

	private static void getLegalKingMoves(int i, int j, int team, HashSet<Integer> legalMoves) {
		// Regular king moves in all 8 directions
		int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}, 
							 {1,1}, {-1,-1}, {1,-1}, {-1,1}};
		
		for(int[] dir : directions) {
			int newI = i + dir[0];
			int newJ = j + dir[1];
			
			if(isValidPosition(newI, newJ) && board[newI][newJ].team != team) {
				legalMoves.add(newI * 10 + newJ);
			}
		}
		
		
		// Kingside castling
		if(canCastle(i, j, true, team)) {
			legalMoves.add(i * 10 + (j + 2));
		}
		// Queenside castling
		if(canCastle(i, j, false, team)) {
			legalMoves.add(i * 10 + (j - 2));
		}
		
		
		// Filter moves that would put king in check
		filterCheckedMoves(legalMoves, team);
	}

	private static void getLegalStraightMoves(int i, int j, int team, HashSet<Integer> legalMoves) {
		// Vertical and horizontal directions
		int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}};
		
		for(int[] dir : directions) {
			int newI = i + dir[0];
			int newJ = j + dir[1];
			
			while(isValidPosition(newI, newJ)) {
				if(board[newI][newJ].isEmpty) {
					legalMoves.add(newI * 10 + newJ);
				} else if(board[newI][newJ].team != team) {
					legalMoves.add(newI * 10 + newJ);
					break;
				} else {
					break;
				}
				newI += dir[0];
				newJ += dir[1];
			}
		}
	}

	private static void getLegalDiagonalMoves(int i, int j, int team, HashSet<Integer> legalMoves) {
		// Diagonal directions
		int[][] directions = {{1,1}, {-1,-1}, {1,-1}, {-1,1}};
		
		for(int[] dir : directions) {
			int newI = i + dir[0];
			int newJ = j + dir[1];
			
			while(isValidPosition(newI, newJ)) {
				if(board[newI][newJ].isEmpty) {
					legalMoves.add(newI * 10 + newJ);
				} else if(board[newI][newJ].team != team) {
					legalMoves.add(newI * 10 + newJ);
					break;
				} else {
					break;
				}
				newI += dir[0];
				newJ += dir[1];
			}
		}
	}

	private static void getLegalKnightMoves(int i, int j, int team, HashSet<Integer> legalMoves) {
		int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, 
							   {1,2}, {1,-2}, {-1,2}, {-1,-2}};
		
		for(int[] move : knightMoves) {
			int newI = i + move[0];
			int newJ = j + move[1];
			
			if(isValidPosition(newI, newJ) && board[newI][newJ].team != team) {
				legalMoves.add(newI * 10 + newJ);
			}
		}
	}

	private static void getLegalPawnMoves(int i, int j, int team, HashSet<Integer> legalMoves) {
		int direction = (team == Chess_Bot.team) ? 1 : -1;
		int startingRow = (team == Chess_Bot.team) ? 1 : 6;
		
		// Forward moves
		if(isValidPosition(i + direction, j) && board[i + direction][j].isEmpty) {
			legalMoves.add((i + direction) * 10 + j);
			
			// Double move from starting position
			if(i == startingRow && board[i + (2 * direction)][j].isEmpty) {
				legalMoves.add((i + (2 * direction)) * 10 + j);
			}
		}
		
		// Captures
		for(int offset : new int[]{-1, 1}) {
			int newJ = j + offset;
			if(isValidPosition(i + direction, newJ)) {
				// Regular capture
				if(board[i + direction][newJ].team != team && !board[i + direction][newJ].isEmpty) {
					legalMoves.add((i + direction) * 10 + newJ);
				}
				// En passant
				if(isValidPosition(i, newJ) && board[i][newJ].enPassant && board[i + direction][newJ].isEmpty) {
					legalMoves.add((i + direction) * 10 + newJ);
				}
			}
		}
	}

	private static boolean isValidPosition(int i, int j) {
		return i >= 0 && i < 8 && j >= 0 && j < 8;
	}

	private static void filterCheckedMoves(HashSet<Integer> moves, int team) {
		moves.removeIf(pos -> {
			int row = pos / 10;
			int col = pos - row * 10;
			return performChecks(row, col, team).size() > 0;
		});
	}

	private static boolean canCastle(int i, int j, boolean kingSide, int team) {
		// Check if king has moved or is in check
		Piece king = board[i][j];
		if(king.hasMoved || performChecks(i, j, team).size() > 0) {
			return false;
		}

		int direction = kingSide ? 1 : -1;
		int rookCol = kingSide ? 7 : 0;
		
		// Check if rook exists and hasn't moved
		Piece rook = board[i][rookCol];
		if(rook.isEmpty || !rook.type.equals("Rook") || 
		   rook.team != team || rook.hasMoved) {
			return false;
		}
		
		// Check if all squares between king and rook are empty
		int start = j + direction;
		int end = kingSide ? rookCol - 1 : rookCol + 1;
		for(int col = Math.min(start, end); col <= Math.max(start, end); col++) {
			if(!board[i][col].isEmpty) {
				return false;
			}
		}
		
		// Check if squares king moves through are under attack
		return performChecks(i, j + direction, team).size() == 0 && 
			   performChecks(i, j + 2 * direction, team).size() == 0;
	}

	public static String formatPieceString(Piece piece) {
		if(piece.isEmpty) return " ";
		
		// First letter of color + first letter of piece type
		String color = (piece.team == 1) ? "W" : "B";
		String type = piece.type;
		return color + type;
	}
	
	public static HashMap<Piece, HashSet<Integer>> CheckKingSafety(Piece King, boolean simulation) 
	{
		piecesLegalMoves.clear();

		int team = King.team;
		List<Piece> checkingPieces = performChecks(King.i, King.j, team);
		
		// No check - check for stalemate or normal moves
		if(checkingPieces.size() == 0) {
			boolean hasLegalMoves = false;
			
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					Piece piece = board[i][j];
					if(piece.isEmpty || piece.team != team) continue;
					
					HashSet<Integer> pieceLegalMoves = GetLegalMoves(piece, piece.team);
					if(pieceLegalMoves.size() > 0) {
						hasLegalMoves = true;
						piecesLegalMoves.put(piece, pieceLegalMoves);
					}
				}
			}
			
			// If no legal moves and not in check, it's stalemate
			if(!hasLegalMoves && !simulation) {
				Client.gameInfoWindow.endGame("draw");
			}
			
			return piecesLegalMoves;
		}
		
		// King is in check
		if(!simulation) System.out.println("King is in check");
		HashSet<Integer> kingLegalMoves = GetLegalMoves(King, team);
		piecesLegalMoves.put(King, kingLegalMoves);
		
		// If king can't move and multiple pieces are checking, it's checkmate
		if(kingLegalMoves.isEmpty() && checkingPieces.size() > 1) {
			if(!simulation) Client.gameInfoWindow.endGame("lose");
			if(!simulation) System.out.println("Check mate with more than 1 checking piece");
			return piecesLegalMoves;
		}
		
		// Single checking piece - can potentially block or capture
		Piece checkingPiece = checkingPieces.get(0);
		int checkingPiecePosition = checkingPiece.i * 10 + checkingPiece.j;
		if(checkingPiece.type.equals("Knight")) {
			// Knights can only be captured, not blocked
			for(int i = 0; i < 8; i++) {
				for(int j = 0; j < 8; j++) {
					Piece piece = board[i][j];
					if(piece.team != team) continue;
					
					HashSet<Integer> moves = GetLegalMoves(piece, piece.team);
					if(moves.contains(checkingPiecePosition)) {
						piecesLegalMoves.put(piece, new HashSet<>(Arrays.asList(checkingPiecePosition)));
					}
				}
			}
			if(!simulation) System.out.println("Checking piece is a Knight. Moveable pieces list size : " + piecesLegalMoves.size());
		} else {
			// Other pieces can be blocked or captured
			piecesLegalMoves.putAll(GetMoveablePiecesForCheckingRoute(checkingPiece, King));
			if(!simulation) System.out.println("There is a checking piece. Moveable pieces list size: " + piecesLegalMoves.size());
		}
		// If king can't move and no other pieces can help, it's checkmate
		if(kingLegalMoves.isEmpty() && piecesLegalMoves.size() <= 1) {  // 1 because king will always be added
			if(!simulation) Client.gameInfoWindow.endGame("lose");
			if(!simulation) System.out.println("No pieces can save king and king is in check with no legal moves");
		}
		
		return piecesLegalMoves;
	}
	
	public static HashSet<Integer> GetCheckingRoute(int kingRow, int kingCol, int checkingRow, int checkingCol) {
		HashSet<Integer> checkingRoute = new HashSet<>();
		
		int rowDiff = checkingRow - kingRow;
		int colDiff = checkingCol - kingCol;
		
		// If it's a knight, only return the checking piece position
		if (Math.abs(rowDiff) == 2 && Math.abs(colDiff) == 1 || 
			Math.abs(rowDiff) == 1 && Math.abs(colDiff) == 2) {
			checkingRoute.add(checkingRow * 10 + checkingCol);
			return checkingRoute;
		}

		// Verify if the checking piece is on a valid line with the king
		boolean isValidLine = false;
		
		// Check if it's a straight line (rook-like movement)
		if (rowDiff == 0 || colDiff == 0) {
			isValidLine = true;
		}
		// Check if it's a diagonal line (bishop-like movement)
		else if (Math.abs(rowDiff) == Math.abs(colDiff)) {
			isValidLine = true;
		}
		
		if (!isValidLine) {
			return checkingRoute; // Return empty set if not a valid line
		}

		// Get direction of movement
		int rowDir = Integer.compare(rowDiff, 0);  // -1, 0, or 1
		int colDir = Integer.compare(colDiff, 0);  // -1, 0, or 1

		// Start from checking piece and move towards king (exclusive)
		int currentRow = checkingRow - rowDir;
		int currentCol = checkingCol - colDir;

		while (currentRow != kingRow || currentCol != kingCol) {
			checkingRoute.add(currentRow * 10 + currentCol);
			currentRow -= rowDir;
			currentCol -= colDir;
		}

		return checkingRoute;
	}
	public static HashSet<Integer> GetRoute(int kingRow, int kingCol, int Row, int Col) {
		HashSet<Integer> checkingRoute = new HashSet<>();
		
		int rowDiff = Row - kingRow;
		int colDiff = Col - kingCol;
	

		// Verify if the checking piece is on a valid line with the king
		boolean isValidLine = false;
		
		// Check if it's a straight line (rook-like movement)
		if (rowDiff == 0 || colDiff == 0) {
			isValidLine = true;
		}
		// Check if it's a diagonal line (bishop-like movement)
		else if (Math.abs(rowDiff) == Math.abs(colDiff)) {
			isValidLine = true;
		}
		
		if (!isValidLine) {
			return checkingRoute; // Return empty set if not a valid line
		}

		// Get direction of movement
		int rowDir = Integer.compare(rowDiff, 0);  // -1, 0, or 1
		int colDir = Integer.compare(colDiff, 0);  // -1, 0, or 1

		// Start from checking piece and move towards king (exclusive)
		int currentRow = Row - rowDir;
		int currentCol = Col - colDir;

		while (currentRow != kingRow || currentCol != kingCol) {
			if(!board[currentRow][currentCol].isEmpty)
			{
				checkingRoute.clear();
				break;
			}
			checkingRoute.add(currentRow * 10 + currentCol);
			currentRow -= rowDir;
			currentCol -= colDir;
		}

		return checkingRoute;
	}
	private static HashMap<Piece, HashSet<Integer>> GetMoveablePiecesForCheckingRoute(Piece checkingPiece, Piece King) {
		HashSet<Integer> checkingRoute = GetCheckingRoute(King.i, King.j, checkingPiece.i, checkingPiece.j);
		HashMap<Piece, HashSet<Integer>> legalPieces = new HashMap<>();
		
		// Check all friendly pieces
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Piece piece = board[i][j];
				if(piece.isEmpty || piece.team != King.team || piece == King) continue;
				
				// Get all legal moves for this piece
				HashSet<Integer> moves = GetLegalMoves(piece, piece.team);
				
				// If any of the piece's legal moves intersect with the checking route
				// (including capturing the checking piece)
				for(int move : moves) {
					if(checkingRoute.contains(move)) {
						// Get or create the set of legal moves for this piece
						HashSet<Integer> pieceMoves = legalPieces.computeIfAbsent(piece, k -> new HashSet<>());
						pieceMoves.add(move);
					}
				}
			}
		}
		
		return legalPieces;
	}
	public static List<Piece> performChecks(int row, int col, int kingTeam) {
		
		List<Piece> checkingPieces = new ArrayList<>();
		
		// Vertical checks (up and down)
		checkVertical(row, col, kingTeam, checkingPieces);
		
		// Horizontal checks (left and right)
		checkHorizontal(row, col, kingTeam, checkingPieces);
		
		// Diagonal checks
		checkDiagonals(row, col, kingTeam, checkingPieces);
		
		// Knight checks
		checkKnightMoves(row, col, kingTeam, checkingPieces);
		
		return checkingPieces;
	}

	public static void checkVertical(int row, int col, int kingTeam, List<Piece> checkingPieces) {
		// Check upwards
		for(int i = row - 1; i >= 0; i--) {
			if(board[i][col].isEmpty) continue;
			if(board[i][col].team == kingTeam) break;
			if(board[i][col].type.equals("Queen") || board[i][col].type.equals("Rook")) {
				checkingPieces.add(board[i][col]);
			}
			break;
		}
		
		// Check downwards
		for(int i = row + 1; i < 8; i++) {
			if(board[i][col].isEmpty) continue;
			if(board[i][col].team == kingTeam) break;
			if(board[i][col].type.equals("Queen") || board[i][col].type.equals("Rook")) {
				checkingPieces.add(board[i][col]);
			}
			break;
		}
	}

	public static void checkHorizontal(int row, int col, int kingTeam, List<Piece> checkingPieces) {
		// Check left
		for(int j = col - 1; j >= 0; j--) {
			if(board[row][j].isEmpty) continue;
			if(board[row][j].team == kingTeam) break;
			if(board[row][j].type.equals("Queen") || board[row][j].type.equals("Rook")) {
				checkingPieces.add(board[row][j]);
			}
			break;
		}
		
		// Check right
		for(int j = col + 1; j < 8; j++) {
			if(board[row][j].isEmpty) continue;
			if(board[row][j].team == kingTeam) break;
			if(board[row][j].type.equals("Queen") || board[row][j].type.equals("Rook")) {
				checkingPieces.add(board[row][j]);
			}
			break;
		}
	}

	public static void checkDiagonals(int row, int col, int kingTeam, List<Piece> checkingPieces) {
		// Check all four diagonal directions
		int[][] directions = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
		
		for(int[] dir : directions) {
			int i = row + dir[0];
			int j = col + dir[1];
			
			while(i >= 0 && i < 8 && j >= 0 && j < 8) {
				if(board[i][j].isEmpty) {
					i += dir[0];
					j += dir[1];
					continue;
				}
				if(board[i][j].team == kingTeam) break;
				
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop")) {
					checkingPieces.add(board[i][j]);
				}
				// FIXED: Pawn attack logic - pawns can only attack one square diagonally
				if(board[i][j].type.equals("Pawn")) {
					// White pawns move up (-1 row), Black pawns move down (+1 row)
					int attackDir = (board[i][j].team == 1) ? 1 : -1;
					if(i - row == attackDir && Math.abs(j - col) == 1) {
						checkingPieces.add(board[i][j]);
					}
				}
				break;
			}
		}
	}

	public static void checkKnightMoves(int row, int col, int kingTeam, List<Piece> checkingPieces) {
		int[][] knightMoves = {{2,1}, {2,-1}, {-2,1}, {-2,-1}, 
								{1,2}, {1,-2}, {-1,2}, {-1,-2}};
		
		for(int[] move : knightMoves) {
			int i = row + move[0];
			int j = col + move[1];
			
			if(i >= 0 && i < 8 && j >= 0 && j < 8) {
				if(board[i][j].type.equals("Knight") && board[i][j].team != kingTeam) {
					checkingPieces.add(board[i][j]);
				}
			}
		}
	}
}
