import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class loadingTest extends JPanel {

	/**
	 * Create the panel.
	 */
	public loadingTest() {
		
		JLabel lblNewLabel = new JLabel("New label");
		ClassLoader cldr = this.getClass().getClassLoader();
		java.net.URL imageURL   = cldr.getResource("C:\\Users\\khoiv\\Downloads\\Rhombus.gif");
		ImageIcon imageIcon = new ImageIcon(imageURL);
//		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\khoiv\\Downloads\\Rhombus.gif"));
		lblNewLabel.setIcon(imageIcon);
		imageIcon.setImageObserver(lblNewLabel);

		add(lblNewLabel);

	}

}
