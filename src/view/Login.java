package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    public Login() {
        // Set the title of the window
        setTitle("KUP Login");

        // Create a container to hold the components
        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        // Add padding to the container
        ((JComponent) container).setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create a panel for the title
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("KUP");
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 36));
        titlePanel.add(titleLabel);

        // Create a panel for the login form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add labels and text fields to the form
        JLabel idLabel = new JLabel("아이디");
        JTextField idField = new JTextField(20);
        JLabel pwLabel = new JLabel("패스워드");
        JPasswordField pwField = new JPasswordField(20);

        // Set preferred size for text fields
        Dimension fieldSize = new Dimension(150, 25);
        idField.setPreferredSize(fieldSize);
        pwField.setPreferredSize(fieldSize);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(pwLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(pwField, gbc);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton checkButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(checkButton);
        buttonPanel.add(cancelButton);

        // Add the panels to the container
        container.add(titlePanel, BorderLayout.NORTH);
        container.add(formPanel, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the initial size and minimum size of the window
        Dimension initialSize = new Dimension(500, 400);
        setSize(initialSize);
        setMinimumSize(initialSize);

        setMaximumSize(new Dimension(600, 500));

        // Center the window
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Create and display the login window
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
