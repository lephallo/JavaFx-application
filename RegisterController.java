package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private RadioButton customerRadio;

    @FXML
    private RadioButton adminRadio;

    @FXML
    private RadioButton employeeRadio;

    // Create an instance of DatabaseHandler to interact with the MySQL database
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML
    private void handleRegister() {
        String name = nameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String role = getSelectedRole(); // Get the selected role

        String validationError = validateInputs(name, password, confirmPassword, role);
        if (validationError != null) {
            showAlert(validationError);
            return;
        }

        // Register the user in the database using DatabaseHandler
        if (databaseHandler.registerUser(name, password, role)) {
            System.out.println("Registered user: " + name);
            switchToLoginScene(); // Redirect to login scene after successful registration
        } else {
            showAlert("Registration failed. Please check your input.");
        }
    }

    private String getSelectedRole() {
        if (customerRadio.isSelected()) {
            return "Customer";
        } else if (adminRadio.isSelected()) {
            return "Admin";
        } else if (employeeRadio.isSelected()) {
            return "Employee";
        }
        return ""; // Return an empty string if no role is selected
    }

    private String validateInputs(String name, String password, String confirmPassword, String role) {
        if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match.";
        }
        if (name.length() < 3) {
            return "Username must be at least 3 characters long.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters long.";
        }
        return null; // All checks passed
    }

    private void switchToLoginScene() {
        try {
            URL loginFxmlUrl = getClass().getResource("/com/example/vehivlesproject/Login.fxml");
            if (loginFxmlUrl == null) {
                throw new IllegalStateException("FXML file not found!");
            }

            FXMLLoader loader = new FXMLLoader(loginFxmlUrl);
            Scene loginScene = new Scene(loader.load());
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading login scene: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // New method to handle "Login" button action
    @FXML
    private void handleLoginRedirect() {
        switchToLoginScene();
    }
}