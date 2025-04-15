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

public class ViewCustomersController {

    @FXML
   public TableView<Customer> customerTable;

    @FXML
    public TableColumn<Customer, Integer> customerIdCol;
    @FXML
    public TableColumn<Customer, String> nameCol;
    @FXML
    public TableColumn<Customer, String> phoneNumberCol;
    @FXML
    public TableColumn<Customer, String> emailCol;
    @FXML
    public TableColumn<Customer, String> licenseCol;

    public Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/vehicles";
        String user = "root";
        String password = "59793789";
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    private void initialize() {
        customerIdCol.setCellValueFactory(data -> data.getValue().customerIdProperty().asObject());
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        phoneNumberCol.setCellValueFactory(data -> data.getValue().phoneNumberProperty());
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());
        licenseCol.setCellValueFactory(data -> data.getValue().licenseProperty());

        loadCustomerData();
    }

    public void loadCustomerData() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try (Connection conn = connectToDatabase()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customers");

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("Name"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Email_Address"),
                        rs.getString("DrivingLicenseNumber")
                );
                customers.add(customer);
            }

            customerTable.setItems(customers);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not load customer data.");
        }
    }

    @FXML
    public void handleLogoutButton() {
        loadScene("Register.fxml");
    }

    @FXML
    public void handleBackButton() {
        loadScene("EmployeeDashboard.fxml");
    }

    private void loadScene(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) customerTable.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to load " + fxmlFile);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
