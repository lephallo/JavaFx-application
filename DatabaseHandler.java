package com.example.vehivlesproject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicles"; // Change to your MySQL DB URL
    private static final String DB_USER = "root"; // Change to your MySQL username
    private static final String DB_PASSWORD = "59793789"; // Change to your MySQL password

    public DatabaseHandler() {
        try {
            // Register the MySQL driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Error handling
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean registerUser(String username, String password, String role) {
        String sql = "INSERT INTO users (name, password, role) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // Store passwords securely (hash in production)
            statement.setString(3, role);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            // Handle specific SQL exceptions
            if (e.getSQLState().equals("23000")) { // Unique constraint violation state for MySQL
                System.out.println("Username already exists: " + username);
                return false;
            } else {
                e.printStackTrace();
                return false;
            }
        }
    }

    public User authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE name = ? AND password = ?";

        try (Connection conn = this.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password); // Consider hashing the password

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                return new User(username, password, role); // Return User object
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Implement proper logging
        }
        return null; // No user found
    }

    // You can add more methods for other database operations
}