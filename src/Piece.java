
public class Piece{
	
	int i, j;
	String type;
	int team;
	boolean isEmpty;
	boolean enPassant;
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
	
	
}