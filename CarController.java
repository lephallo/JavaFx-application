package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class CarController {

    @FXML
    private Button rentButton1; // Rent button for the first car
    @FXML
    private Button rentButton2; // Rent button for the second car
    @FXML
    private Button rentButton3; // Rent button for the third car
    @FXML
    private Button rentButton11; // Another Rent button (if needed)
    @FXML
    private Button btnVan; // Button for Van
    @FXML
    private Button btnBike; // Button for Bike
    @FXML
    private Button btnTruck; // Button for Truck
    @FXML
    private Button btnBack; // Back button

    @FXML
    public void initialize() {
        // Set action events for each button
        rentButton1.setOnAction(event -> loadFXML("Payment.fxml"));
        rentButton2.setOnAction(event -> loadFXML("Payment.fxml"));
        rentButton3.setOnAction(event -> loadFXML("Payment.fxml"));
        rentButton11.setOnAction(event -> loadFXML("Payment.fxml"));

        btnVan.setOnAction(event -> loadFXML("Van.fxml"));
        btnBike.setOnAction(event -> loadFXML("Bike.fxml"));
        btnTruck.setOnAction(event -> loadFXML("Truck.fxml"));

        btnBack.setOnAction(event -> loadFXML("UserDashboard.fxml"));
    }

    // Method to load the specified FXML file
    private void loadFXML(String fxmlFile) {
        try {
            // Load the FXML file and change the scene
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            // Changing the root of the current scene
            Scene currentScene = btnBack.getScene(); // Using the Back button to get the scene
            currentScene.setRoot(newScene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (logging, alert to the user, etc.)
        }
    }
}