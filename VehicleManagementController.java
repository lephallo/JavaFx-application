package com.example.vehivlesproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleManagementController {
    @FXML
    private TableView<Vehicle> vehicleTable;
    @FXML
    private TableColumn<Vehicle, String> vehicleIdColumn;
    @FXML
    private TableColumn<Vehicle, String> brandModelColumn;
    @FXML
    private TableColumn<Vehicle, String> categoryColumn;
    @FXML
    private TableColumn<Vehicle, Double> rentalPriceColumn;
    @FXML
    private TableColumn<Vehicle, String> statusColumn;
    @FXML
    private TableColumn<Vehicle, String> customerIdColumn;

    @FXML
    private TextField vehicleIdField;
    @FXML
    private TextField brandModelField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField rentalPriceField;
    @FXML
    private TextField customerIdField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private DatabaseHandler dbHandler = new DatabaseHandler(); // Instance of DatabaseHandler

    public void initialize() {
        // Set up columns
        vehicleIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getVehicleId()));
        brandModelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrandModel()));
        categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategory()));
        rentalPriceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getRentalPrice()).asObject());
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        customerIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomerId()));

        // Load initial data
        loadVehicleData();

        // Set events
        addButton.setOnAction(event -> addVehicle());
        updateButton.setOnAction(event -> updateVehicle());
        deleteButton.setOnAction(event -> deleteVehicle());
        backButton.setOnAction(event -> goBackToAdminDashboard());
    }

    public void loadVehicleData() {
        String sql = "SELECT * FROM vehicles";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            vehicleList.clear();
            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("VehicleID"),
                        rs.getString("BrandModel"),
                        rs.getString("Category"),
                        rs.getDouble("RentalPricePerDay"),
                        rs.getBoolean("AvailabilityStatus") ? "Available" : "Rented", // Convert boolean to string
                        rs.getString("CustomerID")
                );
                vehicleList.add(vehicle);
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error loading vehicle data: " + e.getMessage());
        }

        vehicleTable.setItems(vehicleList);
    }

    public void addVehicle() {
        String brandModel = brandModelField.getText();
        String category = categoryField.getText();
        String rentalPriceString = rentalPriceField.getText();
        Double rentalPrice;

        // Set CustomerID to null since the vehicle is available
        Integer customerId = null;

        // Validate input
        if (brandModel.isEmpty() || category.isEmpty() || rentalPriceString.isEmpty()) {
            showAlert(AlertType.WARNING, "Input Error", "All fields must be filled out.");
            return;
        }

        try {
            rentalPrice = Double.parseDouble(rentalPriceString);
        } catch (NumberFormatException e) {
            showAlert(AlertType.WARNING, "Input Error", "Rental price must be a valid number.");
            return;
        }

        // Modify SQL to use NULL for customer ID
        String sql = "INSERT INTO vehicles (brandmodel, category, rentalpriceperday, availabilitystatus, customerid) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = dbHandler.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, brandModel);
            pstmt.setString(2, category);
            pstmt.setDouble(3, rentalPrice);
            pstmt.setBoolean(4, true); // Available
            pstmt.setObject(5, customerId); // This will insert NULL for customer id

            pstmt.executeUpdate();

            // Inform the user
            showAlert(AlertType.INFORMATION, "Success", "Vehicle added successfully!");
            // Refresh the table view
            loadVehicleData();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Database Error", "Error adding vehicle: " + e.getMessage());
        }
    }

    public void updateVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            selectedVehicle.setBrandModel(brandModelField.getText());
            selectedVehicle.setCategory(categoryField.getText());
            String rentalPriceString = rentalPriceField.getText();
            double rentalPrice;

            // Validate input
            if (selectedVehicle.getVehicleId().isEmpty() || selectedVehicle.getBrandModel().isEmpty() || selectedVehicle.getCategory().isEmpty() || rentalPriceString.isEmpty()) {
                showAlert(AlertType.WARNING, "Input Error", "All fields must be filled out.");
                return;
            }

            try {
                rentalPrice = Double.parseDouble(rentalPriceString);
            } catch (NumberFormatException e) {
                showAlert(AlertType.WARNING, "Input Error", "Rental price must be a valid number.");
                return;
            }

            selectedVehicle.setRentalPrice(rentalPrice);

            // Set CustomerID as NULL when availability is true (or Available)
            Integer customerId = null;
            if (selectedVehicle.getStatus().equals("Rented")) {
                customerId = Integer.parseInt(customerIdField.getText());
            }

            String sql = "UPDATE vehicles SET brandmodel = ?, category = ?, rentalpriceperday = ?, customerid = ?, availabilitystatus = ? WHERE vehicleid = ?";
            try (Connection conn = dbHandler.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedVehicle.getBrandModel());
                pstmt.setString(2, selectedVehicle.getCategory());
                pstmt.setDouble(3, selectedVehicle.getRentalPrice());
                pstmt.setObject(4, customerId); // Pass NULL if customerId is null
                pstmt.setBoolean(5, customerId == null); // Set availability status
                pstmt.setString(6, selectedVehicle.getVehicleId());

                pstmt.executeUpdate();

                // Inform the user
                showAlert(AlertType.INFORMATION, "Success", "Vehicle updated successfully!");
                // Refresh the table view
                loadVehicleData();
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error updating vehicle: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Selection Error", "No vehicle selected for update.");
        }
    }

    public void deleteVehicle() {
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null) {
            String sql = "DELETE FROM vehicles WHERE vehicleid = ?";
            try (Connection conn = dbHandler.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, selectedVehicle.getVehicleId());
                pstmt.executeUpdate();

                // Inform the user
                showAlert(AlertType.INFORMATION, "Success", "Vehicle deleted successfully!");
                vehicleList.remove(selectedVehicle);
                loadVehicleData();
            } catch (SQLException e) {
                showAlert(AlertType.ERROR, "Database Error", "Error deleting vehicle: " + e.getMessage());
            }
        } else {
            showAlert(AlertType.WARNING, "Selection Error", "No vehicle selected for deletion.");
        }
    }

    public void goBackToAdminDashboard() {

            try {
                // Load the Admin Dashboard
                Stage stage = (Stage)backButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml")); // Adjust the path as necessary
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Admin Dashboard");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }



    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}