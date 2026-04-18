package com.expensetracker.dao;

import com.expensetracker.model.Transaction;
import java.util.List;

// Interface for Abstraction
public interface ITransactionDAO {
    void addTransaction(Transaction t);
    void updateTransaction(Transaction t);
    void deleteTransaction(int id);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByType(String type);
    double getTotalBalance();
}
