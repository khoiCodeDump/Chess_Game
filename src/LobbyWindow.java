import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class LobbyWindow extends JPanel {

//	private static final long serialVersionUID = 1L;
	int time = 0;
	Timer queueTimer;
	JLabel timeLabel ;
	JButton queueUp, vsBot;
	public LobbyWindow(ObjectInputStream in, ObjectOutputStream out) {
		setBounds(100, 100, 450, 300);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(hexToColor("312E2B"));
		
		queueUp = new JButton("Queue");
		queueUp.setBackground(hexToColor("#779952"));
		queueUp.setForeground(Color.white);
		queueUp.setFocusable(false);
		queueUp.setSize(100, 80);
		
		
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
		queueUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(queueUp.getText().equals("Queue")) {
						out.writeObject(new Data("Queue", -1));
						out.flush();
						queueUp.setText("Cancel");
						queueTimer.start();
					}
					else {
						out.writeObject(new Data("Cancel", -1));
						out.flush();
						queueUp.setText("Queue");
						resetTimer();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		vsBot = new JButton("Play vs Bot as White");
		vsBot.setBackground(hexToColor("#779952"));
		vsBot.setForeground(Color.white);
		vsBot.setFocusable(false);
		vsBot.setSize(100, 80);
		vsBot.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{				
				Client.CreateBoard(1, new Chess_Bot(2) );
				Chess_Bot.SetBoard(Board.pieces);
			}
			
		});
		
		add(timeLabel, BorderLayout.SOUTH);
//		add(queueUp);
		add(vsBot);
	}
	public void resetTimer() {
		queueTimer.stop();
		time = 0;
		timeLabel.setText("");
		queueUp.setText("Queue");
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
