import java.awt.Color;
import java.util.Arrays;

public class Chess_Bot 
{
	Piece[][] pieces;
	int team; 
	int[] PieceToMove, PositionToMove;
	boolean pawnPromoTion;
//	pawn = 1, knight = 3, bishop = 3.2, rook = 5, queen = 9, negative values for black.
	public Chess_Bot(int team)
	{
		this.team = team;
	}
	public void SetBoard(Board board)
	{
		pieces = board.pieces;
	}
	public void CalculateMove()
	{
		MinMaxAlgorithm(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		Board.board[PieceToMove[0]][PieceToMove[1]].setBackground(Color.GREEN);
		Board.board[PositionToMove[0]][PositionToMove[1]].setBackground(Color.GREEN);
//		UpdateGameUI();
	}
	public void UpdateGameUI()
	{
		//1 is white, 2 is black
		
		PieceUI callerPieceUI = Board.board[PieceToMove[0]][PieceToMove[1]];
		Piece callerPiece = callerPieceUI.curPiece;
		if(callerPiece.type.equals("Pawn"))
		{
			if(callerPiece.i == 6 && PositionToMove[0] == 4) callerPiece.enPassant = true;
		}
		else if(callerPiece.type.equals("Rook") || callerPiece.type.equals("King")) { PieceManager.castle = false;}
		Board.board[PositionToMove[0]][PositionToMove[1]].updatePiece(callerPiece, false, false);
		callerPieceUI.updatePiece(Board.emptyPiece, true, false);
		
//		Board.board[PositionToMove[0]][PositionToMove[1]].revalidate();
//		Board.board[PositionToMove[0]][PositionToMove[1]].repaint();
//		callerPieceUI.revalidate();
//		callerPieceUI.repaint();
	}
	public int EvaluateGameState()
	{
		
		int totalScore = 0;
		int pawnStructureScore = 0;
		int centerControlScore = 0;
		int mobilityScore = 0;

		// Piece values and position evaluation
		for(int i=0; i<pieces.length; i++) {
			for(int j=0; j<pieces[0].length; j++) {
				if (pieces[i][j].isEmpty) continue;
				
				int pieceValue = 0;
				int positionBonus = 0;
				
				// Base piece values
				switch(pieces[i][j].type) {
					case "King":
						pieceValue = 200;
						// Encourage king safety in corners during middlegame/endgame
						positionBonus = isEndgame() ? 0 : evaluateKingSafety(i, j);
						break;
					case "Queen":
						pieceValue = 900;
						// Bonus for controlling center
						positionBonus = evaluateCenterControl(i, j);
						break;
					case "Rook":
						pieceValue = 500;
						// Bonus for rooks on open files
						positionBonus = evaluateRookPosition(i, j);
						break;
					case "Bishop":
						pieceValue = 330;
						// Bonus for bishops on long diagonals
						positionBonus = evaluateBishopPosition(i, j);
						break;
					case "Knight":
						pieceValue = 320;
						// Knights are stronger in closed positions
						positionBonus = evaluateKnightPosition(i, j);
						break;
					case "Pawn":
						pieceValue = 100;
						// Evaluate pawn structure
						positionBonus = evaluatePawnStructure(i, j);
						pawnStructureScore += positionBonus;
						break;
				}
				
				// Apply the score with proper sign based on piece team
				int finalScore = (pieceValue + positionBonus);
				totalScore += (pieces[i][j].team == team) ? finalScore : -finalScore;
				
			}
		}

		int finalScore = totalScore + pawnStructureScore + centerControlScore + mobilityScore;

		return finalScore;
	}

	private int evaluateKingSafety(int i, int j) {
		// Bonus for king being in corner during early/middle game
		if ((i == 0 || i == 7) && (j == 0 || j == 7)) {
			return 30;
		}
		return 0;
	}

	private int evaluateCenterControl(int i, int j) {
		// Bonus for controlling center squares
		if ((i == 3 || i == 4) && (j == 3 || j == 4)) {
			return 20;
		}
		return 0;
	}

	private int evaluateRookPosition(int i, int j) {
		// Bonus for rooks on open files
		int bonus = 0;
		boolean openFile = true;
		for (int row = 0; row < 8; row++) {
			if (row != i && pieces[row][j].type.equals("Pawn")) {
				openFile = false;
				break;
			}
		}
		return openFile ? 20 : 0;
	}

	private int evaluateBishopPosition(int i, int j) {
		// Bonus for bishops controlling long diagonals
		if ((i + j == 7) || (i == j)) {
			return 15;
		}
		return 0;
	}

	private int evaluateKnightPosition(int i, int j) {
		// Knights are stronger in center positions
		if ((i >= 2 && i <= 5) && (j >= 2 && j <= 5)) {
			return 20;
		}
		return 0;
	}

	private int evaluatePawnStructure(int i, int j) {
		int bonus = 0;
		
		// Fix team comparison for pawn advancement
		if (team == 1) { // White
			bonus += (7 - i) * 10; // More advanced = higher bonus
		} else { // Black
			bonus += i * 10;
		}
		
		// Penalty for doubled pawns
		boolean doubled = false;
		for (int row = 0; row < 8; row++) {
			if (row != i && pieces[row][j].type.equals("Pawn") 
				&& pieces[row][j].team == pieces[i][j].team) {
				doubled = true;
				break;
			}
		}
		if (doubled) {
			bonus -= 20;
		}
		
		return bonus;
	}

	private boolean isEndgame() {
		int pieceCount = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (!pieces[i][j].isEmpty && !pieces[i][j].type.equals("Pawn") 
					&& !pieces[i][j].type.equals("King")) {
					pieceCount++;
				}
			}
		}
		return pieceCount <= 6; // Arbitrary threshold for endgame
	}

	public int MinMaxAlgorithm(int depthCount, boolean maximizingPlayer, int alpha, int beta)
	{
		// Add debug output
		
		if(depthCount == 3)
		{
			int eval = EvaluateGameState();
			return eval;
		}
		int maxEval = Integer.MIN_VALUE;
		int minEval = Integer.MAX_VALUE;
		for(int i=0; i<pieces.length; i++)
		{
			for(int j=0; j<pieces[0].length; j++)
			{
				if(pieces[i][j].team == team && maximizingPlayer)
				{
					int num = Integer.MIN_VALUE;
					switch(pieces[i][j].type)
					{
						case "King": //King
							num = PerformMove("King", depthCount, i, j, true, alpha, beta);
							break;
						case "Queen":  //Queen
							num = PerformMove("Queen", depthCount, i, j, true, alpha, beta);
							break;
						case "Rook":	 //Rook
							num = PerformMove("Rook", depthCount, i, j, true, alpha, beta);
							break;
						case "Bishop":  //Bishop
							num = PerformMove("Bishop", depthCount, i, j, true, alpha, beta);
							break;
						case "Knight":  //Knight
							num = PerformMove("Knight", depthCount, i, j, true, alpha, beta);
							break;
						case "Pawn":  //Pawn
							num = PerformMove("Pawn", depthCount, i, j, true, alpha, beta);
							break;
					}
					if(num > maxEval)
					{
						maxEval = num;
						PieceToMove = new int[]{i, j};
					}
					if(alpha < num)
					{
						alpha = num;
					}
					
					if(beta <= alpha)
					{
						break;
					}
				}
				else if(!maximizingPlayer && pieces[i][j].team != team)
				{
					int num = Integer.MAX_VALUE;
					switch(pieces[i][j].type)
					{
						case "King": //King
							num = PerformMove("King", depthCount+1, i, j, false, alpha, beta);
							break;
						case "Queen":  //Queen
							num = PerformMove("Queen", depthCount+1, i, j, false, alpha, beta);
							break;
						case "Rook":	 //Rook
							num = PerformMove("Rook", depthCount+1, i, j, false, alpha, beta);
							break;
						case "Bishop":  //Bishop
							num = PerformMove("Bishop", depthCount+1, i, j, false, alpha, beta);
							break;
						case "Knight":  //Knight
							num = PerformMove("Knight", depthCount+1, i, j, false, alpha, beta);
							break;
						case "Pawn":  //Pawn
							num = PerformMove("Pawn", depthCount+1, i, j, false, alpha, beta);
							break;
					}
					minEval = Math.min(minEval, num);
					beta = Math.min(num, beta);
					if(beta <= alpha)
					{
						break;
					}
				}			
			}
				
		}
		return (maximizingPlayer) ? maxEval : minEval;
	}
	
	public boolean ValidateMove(int i, int j)
	{
		if(i < 0 || i >= 8 || j < 0 || j >= 8) return false;
		if(pieces[i][j].team == team) return false;
		return true;
	}
	public int PerformMove(String type, int depthCount, int i, int j, boolean maximizingPlayer, int alpha, int beta)
	{
		Piece temp = pieces[i][j];
		pieces[i][j] = Board.emptyPiece;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		switch(type)
		{
			case "King": //King
				
				if(ValidateMove(i + 1, j))
				{
					Piece tempPiece = pieces[i+1][j];
					pieces[i+1][j] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j};
							}
						}						
					}
					else min = Math.min(num, min);
					pieces[i+1][j] = tempPiece;
				}
				if(ValidateMove(i - 1, j))
				{
					Piece tempPiece = pieces[i-1][j];
					pieces[i-1][j] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-1, j};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-1][j] = tempPiece;
				}
				if(ValidateMove(i, j + 1))
				{
					Piece tempPiece = pieces[i][j+1];
					pieces[i][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i][j+1] = tempPiece;
				}
				if(ValidateMove(i, j - 1))
				{
					Piece tempPiece = pieces[i][j-1];
					pieces[i][j-1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i][j-1] = tempPiece;
				}
				if(ValidateMove(i + 1, j + 1))
				{
					Piece tempPiece = pieces[i+1][j+1];
					pieces[i+1][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j+1] = tempPiece;
				}
				if(ValidateMove(i - 1, j + 1))
				{
					Piece tempPiece = pieces[i-1][j+1];
					pieces[i-1][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-1, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-1][j+1] = tempPiece;
				}
				if(ValidateMove(i + 1, j - 1))
				{
					Piece tempPiece = pieces[i+1][j-1];
					pieces[i+1][j-1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j-1] = tempPiece;
				}
				if(ValidateMove(i - 1, j - 1))
				{
					Piece tempPiece = pieces[i-1][j-1];
					pieces[i-1][j-1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-1, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-1][j-1] = tempPiece;
				}
				break;
			case "Queen":  //Queen
				for(int a=i+1; a < 8; a++) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, j};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][j] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, j};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][j] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {i, a};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[i][a] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {i, a};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[i][a] = tempPiece;
	        		}
	        		else break;
	        	}	        	
	        	for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
				break;
			case "Rook":	 //Rook
				for(int a=i+1; a < 8; a++) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, j};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][j] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, j};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][j] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {i, a};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[i][a] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {i, a};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[i][a] = tempPiece;
	        		}
	        		else break;
	        	}	        
				break;
			case "Bishop":  //Bishop
				for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {a, b};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[a][b] = tempPiece;
	        		}
	        		else break;
	        	}
				break;
			case "Knight":  //Knight
				if(ValidateMove(i+2, j + 1)) {
					Piece tempPiece = pieces[i+2][j+1];
					pieces[i+2][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+2, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+2][j+1] = tempPiece;
	            }
	            if(ValidateMove(i+2, j - 1)) {
	            	Piece tempPiece = pieces[i+2][j-1];
	            	pieces[i+2][j-1] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+2, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+2][j-1] = tempPiece;
	            }
	            if(ValidateMove(i-2, j + 1)) {
	            	Piece tempPiece = pieces[i-2][j+1];
	            	pieces[i-2][j+1] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-2, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-2][j+1] = tempPiece;
	            }
	            if(ValidateMove(i-2, j -1)) {
	            	Piece tempPiece = pieces[i-2][j-1];
	            	pieces[i-2][j-1] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-2, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-2][j-1] = tempPiece;
	            }
	            if(ValidateMove(i+1, j + 2 )) {
	            	Piece tempPiece = pieces[i+1][j+2];
	            	pieces[i+1][j+2] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j+2};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j+2] = tempPiece;
	            }
	            if(ValidateMove(i-1, j + 2)) {
	            	Piece tempPiece = pieces[i-1][j+2];
	            	pieces[i-1][j+2] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-1, j+2};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-1][j+2] = tempPiece;
	            }
	            if(ValidateMove(i+1, j - 2) ) {
	            	Piece tempPiece = pieces[i+1][j-2];
	            	pieces[i+1][j-2] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j-2};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j-2] = tempPiece;
	            }
	            if(ValidateMove(i-1, j - 2 )) {
	            	Piece tempPiece = pieces[i-1][j-2];
	            	pieces[i-1][j-2] = temp;
	            	int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i-1, j-2};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i-1][j-2] = tempPiece;
	            }
				break;
			case "Pawn":  //Pawn
