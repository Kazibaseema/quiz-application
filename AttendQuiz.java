package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AttendQuiz extends JFrame implements ActionListener {

    JButton fetchQuizButton, submitButton, backButton;
    JTextField quizLinkField, userNameField;
    JPanel quizPanel;
    JScrollPane scrollPane;
    List<JRadioButton[]> optionsList;
    ButtonGroup[] buttonGroups;
    List<String[]> questionsList;
    List<String> userAnswers; // Store user answers
    private String quizLink; // Add this line to store the quiz link

    // Constructor for AttendQuiz page
    AttendQuiz() {
        // Set background and layout
        getContentPane().setBackground(Color.decode("#F0F8FF"));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Heading
        JLabel heading = new JLabel("Attend Quiz");
        heading.setFont(new Font("Tahoma", Font.BOLD, 28));
        heading.setForeground(Color.decode("#483D8B"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 20, 10);
        add(heading, gbc);

        // User Name label
        JLabel userNameLabel = new JLabel("User Name:");
        userNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(userNameLabel, gbc);

        // User Name input field
        userNameField = new JTextField(20);
        userNameField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(userNameField, gbc);

        // Quiz link input field
        JLabel quizLinkLabel = new JLabel("Quiz Link:");
        quizLinkLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(quizLinkLabel, gbc);

        quizLinkField = new JTextField(20);
        quizLinkField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 10, 10);
        add(quizLinkField, gbc);

        // Button panel for Fetch button
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.decode("#F0F8FF"));

        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.insets = new Insets(0, 10, 0, 10); // Symmetric space between buttons
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;

        // Fetch Quiz button
        fetchQuizButton = new JButton("Fetch Quiz");
        fetchQuizButton.setPreferredSize(new Dimension(130, 35));
        fetchQuizButton.setBackground(new Color(30, 144, 255));
        fetchQuizButton.setFont(new Font("Arial", Font.BOLD, 16));
        fetchQuizButton.setForeground(Color.WHITE);
        fetchQuizButton.setFocusPainted(false);
        fetchQuizButton.addActionListener(this);
        buttonPanel.add(fetchQuizButton, gbcButton);

        // Add space between buttons
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Horizontal space

        // Back button
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(130, 35));
        backButton.setBackground(new Color(255, 69, 0));
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(this);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(buttonPanel, gbc); // Add button panel

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(0, 128, 0));
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(this);
        submitButton.setVisible(false); // Hidden until quiz is fetched

        // Make the window full-screen when opened
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == fetchQuizButton) {
            String quizLinkInput = quizLinkField.getText(); // Get the quiz link from the input field
            String userName = userNameField.getText();

            if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your name.");
            } else if (!quizLinkInput.isEmpty()) {
                try {
                    // Fetch quiz questions and options from the provided link
                    quizLink = quizLinkInput; // Store the quiz link here
                    URL url = new URL(quizLink);
                    questionsList = loadQuizFromURL(url);
                    userAnswers = new ArrayList<>(questionsList.size());

                    // If questions are fetched, display them
                    if (questionsList != null && !questionsList.isEmpty()) {
                        displayQuiz();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Invalid quiz link!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please provide a quiz link.");
            }
        } else if (ae.getSource() == submitButton) {
            // Handle quiz submission and score calculation
            calculateScore();
        } else if (ae.getSource() == backButton) {
            setVisible(false);
            new Login(); // Assuming Login class exists
        }
    }

    // Fetch quiz content from the provided URL
    private List<String[]> loadQuizFromURL(URL url) throws IOException {
        List<String[]> quizData = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            // Split CSV line into question and its four options
            quizData.add(line.split(","));
        }
        reader.close();
        return quizData;
    }

    // Display quiz questions and their options
    private void displayQuiz() {
        // Remove previous quiz if any
        if (scrollPane != null) {
            remove(scrollPane);
        }

        // Panel to display questions and options
        quizPanel = new JPanel();
        quizPanel.setLayout(new GridLayout(questionsList.size() * 2, 1, 10, 10)); // Two rows per question
        quizPanel.setBackground(Color.decode("#F0F8FF"));

        // Radio buttons for options and button groups for each question
        optionsList = new ArrayList<>();
        buttonGroups = new ButtonGroup[questionsList.size()];

        for (int i = 0; i < questionsList.size(); i++) {
            String[] questionData = questionsList.get(i);
            String question = questionData[0];

            // Question label
            JLabel questionLabel = new JLabel("Q" + (i + 1) + ": " + question);
            questionLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
            quizPanel.add(questionLabel);

            // Options for each question
            JRadioButton[] options = new JRadioButton[4];
            buttonGroups[i] = new ButtonGroup();

            JPanel optionsPanel = new JPanel(new GridLayout(4, 1)); // Layout for options
            for (int j = 0; j < 4; j++) {
                options[j] = new JRadioButton(questionData[j + 1]); // Options start from index 1
                options[j].setFont(new Font("Tahoma", Font.PLAIN, 16));
                options[j].setBackground(Color.decode("#F0F8FF"));
                buttonGroups[i].add(options[j]);
                optionsPanel.add(options[j]);
            }
            optionsList.add(options);
            quizPanel.add(optionsPanel);
        }

        // Scrollable quiz panel
        scrollPane = new JScrollPane(quizPanel);
        scrollPane.setPreferredSize(new Dimension(500, 450)); // Set size for the scroll pane

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4; // Adjusted for new position
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        add(scrollPane, gbc);

        // Show the submit button once the quiz is fetched
        submitButton.setVisible(true);
        gbc.gridx = 0;
        gbc.gridy = 5; // Position for Submit button
        gbc.gridwidth = 1; // Reset to default gridwidth for individual buttons
        gbc.insets = new Insets(10, 10, 10, 10); // Adjust spacing
        add(submitButton, gbc);

        validate(); // Revalidate the panel
        repaint(); // Repaint the frame
    }

    private void calculateScore() {
        int score = 0;
        String[] correctAnswers = new String[questionsList.size()];
        
        for (int i = 0; i < questionsList.size(); i++) {
            String correctAnswer = questionsList.get(i)[5].trim(); // Assuming correct answer is at index 5
            correctAnswers[i] = correctAnswer; // Store the correct answer regardless of user input
    
            // Check each option for the current question
            boolean answered = false; // Track if the question was answered
            for (JRadioButton option : optionsList.get(i)) {
                if (option.isSelected()) {
                    userAnswers.add(option.getText().trim()); // Store the user's selected answer
                    answered = true; // Mark that the question was answered
                    if (option.getText().trim().equalsIgnoreCase(correctAnswer)) {
                        score++; // Increment score for correct answer
                    }
                    break; // Exit loop once the selected option is found
                }
            }
            
            if (!answered) {
                userAnswers.add("Not Attempted"); // Add placeholder if no answer was selected
            }
        }
    
        // Convert questionsList to a 2D array correctly
        String[][] questionsArray = new String[questionsList.size()][6];
        
        for (int i = 0; i < questionsList.size(); i++) {
            questionsArray[i] = questionsList.get(i);
        }
    
        // Display the score in the Score frame and save results to the database
        setVisible(false); // Hide the current frame
        new Score(userNameField.getText(), score, questionsList.size(), quizLinkField.getText(), questionsArray,
                  userAnswers.toArray(new String[0]), correctAnswers);
    }
    
    

    public static void main(String[] args) {
        new AttendQuiz();
    }
}
