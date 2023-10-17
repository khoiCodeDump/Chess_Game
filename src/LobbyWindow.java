import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class LobbyWindow extends JFrame {

	private JPanel contentPane;
	int time = 0;
	Timer queueTimer;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					LobbyWindow frame = new LobbyWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public LobbyWindow(ObjectInputStream in, ObjectOutputStream out) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		
		JButton queueUp = new JButton("Play");
		
		JLabel timeLabel = new JLabel();
		queueTimer = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int minutes = time / 60;
			    int seconds = time % 60;
				// TODO Auto-generated method stub
				 String timeF = String.format("%02d:%02d", minutes, seconds); 
				 timeLabel.setText(timeF);
				 time++;
			}
			
		});
		queueUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					if(queueUp.getText().equals("Play")) {
						out.writeObject(new Data("Queue", -1));
						out.flush();
						queueUp.setText("Cancel");
						queueTimer.start();
					}
					else {
						out.writeObject(new Data("Cancel", -1));
						out.flush();
						queueUp.setText("Play");
						queueTimer.stop();
						time = 0;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		
			
		contentPane.add(timeLabel);
		contentPane.add(queueUp);
	}
	public void resetTimer() {
		queueTimer.stop();
		time = 0;
		
	}
}
