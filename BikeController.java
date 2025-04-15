package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class BikeController {

    @FXML
    private Button backButton;

    @FXML
    private Button rentButton1; // Rent button for first bike
    @FXML
    private Button rentButton2; // Rent button for second bike
    @FXML
    private Button rentButton3; // Rent button for third bike
    @FXML
    private Button carButton;    // Button for Car
    @FXML
    private Button truckButton;  // Button for Truck
    @FXML
    private Button vanButton;    // Button for Van

    @FXML
    private void initialize() {
        // You can perform any additional initialization here if needed.
    }

    @FXML
    public void handleBackButtonAction() throws IOException {
        // Load userDashboard.fxml
        Parent root = FXMLLoader.load(getClass().getResource("UserDashboard.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleRentButtonAction1() throws IOException {
        // Load payment.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) rentButton1.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleRentButtonAction2() throws IOException {
        // Load payment.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) rentButton2.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleRentButtonAction3() throws IOException {
        // Load payment.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Payment.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) rentButton3.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleCarButtonAction() throws IOException {
        // Load car.fxml
        Parent root = FXMLLoader.load(getClass().getResource("car.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) carButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleTruckButtonAction() throws IOException {
        // Load truck.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Truck.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) truckButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void handleVanButtonAction() throws IOException {
        // Load van.fxml
        Parent root = FXMLLoader.load(getClass().getResource("Van.fxml")); // Make sure to set the correct path
        Stage stage = (Stage) vanButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
