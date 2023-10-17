import javax.swing.*;
import java.awt.*;


public class HistoryList extends JPanel{
	JList<String> list;
    HistoryList(){

      
        // Create a new JList object with a custom model
        DefaultListModel<String> model = new DefaultListModel<String>();
        list = new JList<>(model);

        // Create a new JScrollPane object to contain the list
        JScrollPane scrollPane = new JScrollPane(list);

        // Create a new JButton object to add new cells to the list
        // Add a new cell to the model
        model.addElement("New Item " + (model.getSize() + 1));
        
        // Add the scroll pane and button to the frame
        add(scrollPane, BorderLayout.CENTER);

        // Set the size and visibility of the frame
        setSize(300, 300);
        setVisible(true);
    }

   
}
