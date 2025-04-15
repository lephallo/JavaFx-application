package com.example.vehivlesproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeDashboardController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void handleBookings(ActionEvent event) {
        loadScene(event, "Bookings.fxml");
    }

    @FXML
    void handlePayments(ActionEvent event) {
        loadScene(event, "ViewPayments.fxml");
    }

    @FXML
    void viewCustomers(ActionEvent event) {
        loadScene(event, "ViewCustomers.fxml");
    }

    @FXML
    void logout(ActionEvent event) {
        loadScene(event, "hello-view.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlFile) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxmlFile));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Proper error handling can be added here
        }
    }
}