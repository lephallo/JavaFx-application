package com.example.vehivlesproject;

import javafx.beans.property.*;

public class Customer {
    private final IntegerProperty customerId;
    private final StringProperty name;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final StringProperty license;

    public Customer(int customerId, String name, String phoneNumber, String email, String license) {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.name = new SimpleStringProperty(name);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.license = new SimpleStringProperty(license);
    }

    public IntegerProperty customerIdProperty() {
        return customerId;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty licenseProperty() {
        return license;
    }
}

