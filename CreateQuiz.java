package quiz.application;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

public class CreateQuiz extends JFrame implements ActionListener {

    JButton uploadButton, copyButton, backButton;
    JTextField linkTextField;
    JFileChooser fileChooser;
    File selectedFile;
    String serverIP;
    int port = 8080;
    JLabel statusLabel; // For displaying status messages

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz";
    private static final String USER = "root";
    private static final String PASSWORD = "Student@123"; 

    // Constructor for CreateQuiz page
    CreateQuiz() {
        // Set default frame properties
        setTitle("Create Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized

        // Set layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Add space between components
        gbc.anchor = GridBagConstraints.CENTER; // Center components

        // Heading
        JLabel heading = new JLabel("Create Quiz");
        heading.setFont(new Font("Tahoma", Font.BOLD, 34)); // Increased font size
        heading.setForeground(Color.decode("#483D8B"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Span across 3 columns
        add(heading, gbc);

        // Upload CSV button (larger size)
        uploadButton = createStyledButton("Upload CSV", new Color(0, 128, 0), new Dimension(300, 70)); // Increased size
        uploadButton.addActionListener(this);
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.gridx = 0;
        gbc.gridy = 1; // Position
        add(uploadButton, gbc);

        // Link display text field (editable and copyable)
        linkTextField = new JTextField(35); // Fixed width
        linkTextField.setFont(new Font("Tahoma", Font.PLAIN, 16));
        linkTextField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        add(linkTextField, gbc);

        // Copy Link button (smaller size, side by side with back button)
        copyButton = createStyledButton("Copy Link", new Color(255, 215, 0), new Dimension(150, 40)); // Smaller size
        copyButton.addActionListener(this);
        copyButton.setEnabled(false); // Initially disabled
        gbc.gridwidth = 1;
        gbc.gridx = 0; // Position to the left
        gbc.gridy = 3;
        add(copyButton, gbc);

        // Back button (smaller size, side by side with copy button)
        backButton = createStyledButton("Back", new Color(255, 69, 0), new Dimension(150, 40)); // Smaller size
        backButton.addActionListener(this);
        gbc.gridx = 1; // Positioned next to copy button
        gbc.gridy = 3;
        add(backButton, gbc);

        // Status label for feedback
        statusLabel = new JLabel();
        statusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3; // Span across 3 columns
        add(statusLabel, gbc);

        // Center the frame on the screen
        setLocationRelativeTo(null); // Center the window

        // Set the default close operation
        setVisible(true);

        // Fetch local IP Address for generating links
        serverIP = getLocalIpAddress();
    }

    // Create a styled button with custom size
    private JButton createStyledButton(String text, Color backgroundColor, Dimension size) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(size); // Set the custom size passed as a parameter
        button.setToolTipText(text + " (Click to " + text.toLowerCase() + ")"); // Tooltip
        return button;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == uploadButton) {
            fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.getName().endsWith(".csv")) { // Check for CSV file
                    try {
                        startHttpServer(selectedFile);
                        String uniqueId = UUID.randomUUID().toString();
                        String quizLink = "http://" + serverIP + ":" + port + "/quiz/" + uniqueId;

                        linkTextField.setText(quizLink);
                        linkTextField.setEditable(true);
                        copyButton.setEnabled(true); // Enable the copy button
                        statusLabel.setText("Quiz link generated successfully!");

                        // Store questions in the database
                        storeQuestionsInDatabase(selectedFile, quizLink);
                    } catch (IOException e) {
                        e.printStackTrace();
                        statusLabel.setText("Error starting the server.");
                    }
                } else {
                    statusLabel.setText("Please upload a valid CSV file.");
                }
            }
        } else if (ae.getSource() == copyButton) {
            // Copy the text from the linkTextField to the clipboard
            StringSelection stringSelection = new StringSelection(linkTextField.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            statusLabel.setText("Link copied to clipboard.");
        } else if (ae.getSource() == backButton) {
            // Close the full-screen window and return to the login screen
            dispose();
            new Login();
        }
    }

    private void startHttpServer(File fileToServe) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/quiz/", new QuizHandler(fileToServe));
        server.setExecutor(null);
        server.start();
    }

    static class QuizHandler implements HttpHandler {
        private final File fileToServe;

        public QuizHandler(File fileToServe) {
            this.fileToServe = fileToServe;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                byte[] fileContent = Files.readAllBytes(fileToServe.toPath());
                exchange.sendResponseHeaders(200, fileContent.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileContent);
                os.close();
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    private String getLocalIpAddress() {
        try {
            return java.net.InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "localhost";
        }
    }

    private void storeQuestionsInDatabase(File csvFile, String quizLink) {
        String insertSQL = "INSERT INTO questions (questionText, option1, option2, option3, option4, correctAnswer, quiz_link) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV format is: questionText, option1, option2, option3, option4, correctAnswer
                if (values.length == 6) { // Ensure there are exactly 6 values
                    try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                        pstmt.setString(1, values[0]);
                        pstmt.setString(2, values[1]);
                        pstmt.setString(3, values[2]);
                        pstmt.setString(4, values[3]);
                        pstmt.setString(5, values[4]);
                        pstmt.setString(6, values[5]);
                        pstmt.setString(7, quizLink);
                        pstmt.executeUpdate();
                    }
                } else {
                    System.out.println("Invalid line: " + line); // For debugging
                }
            }
            //statusLabel.setText("Questions stored in database successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error storing questions in database.");
        }
    }

    public static void main(String[] args) {
        new CreateQuiz();
    }
}
