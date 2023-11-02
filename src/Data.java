import java.io.Serializable;

public class Data implements Serializable{
	String command, commandStat, callerType;
	int i,j, callerI, callerJ, callerTeam, team, pawnPromo;
	Data(String commandStatus){
		commandStat = commandStatus;
	}
	Data(String command, int team){
		this.command = command;
		this.team = team;
	}
	Data(String command, int i, int j, int callerI, int callerJ, int callerTeam, String callerType, int pawnPromo){
//		String[] command = command
		this.command = command;
		this.i = i;
		this.j = j;
		this.callerI = callerI;
		this.callerJ = callerJ;
		this.callerTeam = callerTeam;
		this.callerType = callerType;
		this.pawnPromo = pawnPromo;
	}
	
	@Override
	public String toString() {
		return command;
	}
	
}
