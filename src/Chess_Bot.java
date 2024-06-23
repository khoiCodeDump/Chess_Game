
public class Chess_Bot 
{
	Piece[][] pieces;
	int team; 
	int[] PieceToMove, PositionToMove;
	boolean pawnPromoTion;
//	pawn = 1, knight = 3, bishop = 3.2, rook = 5, queen = 9, negative values for black.
	public Chess_Bot(Board board, int team)
	{
		pieces = board.pieces;
		this.team = team;
	}
	public void CalculateMove()
	{
		MinMaxAlgorithm(0, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		UpdateGameUI();
	}
	public void UpdateGameUI()
	{
		//0 is white, 1 is black
		
		PieceUI callerPieceUI = Board.board[PieceToMove[0]][PieceToMove[1]];
		Piece callerPiece = callerPieceUI.curPiece;
		if(callerPiece.type.equals("Pawn"))
		{
			if(callerPiece.i == 6 && PositionToMove[0] == 4) callerPiece.enPassant = true;
		}
		else if(callerPiece.type.equals("Rook") || callerPiece.type.equals("King")) { PieceManager.castle = false;}
		Board.board[PositionToMove[0]][PositionToMove[1]].updatePiece(callerPiece, false, false);
		callerPieceUI.updatePiece(Board.emptyPiece, true, false);
		
		
		
	}
	public int EvaluateGameState()
	{
		int totalScore = 0;
		for(int i=0; i<pieces.length; i++)
		{
			for(int j=0; j< pieces[0].length; j++)
			{
				switch(pieces[i][j].type)
				{
					case "King":
						totalScore += (pieces[i][j].team == team) ? 200 : -200;
						break;
					case "Queen":
						totalScore += (pieces[i][j].team == team) ? 9 : -9;
						break;
					case "Rook":
						totalScore += (pieces[i][j].team == team) ? 5 : -5;
						break;
					case "Bishop":
						totalScore += (pieces[i][j].team == team) ? 4 : -4;
						break;
					case "Knight":
						totalScore += (pieces[i][j].team == team) ? 3 : -3;
						break;
					case "Pawn":
						if(i==0) //Calculate score for pawn promotion
						{
							
						}
						totalScore += (pieces[i][j].team == team) ? 1 : -1;
						break;
						
				}
			}
		}
		
		return totalScore;
	}	
	public int MinMaxAlgorithm(int depthCount, boolean maximizingPlayer, int alpha, int beta)
	{
		if(depthCount == 3)
		{
			return EvaluateGameState();
		}
		int maxEval = Integer.MIN_VALUE;
		int minEval = Integer.MAX_VALUE;
		for(int i=0; i<pieces.length; i++)
		{
			for(int j=0; j<pieces[0].length; j++)
			{
				if(pieces[i][j].team == team && maximizingPlayer)
				{
					int num = 0;
					switch(pieces[i][j].type)
					{
						case "King": //King
							num = PerformMove("King", depthCount+1, i, j, true, alpha, beta);
							break;
						case "Queen":  //Queen
							num = PerformMove("Queen", depthCount+1, i, j, true, alpha, beta);
							break;
						case "Rook":	 //Rook
							num = PerformMove("Rook", depthCount+1, i, j, true, alpha, beta);
							break;
						case "Bishop":  //Bishop
							num = PerformMove("Bishop", depthCount+1, i, j, true, alpha, beta);
							break;
						case "Knight":  //Knight
							num = PerformMove("Knight", depthCount+1, i, j, true, alpha, beta);
							break;
						case "Pawn":  //Pawn
							num = PerformMove("Pawn", depthCount+1, i, j, true, alpha, beta);
							break;
					}
					
					maxEval = Math.max(maxEval, num);
					if(alpha < num)
					{
						alpha = num;
						if(depthCount == 0)
						{
							PieceToMove = new int[] {i, j};
						}
					}
					
					if(beta <= alpha)
					{
						break;
					}
				}
				else 
				{
					int num = 0;
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
						max = Math.max(num, max);
						
					}
					else min = Math.min(num, min);
					pieces[i+1][j] = tempPiece;
				}
				if(ValidateMove(i - 1, j))
				{
					Piece tempPiece = pieces[i-1][j];
					pieces[i-1][j] = temp;
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}	        	
	        	for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
				break;
			case "Rook":	 //Rook
				for(int a=i+1; a < 8; a++) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(ValidateMove(a, j))
	        		{
	        			Piece tempPiece = pieces[a][j];
	        			pieces[a][j] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		if(ValidateMove(i, a))
	        		{
	        			Piece tempPiece = pieces[i][a];
	        			pieces[i][a] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}	        
				break;
			case "Bishop":  //Bishop
				for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		if(ValidateMove(a, b))
	        		{
	        			Piece tempPiece = pieces[a][b];
	        			pieces[a][b] = temp;
	        			int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	        	}
				break;
			case "Knight":  //Knight
				if(ValidateMove(i+2, j + 1)) {
					Piece tempPiece = pieces[i+2][j+1];
					pieces[i+2][j+1] = temp;
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
	            	int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					if(ValidateMove(i+2, j) && pieces[i+2][j].isEmpty)
					{
						Piece tempPiece = pieces[i+2][j];
						pieces[i+2][j] = temp;
						int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
					int num = MinMaxAlgorithm(depthCount, !maximizingPlayer, alpha, beta);
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
		return (maximizingPlayer) ? max : min;
	}
}
