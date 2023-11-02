import java.io.IOException;
import java.util.HashSet;

public class Game{
	int currentTurn;
	PlayerThread player1, player2;
	HashSet<PlayerThread> lobby;
//	private int blackremainingSeconds, whiteremainingSeconds;
	Game(PlayerThread player1, PlayerThread player2){
		this.player1 = player1;
		this.player2 = player2;
		currentTurn = 1;
		try {
			player1.out.writeObject(new Data("Start", 1));
			player2.out.writeObject(new Data("Start", 2));
			player1.out.flush();
			player2.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void updateGameState(PlayerThread caller, PlayerThread toBeUpdate, Data data) {
		
		try {
//			caller.out.writeObject(new Data("Success"));
//			caller.out.flush();
			toBeUpdate.out.writeObject(data);
			toBeUpdate.out.flush();
//			caller.out.writeObject(new Data("Start Timer", currentTurn));
//			caller.out.flush();
			if(currentTurn == 1) currentTurn = 2;
			else currentTurn = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		
		}
		
	}
//	public void startGame(PlayerThread player) {
//		if(player == player1) {
//			try {
//				player2.
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		else {
//			try {
//				player1.out.writeObject(new Data("Start", 2));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
	public void endGame(PlayerThread player, Data data) {
		player1.game= null;
		player2.game = null;
		if(player == player1) {
			try {
				player2.out.writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				player1.out.writeObject(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public synchronized void startTimer() {
		try {
			player1.out.writeObject(new Data("Start Timer", currentTurn));
			player2.out.writeObject(new Data("Start Timer", currentTurn));
			player1.out.flush();
			player2.out.flush();
			if(currentTurn == 1) currentTurn = 2;
			else currentTurn = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
//	public void updateBoardAndTurn(int i, int j, int callerI, int callerJ){
//		try {
//			player2.out.writeObject(new Data("Move", i, j, callerI, callerJ));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//	}
//	public void readInputStreams(PlayerThread player) {
//		
//	}
//	public void run() {
//		System.out.println("In Game Thread");
//		try {
//			player1.out.writeInt(1);
//			player1.out.flush();
//			player2.out.writeInt(2); 
//			player2.out.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		while(player1.isAlive() && player2.isAlive()) {
//			if(currentTurn == 1) {
//				System.out.println("In turn 1");
//				try {
//					Data data = (Data) player1.in.readObject();
//					System.out.println(data.command);
//					currentTurn = 2;
//				} catch (ClassNotFoundException | IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			else {
//				System.out.println("In turn 2");
//				try {
//					Data data = (Data) player2.in.readObject();
//					System.out.println(data.command);
//					currentTurn = 1;
//				} catch (ClassNotFoundException | IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
	
}
