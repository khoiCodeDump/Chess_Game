package Network;
import java.io.Serializable;

public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String command, type;
	int i,j, callerI, callerJ;
	int team;
	boolean pawnPromo, isEmpty, enPassant;
	public Data(String command, int team){
		this.command = command;
		this.team = team;
	}
	public Data(String command, int i, int j, int callerI, int callerJ){
		this.command = command;
		this.i = i;
		this.j = j;
		this.callerI = callerI;
		this.callerJ = callerJ;
		if(command.equals("En Passant")) enPassant = true;

	}
	public Data(String command, int i, int j){
		this.command = command;
		this.i = i;
		this.j = j;
		isEmpty = true;
	}
	public Data(String command, int i, int j, String type){
		this.command = command;
		this.i = i;
		this.j = j;
		this.type = type;
		pawnPromo = true;
	}
	@Override
	public String toString() {
		return command;
	}
	
}
