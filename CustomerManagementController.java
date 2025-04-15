package com.example.vehivlesproject;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;  // Importing SQL related classes
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;

public class CustomerManagementController {

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneNumberColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> licenseNumberColumn;

    @FXML
    private TextField nameField;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField licenseNumberField;

    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button clearButton;

    private DatabaseHandler databaseHandler;

    public CustomerManagementController() {
        // Initialize the DatabaseHandler
        databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        setupTableColumns(); // Setup columns mapping
        loadCustomerData();

        // Add listener for table selection
        customerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                fillTextFields(newValue);
            }
        });

        // Set button actions
        addButton.setOnAction(event -> addCustomer());
        updateButton.setOnAction(event -> updateCustomer());
        deleteButton.setOnAction(event -> deleteCustomer());
        logoutButton.setOnAction(event -> logout());
        clearButton.setOnAction(event -> clearFields());
    }

    private void setupTableColumns() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        licenseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("drivingLicenseNumber"));
    }

    public void loadCustomerData() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String customerId = rs.getString("CustomerID");
                String name = rs.getString("Name");
                String phoneNumber = rs.getString("PhoneNumber");
                String email = rs.getString("Email_Address");
                String drivingLicenseNumber = rs.getString("DrivingLicenseNumber");
                customerList.add(new Customer(customerId, name, phoneNumber, email, drivingLicenseNumber));
            }
            customerTable.setItems(customerList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCustomer() {
        String name = nameField.getText();
        String customerId = customerIdField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String licenseNumber = licenseNumberField.getText();

        String sql = "INSERT INTO customers (CustomerID, Name, PhoneNumber, Email_Address, DrivingLicenseNumber) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            pstmt.setString(2, name);
            pstmt.setString(3, phoneNumber);
            pstmt.setString(4, email);
            pstmt.setString(5, licenseNumber);
            pstmt.executeUpdate();
            loadCustomerData(); // Refresh the data in the TableView
            clearFields();
            showAlert("Success", "User added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add user: " + e.getMessage());
        }
    }

    public void updateCustomer() {
        String name = nameField.getText();
        String customerId = customerIdField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String licenseNumber = licenseNumberField.getText();

        String sql = "UPDATE customers SET Name = ?, PhoneNumber = ?, Email_Address = ?, DrivingLicenseNumber = ? WHERE CustomerID = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phoneNumber);
            pstmt.setString(3, email);
            pstmt.setString(4, licenseNumber);
            pstmt.setString(5, customerId);
            pstmt.executeUpdate();
            loadCustomerData(); // Refresh the data in the TableView
            clearFields();
            showAlert("Success", "User updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update user: " + e.getMessage());
        }
    }

    public void deleteCustomer() {
        String customerId = customerIdField.getText();

        String sql = "DELETE FROM customers WHERE CustomerID = ?";

        try (Connection connection = databaseHandler.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, customerId);
            pstmt.executeUpdate();
            loadCustomerData(); // Refresh the data in the TableView
            clearFields();
            showAlert("Success", "User deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete user: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fillTextFields(Customer customer) {
        customerIdField.setText(customer.getCustomerID());
        nameField.setText(customer.getName());
        phoneNumberField.setText(customer.getPhoneNumber());
        emailField.setText(customer.getEmail());
        licenseNumberField.setText(customer.getDrivingLicenseNumber());
    }

    public void clearFields() {
        nameField.clear();
        customerIdField.clear();
        phoneNumberField.clear();
        emailField.clear();
        licenseNumberField.clear();
    }

    public void logout() {
        try {
            // Load the Admin Dashboard
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("AdminDashboard.fxml")); // Adjust the path as necessary
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Customer class with necessary fields and constructor
    public static class Customer {
        private String customerID;
        private String name;
        private String phoneNumber;
        private String email;
        private String drivingLicenseNumber;

        public Customer(String customerID, String name, String phoneNumber, String email, String drivingLicenseNumber) {
            this.customerID = customerID;
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.drivingLicenseNumber = drivingLicenseNumber;
        }

        public String getCustomerID() { return customerID; }
        public String getName() { return name; }
        public String getPhoneNumber() { return phoneNumber; }
        public String getEmail() { return email; }
        public String getDrivingLicenseNumber() { return drivingLicenseNumber; }
    }
}