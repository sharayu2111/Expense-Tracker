package com.expensetracker.model;

import java.util.Date;

// Inheritance
public class Expense extends Transaction {
    
    public Expense() {
        super();
    }

    public Expense(int id, double amount, String category, Date date, String description) {
        super(id, amount, category, date, description);
    }

    // Polymorphism
    @Override
    public String getTransactionType() {
        return "EXPENSE";
    }
}
