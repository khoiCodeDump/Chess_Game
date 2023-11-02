import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class Lobby {
	HashSet<PlayerThread> allPlayersInLobby ;
	Queue<PlayerThread> gameQueue;
	ArrayList<Game> allGames;
	Lobby(){
		allPlayersInLobby = new HashSet<>();
		gameQueue = new LinkedList<>();
		allGames= new ArrayList<>();
	}
	public void addPlayer(PlayerThread player) {
		allPlayersInLobby.add(player);
	}
	public synchronized void queue(PlayerThread player) {
		if(gameQueue.isEmpty()) gameQueue.add(player);
		else {
			PlayerThread player1 = gameQueue.remove();
			Game game = new Game(player1, player);
			player1.setGame(game);
			player.setGame(game);
			allGames.add(game);
//			try {
//				player1.out.writeObject(new Data("Start", -1));
//				player.out.writeObject(new Data("Start", -1));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
	}
	public synchronized void dequeue(PlayerThread player) {
		gameQueue.remove(player);
	}
}
