package Sandbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Piece{
	
	int i, j;
	String type;
	int team;
	boolean isEmpty;
	boolean enPassant;
	public int originalI;
	public int originalJ;
	public boolean hasMoved;
	public Piece() {
		isEmpty = true;
		team = 0;
		type = "";
	}

	Piece(String type, int i, int j, int team){
		this.type = type;
		this.team = team;
		this.i = i;
		this.j = j;
		 
	}
	
	public boolean isPinned()
	{
		
		// Now get the checking route
		HashSet<Integer> routeToKing = PieceManager.GetRoute(Board.King.i, Board.King.j, i, j);
		
		if(!routeToKing.isEmpty())
		{
			// Get direction of checking route
			int rowDiff = i - Board.King.i;
			int colDiff = j - Board.King.j;

			// Get unit direction (-1, 0, or 1 for each component)
			int rowDir = Integer.compare(rowDiff, 0);
			int colDir = Integer.compare(colDiff, 0);
			
			List<Piece> checkingPieces = new ArrayList<>();
			
			// Check based on direction
			if (rowDir == 0) {
				// Pure horizontal check
				PieceManager.checkHorizontal(i, j, team, checkingPieces);
			} 
			else if (colDir == 0) {
				// Pure vertical check
				PieceManager.checkVertical(i, j, team, checkingPieces);
			}
			else if (Math.abs(rowDiff) == Math.abs(colDiff)) {
				// Pure diagonal check - check opposite direction of king
				int currentRow, currentCol;
				
				if (rowDir > 0) {
					if (colDir > 0) {
						// King is bottom-right, check top-left diagonal
						currentRow = i - 1;
						currentCol = j - 1;
						while (currentRow >= 0 && currentCol >= 0) {
							Piece piece = PieceManager.board[currentRow][currentCol];
							if (!piece.isEmpty) {
								if (piece.team != team && 
								   (piece.type.equals("Queen") || piece.type.equals("Bishop"))) {
									checkingPieces.add(piece);
								}
								break;
							}
							currentRow--;
							currentCol--;
						}
					} else {
						// King is bottom-left, check top-right diagonal
						currentRow = i - 1;
						currentCol = j + 1;
						while (currentRow >= 0 && currentCol < 8) {
							Piece piece = PieceManager.board[currentRow][currentCol];
							if (!piece.isEmpty) {
								if (piece.team != team && 
								   (piece.type.equals("Queen") || piece.type.equals("Bishop"))) {
									checkingPieces.add(piece);
								}
								break;
							}
							currentRow--;
							currentCol++;
						}
					}
				} else {
					if (colDir > 0) {
						// King is top-right, check bottom-left diagonal
						currentRow = i + 1;
						currentCol = j - 1;
						while (currentRow < 8 && currentCol >= 0) {
							Piece piece = PieceManager.board[currentRow][currentCol];
							if (!piece.isEmpty) {
								if (piece.team != team && 
								   (piece.type.equals("Queen") || piece.type.equals("Bishop"))) {
									checkingPieces.add(piece);
								}
								break;
							}
							currentRow++;
							currentCol--;
						}
					} else {
						// King is top-left, check bottom-right diagonal
						currentRow = i + 1;
						currentCol = j + 1;
						while (currentRow < 8 && currentCol < 8) {
							Piece piece = PieceManager.board[currentRow][currentCol];
							if (!piece.isEmpty) {
								if (piece.team != team && 
								   (piece.type.equals("Queen") || piece.type.equals("Bishop"))) {
									checkingPieces.add(piece);
								}
								break;
							}
							currentRow++;
							currentCol++;
						}
					}
				}
			}

//			if(checkingPieces)
		}
		
		return false;
	}
}

