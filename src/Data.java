import java.io.Serializable;

public class Data implements Serializable{
	String command, commandStat, callerType;
	int i,j, callerI, callerJ, callerTeam, team;
	long time;
	Data(String commandStatus){
		commandStat = commandStatus;
	}
	Data(String command, int team){
		this.command = command;
		this.team = team;
		time = (System.currentTimeMillis() + 3000) / 1000L ;
	}
	Data(String command, int i, int j, int callerI, int callerJ, int callerTeam, String callerType){
//		String[] command = command
		this.command = command;
		this.i = i;
		this.j = j;
		this.callerI = callerI;
		this.callerJ = callerJ;
		this.callerTeam = callerTeam;
		this.callerType = callerType;
		
	}
	@Override
	public String toString() {
		return command;
	}
	
}
