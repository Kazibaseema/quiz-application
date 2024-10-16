/*
package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Quiz extends JFrame implements ActionListener {

    String[][] questions; // Questions with options
    String[] answers;     // Correct answers in their original order
    String[] userAnswers;

    JLabel qno, question;
    JRadioButton opt1, opt2, opt3, opt4;
    ButtonGroup options;
    JButton next, back, submit;
    public static int count = 0;
    public static int score = 0;
    String name;

    Quiz(String name) {
        this.name = name;
        userAnswers = new String[20]; // Adjust size according to the number of questions
        setBounds(50, 0, 1440, 850);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/quiz.jpg"));
        JLabel image = new JLabel(i1);
        image.setBounds(0, 0, 1440, 392);
        add(image);

        qno = new JLabel();
        qno.setBounds(100, 450, 50, 30);
        qno.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(qno);

        question = new JLabel();
        question.setBounds(150, 450, 900, 30);
        question.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(question);

        opt1 = new JRadioButton();
        opt1.setBounds(170, 520, 700, 30);
        opt1.setBackground(Color.WHITE);
        opt1.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt1);

        opt2 = new JRadioButton();
        opt2.setBounds(170, 560, 700, 30);
        opt2.setBackground(Color.WHITE);
        opt2.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt2);

        opt3 = new JRadioButton();
        opt3.setBounds(170, 600, 700, 30);
        opt3.setBackground(Color.WHITE);
        opt3.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt3);

        opt4 = new JRadioButton();
        opt4.setBounds(170, 640, 700, 30);
        opt4.setBackground(Color.WHITE);
        opt4.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(opt4);

        options = new ButtonGroup();
        options.add(opt1);
        options.add(opt2);
        options.add(opt3);
        options.add(opt4);

        next = new JButton("Next");
        next.setBounds(1100, 450, 100, 40);
        next.setFont(new Font("Tahoma", Font.PLAIN, 22));
        next.setBackground(new Color(30, 144, 255));
        next.setForeground(Color.WHITE);
        next.addActionListener(this);
        add(next);

        back = new JButton("Back");
        back.setBounds(1100, 500, 100, 40);
        back.setFont(new Font("Tahoma", Font.PLAIN, 22));
        back.setBackground(new Color(255, 69, 0));
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        submit = new JButton("Submit");
        submit.setBounds(1100, 550, 200, 40);
        submit.setFont(new Font("Tahoma", Font.PLAIN, 22));
        submit.setBackground(new Color(30, 144, 255));
        submit.setForeground(Color.WHITE);
        submit.setEnabled(false);
        submit.addActionListener(this);
        add(submit);
        loadQuestionsFromCSV("C://Users//Baseema//OneDrive//Desktop//ROSP project//Quiz-Application-Using-Java-copy//src//quiz//application//que.csv");
        //loadQuestionsFromCSV("src/quiz/application/que.csv");

        start(count);
        setVisible(true);
    }

    public Quiz(String name2, File selectedFile) {
        
    }

    private void loadQuestionsFromCSV(String filePath) {
        ArrayList<String[]> questionList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 6) { // Expecting 6 columns (Question + 4 Options + Correct Answer)
                    questionList.add(values);
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        questions = questionList.stream()
                .map(arr -> new String[]{arr[0], arr[1], arr[2], arr[3], arr[4]}) // Get question and options
                .toArray(String[][]::new);

        answers = questionList.stream()
                .map(arr -> arr[5]) // Get the correct answer
                .toArray(String[]::new);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == next) {
            recordAnswer();
            count++;
            if (count < questions.length) {
                start(count);
                back.setEnabled(true);
                if (count == questions.length - 1) {
                    next.setEnabled(false);
                    submit.setEnabled(true);
                }
            }
        } else if (ae.getSource() == back) {
            recordAnswer();
            count--;
            if (count >= 0) {
                start(count);
                next.setEnabled(true);
                if (count == 0) {
                    back.setEnabled(false);
                }
                if (count == questions.length - 1) {
                    next.setEnabled(false);
                    submit.setEnabled(true);
                } else {
                    submit.setEnabled(false);
                }
            }
        } else if (ae.getSource() == submit) {
            recordAnswer();
            calculateScore();
            setVisible(false);
            new Score(name, score, questions.length, questions, userAnswers, answers); // Pass all required data
        }
    }

    private void recordAnswer() {
        if (options.getSelection() == null) {
            userAnswers[count] = "";
        } else {
            userAnswers[count] = options.getSelection().getActionCommand();
        }
    }

    private void calculateScore() {
        score = 0; // Reset score
        int totalQuestions = answers.length;

        for (int i = 0; i < totalQuestions; i++) {
            // Check if the userAnswer is not empty and matches the correct answer
            if (userAnswers[i] != null && !userAnswers[i].isEmpty() && userAnswers[i].equals(answers[i])) {
                score++; // Increment score for each correct answer
            }
        }
    }

    public void start(int count) {
        if (count < questions.length) {
            qno.setText("" + (count + 1) + ". ");
            question.setText(questions[count][0]); // Display question text
            opt1.setText(questions[count][1]);    // Display option 1
            opt1.setActionCommand(questions[count][1]);

            opt2.setText(questions[count][2]);    // Display option 2
            opt2.setActionCommand(questions[count][2]);

            opt3.setText(questions[count][3]);    // Display option 3
            opt3.setActionCommand(questions[count][3]);

            opt4.setText(questions[count][4]);    // Display option 4
            opt4.setActionCommand(questions[count][4]);

            options.clearSelection();

            // Restore the selected option if any
            if (userAnswers[count] != null && !userAnswers[count].isEmpty()) {
                for (AbstractButton button : Collections.list(options.getElements())) {
                    if (button.getActionCommand().equals(userAnswers[count])) {
                        button.setSelected(true);
                        break;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Quiz("User");
    }
}
*/
package quiz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Quiz extends JFrame implements ActionListener {

    String userName;
    String quizLink; // Add this variable
    List<Question> questions;
    int currentQuestionIndex = 0;
    int score = 0;

    // GUI components
    JLabel questionLabel;
    JRadioButton option1, option2, option3, option4;
    ButtonGroup optionsGroup;
    JButton nextButton;

    public Quiz(String name, String quizLink, File csvFile) {
        this.userName = name;
        this.quizLink = quizLink; // Initialize quiz link
        this.questions = new ArrayList<>();

        // Load questions from CSV file
        loadQuestionsFromCSV(csvFile);

        // Setup the GUI
        setTitle("Quiz Application");
        setSize(800, 600);
        setLocation(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        questionLabel = new JLabel();
        questionLabel.setBounds(50, 50, 700, 50);
        questionLabel.setFont(new Font("Tahoma", Font.PLAIN, 24));
        add(questionLabel);

        option1 = new JRadioButton();
        option1.setBounds(50, 150, 700, 30);
        add(option1);

        option2 = new JRadioButton();
        option2.setBounds(50, 200, 700, 30);
        add(option2);

        option3 = new JRadioButton();
        option3.setBounds(50, 250, 700, 30);
        add(option3);

        option4 = new JRadioButton();
        option4.setBounds(50, 300, 700, 30);
        add(option4);

        optionsGroup = new ButtonGroup();
        optionsGroup.add(option1);
        optionsGroup.add(option2);
        optionsGroup.add(option3);
        optionsGroup.add(option4);

        nextButton = new JButton("Next");
        nextButton.setBounds(600, 400, 100, 40);
        nextButton.addActionListener(this);
        add(nextButton);

        // Display the first question
        displayQuestion(currentQuestionIndex);

        // Force GUI visibility
        setVisible(true);
    }

    // Load questions from the CSV file
    private void loadQuestionsFromCSV(File csvFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    continue;
                }
                String questionText = data[0];
                String option1 = data[1];
                String option2 = data[2];
                String option3 = data[3];
                String option4 = data[4];
                String correctAnswer = data[5].trim(); // Ensure no extra spaces

                // Add question to the list
                Question question = new Question(questionText, option1, option2, option3, option4, correctAnswer);
                questions.add(question);
                System.out.println("Loaded question: " + questionText); // Debugging: Print the loaded questions
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading CSV file.");
        }
    }

    // Display the question and options on the screen
    private void displayQuestion(int index) {
        Question question = questions.get(index);
        questionLabel.setText((index + 1) + ". " + question.getQuestionText());
        option1.setText(question.getOption1());
        option2.setText(question.getOption2());
        option3.setText(question.getOption3());
        option4.setText(question.getOption4());

        // Clear previously selected options
        optionsGroup.clearSelection();
    }

    // Handle button actions (Next)
    public void actionPerformed(ActionEvent ae) {
        // Save the user's answer and check if it's correct
        if (optionsGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Please select an option.");
            return;
        }

        String selectedOption = null;
        if (option1.isSelected()) {
            selectedOption = option1.getText();
        } else if (option2.isSelected()) {
            selectedOption = option2.getText();
        } else if (option3.isSelected()) {
            selectedOption = option3.getText();
        } else if (option4.isSelected()) {
            selectedOption = option4.getText();
        }

        // Check answer
        if (selectedOption.equals(questions.get(currentQuestionIndex).getCorrectAnswer())) {
            score++;
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion(currentQuestionIndex);
        } else {
            // Save score to the database
            saveScoreToDatabase(userName, score, quizLink); // Pass the quiz link
            JOptionPane.showMessageDialog(this, "Quiz finished! Your score: " + score + "/" + questions.size());
            dispose(); // Close the quiz window
        }
    }

    private void saveScoreToDatabase(String username, int score, String quizLink) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO scores (user_id, score, quiz_link) SELECT id, ?, ? FROM users WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, score);
            statement.setString(2, quizLink);
            statement.setString(3, username);
            statement.executeUpdate();
            System.out.println("Score saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving score.");
        }
    }

    public static void main(String[] args) {
        // Example usage of the Quiz class (assuming the CSV file is available)
        File csvFile = new File("path_to_your_csv_file.csv");
        new Quiz("User", "quiz_link_example", csvFile); // Replace with actual quiz link
    }
}
