package Sandbox;

import java.util.HashSet;

public class Piece{
	
	int i, j;
	String type;
	int team;
	boolean isEmpty;
	boolean enPassant;
	public int originalI;
	public int originalJ;
	public boolean hasMoved;
	public HashSet<Integer> legalMoves;
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
		
		// Get direction of route to king
		int rowDiff = i - Board.King.i;
		int colDiff = j - Board.King.j;
		
		// Now get the checking route
		boolean isValidLine = false;
		
		// Check if it's a straight line (rook-like movement)
		if (rowDiff == 0 || colDiff == 0) {
			isValidLine = true;
		}
		// Check if it's a diagonal line (bishop-like movement)
		else if (Math.abs(rowDiff) == Math.abs(colDiff)) {
			isValidLine = true;
		}

		if(isValidLine)
		{
			// Get unit direction (-1, 0, or 1 for each component)
			int rowDir = Integer.compare(rowDiff, 0);
			int colDir = Integer.compare(colDiff, 0);
			
			HashSet<Integer> route = ChessEngine.GetRouteOnDir( new int[]{rowDir, colDir}, this);
			if(!route.isEmpty())
			{
				this.legalMoves = route;
				return true;
			}
		}
		
		return false;
	}
}

