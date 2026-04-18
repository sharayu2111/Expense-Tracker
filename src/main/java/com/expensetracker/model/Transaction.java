package com.expensetracker.model;

import java.util.Date;

// Abstract Class demonstrating OOP (Abstraction)
public abstract class Transaction {
    private int id;
    private double amount;
    private String category;
    private Date date;
    private String description;

    public Transaction() {}

    public Transaction(int id, double amount, String category, Date date, String description) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters for Encapsulation
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    // Abstract method to be implemented by child classes
    public abstract String getTransactionType();
}
