package com.example.vehivlesproject;

import javafx.beans.property.*;

public class Payment {
    public final IntegerProperty paymentId;
    public final IntegerProperty bookingId;
    public final StringProperty paymentMethod;
    public final DoubleProperty amount;
    public final StringProperty paymentDate;

    public Payment(int paymentId, int bookingId, String paymentMethod, double amount, String paymentDate) {
        this.paymentId = new SimpleIntegerProperty(paymentId);
        this.bookingId = new SimpleIntegerProperty(bookingId);
        this.paymentMethod = new SimpleStringProperty(paymentMethod);
        this.amount = new SimpleDoubleProperty(amount);
        this.paymentDate = new SimpleStringProperty(paymentDate);
    }

    public IntegerProperty paymentIdProperty() { return paymentId; }
    public IntegerProperty bookingIdProperty() { return bookingId; }
    public StringProperty paymentMethodProperty() { return paymentMethod; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty paymentDateProperty() { return paymentDate; }
}
