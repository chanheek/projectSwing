package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Set the title of the window
        setTitle("KUP Main Screen");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(1024, 768);

        // Create a panel for the left menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Add buttons or labels to the menu panel
        String[] menuItems = {"학적 관리", "일정 관리"};
        for (String item : menuItems) {
            JLabel label = new JLabel(item);
            label.setFont(new Font("Serif", Font.BOLD, 20));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            menuPanel.add(label);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space between labels
        }

        // Create a panel for the main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.decode("#1565C0")); // Set background color
        contentPanel.setLayout(new GridBagLayout());

        // Create a panel for the table
        JPanel tablePanel = new JPanel();
        tablePanel.setBackground(Color.decode("#E57373")); // Set background color
        tablePanel.setLayout(new GridLayout(1, 4, 20, 0)); // Set layout with gaps between columns
        tablePanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Add labels to the table panel
        String[] columnNames = {"학번", "이름", "학년", "전공"};
        for (String columnName : columnNames) {
            JLabel columnLabel = new JLabel(columnName, SwingConstants.CENTER);
            columnLabel.setFont(new Font("Serif", Font.BOLD, 20));
            columnLabel.setOpaque(true);
            columnLabel.setBackground(new Color(224, 224, 224));
            columnLabel.setPreferredSize(new Dimension(150, 100));
            tablePanel.add(columnLabel);
        }

        // Set the layout of the content panel to center the table panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(tablePanel, gbc);

        // Set the layout of the frame and add panels
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Center the window
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Create and show the main frame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
