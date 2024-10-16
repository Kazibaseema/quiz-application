package quiz.application;

import javax.swing.*;
import java.awt.*;

public class ReviewAnswers extends JFrame {

    ReviewAnswers(String name, int score, String quizLink, String[][] questions, String[] userAnswers, String[] correctAnswers) {
        // Set to full screen
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(800, 100 * questions.length + 100)); // Adjust height based on number of questions

        JLabel heading = new JLabel("Review Your Answers");
        heading.setBounds(50, 30, 600, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(new Color(30, 144, 255));
        panel.add(heading);

        int y = 80;
        for (int i = 0; i < questions.length; i++) {
            JLabel questionLabel = new JLabel((i + 1) + ". " + questions[i][0]);
            questionLabel.setBounds(50, y, 700, 30);
            questionLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
            panel.add(questionLabel);

            // Show user answer or "Not Answered" if no answer was given
            String userAnswerText = (userAnswers[i] != null && !userAnswers[i].isEmpty()) ? userAnswers[i] : "Not Answered";
            JLabel userAnswerLabel = new JLabel("Your Answer: " + userAnswerText);
            userAnswerLabel.setBounds(50, y + 30, 700, 30);
            userAnswerLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            panel.add(userAnswerLabel);

            // Always show the correct answer
            JLabel correctAnswerLabel = new JLabel("Correct Answer: " + correctAnswers[i]);
            correctAnswerLabel.setBounds(50, y + 60, 700, 30);
            correctAnswerLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            panel.add(correctAnswerLabel);

            y += 100; // Adjust spacing between questions
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        JLabel scoreLabel = new JLabel("Your Score: " + score + "/" + questions.length);
        scoreLabel.setBounds(50, y + 20, 300, 30);
        scoreLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        panel.add(scoreLabel);

        JButton backButton = new JButton("Back");
        backButton.setBounds(50, y + 60, 100, 40);
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 22));
        backButton.setBackground(new Color(30, 144, 255));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> {
            setVisible(false);
            new Score(name, score, questions.length, quizLink, questions, userAnswers, correctAnswers); // Pass the quiz link
        });
        panel.add(backButton);

        // Center the window on the screen
        setLocationRelativeTo(null);
        setVisible(true); // Show the frame
    }
}
