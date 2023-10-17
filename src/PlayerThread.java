

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class PlayerThread extends Thread {
	ObjectInputStream in;
	ObjectOutputStream out;
	Lobby lobby;
	Game game;
	PlayerThread(Socket s, Lobby lobby) throws IOException{
		in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());
		this.lobby = lobby;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public void run() {
		try {
			while(true) {
				try {
					Data data = (Data)in.readObject();
					System.out.println("PlayerThread " + this.getId() + " " + data.command);
					if(data.command.equals("Queue")) {
						lobby.queue(this);
//						while(true) {
//							if(gameQueue.isEmpty()) gameQueue.add(this);
//							else if(gameQueue.peek() != this){
//								PlayerThread secondPlayer = gameQueue.poll();
//								game = new Game(this, secondPlayer);
//								gameQueue.remove(this);
//
//								break;
//							}
//						}
						
					}
					else if(data.command.equals("Move")) {
//						System.out.println("In move" + " PlayerThread " + this.getId() );
//						System.out.println(game);
//						System.out.println(data.callerTeam);
//						if(data.callerTeam == game.currentTurn) {
//							System.out.println("PlayerThread" + this.getId() + "updating the other player board");
							if(game.player1.getId() == this.getId()) game.updateGameState(this, game.player2, data);
							else game.updateGameState(this, game.player1, data);							
//						}
						
					}
//					else if(data.command.equals("Start")) {
//						out.writeObject(new Data("Start", 2));
//					}
					else if(data.command.equals("End")) {
						game.endGame(this);
					}
					else if(data.command.equals("Cancel")){
						lobby.dequeue(this);
					}
					
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("PlayerThread error");
		}
	}
	
}
