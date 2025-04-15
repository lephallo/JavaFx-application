package com.example.vehivlesproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class ViewPaymentsController {

    @FXML
    public TableView<Payment> paymentTable;
    @FXML
    public TableColumn<Payment, Integer> paymentIdCol;
    @FXML
    public TableColumn<Payment, Integer> bookingIdCol;
    @FXML
    public TableColumn<Payment, String> paymentMethodCol;
    @FXML
    public TableColumn<Payment, Double> amountCol;
    @FXML
    public TableColumn<Payment, String> paymentDateCol;

    public Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/vehicles";
        String user = "root";
        String password = "59793789";
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    public void initialize() {
        paymentIdCol.setCellValueFactory(data -> data.getValue().paymentIdProperty().asObject());
        bookingIdCol.setCellValueFactory(data -> data.getValue().bookingIdProperty().asObject());
        paymentMethodCol.setCellValueFactory(data -> data.getValue().paymentMethodProperty());
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
        paymentDateCol.setCellValueFactory(data -> data.getValue().paymentDateProperty());

        loadPayments();
    }

    public void loadPayments() {
        ObservableList<Payment> paymentList = FXCollections.observableArrayList();

        try (Connection conn = connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM payments")) {

            while (rs.next()) {
                Payment payment = new Payment(
                        rs.getInt("PaymentID"),
                        rs.getInt("BookingID"),
                        rs.getString("PaymentMethod"),
                        rs.getDouble("Amount"),
                        rs.getTimestamp("PaymentDate").toString()
                );
                paymentList.add(payment);
            }

            paymentTable.setItems(paymentList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load payment records.");
        }
    }

    @FXML
    public void handleBackButton() {
        loadScene("EmployeeDashboard.fxml");
    }

    @FXML
    public void handleLogoutButton() {
        loadScene("hello-view.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) paymentTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Vehicle Rentals"); // optional
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Failed to load " + fxmlFile);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
