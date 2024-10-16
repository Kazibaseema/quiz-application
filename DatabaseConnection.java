// package quiz.application;   

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;

// public class DatabaseConnection {

//     private static final String URL = "jdbc:mysql://localhost:3306/quiz";
//     private static final String USER = "root";
//     private static final String PASSWORD = "Student@123";

//     // Method to establish a connection to the database
//     public static Connection getConnection() {
//         Connection connection = null;
//         try {
//             // Load the MySQL JDBC driver
//             Class.forName("com.mysql.cj.jdbc.Driver");

//             // Establish the connection
//             connection = DriverManager.getConnection(URL, USER, PASSWORD);
//             System.out.println("Database connected successfully!");
//         } catch (ClassNotFoundException e) {
//             System.out.println("MySQL JDBC Driver not found.");
//             e.printStackTrace();
//         } catch (SQLException e) {
//             System.out.println("Failed to connect to the database.");
//             e.printStackTrace();
//         }
//         return connection;
//     }

//     // Main method to test the connection
//     public static void main(String[] args) {
//         // Test the connection
//         Connection conn = getConnection();
//         if (conn != null) {
//             System.out.println("Connection successful!");
//         } else {
//             System.out.println("Connection failed.");
//         }
//     }
// }

package quiz.application; 

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quiz"; // Update with your DB name
    private static final String USER = "root"; // Update with your DB username
    private static final String PASSWORD = "#Baseema@123"; // Update with your DB password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connected successfully.");
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }
}
