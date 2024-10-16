package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login extends JFrame implements ActionListener {

    JButton createQuizButton, attendQuizButton;

    // Constructor for Login page
    Login() {
        // Set background color and layout manager
        getContentPane().setBackground(Color.decode("#F0F8FF"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Image at the top
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/login.jpeg"));
        JLabel image = new JLabel(i1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 20, 0); // Add some spacing below the image
        add(image, gbc);

        // Welcome heading
        JLabel heading = new JLabel("Welcome to the Quiz");
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(Color.decode("#483D8B"));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0); // Add some spacing below the heading
        add(heading, gbc);

        // Create Quiz button
        createQuizButton = new JButton("Create Quiz");
        createQuizButton.setBackground(new Color(0, 128, 0));
        createQuizButton.setFont(new Font("Arial", Font.BOLD, 20));
        createQuizButton.setForeground(Color.WHITE);
        createQuizButton.setFocusPainted(false);
        createQuizButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 20, 20); // Spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make button fill horizontally
        add(createQuizButton, gbc);

        // Attend Quiz button
        attendQuizButton = new JButton("Attend Quiz");
        attendQuizButton.setBackground(new Color(30, 144, 255));
        attendQuizButton.setFont(new Font("Arial", Font.BOLD, 20));
        attendQuizButton.setForeground(Color.WHITE);
        attendQuizButton.setFocusPainted(false);
        attendQuizButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 20, 20, 0); // Spacing between buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(attendQuizButton, gbc);

        // Set frame properties
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize window to full screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Action handling for both buttons
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == createQuizButton) {
            setVisible(false); // Hide the current window
            new CreateQuiz(); // Open the CreateQuiz window
        } else if (ae.getSource() == attendQuizButton) {
            setVisible(false); // Hide the current window
            new AttendQuiz(); // Open the AttendQuiz window
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        new Login();
    }
}
