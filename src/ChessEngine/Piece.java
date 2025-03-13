package ChessEngine;
public class Piece{
	
	public int i;
	public int j;
	public String type;
	public int team;
	public boolean isEmpty;
	public boolean enPassant;
	public int originalI;
	public int originalJ;
	public boolean hasMoved;
	public Piece() {
		isEmpty = true;
		team = 0;
		type = "";
	}

	public Piece(String type, int i, int j, int team){
		this.type = type;
		this.team = team;
		this.i = i;
		this.j = j;
		 
	}
	
	
}

