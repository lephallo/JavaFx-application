package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentController {

    @FXML
    private TextField nameField; // TextField for Name
    @FXML
    private TextField phoneField; // TextField for Phone Number
    @FXML
    private TextField emailField; // TextField for Email
    @FXML
    private TextField licenseField; // TextField for Driving License Number
    @FXML
    private Button btnBack; // Back button
    @FXML
    private Button btnRegister; // Register button
    @FXML
    private Button btnLogin; // Login button to navigate to ContinueToPay.fxml
    @FXML
    private Button btnProceed; // Proceed to Payment button

    @FXML
    public void initialize() {
        btnRegister.setOnAction(event -> registerUser());
        btnBack.setOnAction(event -> loadFXML("UserDashboard.fxml")); // Ensure this path is correct
        btnLogin.setOnAction(event -> loadFXML("Book.fxml")); // Navigate to ContinueToPay.fxml
        btnProceed.setOnAction(event -> loadFXML("Book.fxml")); // Ensure this filename is correct
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String licenseNumber = licenseField.getText().trim();

        if (isValidInput(name, phone, email, licenseNumber)) {
            saveToDatabase(name, phone, email, licenseNumber);
        } else {
            // Handle invalid inputs (e.g., using an alert dialog)
            System.out.println("Invalid input, please fill all fields correctly.");
        }
    }

    private boolean isValidInput(String name, String phone, String email, String licenseNumber) {
        return !name.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !licenseNumber.isEmpty();
    }

    private void saveToDatabase(String name, String phone, String email, String licenseNumber) {
        // Update JDBC URL for PostgreSQL
        String url = "jdbc:mysql://localhost:3306/vehicles";

        String user = "root";
        String password = "59793789";

        String sql = "INSERT INTO Customers (Name, PhoneNumber, Email_Address, DrivingLicenseNumber) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, licenseNumber);

            pstmt.executeUpdate();
            System.out.println("Customer registered successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private void loadFXML(String fxmlFile) {
        try {
            Pane newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene currentScene = btnBack.getScene(); // Using the back button to get the scene
            currentScene.setRoot(newScene);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML: " + e.getMessage());
        }
    }
}