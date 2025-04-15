package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BookingManagementController {

    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, String> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> vehicleIdColumn;
    @FXML
    private TableColumn<Booking, String> customerIdColumn;
    @FXML
    private TableColumn<Booking, String> startDateColumn;
    @FXML
    private TableColumn<Booking, String> endDateColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private Button approveAllButton;
    @FXML
    private Button backButton;

    private final DatabaseHandler databaseHandler;

    public BookingManagementController() {
        // Initialize the DatabaseHandler
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        setupTableColumns(); // Set up columns mapping
        loadBookingData();

        // Set button actions
        approveAllButton.setOnAction(event -> approveAllBookings());
        backButton.setOnAction(event -> goBackToDashboard());
    }

    private void setupTableColumns() {
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void loadBookingData() {
        ObservableList<Booking> bookingList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bookings"; // Adjust the SQL query based on your database schema

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String bookingId = rs.getString("BookingId");
                String vehicleId = rs.getString("VehicleId");
                String customerId = rs.getString("CustomerID");
                String startDate = rs.getString("StartDate");
                String endDate = rs.getString("EndDate");
                String status = rs.getString("Status");
                bookingList.add(new Booking(bookingId, vehicleId, customerId, startDate, endDate, status));
            }
            bookingTable.setItems(bookingList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load bookings: " + e.getMessage());
        }
    }

    public void approveAllBookings() {
        String sql = "UPDATE bookings SET Status = 'Approved' WHERE Status = 'Pending'";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            int updatedRows = pstmt.executeUpdate();
            if (updatedRows > 0) {
                showAlert("Success", updatedRows + " bookings approved!");
                loadBookingData(); // Reload the updated data
            } else {
                showAlert("Info", "No pending bookings to approve.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to approve bookings: " + e.getMessage());
        }
    }

    public void goBackToDashboard() {
        try {
            // Load the Admin Dashboard
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml")); // Adjust the path as necessary
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the Admin Dashboard: " + e.getMessage());
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Booking class with necessary fields and constructor
    public static class Booking {
        private final String bookingId;
        private final String vehicleId;
        private final String customerId;
        private final String startDate;
        private final String endDate;
        private final String status;

        public Booking(String bookingId, String vehicleId, String customerId, String startDate, String endDate, String status) {
            this.bookingId = bookingId;
            this.vehicleId = vehicleId;
            this.customerId = customerId;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
        }

        public String getBookingId() { return bookingId; }
        public String getVehicleId() { return vehicleId; }
        public String getCustomerId() { return customerId; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getStatus() { return status; }
    }
}