package com.example.vehivlesproject;

public class Booking {
    private final int bookingId;
    private final int customerId;
    private final int vehicleId;
    private final String startDate;
    private final String endDate;
    private final String status;

    public Booking(int bookingId, int customerId, int vehicleId, String startDate, String endDate, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}