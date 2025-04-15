package com.example.vehivlesproject;

import com.example.vehivlesproject.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateProfileController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button updateButton;

    @FXML
    private Button backButton;

    private DatabaseHandler databaseHandler = new DatabaseHandler(); // Assume you have this handler

    @FXML
    public void initialize() {
        loadUserData();

        updateButton.setOnAction(e -> updateUserProfile());
        backButton.setOnAction(e -> navigateToUserDashboard());
    }

    // Method to load user data from the database
    private void loadUserData() {
        String query = "SELECT name, password FROM users WHERE user_id = ?"; // Use correct condition

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, getCurrentUserId()); // Retrieve logged-in user's ID
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nameField.setText(resultSet.getString("name"));
                passwordField.setText(resultSet.getString("password")); // Avoid displaying passwords
            } else {
                showAlert("User Not Found", "No user found with the current ID.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load user data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to update user profile
    public void updateUserProfile() {
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic input validation
        if (name.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please fill in both fields.");
            return;
        }

        // In a real application, consider hashing the password before storing it
        String updateQuery = "UPDATE users SET name = ?, password = ? WHERE user_id = ?"; // Use correct user condition

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password); // Ensure password is handled securely
            preparedStatement.setInt(3, getCurrentUserId()); // Use current user's ID

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "User profile updated successfully!");
            } else {
                showAlert("Update Error", "No changes were made or user not found.");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update user data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to navigate back to the user dashboard
    public void navigateToUserDashboard() {
        try {
            // Load the FXML file for the User Dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();

            // Get the current stage (the window that is currently open)
            Stage stage = (Stage) backButton.getScene().getWindow(); // Using the backButton to obtain the current stage

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load the dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to get the current user's ID securely
    private int getCurrentUserId() {
        // Fetch and return the ID of the currently logged-in user securely
        // This could be implemented using session management, a singleton pattern, or any suitable method
        // For actual implementations, use context from the login process
        return CurrentSession.getInstance().getUserId(); // Example of fetching logged-in user ID from session
    }
}