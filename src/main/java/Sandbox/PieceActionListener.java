package Sandbox;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

public class PieceActionListener implements ActionListener {
	int i, j;
	PieceUI pieceUI;
	PieceActionListener(PieceUI pieceUI, int i, int j)
	{	
		this.i = i;
		this.j = j;
		this.pieceUI = pieceUI;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(ChessEngine.turn != Client.team) return;
		if(ChessEngine.currentSelectedPiece == null)
		{
			if(pieceUI.curPiece.isEmpty) {
				return;
			}
			if(ChessEngine.piecesLegalMoves.containsKey(pieceUI.curPiece)) {
				ChessEngine.currentSelectedPiece = pieceUI.curPiece;
			}

		}
		else 
		{
			Piece callerPiece = ChessEngine.currentSelectedPiece;
			HashSet<Integer> pieceLegalMoves = ChessEngine.piecesLegalMoves.get(callerPiece);
			if(!pieceLegalMoves.contains(i*10 + j)) {
				if(pieceUI.curPiece != Board.emptyPiece && ChessEngine.piecesLegalMoves.containsKey(pieceUI.curPiece)) ChessEngine.currentSelectedPiece = pieceUI.curPiece;
				else ChessEngine.currentSelectedPiece = null;
				return;
			}
			String callerType = callerPiece.type;
			int callerTeam = callerPiece.team;
			int callerI = callerPiece.i;
			int callerJ = callerPiece.j;
			PieceUI callerPieceUI = Board.board[callerI][callerJ];

			if(callerPiece.type.equals("King") && !callerPiece.hasMoved && callerPiece.i == 7 && Math.abs(callerPiece.j-j) == 2) 
			{
				//castling logic
				if(callerTeam == 1 && callerPiece.j == 4) { //white team
					if(this.j == 2) { //queen side
						
						Piece king = callerPiece;
						Piece rook = ChessEngine.board[0][0];
						Board.board[king.i][king.j].updatePiece(Board.emptyPiece, true, false);
						Board.board[0][0].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][3].updatePiece(rook, false, false);
						Board.board[7][2].updatePiece(king, false, false);
						UpdateTurn();

					}
					else if(this.j==6) { //king side
						Piece king = callerPiece;
						Piece rook = ChessEngine.board[7][7];
						Board.board[king.i][king.j].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][7].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][5].updatePiece(rook, false, false);
						Board.board[7][6].updatePiece(king, false, false);
						UpdateTurn();

					}
				}
				else if(callerTeam == 2 && callerJ == 3) { //black team
					if(this.j==1) { //king side
						Piece king = callerPiece;
						Piece rook = ChessEngine.board[7][0];
						Board.board[king.i][king.j].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][0].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][2].updatePiece(rook, false, false);
						Board.board[7][1].updatePiece(king, false, false);
						UpdateTurn();
					}
					else if(this.j==5) { //queen side
						Piece king = callerPiece;
						Piece rook = ChessEngine.board[7][7];
						Board.board[king.i][king.j].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][7].updatePiece(Board.emptyPiece, true, false);
						Board.board[7][4].updatePiece(rook, false, false);
						Board.board[7][5].updatePiece(king, false, false);
						UpdateTurn();

					}
				}
			}
			else if(callerType.equals("Pawn")) 
			{ 
				if(i == 0)
				{
					JDialog options = new JDialog();
					options.setLocationRelativeTo(Client.gameWindowPanel);
					options.setLayout(new GridLayout(2, 2, 0, 0));
					options.setSize(220, 200);
					options.setResizable(false);
					options.setUndecorated(true);
					JButton queen = new JButton();
					if(callerTeam ==1) {
						queen.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Queen", Color.WHITE)));
					}
					else {
						queen.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Queen", Color.BLACK)));
					}
					options.add(queen);
					queen.addActionListener( new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							options.dispose();
							pieceUI.updatePiece(new Piece("Queen", i, j, callerPiece.team), false, true);
							callerPieceUI.updatePiece(Board.emptyPiece, true, false);
							UpdateTurn();

						}
						
					});
				
					
					JButton rook = new JButton();
					if(callerTeam ==1) {
						rook.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Rook", Color.WHITE)));
					}
					else {
						rook.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Rook", Color.BLACK)));
					}
					options.add(rook);
					rook.addActionListener( new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							options.dispose();
							pieceUI.updatePiece(new Piece("Rook", i, j, callerPiece.team), false, true);
							callerPieceUI.updatePiece(Board.emptyPiece, true, false);
							UpdateTurn();

						}
						
					});
				
				
					JButton bishop = new JButton();
					if(callerTeam ==1) {
						bishop.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Bishop", Color.WHITE)));
					}
					else {
						bishop.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Bishop", Color.BLACK)));
					}
					
					options.add(bishop);
					bishop.addActionListener( new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							options.dispose();
							pieceUI.updatePiece(new Piece("Bishop", i, j, callerPiece.team), false, true);
							callerPieceUI.updatePiece(Board.emptyPiece, true, false);
							UpdateTurn();

						}
						
					});

				
				
					JButton knight = new JButton();
					if(callerTeam ==1) {
						knight.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Knight", Color.WHITE)));
					}
					else {
						knight.setIcon(new ImageIcon(pieceUI.generateChessPieceImage("Knight", Color.BLACK)));
					}
					options.add(knight);
					knight.addActionListener( new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							options.dispose();
							pieceUI.updatePiece(new Piece("Knight", i, j, callerPiece.team), false, true);
							callerPieceUI.updatePiece(Board.emptyPiece, true, false);
							UpdateTurn();

						}
						
					});
					
					options.setVisible(true);
				}
				else if(callerI == 6 && i == 4) {
					// White pawn moving two squares, setting en passant flag
					callerPiece.enPassant = true;
					pieceUI.updatePiece(callerPiece, false, false);
					callerPieceUI.updatePiece(Board.emptyPiece, true, false);
					UpdateTurn();
				}
				else if(Math.abs(j - callerJ) == 1 && Math.abs(i - callerI) == 1) {
					// Diagonal capture (including en passant)
					if(callerTeam == 1 && i == 2 && Board.pieces[i+1][j].enPassant) { //enPassant capturing
						Board.board[i+1][j].updatePiece(Board.emptyPiece, true, false);
						pieceUI.updatePiece(callerPiece, false, false);
						callerPieceUI.updatePiece(Board.emptyPiece, true, false);
						UpdateTurn();
					}
					else if(!pieceUI.curPiece.isEmpty) { // Regular diagonal capture
						pieceUI.updatePiece(callerPiece, false, false);
						callerPieceUI.updatePiece(Board.emptyPiece, true, false);
						UpdateTurn();
					}
				}
				else {
					// Normal pawn move
					pieceUI.updatePiece(callerPiece, false, false);
					callerPieceUI.updatePiece(Board.emptyPiece, true, false);
					
					UpdateTurn();
				}
			} //end if

			else {				
				pieceUI.updatePiece(callerPiece, false, false);
				callerPieceUI.updatePiece(Board.emptyPiece, true, false);
				
				UpdateTurn();

			}
			callerPiece.hasMoved = true;
		}		
	} 
	 
	public void UpdateTurn()
    {
		Board.addPosition(ChessEngine.board);

		// Check for threefold repetition
		if (Board.isThreefoldRepetition()) {
			Client.gameInfoWindow.endGame("draw");
			return;
		}
		
		ChessEngine.currentSelectedPiece = null;
    	ChessEngine.turn = (ChessEngine.turn == 1) ? 2 : 1;
		
		// Stop the timer immediately
		Client.gameInfoWindow.stopTimer(Client.team);
		Client.gameInfoWindow.startTimer(Chess_Bot.team);
		
		// Run bot calculation in separate thread
		Thread botThread = new Thread(() -> {
			ChessEngine.CheckKingSafety(Chess_Bot.King, false);
			Client.chessBot.CalculateMove();
			
			// Use SwingUtilities to update UI and resume timer on EDT
			SwingUtilities.invokeLater(() -> {
				Client.gameInfoWindow.stopTimer(Chess_Bot.team);
				Client.gameInfoWindow.startTimer(Client.team);
				Client.gameInfoWindow.updateCurTurn();
				ChessEngine.CheckKingSafety(Board.King, false);
			});
		});
		botThread.setPriority(Thread.MIN_PRIORITY);
		botThread.start();
    }
}