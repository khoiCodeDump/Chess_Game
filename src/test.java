import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.SpringLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

public class test extends JPanel {

	/**
	 * Create the panel.
	 */
	public test() {
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JButton btnNewButton = new JButton("New button");
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 60, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 35, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton, -60, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 139, SpringLayout.WEST, this);
		add(btnNewButton);

	}
}
