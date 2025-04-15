package com.example.vehivlesproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookingController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, Integer> customerIdColumn;

    @FXML
    private TableColumn<Booking, Integer> vehicleIdColumn;

    @FXML
    private TableColumn<Booking, String> startDateColumn;

    @FXML
    private TableColumn<Booking, String> endDateColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private Button logoutButton;


    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehicles";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "59793789";

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBookings();
    }

    private void setupTableColumns() {
        bookingIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getBookingId()).asObject());
        customerIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCustomerId()).asObject());
        vehicleIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVehicleId()).asObject());
        startDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartDate()));
        endDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEndDate()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
    }

    public void loadBookings() {
        String query = "SELECT * FROM bookings"; // Modify this as per your schema

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("BookingID"),
                        rs.getInt("CustomerID"),
                        rs.getInt("VehicleID"),
                        rs.getString("StartDate"),
                        rs.getString("EndDate"),
                        rs.getString("Status")
                );
                bookingsTable.getItems().add(booking);
                System.out.println("Retrieved booking: " + booking.getBookingId() + ", " + booking.getCustomerId());
            }

        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while retrieving bookings: " + e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogoutAction() {
        loadScene("hello-view.fxml");
    }




    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadScene(String fxmlFileName) {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlFileName));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert("Scene Loading Error", "An error occurred while loading the scene: " + e.getMessage(), AlertType.ERROR);
        }
    }
}