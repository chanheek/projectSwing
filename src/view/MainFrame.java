package view;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        // Set the title of the window
        setTitle("Java Swing Layout Example");

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(800, 600);

        // Create a panel for the left menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Add buttons or labels to the menu panel
        JLabel label1 = new JLabel("학적 관리");
        JLabel label2 = new JLabel("일정 관리");
        label1.setFont(new Font("Serif", Font.PLAIN, 20));
        label2.setFont(new Font("Serif", Font.PLAIN, 20));
        menuPanel.add(label1);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add space between labels
        menuPanel.add(label2);

        // Create a panel for the main content area
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.LIGHT_GRAY);
        contentPanel.setLayout(new BorderLayout());

        // Add a label with "KUP" text to the content panel
        JLabel contentLabel = new JLabel("KUP", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Serif", Font.PLAIN, 50));
        contentLabel.setForeground(new Color(200, 200, 200)); // Light gray color
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        // Set the layout of the frame and add panels
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Create and show the main frame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}