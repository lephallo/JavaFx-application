package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class BookController {

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField vehicleIdField;

    @FXML
    private TextField startDateField;

    @FXML
    private TextField endDateField;

    @FXML
    private Button bookButton;

    @FXML
    private Button trackStatusButton;

    @FXML
    private Button payButton;

    @FXML
    private Button backButton;

    // Fixed username "roor" to "root"
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicles";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "59793789";

    @FXML
    public void handleBookButtonAction() {
        if (!validateInputs()) {
            return;
        }

        int customerId = Integer.parseInt(customerIdField.getText());
        int vehicleId = Integer.parseInt(vehicleIdField.getText());
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        if (!isVehicleExists(vehicleId)) {
            showAlert("Booking Error", "The specified Vehicle ID does not exist.", AlertType.ERROR);
            return;
        }

        if (!createBooking(customerId, vehicleId, startDate, endDate)) {
            showAlert("Booking Error", "An error occurred while creating the booking.", AlertType.ERROR);
        } else {
            showAlert("Booking Successful", "Your booking has been created.", AlertType.INFORMATION);
        }
    }

    private boolean isVehicleExists(int vehicleId) {
        String query = "SELECT COUNT(*) FROM vehicles WHERE vehicleid = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, vehicleId);
            ResultSet resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while checking vehicle existence: " + e.getMessage(), AlertType.ERROR);
        }

        return false;
    }

    public boolean createBooking(int customerId, int vehicleId, String startDate, String endDate) {
        String query = "INSERT INTO bookings (customerid, vehicleid, startdate, enddate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, customerId);
            pstmt.setInt(2, vehicleId);
            pstmt.setDate(3, Date.valueOf(startDate));
            pstmt.setDate(4, Date.valueOf(endDate));

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            showAlert("Booking Error", "An error occurred while booking: " + e.getMessage(), AlertType.ERROR);
            return false;
        } catch (IllegalArgumentException e) {
            showAlert("Date Format Error", "Ensure start and end dates are in the correct format (YYYY-MM-DD).", AlertType.ERROR);
            return false;
        }
    }

    @FXML
    public void handlePayButtonAction() {
        loadScene("ContinueToPay.fxml");
    }

    @FXML
    public void handleTrackStatusButtonAction() {
        loadScene("Bookings.fxml");
    }

    @FXML
    public void handleBackButtonAction() {
        loadScene("UserDashboard.fxml");
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(String fxmlFileName) {
        try {
            Stage stage = (Stage) bookButton.getScene().getWindow();
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlFileName));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Scene Loading Error", "An error occurred while loading the scene: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private boolean validateInputs() {
        if (customerIdField.getText().isEmpty() || vehicleIdField.getText().isEmpty() ||
                startDateField.getText().isEmpty() || endDateField.getText().isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.", AlertType.ERROR);
            return false;
        }

        try {
            Integer.parseInt(customerIdField.getText());
            Integer.parseInt(vehicleIdField.getText());
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Customer ID and Vehicle ID must be numeric.", AlertType.ERROR);
            return false;
        }

        // Validate date format
        try {
            Date.valueOf(startDateField.getText());
            Date.valueOf(endDateField.getText());
        } catch (IllegalArgumentException e) {
            showAlert("Date Format Error", "Use date format: YYYY-MM-DD.", AlertType.ERROR);
            return false;
        }

        return true;
    }
}