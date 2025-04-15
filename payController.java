package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class payController {

    @FXML
    private TextField carNameField;

    @FXML
    private TextField durationField;

    @FXML
    private TextField totalAmountField;

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    private TextField additionalField; // For cash or card details

    @FXML
    private Button calculateButton;

    @FXML
    private Button payButton;

    @FXML
    private Button backButton;

    @FXML
    private Button trackStatusButton;

    private DatabaseHandler databaseHandler = new DatabaseHandler();

    public void initialize() {
        paymentMethodComboBox.getItems().addAll("Cash", "Credit Card", "Online");

        paymentMethodComboBox.setOnAction(e -> handlePaymentMethodSelection());
        calculateButton.setOnAction(e -> calculateTotal());
        payButton.setOnAction(e -> handlePayment());
        backButton.setOnAction(e -> navigateToUserDashboard());
        trackStatusButton.setOnAction(e -> navigateToTrackStatus());
    }

    public void handlePaymentMethodSelection() {
        String selectedMethod = paymentMethodComboBox.getValue();
        if ("Cash".equals(selectedMethod)) {
            additionalField.setPromptText("Enter amount to pay");
        } else if ("Credit Card".equals(selectedMethod)) {
            additionalField.setPromptText("Enter your card details");
        } else {
            additionalField.clear();
        }
    }

    public void calculateTotal() {
        String carName = carNameField.getText().trim();
        String durationString = durationField.getText().trim();

        if (carName.isEmpty() || durationString.isEmpty()) {
            showAlert("Input Error", "Please fill in both the car name and duration.");
            return;
        }

        try {
            int duration = Integer.parseInt(durationString);
            double pricePerDay = getPriceFromDatabase(carName);
            if (pricePerDay == -1) {
                showAlert("Database Error", "Car not found or there was an error retrieving the price.");
                return;
            }
            double totalAmount = pricePerDay * duration;
            totalAmountField.setText(String.valueOf(totalAmount));
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for duration.");
        }
    }

    public double getPriceFromDatabase(String carName) {
        double price = -1;
        String query = "SELECT RentalPricePerDay FROM Vehicles WHERE BrandModel = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, carName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                price = resultSet.getDouble("RentalPricePerDay");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error accessing the database: " + e.getMessage());
            e.printStackTrace();
        }
        return price;
    }

    // Modified: Only fetch approved bookings
    public int getApprovedBookingIdFromVehicle(String brandModel) {
        String query = "SELECT b.BookingID FROM Bookings b " +
                "JOIN Vehicles v ON b.VehicleID = v.VehicleID " +
                "WHERE v.BrandModel = ? AND b.Status = 'Approved' " +
                "ORDER BY b.BookingID DESC LIMIT 1";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, brandModel);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("BookingID");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to retrieve booking ID: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public void handlePayment() {
        String selectedMethod = paymentMethodComboBox.getValue();
        String totalAmountString = totalAmountField.getText();
        String carName = carNameField.getText().trim();

        if (selectedMethod == null || totalAmountString.isEmpty() || carName.isEmpty()) {
            showAlert("Input Error", "Please fill in all payment details including car name.");
            return;
        }

        try {
            double amount = Double.parseDouble(totalAmountString);
            int bookingId = getApprovedBookingIdFromVehicle(carName);

            if (bookingId == -1) {
                showAlert("Booking Not Approved", "Payment can only be made for approved bookings. Please wait for approval.");
                return;
            }

            savePaymentToDatabase(bookingId, selectedMethod, amount);
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Total amount must be a valid number.");
        }
    }

    public void savePaymentToDatabase(int bookingId, String paymentMethod, double amount) {
        String insertSQL = "INSERT INTO Payments (BookingID, PaymentMethod, Amount) VALUES (?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setInt(1, bookingId);
            preparedStatement.setString(2, paymentMethod);
            preparedStatement.setDouble(3, amount);
            preparedStatement.executeUpdate();

            showAlert("Success", "Payment recorded successfully!");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to record payment in database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void navigateToUserDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Navigation Error", "Failed to load User Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void navigateToTrackStatus() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Bookings.fxml"));
            Stage stage = (Stage) trackStatusButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Navigation Error", "Failed to load Booking Status page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
