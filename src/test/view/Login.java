package test.view;

import db.DBConnection;
import test.dao.UserDao;
import test.dao.UserDaoImpl;
import test.vo.EventCalendarVo;
import test.vo.UserVo;
import view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Login extends JFrame {
    private JTextField idField;
    private JPasswordField pwField;

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
        idField = new JTextField(20);
        JLabel pwLabel = new JLabel("패스워드");
        pwField = new JPasswordField(20);

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
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin();
            }
        });

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

    private void checkLogin() {
        int id = Integer.parseInt(idField.getText());
        String password = new String(pwField.getPassword());

        String call = "{CALL validate_user(?, ?, ?)}";

        Connection connection = null;
        CallableStatement statement = null;

        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareCall(call);

            statement.setInt(1, id);
            statement.setString(2, password);
            statement.registerOutParameter(3, Types.INTEGER);

            statement.execute();

            UserVo userVo = new UserVo();
            EventCalendarVo eventCalendarVo = new EventCalendarVo();

            int userExists = statement.getInt(3);

            if (userExists > 0) {

                userVo.setLoginId(id);
                eventCalendarVo.setUsersLoginId(id);

                UserDaoImpl userDao = new UserDaoImpl(connection);
                userDao.readUser(userVo);

                JOptionPane.showMessageDialog(this, "로그인 성공!");

                // 로그인 창을 닫고 MainScreen을 띄웁니다
                this.dispose(); // 현재 로그인 창을 닫습니다
                SwingUtilities.invokeLater(() -> {
                    new MainFrame().setVisible(true); // MainFrame을 엽니다
                });
            } else {
                JOptionPane.showMessageDialog(this, "아이디나 비밀번호를 다시 입력해주세요.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 연결에 문제가 발생했습니다.");
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // Create and display the login window
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}
