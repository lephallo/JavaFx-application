package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class VanController {

    @FXML
    private Button btnRent1; // Rent button for the first van
    @FXML
    private Button btnRent2; // Rent button for the second van
    @FXML
    private Button btnRent3; // Rent button for the third van
    @FXML
    private Button btnRent4; // Rent button for the fourth van
    @FXML
    private Button btnBike;   // Button for Bike
    @FXML
    private Button btnCar;    // Button for Car
    @FXML
    private Button btnTruck;  // Button for Truck
    @FXML
    private Button btnBack;   // Back button

    @FXML
    public void initialize() {
        // Set action events for each button
        btnRent1.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent2.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent3.setOnAction(event -> loadFXML("Payment.fxml"));
        btnRent4.setOnAction(event -> loadFXML("Payment.fxml"));

        btnBike.setOnAction(event -> loadFXML("Bike.fxml"));
        btnCar.setOnAction(event -> loadFXML("car.fxml"));
        btnTruck.setOnAction(event -> loadFXML("Truck.fxml")); // Assuming "Truck.fxml" refers to your Truck page.

        btnBack.setOnAction(event -> loadFXML("UserDashboard.fxml"));
    }

    // Method to load the specified FXML file
    private void loadFXML(String fxmlFile) {
        try {
            // Load the specified FXML file and change the current scene
            Parent newScene = FXMLLoader.load(getClass().getResource(fxmlFile));
            // Assuming you have a parent container or stage reference
            Scene currentScene = btnRent1.getScene(); // Using one of the buttons to get the scene
            currentScene.setRoot(newScene);
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception (logging, alert to the user, etc.)
        }
    }
}