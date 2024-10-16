package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Score extends JFrame {
    private String userName;
    private int score;
    private int totalQuestions;
    private String quizLink;
    private String[][] questions;
    private String[] userAnswers;
    private String[] correctAnswers;

    // Constructor for Score page
    Score(String name, int score, int totalQuestions, String quizLink, String[][] questions, String[] userAnswers,
          String[] correctAnswers) {
        this.userName = name;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.quizLink = quizLink;
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.correctAnswers = correctAnswers;

        setTitle("Quiz Score");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to full screen
        setUndecorated(true); // Remove the title bar and borders
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Setting background color
        getContentPane().setBackground(new Color(255, 250, 240)); // Soft background color

        // Displaying Score heading
        JLabel scoreLabel = new JLabel("Your Score: " + score + "/" + totalQuestions);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 60)); // Increased font size
        scoreLabel.setForeground(new Color(0, 128, 0)); // Dark green color for score
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(50, 20, 20, 20);
        add(scoreLabel, gbc);

        // Displaying results
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 22)); // Slightly increased font size
        resultsArea.setBackground(new Color(255, 255, 255)); // White background for results
        resultsArea.setMargin(new Insets(10, 10, 10, 10));
        resultsArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1)); // Blue border
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        // Build the results string
        StringBuilder results = new StringBuilder();
        for (int i = 0; i < questions.length; i++) {
            String userAnswerText = (userAnswers[i] != null && !userAnswers[i].isEmpty()) ? userAnswers[i] : "Not Answered";
            results.append("Q").append(i + 1).append(": ").append(questions[i][0]).append("\n")
                   .append("Your Answer: ").append(userAnswerText).append("\n")
                   .append("Correct Answer: ").append(correctAnswers[i]).append("\n\n");
        }

        resultsArea.setText(results.toString());

        // Use a JScrollPane for better results display
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Make the scroll pane bigger
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED); // Add scrollbar if needed
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // No horizontal scrollbar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.weighty = 1; // Allow the scroll pane to take up more vertical space
        gbc.fill = GridBagConstraints.BOTH; // Allow the scroll pane to resize properly
        add(scrollPane, gbc);

        // Back button to go back to the quiz
        JButton backButton = new JButton("Back to Quiz");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setBackground(new Color(0, 102, 204));
        backButton.setFont(new Font("Arial", Font.BOLD, 24));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AttendQuiz(); // Navigate back to the AttendQuiz
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 20, 30, 20);
        gbc.weighty = 0; // Reset weight for the button
        gbc.fill = GridBagConstraints.HORIZONTAL; // Keep button horizontal
        add(backButton, gbc);

        // Footer message
        JLabel footerLabel = new JLabel("Thank you for participating!");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 28)); // Increased font size
        footerLabel.setForeground(new Color(128, 128, 128)); // Gray color for footer
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 20, 20);
        add(footerLabel, gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true); // Make the frame visible

        saveQuizResults(name, score, quizLink, userAnswers, correctAnswers);
    }

    private void saveQuizResults(String userName, int score, String quizLink, String[] userAnswers, String[] correctAnswers) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection(); // Get database connection
            String insertQuery = "INSERT INTO quiz_results (username, score, quiz_link, user_answers, correct_answers) VALUES (?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

            preparedStatement.setString(1, userName);
            preparedStatement.setInt(2, score);
            preparedStatement.setString(3, quizLink);
            preparedStatement.setString(4, String.join(",", userAnswers)); // Convert array to comma-separated string
            preparedStatement.setString(5, String.join(",", correctAnswers)); // Convert array to comma-separated string

            int rowsAffected = preparedStatement.executeUpdate(); // Execute the insert
            if (rowsAffected > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Insertion failed.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Print any SQL exceptions
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close(); // Close connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Score("User", 2, 21, "quizLink", new String[][] {{"Question 1?"}, {"Question 2?"}}, new String[] {"Answer 1", "Not Answered"}, new String[] {"Correct Answer 1", "Correct Answer 2"}); // For testing
    }
}
