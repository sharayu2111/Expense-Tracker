package com.expensetracker.model;

import java.util.Date;

// Inheritance
public class Income extends Transaction {
    
    public Income() {
        super();
    }

    public Income(int id, double amount, String category, Date date, String description) {
        super(id, amount, category, date, description);
    }

    // Polymorphism
    @Override
    public String getTransactionType() {
        return "INCOME";
    }
}
