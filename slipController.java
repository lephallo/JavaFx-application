package com.example.vehivlesproject;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class slipController {

    @FXML
    private TextField customerIdField;

    @FXML
    private Label invoiceNumberLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label customerIdLabel;
    @FXML
    private Label paymentIdLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label totalAmountLabel;
    @FXML
    public Button printButton;

    @FXML
    public Button backButton;

    @FXML
    private Pane invoicePane;


    private Connection connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/vehicles"; // Update with your DB name
        String user = "root"; // Update with your MySQL username
        String password = "59793789"; // Update with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }

    @FXML
    private void handleSubmitButtonAction() {
        String customerId = customerIdField.getText().trim();

        if (customerId.isEmpty()) {
            showAlert("Input Error", "Please enter a Customer ID.");
            return;
        }

        try (Connection conn = connectToDatabase()) {
            PreparedStatement stmt = conn.prepareStatement("""
            SELECT 
                c.Name,
                c.CustomerID,
                b.BookingID,
                b.StartDate,
                b.EndDate,
                p.PaymentID,
                p.Amount,
                p.PaymentDate
            FROM customers c
            JOIN bookings b ON c.CustomerID = b.CustomerID
            JOIN payments p ON b.BookingID = p.BookingID
            WHERE c.CustomerID = ?
        """);

            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                int custId = rs.getInt("CustomerID");
                int bookingId = rs.getInt("BookingID");
                int paymentId = rs.getInt("PaymentID");
                double amount = rs.getDouble("Amount");
                Date startDate = rs.getDate("StartDate");
                Date endDate = rs.getDate("EndDate");
                Date paymentDate = rs.getDate("PaymentDate");

                long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);

                // Set the values in the UI with labels
                invoiceNumberLabel.setText("Invoice Number: " + bookingId);
                dateLabel.setText("Date: " + paymentDate);
                nameLabel.setText("Name: " + name);
                customerIdLabel.setText("Customer ID: " + custId);
                paymentIdLabel.setText("Payment ID: " + paymentId);
                durationLabel.setText("Duration: " + days + " day(s)");
                totalAmountLabel.setText("Total Amount: R" + amount);
            } else {
                showAlert("No Data", "No invoice found for the entered Customer ID.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to retrieve data.");
        }

    }


    @FXML
    public void handlePrintButtonAction() {
        javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(invoicePane.getScene().getWindow())) {
            boolean success = job.printPage(invoicePane);
            if (success) {
                job.endJob();
            }
        }
    }

    @FXML
    public void handleBackButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("User Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to return to dashboard.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
