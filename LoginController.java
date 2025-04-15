package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField loginNameField;

    @FXML
    private PasswordField loginPasswordField;

    private DatabaseHandler databaseHandler; // Create an instance of your DatabaseHandler

    public LoginController() {
        this.databaseHandler = new DatabaseHandler();  // Initialize your database handler
    }

    @FXML
    protected void handleLogin(ActionEvent event) {
        String username = loginNameField.getText().trim();
        String password = loginPasswordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        // Authenticate the user
        User user = databaseHandler.authenticateUser(username, password);

        if (user != null) { // User authenticated
            // Route to the respective dashboard based on role
            String fxmlFile = switch (user.getRole()) {
                case "Admin" -> "/com/example/vehivlesproject/AdminDashboard.fxml";
                case "Customer" -> "/com/example/vehivlesproject/userDashboard.fxml";
                case "Employee" -> "/com/example/vehivlesproject/EmployeeDashboard.fxml";
                default -> null;
            };

            if (fxmlFile != null) {
                loadScene(fxmlFile);
            } else {
                showAlert("Unknown role.");
            }
        } else {
            showAlert("Invalid username or password.");
        }
    }

    @FXML
    protected void handleBack(ActionEvent event) {
        loadScene("/com/example/vehivlesproject/hello-view.fxml"); // Load the registration scene
    }

    private void loadScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) loginNameField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading scene: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}