//				if(i==6) {
//					if(ValidateMove(i-2, j, false) && pieces[i-1][j] == 0)
//					{
//						pieces[i-2][j] = temp;
//						num = MinMaxAlgorithm(depthCount, true, alpha, beta);
//						pieces[i-2][j] = 0;
//					}
//				}
				if(i==1) {
					if(ValidateMove(i+2, j) && pieces[i+2][j].isEmpty && pieces[i+1][j].isEmpty)
					{
						Piece tempPiece = pieces[i+2][j];
						pieces[i+2][j] = temp;
						int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
						if(maximizingPlayer)
						{
							if(max < num)
							{
								max = num;
								if(depthCount == 0) 
								{
									PositionToMove = new int[] {i+2, j};
								}
							}
						}
						else min = Math.min(num, min);
						pieces[i+2][j] = tempPiece;
					}
				}
				if(ValidateMove(i+1, j) && pieces[i+1][j].isEmpty) {
					Piece tempPiece = pieces[i+1][j];
					pieces[i+1][j] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j] = tempPiece;
				}
				if(ValidateMove(i+1,j+1) && !pieces[i+1][j+1].isEmpty) {
					Piece tempPiece = pieces[i+1][j+1];
					pieces[i+1][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j+1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j+1] = tempPiece;
				}
				if(ValidateMove(i+1, j-1)  && !pieces[i+1][j-1].isEmpty) {
					Piece tempPiece = pieces[i+1][j-1];
					pieces[i+1][j-1] = temp;
					int num = MinMaxAlgorithm(depthCount+1, !maximizingPlayer, alpha, beta);
					if(maximizingPlayer)
					{
						if(max < num)
						{
							max = num;
							if(depthCount == 0) 
							{
								PositionToMove = new int[] {i+1, j-1};
							}
						}
					}
					else min = Math.min(num, min);
					pieces[i+1][j-1] = tempPiece;
				}
//				if( j+1 < 8 && pieces[i-1][j+1].team == 0 &&  pieces[i-1][j-1].enPassante) {
//					
//				}
//				if(j-1 > -1 && pieces[i-1][j-1].team == 0 && pieces[i-1][j+1].enPassante)	{
//					
//				}
				break;
		}
		pieces[i][j] = temp;
//		if(PositionToMove != null) 
//		{
//			PieceToMove = new int[] {i, j};
//			if(maximizingPlayer) {
//				Board.board[PieceToMove[0]][PieceToMove[1]].setBackground(Color.GREEN);
//				Board.board[PositionToMove[0]][PositionToMove[1]].setBackground(Color.RED);
//			}
//			else {
//				Board.board[PieceToMove[0]][PieceToMove[1]].setBackground(Color.GRAY);
//				Board.board[PositionToMove[0]][PositionToMove[1]].setBackground(Color.BLUE);
//			}
//
//		}
		return (maximizingPlayer) ? max : min;
	}
}
