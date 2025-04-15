package com.example.vehivlesproject;

import javafx.beans.property.*;

public class PaymentInfo {
    private final IntegerProperty bookingId;
    private final StringProperty paymentMethod;
    private final DoubleProperty amount;
    private final StringProperty paymentDate;

    public PaymentInfo(int bookingId, String paymentMethod, double amount, String paymentDate) {
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentDate = new SimpleStringProperty(paymentDate);
    }

    public IntegerProperty bookingIdProperty() { return bookingId; }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty paymentDateProperty() { return paymentDate; }
}
