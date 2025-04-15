package com.example.vehivlesproject;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ViewVehicleController {

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private Button backButton;

    @FXML
    private Button submitButton;

    @FXML
    private Label messageLabel;

    @FXML
    void initialize() {
        // Populate the choice box with vehicle categories
        categoryChoiceBox.getItems().addAll("Car", "Bike", "Van", "Truck");

        // Set up action for the back button
        backButton.setOnMouseClicked(this::loadUserDashboard);

        // Set up action for the submit button
        submitButton.setOnMouseClicked(this::handleCategorySelection);
    }

    private void handleCategorySelection(MouseEvent event) {
        String selectedCategory = categoryChoiceBox.getValue();
        if (selectedCategory != null) {
            try {
                // Determine which FXML to load based on the selected category
                String fxmlFile = null;
                switch (selectedCategory) {
                    case "Car":
                        fxmlFile = "Car.fxml";
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
                        messageLabel.setText("Unknown category selected.");
                        return;
                }

                // Load the corresponding FXML and display it
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) submitButton.getScene().getWindow(); // Get current stage
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                messageLabel.setText("Error loading the selected category.");
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Please select a category.");
        }
    }

    private void loadUserDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            messageLabel.setText("Error loading the User Dashboard.");
            e.printStackTrace();
        }
    }
}
