import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class PieceManager 
{
	static Piece[][] board;
	static HashMap<Piece, HashSet<Integer>> piecesLegalMoves; //this contains all the possible pieces that can move
	static Piece currentSelectedPiece; 
	static boolean castle;
    static HashSet<Piece> enpassantList;
	static int turn;
	static Piece King;
	public static void Initialize()
	{
		castle = true;
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
	private static HashSet<Integer> GetLegalMoves(Piece piece, int team)
	{
		return GetLegalMoves(piece.i, piece.j, piece.type, team);
	}
	private static HashSet<Integer> GetLegalMoves(int i, int j, String type, int team)
	{
		HashSet<Integer> legalMoves = new HashSet<>();
		switch(type)
		{
			case "King":
				if(i+1 < 8 && board[i+1][j].team != team) {
	        		legalMoves.add( (i+1)*10 + j);
	        	}
	        	if(i-1 > -1 && board[i-1][j].team != team) {
	        		legalMoves.add( (i-1)*10 + j);
	        	}
	        	if(j-1 > -1 && board[i][j-1].team != team) {
	        		legalMoves.add( (i)*10 + j-1);
	        	}
	        	if(j+1 < 8 && board[i][j+1].team != team) {
	        		legalMoves.add( (i)*10 + j+1);
	        	}
	        	
	        	if(i+1 <8 && j+1 < 8 && board[i+1][j+1].team != team) {
	        		legalMoves.add( (i+1)*10 + j+1);
	        	}
	        	if(i-1 > -1 && j-1 > -1 && board[i-1][j-1].team != team) {
	        		legalMoves.add( (i-1)*10 + j-1);
	        	}
	        	if(i+1 <8 && j-1 > -1 && board[i+1][j-1].team != team) {
	        		legalMoves.add( (i+1)*10 + j-1);
	        	}
	        	if(i-1 > -1 && j+1 < 8 && board[i-1][j+1].team != team) {
	        		legalMoves.add( (i-1)*10 + j+1);
	        	}
	        	
	        	if( castle) {

	        		if(board[i][j-1].isEmpty && board[i][j-2].isEmpty) {
	        			
	        			if(performChecks(i, j-1, board[i][j].team).size() == 0 && performChecks(i, j-2, board[i][j].team).size() == 0) {
	    	        		legalMoves.add( (i)*10 + j-2);
	        			}
	        			
	        		}
	        		if(board[i][j+1].isEmpty && board[i][j+2].isEmpty) {
	        			
	        			if(performChecks(i, j+1, board[i][j].team).size() == 0 && performChecks(i, j+2, board[i][j].team).size() == 0) {
	    	        		legalMoves.add( (i)*10 + j+2);
	        			}
	        			
	        		}
	        	}
	        	HashSet<Integer> filteredLegalMoves = new HashSet<>();
	        	for(Integer position : legalMoves)
	        	{
	        		int row = position/10;
	        		int col = position - row*10;
	        		if(performChecks(row, col, team).size() == 0) filteredLegalMoves.add(position);
	        	}
	        	return filteredLegalMoves;
			case "Queen":
				for(int a=i+1; a < 8; a++) {
	        		if(board[a][j].team==0) {
		        		legalMoves.add( (a)*10 + j);
	        		}
	        		else if(board[a][j].team != team) {
		        		legalMoves.add( (a)*10 + j);
		        		break;
	        		}
	        		else if(board[a][j].team == team) {
	        			break;
	        		} 
	        		
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(board[a][j].isEmpty) {
		        		legalMoves.add( (a)*10 + j);
	        		}
	        		else if(board[a][j].team != team) {
		        		legalMoves.add( (a)*10 + j);
		        		break;
	        		}
	        		else if(board[a][j].team == team) {
	        			break;
	        		}        		
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		
	        		if(board[i][a].isEmpty) {
		        		legalMoves.add( (i)*10 + a);
	        		}
	        		else if(board[i][a].team != team) {
		        		legalMoves.add( (i)*10 + a);
		        		break;
	        		}
	        		else if(board[i][a].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		
	        		if(board[i][a].isEmpty) {
		        		legalMoves.add( (i)*10 + a);
	        		}
	        		else if(board[i][a].team != team) {
		        		legalMoves.add( (i)*10 + a);
		        		break;
	        		}
	        		else if(board[i][a].team == team) {
	        			break;
	        		}      

	        	}	        	
	        	
	        	
	        	for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);

		        		break;
	        		}
	        		else if(board[a][b].team == team) {

	        			break;
	        		}      

	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
				break;
			case "Rook":
				for(int a=i+1; a < 8; a++) {
	        		if(board[a][j].team==0) {
		        		legalMoves.add( (a)*10 + j);
	        		}
	        		else if(board[a][j].team != team) {
		        		legalMoves.add( (a)*10 + j);
		        		break;
	        		}
	        		else if(board[a][j].team == team) {
	        			break;
	        		} 
	        		
	        	}
	        	for(int a=i-1; a > -1 ; a--) {
	        		if(board[a][j].isEmpty) {
		        		legalMoves.add( (a)*10 + j);
	        		}
	        		else if(board[a][j].team != team) {
		        		legalMoves.add( (a)*10 + j);
		        		break;
	        		}
	        		else if(board[a][j].team == team) {
	        			break;
	        		}        		
	        	}
	        	for(int a=j-1; a > -1; a--) {
	        		
	        		if(board[i][a].isEmpty) {
		        		legalMoves.add( (i)*10 + a);
	        		}
	        		else if(board[i][a].team != team) {
		        		legalMoves.add( (i)*10 + a);
		        		break;
	        		}
	        		else if(board[i][a].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=j+1; a < 8; a++) {
	        		
	        		if(board[i][a].isEmpty) {
		        		legalMoves.add( (i)*10 + a);
	        		}
	        		else if(board[i][a].team != team) {
		        		legalMoves.add( (i)*10 + a);
		        		break;
	        		}
	        		else if(board[i][a].team == team) {
	        			break;
	        		}      

	        	}	        	
				break;
			case "Bishop":
				for(int a=i+1, b=j+1; a <8 && b < 8; a++, b++) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);

		        		break;
	        		}
	        		else if(board[a][b].team == team) {

	        			break;
	        		}      

	        	}
	        	for(int a=i-1, b=j-1; a > -1 && b > -1; a--, b--) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i+1, b=j-1; a <8 && b > -1; a++, b--) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
	        	for(int a=i-1, b=j+1; a > -1 && b < 8; a--, b++) {
	        		
	        		if(board[a][b].isEmpty) {
		        		legalMoves.add( (a)*10 + b);
	        		}
	        		else if(board[a][b].team != team) {
		        		legalMoves.add( (a)*10 + b);
		        		break;
	        		}
	        		else if(board[a][b].team == team) {
	        			break;
	        		}      

	        	}
				break;
			case "Knight":
				if(i+2 < 8 && j + 1 < 8 && board[i+2][j+1].team != team) {
	        		legalMoves.add( (i+2)*10 + j+1);

	            }
	            if(i+2 < 8 && j - 1 > -1 && board[i+2][j-1].team != team) {
	        		legalMoves.add( (i+2)*10 + j-1);
	            }
	            if(i-2 > -1 && j + 1 < 8 && board[i-2][j+1].team != team) {
	        		legalMoves.add( (i-2)*10 + j+1);
	            }
	            if(i-2 > -1 && j -1 > -1 && board[i-2][j-1].team != team) {
	        		legalMoves.add( (i-2)*10 + j-1);
	            }
	            if(i+1 < 8 && j + 2 < 8 && board[i+1][j+2].team != team) {
	        		legalMoves.add( (i+1)*10 + j+2);
	            }
	            if(i-1 >-1 && j + 2 < 8 && board[i-1][j+2].team != team) {
	        		legalMoves.add( (i-1)*10 + j+2);
	            }
	            if(i+1 < 8 && j - 2 > -1 && board[i+1][j-2].team != team) {
	        		legalMoves.add( (i+1)*10 + j-2);
	            }
	            if(i-1 >-1 && j - 2 > -1 && board[i-1][j-2].team != team) {
	        		legalMoves.add( (i-1)*10 + j-2);
	            }
				break;
			case "Pawn":
				if(i==6) {
					if(board[i-2][j].isEmpty) { //checking if the piece moving to is empty
		        		legalMoves.add( (i-2)*10 + j);
	        		}
				}
				if(board[i-1][j].isEmpty) {
	        		legalMoves.add( (i-1)*10 + j);
				}
				if(j+1 < 8 && board[i-1][j+1].team != 0 && board[i-1][j+1].team != team) {
	        		legalMoves.add( (i-1)*10 + j+1);
				}
				if(j-1 > -1 && board[i-1][j-1].team != 0 && board[i-1][j-1].team != team) {
	        		legalMoves.add( (i-1)*10 + j-1);
				}

				if(j - 1 > -1 && board[i][j-1].enPassant && board[i-1][j-1].isEmpty){
	        		legalMoves.add( (i)*10 + j-1);
				}
				if(j + 1 < 8 && board[i][j+1].enPassant && board[i-1][j+1].isEmpty){
	        		legalMoves.add( (i-1)*10 + j+1);
				}
				break;
		}
		return legalMoves;
	}
	private static HashSet<Integer> GetLegalMoves(Piece piece, Piece checkingPiece)
	{
		HashSet<Integer> checkingRoute = GetCheckingRoute(King.i, King.j, checkingPiece);
		HashSet<Integer> pieceLegalMoves = GetLegalMoves(piece, piece.team);
		
		HashSet<Integer> legalMoves = new HashSet<>();
		for(int position : pieceLegalMoves)
		{
			if(checkingRoute.contains(position))
			{
				legalMoves.add(position);
			}
		}
		return legalMoves;
	}
	public static HashSet<Integer> GetLegalMoves(int row, int col, String type, int team, int checkingRow, int checkingCol)
	{
		HashSet<Integer> checkingRoute = GetCheckingRoute(row, col, checkingRow, checkingCol);
		HashSet<Integer> pieceLegalMoves = GetLegalMoves(row, col, type , team);
		
		HashSet<Integer> legalMoves = new HashSet<>();
		for(int position : pieceLegalMoves)
		{
			if(checkingRoute.contains(position))
			{
				legalMoves.add(position);
			}
		}
		return legalMoves;
	}
	public static void checkForCheckMate() { //this is called every time the opponent makes a move
		
		piecesLegalMoves.clear();
		piecesLegalMoves.put(Board.emptyPiece, null);
		int team = King.team;
		List<Piece> checkingPieces = performChecks(King.i, King.j, team);
		if(checkingPieces.size() == 0) // since there are no checking pieces, the king and other team pieces can move
		{
			for(int i=0; i<8; i++) {
				for(int j=0; j<8; j++) {
					if(board[i][j].isEmpty) continue;
					Piece piece = board[i][j];
					if(piece.team != Board.playerTeam) continue;
//					Board.board[i][j].setBackground(null);
					HashSet<Integer> pieceLegalMoves = GetLegalMoves(piece, piece.team);
					if(pieceLegalMoves.size() > 0)
					{
//						if(Client.team == 2 && board[i][j].type.equals("Pawn"))
//						{
//							System.out.println(Client.team + ":" + board[i][j].type);
//							for(Integer num : pieceLegalMoves)
//							{
//								System.out.println(num);
//							}
//						}
						
						piecesLegalMoves.put(piece, pieceLegalMoves);
						
//						Board.board[i][j].setBackground(Color.green);
					}
				}
			}
			return;
		}
//		System.out.println(checkingPieces.size());
		//King is in check
		HashSet<Integer> legalMoves = GetLegalMoves(King, team);
		if(legalMoves.size() > 0) //King is in check but is able to move
		{
			piecesLegalMoves.put(King, legalMoves);
			if(checkingPieces.size() == 1) //if there is only 1 checking piece, then there are other team pieces that can either kill or block the checking piece
			{
				Piece checkingPiece = checkingPieces.iterator().next();
				HashSet<Piece> legalPieces = GetMoveablePiecesForCheckingRoute(checkingPiece);
				for(Piece piece : legalPieces)
				{
					piecesLegalMoves.put(piece, GetLegalMoves(piece, checkingPiece)); //the legal moves here should only include moves that can block or kill the checking piece
				}
			}
			// if there are more than 1 checking piece, then can only move the king
		}
		
		else if(legalMoves.size() == 0) // the king can not move
		{
			if(checkingPieces.size() > 1)
			{
				//checkmate
				Client.gameInfoWindow.endGame("lose");
			}
			else //this mean that there is only 1 checking piece
			{
				System.out.println("King is in check with no positions to move to in order to avoid the check, checking if other pieces can block or kill the checking route");
				Piece checkingPiece = checkingPieces.iterator().next();
				
				if(checkingPiece.type.equals("Knight")) // if it is a knight, then nothing can block it. However, need to check if any pieces can kill this piece.
				{
					List<Piece> legalPieces = performChecks(checkingPiece.i, checkingPiece.j, (checkingPiece.team == 1) ? 2 : 1);
					if(legalPieces.size() == 0)
					{
						//checkmate
						Client.gameInfoWindow.endGame("lose");
					}
					else 
					{
						for(Piece piece : legalPieces)
						{
							piecesLegalMoves.put(piece, GetLegalMoves(piece, checkingPiece)); //the legal moves here should only include moves that can block or kill the checking piece
						}
					}
				}
				else { // checking piece is not a knight
					HashSet<Piece> legalPieces = GetMoveablePiecesForCheckingRoute(checkingPieces.iterator().next());

					if(legalPieces.size() == 0) //if there are no pieces that can kill the checking piece, end game
					{
						Client.gameInfoWindow.endGame("lose");
					}
					else
					{
						for(Piece piece : legalPieces)
						{
							piecesLegalMoves.put(piece, GetLegalMoves(piece, checkingPiece)); //the legal moves here should only include moves that can block or kill the checking piece
						}
					}
				}
			}
		}
	}
	private static HashSet<Integer> GetCheckingRoute(int row, int col, Piece checkingPiece)
	{
		return GetCheckingRoute(row, col, checkingPiece.i, checkingPiece.j);
	}
	private static HashSet<Integer> GetCheckingRoute(int row, int col, int checkingPieceRow, int checkingPieceCol)
	{
		//includes checking piece. Excludes piece[row][col]
		HashSet<Integer> checkingRoute = new HashSet<>();
		checkingRoute.add(checkingPieceRow*10 + checkingPieceCol);

		int row_diff = checkingPieceRow-row;
		int col_diff = checkingPieceCol-col;
		int row_diff_abs = Math.abs(row_diff);
		int col_diff_abs = Math.abs(col_diff);
		if(row_diff_abs==col_diff_abs) { //this means the king is diagonal relative to the piece.
			//checking piece to king.
			if(col_diff < 0 && row_diff < 0 ) {
				int startPointX = checkingPieceRow;
				int startPointY = checkingPieceCol;
				for(; startPointX < row && startPointY < col; startPointX++, startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );
				}
			}
			//king to checking piece
			else if(col_diff > 0 && row_diff > 0){
				int startPointX = row+1;
				int startPointY = col+1;
				for(; startPointX < 8 && startPointY < 8; startPointX++, startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );
				}
			}
			//checking piece to king
			else if(col_diff < 0 && row_diff > 0) { 
				int startPointX = checkingPieceRow;
				int startPointY = checkingPieceCol;
				for(; startPointX < row && startPointY < col; startPointX--, startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );

				}
			}
			//king to checking piece
			else {
				int startPointX = row-1;
				int startPointY = col+1;
				for(; startPointX > -1 && startPointY < 8; startPointX-- , startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );

				}
			}
		}
		else if(col_diff==0) {
			//checking piece to king. The checking piece is inclusive
			if(row_diff < 0) {
				int startPointX = checkingPieceRow;
				int startPointY = col;
				for(; startPointX < row ; startPointX++) {
					checkingRoute.add(startPointX*10 + startPointY );

				}
			}
			else {
				//king to checking piece. King is exclusive
				int startPointX = row+1;
				int startPointY = col;
				for(; startPointX < 8 ; startPointX++) {
					checkingRoute.add(startPointX*10 + startPointY );

				}
			}
		}
		else if(row_diff==0) {
			if(col_diff < 0) {
				//checking piece to king. Checking piece is inclusive
				int startPointX = row;
				int startPointY = checkingPieceCol;
				for(; startPointY < col; startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );
				}
			}
			else {
				//king to checking piece. King is exclusive
				int startPointX = row;
				int startPointY = col+1; 
				for(; startPointY < 8; startPointY++) {
					checkingRoute.add(startPointX*10 + startPointY );

				}
			}
		}
		return checkingRoute;
	}
	private static HashSet<Piece> GetMoveablePiecesForCheckingRoute(Piece checkingPiece)
	{
		HashSet<Integer> checkingRoute = GetCheckingRoute(King.i, King.j, checkingPiece);
		HashSet<Piece> legalPieces = new HashSet<>();
		for(int position : checkingRoute) {
			int row = position/10;
			int col = position - row*10;
			legalPieces.addAll(performChecks(row, col, checkingPiece.team));
		}
		return legalPieces;
	}
	public static List<Piece> performChecks(int row, int col, int kingTeam) {
		// returns positions of checking pieces
		List<Piece> checkingPieces = new ArrayList<>();
		Piece curPieceVert = null;
		//Vertical
		//Top -> king[row][col]
		for(int i=0; i<row; i++) {
			
			if(board[i][col].isEmpty) continue;
			curPieceVert = board[i][col];
		}
		if(curPieceVert != null) {
			if(curPieceVert.team != kingTeam && (curPieceVert.type.equals("Queen") || curPieceVert.type.equals("Rook"))) {
				checkingPieces.add(curPieceVert);
				
			}
		}
		//Vertical
		//king[row+1][col] -> bottom
		
		for(int i=row+1; i < 8; i++) {
			if(board[i][col].isEmpty) continue;
			if(board[i][col].team == kingTeam) break;
			
			if(board[i][col].team != kingTeam ) {
				if(board[i][col].type.equals("Queen") || board[i][col].type.equals("Rook")) {
					checkingPieces.add(board[i][col]);
				}
				else if(board[i][col].type.equals("Pawn"))
				{
					if( board[row][col].isEmpty)
					{
						if(i == row+1 || (i == 6 && board[i-1][col].isEmpty && board[i-2][col] == board[row][col]) ) checkingPieces.add(board[i][col]);
					}
				}
				break;
			}
			
		}		
		
		Piece curPieceHorz = null;

		//Horizontal
		//Left -> Right
		for(int j=0; j<col; j++) {

			if(board[row][j].isEmpty) continue;
			curPieceHorz = board[row][j];
				
		}
		if(curPieceHorz != null) {
			if(curPieceHorz.team != kingTeam && (curPieceHorz.type.equals("Queen") || curPieceHorz.type.equals("Rook"))) {
				checkingPieces.add(curPieceHorz);
			
			}
		}
		for(int j=col+1; j < 8; j++) {
			if(board[row][j].isEmpty) continue;
			if(board[row][j].team == kingTeam) break;
			
			if(board[row][j].team != kingTeam ) {
				if(board[row][j].type.equals("Queen") || board[row][j].type.equals("Rook")) {
					checkingPieces.add(board[row][j]);
				}
				break;
			}
			
		}		
				
		//Diagonal check
				
		//Checking piece is Bottom-Right. This direction, pawns do not matter since it can not create a check
		 //do not remove this line
		for(int i=row+1, j=col+1; i<8 && j <8; i++, j++) {
			if(board[i][j].isEmpty) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam) { 
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop") ) {
					checkingPieces.add(board[i][j]);
				}
				break;
			}
		}
		
		//Checking piece is Bottom-Left
		for(int i=row+1, j=col-1; i < 8 && j > -1; i++, j--) {
			if(board[i][j].isEmpty) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop")) {
					checkingPieces.add(board[i][j]);
				}
				break;
			}
		}
				
		//Checking piece is Top-left
		for(int i=row-1, j=col-1; i > -1 && j > -1; i--, j--) {
			if(board[i][j].isEmpty) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop")) {
					checkingPieces.add(board[i][j]);
				}
				else if(board[i][j].type.equals("Pawn") && board[i-1][j-1] == board[row][col])
				{
					checkingPieces.add(board[i][j]);
				}
				break;
			}
		}
						
		//Checking piece is Top-right	
		for(int i=row-1, j=col+1; i > -1 && j < 8; i--, j++) {
			if(board[i][j].isEmpty) continue;
			if(board[i][j].team == kingTeam) break;
			else if(board[i][j].team != kingTeam ){
				if(board[i][j].type.equals("Queen") || board[i][j].type.equals("Bishop") ) {
					checkingPieces.add(board[i][j]);
				}
				else if(board[i][j].type.equals("Pawn") && board[i-1][j+1] == board[row][col])
				{
					checkingPieces.add(board[i][j]);
				}
				break;
			}
		}
		
		//Knight check
		int[][] possibleCombs = { {2,1}, {-2,-1}, {-2,1}, {2,-1}, {1,2}, {-1,-2}, {1,-2}, {-1,2}};
		for(int[] comb : possibleCombs) {
			int i=row+comb[0];
			int j=col+comb[1];
			try {
				if(board[i][j].type.equals("Knight") && board[i][j].team != kingTeam) {
					checkingPieces.add(board[row][j]);
				}	
			} catch(Exception e) {
				continue;
			}
			
		}
		return checkingPieces;
		
	}
}
