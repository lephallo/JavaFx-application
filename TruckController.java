package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class TruckController {

    @FXML
    private Button btnRent1;      // Rent button for the first truck
    @FXML
    private Button btnRent2;      // Rent button for the second truck
    @FXML
    private Button btnRent3;      // Rent button for the third truck
    @FXML
    private Button btnRent4;      // Rent button for the fourth truck
    @FXML
    private Button btnVan;        // Button for Van
    @FXML
    private Button btnCar;        // Button for Car
    @FXML
    private Button btnBike;       // Button for Bike
    @FXML
    private Button btnBack;       // Back button

    @FXML
    public void initialize() {
        // Initialize and bind action events to the buttons
        btnRent1.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent2.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent3.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent4.setOnAction(event -> loadFXML("Payment.fxml"));

        btnVan.setOnAction(event -> loadFXML("Van.fxml"));
        btnCar.setOnAction(event -> loadFXML("car.fxml"));
        btnBike.setOnAction(event -> loadFXML("Bike.fxml"));

        btnBack.setOnAction(event -> loadFXML("UserDashboard.fxml"));
    }

    // Method to load the specified FXML file
    private void loadFXML(String fxmlFile) {
        try {
            // Load the FXML file and set the scene
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            // Assuming you have a reference to the current stage (window)
            Scene currentScene = btnRent1.getScene(); // You could use any button to get the current scene
            currentScene.setRoot(newScene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (logging, alert to the user, etc.)
        }
    }
}
