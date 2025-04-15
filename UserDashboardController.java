package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class UserDashboardController {

    @FXML
    public ComboBox<String> vehicleChoiceBox;
    @FXML
    public Button paymentHistoryButton;
    @FXML
    public Button updateProfileButton;
    @FXML
    public Button viewInvoiceButton;
    @FXML
    public Button trackStatusButton;
    @FXML
    public Button logoutButton;

    @FXML
    public void initialize() {
        vehicleChoiceBox.getItems().addAll("Car", "Bike", "Van", "Truck");

        vehicleChoiceBox.setOnAction(event -> handleVehicleChoice());
        paymentHistoryButton.setOnAction(event -> handlePaymentHistoryButtonClick());
        updateProfileButton.setOnAction(event -> handleUpdateProfileButtonClick());
        viewInvoiceButton.setOnAction(event -> handleViewInvoiceButtonClick());
        trackStatusButton.setOnAction(event -> handleTrackStatusButtonClick());
        logoutButton.setOnAction(event -> handleLogoutButtonClick());
    }

    public void handleViewInvoiceButtonClick() {
        loadPage("slip.fxml", "View Invoice");
    }

    public void handleTrackStatusButtonClick() {
        loadPage("bookings.fxml", "Track Status");
    }

    public void handleUpdateProfileButtonClick() {
        loadPage("UpdateProfile.fxml", "Update Profile");
    }

    public void handlePaymentHistoryButtonClick() {
        loadPage("PaymentHistory.fxml", "Payment History");
    }

    public void handleVehicleChoice() {
        String selectedVehicle = vehicleChoiceBox.getValue();
        if (selectedVehicle != null) {
            String fxmlFile;
            switch (selectedVehicle) {
                case "Car":
                    fxmlFile = "car.fxml";
                    break;
                case "Bike":
                    fxmlFile = "Bike.fxml";
                    break;
                case "Van":
                    fxmlFile = "Van.fxml";
                    break;
                case "Truck":
                    fxmlFile = "Truck.fxml";
                    break;
                default:
                    showAlert("Error", "Unknown vehicle type selected.");
                    return;
            }
            loadPage(fxmlFile, selectedVehicle + " Information");
        }
    }

    private void loadPage(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) vehicleChoiceBox.getScene().getWindow(); // Changed to use vehicleChoiceBox
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load " + title + " page.");
        }
    }
    public void handleLogoutButtonClick() {
        loadPage("hello-view.fxml", "Register");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
