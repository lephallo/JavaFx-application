package com.example.vehivlesproject;

public class Vehicle {
    private String vehicleId;
    private String brandModel;
    private String category;
    private double rentalPrice;
    private String status;
    private String customerId; // Add customer ID field for rented vehicles

    // Constructor
    public Vehicle(String vehicleId, String brandModel, String category, double rentalPrice, String status, String customerId) {
        this.vehicleId = vehicleId;
        this.brandModel = brandModel;
        this.category = category;
        this.rentalPrice = rentalPrice;
        this.status = status;
        this.customerId = customerId;  // Initialize customer ID
    }

    // Getters and setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}