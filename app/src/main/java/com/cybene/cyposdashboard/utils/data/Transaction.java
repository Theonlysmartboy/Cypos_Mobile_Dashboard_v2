package com.cybene.cyposdashboard.utils.data;

import java.util.Date;
import java.util.Locale;

public class Transaction {
    private String id;
    private String title;
    private double amount;
    private Date timestamp;
    private boolean isSale;  // true for sale, false for purchase
    private String status;   // "Completed", "Pending", "Refunded", etc.

    public Transaction(String id, String title, double amount, Date timestamp, boolean isSale, String status) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.timestamp = timestamp;
        this.isSale = isSale;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isSale() {
        return isSale;
    }

    public String getStatus() {
        return status;
    }

    // Formatted getters for UI
    public String getFormattedAmount() {
        return String.format(Locale.ENGLISH, "KES %,.2f", amount);
    }

    public String getTime() {
        // Format the time as needed (e.g., "10:30 AM")
        return android.text.format.DateFormat.format("hh:mm a", timestamp).toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setSale(boolean sale) {
        isSale = sale;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
