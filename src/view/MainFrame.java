package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        // Set the title of the window
        setTitle("KUP Management System");
        setSize(800,1000);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the size of the window
        setSize(1200, 1010);

        // Create a panel for the left menu
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setLayout(new GridLayout(2, 1));

        // Add buttons or labels to the menu panel
        JLabel label1 = new JLabel("학적 관리", SwingConstants.CENTER);
        JLabel label2 = new JLabel("일정 관리", SwingConstants.CENTER);
        label1.setFont(new Font("Serif", Font.BOLD, 20));
        label2.setFont(new Font("Serif", Font.BOLD, 20));

        label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(contentPanel, "defaultPanel");
            }
        });
        label2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cardLayout.show(contentPanel, "calendarPanel");
            }
        });

        menuPanel.add(label1);
        menuPanel.add(label2);

        // Create a panel for the main content area
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // Create a panel for the default content
        JPanel defaultPanel = new JPanel();
        defaultPanel.setBackground(new Color(70, 130, 180)); // Blue color from the image
        defaultPanel.setLayout(new GridBagLayout());

        // Add a label with "KUP" text to the default content panel
        JLabel contentLabel = new JLabel("KUP", SwingConstants.CENTER);
        contentLabel.setFont(new Font("Serif", Font.BOLD, 50));
        contentLabel.setForeground(Color.BLACK); // Black color
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(20, 0, 20, 0);
        defaultPanel.add(contentLabel, gbc);

        // Add text fields for 학번, 이름, 학년, 전공 to the default content panel
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField yearField = new JTextField(10);
        JTextField majorField = new JTextField(10);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        defaultPanel.add(new JLabel("학번"), gbc);
        gbc.gridx = 1;
        defaultPanel.add(idField, gbc);

        gbc.gridx = 2;
        defaultPanel.add(new JLabel("이름"), gbc);
        gbc.gridx = 3;
        defaultPanel.add(nameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        defaultPanel.add(new JLabel("학년"), gbc);
        gbc.gridx = 1;
        defaultPanel.add(yearField, gbc);

        gbc.gridx = 2;
        defaultPanel.add(new JLabel("전공"), gbc);
        gbc.gridx = 3;
        defaultPanel.add(majorField, gbc);

        // Add panels to the content panel
        contentPanel.add(defaultPanel, "defaultPanel");
        contentPanel.add(new CalendarPanel(), "calendarPanel");

        // Set the layout of the frame and add panels
        setLayout(new BorderLayout());
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Show the default panel initially
        cardLayout.show(contentPanel, "defaultPanel");
    }

    public static void main(String[] args) {
        // Create and show the main frame
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
