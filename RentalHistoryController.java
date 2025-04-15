package com.example.vehivlesproject;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class RentalHistoryController {

    @FXML
    public TextField customerIdField;

    @FXML
    public  TableView<PaymentInfo> paymentTable;

    @FXML
    public  TableColumn<PaymentInfo, Integer> bookingIdCol;
    @FXML
    public  TableColumn<PaymentInfo, String> paymentMethodCol;
    @FXML
    public  TableColumn<PaymentInfo, Double> amountCol;
    @FXML
    public  TableColumn<PaymentInfo, String> paymentDateCol;

    public  Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/vehicles";
        String user = "root";
        String password = "59793789";
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    private void initialize() {
        // Set column bindings
        bookingIdCol.setCellValueFactory(data -> data.getValue().bookingIdProperty().asObject());
        paymentMethodCol.setCellValueFactory(data -> data.getValue().paymentMethodProperty());
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
        paymentDateCol.setCellValueFactory(data -> data.getValue().paymentDateProperty());
    }

    @FXML
    public  void handleSubmitButtonAction() {
        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            showAlert("Input Error", "Please enter a Customer ID.");
            return;
        }

        ObservableList<PaymentInfo> paymentList = FXCollections.observableArrayList();

        try (Connection conn = connectToDatabase()) {
            PreparedStatement stmt = conn.prepareStatement("""
                SELECT p.BookingID, p.PaymentMethod, p.Amount, p.PaymentDate
                FROM payments p
                JOIN bookings b ON p.BookingID = b.BookingID
                WHERE b.CustomerID = ?
            """);
            stmt.setString(1, customerId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PaymentInfo payment = new PaymentInfo(
                        rs.getInt("BookingID"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount"),
                        rs.getString("PaymentDate")
                );
                paymentList.add(payment);
            }

            paymentTable.setItems(paymentList);

            if (paymentList.isEmpty()) {
                showAlert("No Data", "No rental history found for the given Customer ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not retrieve payment data.");
        }
    }

    @FXML
    public  void handleLogoutButtonAction() {
        loadScene("hello-view.fxml");
    }

    @FXML
    public  void handleBackButtonAction() {
        loadScene("UserDashboard.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) customerIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load the requested page.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

