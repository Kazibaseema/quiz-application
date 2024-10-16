package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends JFrame implements ActionListener {
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public Register() {
        setTitle("Register");
        setSize(400, 300);
        setLocation(500, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        add(userLabel);

        userNameField = new JTextField();
        userNameField.setBounds(150, 50, 200, 30);
        add(userNameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 100, 100, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(150, 150, 100, 30);
        registerButton.addActionListener(this);
        add(registerButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String username = userNameField.getText();
            String password = new String(passwordField.getPassword());
            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(this, "Username already taken. Please choose another one.");
            } else {
                saveUserToDatabase(username, password);
                JOptionPane.showMessageDialog(this, "User registered successfully!");
                dispose(); // Close the register window
            }
        }
    }

    private boolean isUsernameTaken(String username) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If a record is found, the username is taken
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking username.");
            return false;
        }
    }

    private void saveUserToDatabase(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error registering user.");
        }
    }

    public static void main(String[] args) {
        new Register();
    }
}
