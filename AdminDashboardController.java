package com.example.vehivlesproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.IOException;
import java.sql.*;

public class AdminDashboardController {

    @FXML private AnchorPane anchorPane;

    @FXML private Button manageVehiclesButton;
    @FXML private Button manageUsersButton;
    @FXML private Button managePaymentsButton;
    @FXML private Button manageReportsButton;
    @FXML private Button generateReportButton; // New button for report generation
    @FXML private Button logoutButton;

    @FXML private LineChart<String, Number> lineChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;

    @FXML
    public void initialize() {
        setupChartTitles();
        loadCharts();

        // Button actions
        manageVehiclesButton.setOnAction(e -> loadView("VehicleManagement.fxml"));
        manageUsersButton.setOnAction(e -> loadView("CustomerManagement.fxml"));
        managePaymentsButton.setOnAction(e -> loadView("ManageBookings.fxml"));
        //manageReportsButton.setOnAction(e -> loadView("ManageReports.fxml"));
        logoutButton.setOnAction(e -> loadView("hello-view.fxml"));

        // Action for generating report
        generateReportButton.setOnAction(e -> generateReport());
    }

    private void setupChartTitles() {
        lineChart.setTitle("Vehicle Categories");
        lineChart.getXAxis().setLabel("Category");
        lineChart.getYAxis().setLabel("Number of Vehicles");

        barChart.setTitle("Registered Customers");
        barChart.getXAxis().setLabel("Customer Name");
        barChart.getYAxis().setLabel("Record Count");

        pieChart.setTitle("Payments Made");
    }

    private void loadCharts() {
        try {
            loadLineChart();
            loadBarChart();
            loadPieChart();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLineChart() throws SQLException {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Vehicles by Category");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT category, COUNT(*) AS count FROM vehicles GROUP BY category";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String category = rs.getString("category");
                    int count = rs.getInt("count");
                    series.getData().add(new XYChart.Data<>(category, count));
                }
            }
        }

        Platform.runLater(() -> lineChart.getData().add(series));
    }

    private void loadBarChart() throws SQLException {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Customers");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT Name FROM customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("Name");
                    XYChart.Data<String, Number> data = new XYChart.Data<>(name, 1);
                    series.getData().add(data);
                }
            }
        }

        // Set bar color to orange for each entry in the series
        for (XYChart.Data<String, Number> data : series.getData()) {
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: orange;"); // Set fill color to orange
                }
            });
        }

        Platform.runLater(() -> barChart.getData().add(series));
    }

    private void loadPieChart() throws SQLException {
        pieChart.getData().clear();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT PaymentMethod, COUNT(*) AS count FROM payments GROUP BY PaymentMethod";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String method = rs.getString("PaymentMethod");
                    int count = rs.getInt("count");
                    pieChart.getData().add(new PieChart.Data(method, count));
                }
            }
        }
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            AnchorPane view = loader.load();
            anchorPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to generate a simplified PDF report
    public void generateReport() {
        String filePath = "vehicles_report.pdf"; // Specify your desired file path
        try (PdfWriter writer = new PdfWriter(filePath);
             PdfDocument pdfDoc = new PdfDocument(writer);
             Document document = new Document(pdfDoc)) {

            // Add Title
            document.add(new Paragraph("Vehicle Management Report"));
            document.add(new Paragraph("Generated on: " + new java.util.Date()));

            // Fetch data for the report
            int totalCars = getTotalCars();
            int totalCustomers = getTotalCustomers();
            int totalCarsRented = getTotalCarsRented();
            double totalPayments = getTotalPayments();

            // Add Summary Information
            document.add(new Paragraph("Total Number of Available Cars: " + totalCars));
            document.add(new Paragraph("Total Number of Customers: " + totalCustomers));
            document.add(new Paragraph("Total Number of Cars Rented: " + totalCarsRented));
            document.add(new Paragraph("Total Amount Paid: $" + String.format("%.2f", totalPayments)));

            System.out.println("Report generated successfully at: " + filePath);
            showConfirmationDialog("Report is downloaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to get the total count of available cars
    private int getTotalCars() throws SQLException {
        int totalCars = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT COUNT(*) AS count FROM vehicles WHERE status = 'available'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalCars = rs.getInt("count");
                }
            }
        }
        return totalCars;
    }

    // Helper method to get the total count of customers
    private int getTotalCustomers() throws SQLException {
        int totalCustomers = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT COUNT(*) AS count FROM customers";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalCustomers = rs.getInt("count");
                }
            }
        }
        return totalCustomers;
    }

    // Helper method to get the total number of rented cars
    private int getTotalCarsRented() throws SQLException {
        int totalCarsRented = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT COUNT(*) AS count FROM rentals WHERE rental_status = 'rented'";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalCarsRented = rs.getInt("count");
                }
            }
        }
        return totalCarsRented;
    }

    // Helper method to get the total amount of payments made
    private double getTotalPayments() throws SQLException {
        double totalPayments = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/vehicles", "root", "59793789")) {
            String sql = "SELECT SUM(amount) AS total FROM payments";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalPayments = rs.getDouble("total");
                }
            }
        }
        return totalPayments;
    }

    // Method to show a confirmation dialog
    private void showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Download");
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);

        alert.showAndWait(); // This will block until the user closes the dialog
    }
}