package Sandbox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class LobbyWindow extends JPanel {

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	private static final long serialVersionUID = 1L;
	int time = 0;
	Timer queueTimer;
	JLabel timeLabel ;
	JButton vsBotRand, vsBotasWhite, vsBotasBlack;
	public LobbyWindow() {
		setBounds(100, 100, 450, 300);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(hexToColor("312E2B"));
		
		timeLabel = new JLabel();
		timeLabel.setForeground(Color.WHITE);
		queueTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int minutes = time / 60;
			    int seconds = time % 60;
				 String timeF = String.format("%02d:%02d", minutes, seconds); 
				 timeLabel.setText(timeF);
				 time++;
			}
			
		});
		
		vsBotRand = new JButton("Play vs Bot as Random");
		vsBotRand.setBackground(hexToColor("#779952"));
		vsBotRand.setForeground(Color.white);
		vsBotRand.setFocusable(false);
		vsBotRand.setSize(100, 80);
		vsBotRand.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{				
				int playerTeam = (Math.random() < 0.5) ? 1 : 2;
				int botTeam = (playerTeam == 1) ? 2 : 1;
				Client.CreateBoard(playerTeam, new Chess_Bot(botTeam));
				Chess_Bot.SetBoard(Board.pieces);
				if(playerTeam == 2)
				{
					Client.chessBot.CalculateMove();
				}
			}
			
		});
		
		vsBotasWhite = new JButton("Play vs Bot as White");
		vsBotasWhite.setBackground(hexToColor("#779952"));
		vsBotasWhite.setForeground(Color.white);
		vsBotasWhite.setFocusable(false);
		vsBotasWhite.setSize(100, 80);
		vsBotasWhite.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{				
				Client.CreateBoard(1, new Chess_Bot(2) );
				Chess_Bot.SetBoard(Board.pieces);
			}
			
		});
		
		vsBotasBlack = new JButton("Play vs Bot as Black");
		vsBotasBlack.setBackground(hexToColor("#779952"));
		vsBotasBlack.setForeground(Color.white);
		vsBotasBlack.setFocusable(false);
		vsBotasBlack.setSize(100, 80);
		vsBotasBlack.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{				
				Client.CreateBoard(2, new Chess_Bot(1) );
				Chess_Bot.SetBoard(Board.pieces);
				Thread botThread = new Thread(() -> {
					Client.chessBot.CalculateMove();
					
					// Use SwingUtilities to update UI and resume timer on EDT
					SwingUtilities.invokeLater(() -> {
						Client.gameInfoWindow.stopTimer(Chess_Bot.team);
						Client.gameInfoWindow.startTimer(Client.team);
						Client.gameInfoWindow.updateCurTurn();
					});
				});
				botThread.setPriority(Thread.MIN_PRIORITY);
				botThread.start();
			}
			
		});
		
		add(timeLabel, BorderLayout.SOUTH);
		add(vsBotRand);
		add(vsBotasWhite);
		add(vsBotasBlack);

	}
	public void resetTimer() {
		queueTimer.stop();
		time = 0;
		timeLabel.setText("");
	}
	public Color hexToColor(String hex) {
        // Remove the "#" symbol if present
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        // Parse the hex string to get RGB values
        int red = Integer.parseInt(hex.substring(0, 2), 16);
        int green = Integer.parseInt(hex.substring(2, 4), 16);
        int blue = Integer.parseInt(hex.substring(4, 6), 16);

        // Create and return the Color object
        return new Color(red, green, blue);
    }
}
